package org.protobeans.crypto;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Null;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;

import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.CMSVersion;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.CertificateChoices;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.CertificateSet;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.ContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.DigestAlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.DigestAlgorithmIdentifiers;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.EncapsulatedContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.IssuerAndSerialNumber;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignatureAlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignatureValue;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignedData;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerInfos;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.CertificateSerialNumber;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Name;
import ru.CryptoPro.JCP.params.OID;
import ru.CryptoPro.JCP.tools.AlgorithmUtility;

public class Cms {
    public static final String STR_CMS_OID_DATA = "1.2.840.113549.1.7.1";
    public static final String STR_CMS_OID_SIGNED = "1.2.840.113549.1.7.2";

    public static byte[] CMSSignEx(byte[] data, PrivateKey key, Certificate cert, boolean detached) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, CertificateEncodingException, Asn1Exception, IOException {
        String signOid = AlgorithmUtility.keyAlgToSignatureOid(key.getAlgorithm());
    
        // sign
        final Signature signature = Signature.getInstance(signOid, JCP.PROVIDER_NAME);
        signature.initSign(key);
        signature.update(data);
    
        final byte[] sign = signature.sign();
    
        // create cms format
        return createCMSEx(data, sign, cert, detached);
    }

    public static byte[] createCMSEx(byte[] buffer, byte[] sign, Certificate cert, boolean detached) throws CertificateEncodingException, Asn1Exception, IOException {
        String pubKeyAlg = cert.getPublicKey().getAlgorithm();
        String digestOid = AlgorithmUtility.keyAlgToDigestOid(pubKeyAlg);
        String keyOid    = AlgorithmUtility.keyAlgToKeyAlgorithmOid(pubKeyAlg); // алгоритм ключа подписи

        final ContentInfo all = new ContentInfo();
        all.contentType = new Asn1ObjectIdentifier(new OID(STR_CMS_OID_SIGNED).value);
        
        final SignedData cms = new SignedData();
        all.content = cms;
        cms.version = new CMSVersion(1);

        // digest
        cms.digestAlgorithms = new DigestAlgorithmIdentifiers(1);
        final DigestAlgorithmIdentifier a = new DigestAlgorithmIdentifier(new OID(digestOid).value);

        a.parameters = new Asn1Null();
        cms.digestAlgorithms.elements[0] = a;

        if (detached) {
            cms.encapContentInfo = new EncapsulatedContentInfo(new Asn1ObjectIdentifier(new OID(STR_CMS_OID_DATA).value), null);
        } else {
            cms.encapContentInfo = new EncapsulatedContentInfo(new Asn1ObjectIdentifier(new OID(STR_CMS_OID_DATA).value), new Asn1OctetString(buffer));
        }
    
        // certificate
        cms.certificates = new CertificateSet(1);
        final ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate certificate = new ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate();
        final Asn1BerDecodeBuffer decodeBuffer = new Asn1BerDecodeBuffer(cert.getEncoded());
        certificate.decode(decodeBuffer);
    
        cms.certificates.elements = new CertificateChoices[1];
        cms.certificates.elements[0] = new CertificateChoices();
        cms.certificates.elements[0].set_certificate(certificate);
    
        // signer info
        cms.signerInfos = new SignerInfos(1);
        cms.signerInfos.elements[0] = new SignerInfo();
        cms.signerInfos.elements[0].version = new CMSVersion(1);
        cms.signerInfos.elements[0].sid = new SignerIdentifier();
    
        final byte[] encodedName = ((X509Certificate) cert).getIssuerX500Principal().getEncoded();
        final Asn1BerDecodeBuffer nameBuf = new Asn1BerDecodeBuffer(encodedName);
        final Name name = new Name();
        name.decode(nameBuf);
    
        final CertificateSerialNumber num = new CertificateSerialNumber(((X509Certificate) cert).getSerialNumber());
        cms.signerInfos.elements[0].sid.set_issuerAndSerialNumber(new IssuerAndSerialNumber(name, num));
        cms.signerInfos.elements[0].digestAlgorithm = new DigestAlgorithmIdentifier(new OID(digestOid).value);
        cms.signerInfos.elements[0].digestAlgorithm.parameters = new Asn1Null();
        cms.signerInfos.elements[0].signatureAlgorithm = new SignatureAlgorithmIdentifier(new OID(keyOid).value);
        cms.signerInfos.elements[0].signatureAlgorithm.parameters = new Asn1Null();
        cms.signerInfos.elements[0].signature = new SignatureValue(sign);
    
        // encode
        final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
        all.encode(asnBuf, true);
        return asnBuf.getMsgCopy();
    }
}
