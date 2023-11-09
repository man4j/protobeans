package org.protobeans.feign.config;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.protobeans.feign.exception.FeignJsonBodyException;
import org.protobeans.feign.exception.FeignNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;

import com.rainerhahnekamp.sneakythrow.Sneaky;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.Request;
import feign.RequestInterceptor;
import feign.Response;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.http2client.Http2Client;
import feign.slf4j.Slf4jLogger;

abstract public class BaseFeignFactory<T> {
    private static Logger log = LoggerFactory.getLogger(BaseFeignFactory.class);
    
    public static final int CONNECTION_TIMEOUT_SEC = 60;
    
    protected Class<T> apiClass;
    
    @Autowired
    protected Encoder encoder;
    
    @Autowired
    protected Decoder decoder;
    
    @Autowired
    protected Contract contract;
    
    protected TrustManager[] trustAllCerts = new TrustManager[] {
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
    
    @SuppressWarnings("unchecked")
    public BaseFeignFactory() {
        apiClass = (Class<T>) ResolvableType.forClass(BaseFeignFactory.class, this.getClass()).resolveGeneric(0);
    }
    
    public T create(String url, RequestInterceptor... interceptors) {
        return Feign.builder().encoder(encoder)
                              .decoder(getDecoder(decoder))
                              .contract(contract)
                              .retryer(Retryer.NEVER_RETRY)
                              .logger(new Slf4jLogger(this.getClass()))
                              .errorDecoder((methodKey, response) -> errorDecoder(response))
                              .options(new Request.Options(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS, 10, TimeUnit.MINUTES, true))
                              .client(javaClient())
                              .requestInterceptors(Arrays.asList(interceptors))
                              .target(apiClass, url);
    }
        
    protected Decoder getDecoder(Decoder decoder) {
        return decoder;
    }

    protected Exception errorDecoder(Response response) {
        if (response.status() == 404) {
            return new FeignNotFoundException();
        }
        
        String body = getResponseBody(response);

        if (body.startsWith("{") && body.endsWith("}")) {//jsonBody
            return new FeignJsonBodyException(body);
        }

        log.warn("Exception in call url: {}, status: {}, body: {}", response.request().url(), response.status(), body);
        return new Exception(body);
    }
    
    @SuppressWarnings("resource")
    protected static String getResponseBody(Response response) {
        if (response.body() == null) {
            return "";
        }
        
        return Sneaky.sneak(() -> IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8));
    }
    
    protected Client javaClient() {
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
                                          .connectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT_SEC))
                                          .sslContext(sslContext)
                                          .sslParameters(sslParams)
                                          .followRedirects(Redirect.ALWAYS)
                                          .build();
        
        return new Http2Client(httpClient);
    }
}
