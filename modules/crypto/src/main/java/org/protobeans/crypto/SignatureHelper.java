package org.protobeans.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.util.Store;

public class SignatureHelper {
    private static final String INN_OID = "1.2.643.3.131.1.1";
    private static final String INNLE_OID = "1.2.643.100.4";
    
    private static final String EMAIL_OID = "1.2.840.113549.1.9.1";
    
    private static final String SN = "2.5.4.4";
    
    private static final String GN = "2.5.4.42";

    public SignatureInfo getSignatureInfo(String signature) throws CMSException {
        CMSSignedData cms = new CMSSignedData(Base64.getDecoder().decode(signature.getBytes(StandardCharsets.UTF_8)));

        Store<X509CertificateHolder> certStore = cms.getCertificates();
        Collection<SignerInformation> signers = cms.getSignerInfos().getSigners();

        for (SignerInformation signer : signers) {
            Collection<X509CertificateHolder> matches = certStore.getMatches(signer.getSID());
            for (X509CertificateHolder certificateHolder : matches) {
                String serial = certificateHolder.getSerialNumber().toString(16);
                long expiredDate = certificateHolder.getNotAfter().toInstant().getEpochSecond();
                long date = certificateHolder.getNotBefore().toInstant().getEpochSecond();
                X500Name subject = certificateHolder.getSubject();
                var signedContent = new String((byte[]) cms.getSignedContent().getContent());
                String issuer = certificateHolder.getIssuer().toString();
                boolean isTestCert = issuer.toLowerCase().contains("тестовый");
                return new SignatureInfo(serial, getCn(subject), getInn(subject), expiredDate, date, getEmail(subject), getFio(subject), isLegal(subject), signedContent, isTestCert);
            }
        }
        
        throw new CMSException("ЭЦП не соответствует CMS формату");
    }

    private String getInn(X500Name subject) {
        RDN[] innleRdns = subject.getRDNs(new ASN1ObjectIdentifier(INNLE_OID));
        String innle = getRdnString(innleRdns);
        if (innle != null) {
            return innle;
        }

        return getRdnString(subject.getRDNs(new ASN1ObjectIdentifier(INN_OID)));
    }
    
    private boolean isLegal(X500Name subject) {
        RDN[] innleRdns = subject.getRDNs(new ASN1ObjectIdentifier(INNLE_OID));
        return getRdnString(innleRdns) != null;
    }

    private String getCn(X500Name subject) {
        return getRdnString(subject.getRDNs(BCStyle.CN));
    }
    
    private String getEmail(X500Name subject) {
        return getRdnString(subject.getRDNs(new ASN1ObjectIdentifier(EMAIL_OID)));
    }
    
    private String getFio(X500Name subject) {
        return getRdnString(subject.getRDNs(new ASN1ObjectIdentifier(SN))) + " " + getRdnString(subject.getRDNs(new ASN1ObjectIdentifier(GN)));
    }

    private String getRdnString(RDN[] rdns) {
        if (rdns.length == 0) {
            return null;
        }

        return rdns[0].getFirst().getValue().toString();
    }
}
