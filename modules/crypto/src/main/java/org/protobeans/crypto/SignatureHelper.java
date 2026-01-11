package org.protobeans.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;

public class SignatureHelper {
    private static final String INN_OID = "1.2.643.3.131.1.1";
    private static final String INNLE_OID = "1.2.643.100.4";
    
    private static final String EMAIL_OID = "1.2.840.113549.1.9.1";
    
    private static final String SN = "2.5.4.4";
    
    private static final String GN = "2.5.4.42";
    
    private static final String OGRN_OID = "1.2.643.100.1";
    
    private static final String OGRNIP_OID = "1.2.643.100.5";
    
    private static final String SNILS_OID = "1.2.643.100.3";

    public SignatureInfo getSignatureInfo(String signature) throws CMSException {
        var cms = new CMSSignedData(Base64.getDecoder().decode(signature.getBytes(StandardCharsets.UTF_8)));

        var certStore = cms.getCertificates();
        var signers = cms.getSignerInfos().getSigners();

        for (var signer : signers) {
            var matches = certStore.getMatches(signer.getSID());
            for (var cert : matches) {
                if (cert instanceof X509CertificateHolder certificateHolder) {
                    X500Name subject = certificateHolder.getSubject();
                    
                    var serial = certificateHolder.getSerialNumber().toString(16);
                    var orgName = getCn(subject);
                    var inn = getInn(subject);
                    var ogrn = getOgrn(subject);
                    var snils = getSnils(subject);
                    var expiredDate = certificateHolder.getNotAfter().toInstant().getEpochSecond();
                    var date = certificateHolder.getNotBefore().toInstant().getEpochSecond();
                    var email = getEmail(subject);
                    var surname = getSn(subject);
                    var nameMiddleName = getGn(subject);
                    var legal = isLegal(subject);
                    var signedContent = new String((byte[]) cms.getSignedContent().getContent());
                    boolean isTestCert = certificateHolder.getIssuer().toString().toLowerCase().contains("тестовый");
                    
                    return new SignatureInfo(serial, orgName, inn, ogrn, snils, expiredDate, date, email, surname, nameMiddleName, legal, signedContent, isTestCert);
                }
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
    
    private String getOgrn(X500Name subject) {
        RDN[] ogrnRdns = subject.getRDNs(new ASN1ObjectIdentifier(OGRN_OID));
        String ogrn = getRdnString(ogrnRdns);
        if (ogrn != null) {
            return ogrn;
        }

        return getRdnString(subject.getRDNs(new ASN1ObjectIdentifier(OGRNIP_OID)));
    }

    private String getSnils(X500Name subject) {
        return getRdnString(subject.getRDNs(new ASN1ObjectIdentifier(SNILS_OID)));
    }

    private String getSn(X500Name subject) {
        return getRdnString(subject.getRDNs(new ASN1ObjectIdentifier(SN)));
    }

    private String getGn(X500Name subject) {
        return getRdnString(subject.getRDNs(new ASN1ObjectIdentifier(GN)));
    }


    private String getRdnString(RDN[] rdns) {
        if (rdns.length == 0) {
            return null;
        }

        return rdns[0].getFirst().getValue().toString();
    }
}
