package org.protobeans.exchange;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.rainerhahnekamp.sneakythrow.Sneaky;

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
        return create(url, cls, null, null);
    }
    
    public <T> T create(String url, Class<T> cls, String username, String password) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient(url, username, password))).build();
        return factory.createClient(cls);
    }
    
    protected ResponseErrorHandler getErrorhandler() {
        return new DefaultResponseErrorHandler() {
            @Override
            protected void handleError(ClientHttpResponse response, HttpStatusCode statusCode) throws java.io.IOException {
                try {
                    super.handleError(response, statusCode);
                } catch (HttpStatusCodeException ex) {
                    if (statusCode == HttpStatus.FORBIDDEN) {
                        throw new AccessDeniedException(response.getStatusText());
                    }
                    
                    if (statusCode == HttpStatus.UNAUTHORIZED) {
                        throw new BadCredentialsException(response.getStatusText());
                    }
                    
                    var rr = ex.getResponseBodyAs(RestResult.class);
                    
                    if (statusCode == HttpStatus.NOT_FOUND) {
                        throw new NotFoundException(rr.getGlobalErrors().get(0));
                    }
                    
                    throw new RestResultException(statusCode, rr);
                }
            }
        };
    }
    
    @SuppressWarnings("resource")
    private RestClient restClient(String baseUrl, String username, String password) {
        SSLContext sslContext;
        
        try {
            System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
            sslContext = Sneaky.sneak(() -> SSLContext.getInstance("TLS"));
            sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        SSLParameters sslParams = new SSLParameters();
        sslParams.setEndpointIdentificationAlgorithm("");
        
        HttpClient httpClient = HttpClient.newBuilder()
                                          .connectTimeout(Duration.ofSeconds(30))
                                          .sslContext(sslContext)
                                          .sslParameters(sslParams)
                                          .followRedirects(Redirect.ALWAYS)
                                          .build();
        
        var errorHandler = getErrorhandler();
        
        var msgConverters = ProtobeansHttpInterfaceUtils.protobeansConverters();
        
        Sneaky.sneaked(() -> {
            var field = DefaultResponseErrorHandler.class.getDeclaredField("messageConverters");
            field.setAccessible(true);
            field.set(errorHandler, msgConverters);
        }).run();
        
        var builder = RestClient.builder().messageConverters(converters -> {converters.clear(); converters.addAll(msgConverters);})
                                          .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                                          .defaultStatusHandler(errorHandler)
                                          .baseUrl(baseUrl);
        
        if (username != null) {
            builder.defaultHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        }
        
        return builder.build();
    }
}
