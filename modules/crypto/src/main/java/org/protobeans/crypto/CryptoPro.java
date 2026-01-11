package org.protobeans.crypto;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Base64;

import com.objsys.asn1j.runtime.Asn1Exception;

import ru.CryptoPro.CAdES.CAdESSignature;
import ru.CryptoPro.CAdES.exception.CAdESException;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.KeyStore.JCPPrivateKeyEntry;
import ru.CryptoPro.JCP.Util.JCPInit;

public class CryptoPro {
    static {
        System.setProperty("com.sun.security.enableCRLDP", "true");
        System.setProperty("com.ibm.security.enableCRLDP", "true");
        System.setProperty("ocsp.enable", "true");

        JCPInit.initProviders(false);
    }
    
    public synchronized JCPPrivateKeyEntry getPrivateKeyEntry(String alias, String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException {
        var keyStore = KeyStore.getInstance(JCP.HD_STORE_NAME);
        keyStore.load(null, null);
        
        var privateKey = loadByAlias(keyStore, alias, password);
        
        if (privateKey != null) {
            return privateKey;
        }
        
        return null;
    }
    
    private JCPPrivateKeyEntry loadByAlias(KeyStore keyStore, String alias, String password) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        var it = keyStore.aliases().asIterator();
        
        while (it.hasNext()) {
            var a = it.next();
            
            if (a.equals(alias)) {
                return (JCPPrivateKeyEntry) keyStore.getEntry(a, new KeyStore.PasswordProtection(password.toCharArray()));
            }
        }
        return null;
    }
    
    public static byte[] createCmsSignature(JCPPrivateKeyEntry e, byte[] data, boolean detached) throws NoSuchAlgorithmException, CertificateException, IOException, InvalidKeyException, NoSuchProviderException, SignatureException, Asn1Exception {
        return Cms.signCms(e, data, detached);
    }
    
    public static byte[] createCadesBesSignature(JCPPrivateKeyEntry e, byte[] data, boolean detached) throws CertificateEncodingException, CAdESException, IOException {
        return Cades.signCadesBes(e, data, detached);
    }
        
    public void checkSignature(String signature) throws CAdESException {
        checkSignature(Base64.getMimeDecoder().decode(signature));
    }

    public void checkSignature(byte[] signature) throws CAdESException {
        new CAdESSignature(signature, null, null).verify(null, null);
    }
}
