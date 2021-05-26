import java.sql.Timestamp;

public class CodeStatsRecord {
    private String orderId;
    
    private String gtin;
    
    private long orderedCodes;//КМ заказанные в СУЗ
    
    private long appliedCodes;//КМ преобразованные в средства идентификации
    
    private long leftCodes;//КМ не извлеченные из буфера
    
    private long rejectedCodes;//КМ отклоненные СУЗ
    
    private long unprocessedCodes;//КМ не преобразованные в средства идентификации
    
    private String issuerId;
    
    private String issuerName;
    
    private String senderId;
    
    private String senderName;
    
    private String recipientAreaId;
    
    private String recipientAreaName;
    
    private Timestamp createTime;

    public CodeStatsRecord(String orderId, 
                           String gtin,
                           long orderedCodes, 
                           long appliedCodes, 
                           long leftCodes,
                           long rejectedCodes,
                           long unprocessedCodes,
                           String issuerId,
                           String issuerName, 
                           String senderId, 
                           String senderName, 
                           String recipientAreaId, 
                           String recipientAreaName,
                           long createTimeMs) {
        this.orderId = orderId;
        this.gtin = gtin;
        this.orderedCodes = orderedCodes;
        this.appliedCodes = appliedCodes;
        this.leftCodes = leftCodes;
        this.rejectedCodes = rejectedCodes;
        this.unprocessedCodes = unprocessedCodes;
        this.issuerId = issuerId;
        this.issuerName = issuerName;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientAreaId = recipientAreaId;
        this.recipientAreaName = recipientAreaName;
        this.createTime = new Timestamp(createTimeMs);
    }
    
    public CodeStatsRecord() {
        //empty
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public long getOrderedCodes() {
        return orderedCodes;
    }

    public void setOrderedCodes(long orderedCodes) {
        this.orderedCodes = orderedCodes;
    }

    public long getAppliedCodes() {
        return appliedCodes;
    }

    public void setAppliedCodes(long appliedCodes) {
        this.appliedCodes = appliedCodes;
    }

    public long getLeftCodes() {
        return leftCodes;
    }

    public void setLeftCodes(long leftCodes) {
        this.leftCodes = leftCodes;
    }

    public long getRejectedCodes() {
        return rejectedCodes;
    }

    public void setRejectedCodes(long rejectedCodes) {
        this.rejectedCodes = rejectedCodes;
    }

    public long getUnprocessedCodes() {
        return unprocessedCodes;
    }

    public void setUnprocessedCodes(long unprocessedCodes) {
        this.unprocessedCodes = unprocessedCodes;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientAreaId() {
        return recipientAreaId;
    }

    public void setRecipientAreaId(String recipientAreaId) {
        this.recipientAreaId = recipientAreaId;
    }

    public String getRecipientAreaName() {
        return recipientAreaName;
    }

    public void setRecipientAreaName(String recipientAreaName) {
        this.recipientAreaName = recipientAreaName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
