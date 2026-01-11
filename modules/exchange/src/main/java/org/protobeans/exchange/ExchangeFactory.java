package org.protobeans.exchange;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.protobeans.exchange.exception.NotFoundException;
import org.protobeans.exchange.exception.RestResultException;
import org.protobeans.exchange.model.RestResult;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.Builder;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import lombok.SneakyThrows;

public class ExchangeFactory {
    private static TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //empty
                }
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //empty
                }
            }
        };
    
    
    public <T> T create(String url, Class<T> cls) {
        return create(url, cls, null);
    }
    
    public <T> T create(String url, Class<T> cls, String token) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient(url, token))).build();
        return factory.createClient(cls);
    }
    
    public <T> T create(String url, Class<T> cls, String username, String password) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient(url, username, password))).build();
        return factory.createClient(cls);
    }
    
    protected ResponseErrorHandler getErrorhandler() {
        var mapper = ProtobeansHttpInterfaceUtils.mapper();
        
        return new DefaultResponseErrorHandler() {
            @Override
            protected void handleError(ClientHttpResponse response, HttpStatusCode statusCode, URI url, HttpMethod method) throws IOException {
                try {
                    super.handleError(response, statusCode, url, method);
                } catch (HttpStatusCodeException ex) {
                    if (statusCode == HttpStatus.FORBIDDEN) {
                        throw new AccessDeniedException(response.getStatusText());
                    }
                    
                    if (statusCode == HttpStatus.UNAUTHORIZED) {
                        throw new BadCredentialsException(response.getStatusText());
                    }
                    
                    var rr = mapper.readValue(ex.getResponseBodyAsString(), RestResult.class);
                    
                    if (statusCode == HttpStatus.NOT_FOUND) {
                        throw new NotFoundException(rr.getGlobalErrors().get(0));
                    }
                    
                    throw new RestResultException(statusCode, rr);
                }
            }
        };
    }
    
    protected RestClient restClient(String baseUrl, String token) {
        var builder = builder(baseUrl);
        
        if (token != null) {
            builder.defaultHeader("Authorization", "Bearer " + token);
        }
        
        return builder.build();
    }
    
    protected RestClient restClient(String baseUrl, String username, String password) {
        var builder = builder(baseUrl);
        
        if (username != null) {
            builder.defaultHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        }
        
        return builder.build();
    }
    
    @SneakyThrows
    @SuppressWarnings("resource")
    protected Builder builder(String baseUrl) {
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
        
        var sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        
        var sslParams = new SSLParameters();
        sslParams.setEndpointIdentificationAlgorithm("");
        
        HttpClient httpClient = HttpClient.newBuilder()
                                          .connectTimeout(Duration.ofSeconds(30))
                                          .sslContext(sslContext)
                                          .sslParameters(sslParams)
                                          .followRedirects(Redirect.ALWAYS)
                                          .build();
        
        var errorHandler = getErrorhandler();
        
        var builder = RestClient.builder().configureMessageConverters(c -> {
                                              c.registerDefaults()
                                               .withStringConverter(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                                               .withJsonConverter(new JacksonJsonHttpMessageConverter(ProtobeansHttpInterfaceUtils.mapper())).build();
                                          })
                                          .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                                          .defaultStatusHandler(errorHandler)
                                          .baseUrl(baseUrl);
        return builder;
    }
}
