package org.protobeans.crypto;

public class SignatureInfo {
    private String serial;
    
    private String subject;
    
    private String inn;
    
    private long expiredDate;
    
    private long date;
    
    private String email;
    
    private String fio;
    
    private boolean legal;
    
    private String signedData;
    
    private boolean testCert;
    
    public SignatureInfo(String serial, String subject, String inn, long expiredDate, long date, String email, String fio, boolean legal, String signedData, boolean testCert) {
        this.serial = serial;
        this.subject = subject;
        this.inn = inn;
        this.expiredDate = expiredDate;
        this.date = date;
        this.email = email;
        this.fio = fio;
        this.legal = legal;
        this.signedData = signedData;
        this.testCert = testCert;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public long getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(long expiredDate) {
        this.expiredDate = expiredDate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public boolean isLegal() {
        return legal;
    }

    public void setLegal(boolean legal) {
        this.legal = legal;
    }

    public String getSignedData() {
        return signedData;
    }

    public void setSignedData(String signedData) {
        this.signedData = signedData;
    }
    
    public boolean isTestCert() {
        return testCert;
    }
}

