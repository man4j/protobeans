package org.protobeans.crypto;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.rainerhahnekamp.sneakythrow.Sneaky;

public class SslUtils {
    public static TrustManager[] trustAllCerts = new TrustManager[] {
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
    
    public static SSLContext getSslContext() { 
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
        SSLContext sslContext = Sneaky.sneak(() -> SSLContext.getInstance("TLS"));
        try {
            sslContext.init(null, SslUtils.trustAllCerts, new SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sslContext;
    }
}

