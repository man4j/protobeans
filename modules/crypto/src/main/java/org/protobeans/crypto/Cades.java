package org.protobeans.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.util.CollectionStore;

import ru.CryptoPro.CAdES.CAdESParameters;
import ru.CryptoPro.CAdES.CAdESSignature;
import ru.CryptoPro.CAdES.exception.CAdESException;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.KeyStore.JCPPrivateKeyEntry;
import ru.CryptoPro.JCP.tools.AlgorithmUtility;

public class Cades {
    public static byte[] signCadesBes(JCPPrivateKeyEntry e, byte[] data, boolean detached) throws CAdESException, CertificateEncodingException, IOException {
        var chain = new ArrayList<>(Arrays.asList((X509Certificate[]) e.getCertificateChain()));
        
        var cades = new CAdESSignature(detached);
        
        cades.addSigner(
        JCP.PROVIDER_NAME,
        AlgorithmUtility.keyAlgToDigestOid(e.getPrivateKey().getAlgorithm()),
        AlgorithmUtility.keyAlgToKeyAlgorithmOid(e.getPrivateKey().getAlgorithm()),
        e.getPrivateKey(),
        chain,
        CAdESParameters.CAdES_BES,
        null,   // TSA URL
        false,  // embed CRLs
        null,   // signed attrs
        null,   // unsigned attrs
        null);  // CRLs list
        
        var holders = new ArrayList<>();
        
        for (var cert : chain) {
            holders.add(new JcaX509CertificateHolder(cert));
        }
        
        cades.setCertificateStore(new CollectionStore<>(holders));
        
        byte[] signature;

        try (var sigOut = new ByteArrayOutputStream()) {
            cades.open(sigOut);
            cades.update(data);
            cades.close();
            signature = sigOut.toByteArray();
        }
        
        return signature;
    }
}
