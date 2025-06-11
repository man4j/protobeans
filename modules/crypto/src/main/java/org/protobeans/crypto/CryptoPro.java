package org.protobeans.crypto;

import java.util.Base64;

import ru.CryptoPro.CAdES.CAdESSignature;
import ru.CryptoPro.CAdES.exception.CAdESException;
import ru.CryptoPro.JCP.Util.JCPInit;

public class CryptoPro {
    static {
        System.setProperty("com.sun.security.enableCRLDP", "true");
        System.setProperty("com.ibm.security.enableCRLDP", "true");
        System.setProperty("ocsp.enable", "true");

        JCPInit.initProviders(false);
    }
        
    public void checkSignature(String signature) throws CAdESException {
        checkSignature(Base64.getDecoder().decode(signature));
    }

    public void checkSignature(byte[] signature) throws CAdESException {
        new CAdESSignature(signature, null, null).verify(null, null);
    }
}
