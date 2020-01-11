package org.protobeans.core.model;

public class CouchDbQueryStatInfo {
    public static final String QUERY_STATS_KEY = "COUCHDB_QUERY_STATS";
    
    private final String operationType;
    
    private final String operationInfo;
    
    private final long count;
    
    private final long avgOperationTime;
    
    private final long maxOperationTime;
    
    private final long minOperationTime;
    
    private final long totalTime;
    
    public CouchDbQueryStatInfo(String operationType, String operationInfo, long count, long avgOperationTime, long maxOperationTime, long minOperationTime, long totalTime) {
        this.operationType = operationType;
        this.operationInfo = operationInfo;
        this.count = count;
        this.avgOperationTime = avgOperationTime;
        this.maxOperationTime = maxOperationTime;
        this.minOperationTime = minOperationTime;
        this.totalTime = totalTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getOperationInfo() {
        return operationInfo;
    }
    
    public long getCount() {
        return count;
    }

    public long getAvgOperationTime() {
        return avgOperationTime;
    }

    public long getMaxOperationTime() {
        return maxOperationTime;
    }

    public long getMinOperationTime() {
        return minOperationTime;
    }
    
    public long getTotalTime() {
        return totalTime;
    }
}
