package org.protobeans.postgresql.aspect;

public class OperationProfile {
    private long totalTime;
    private long count;
    
    public OperationProfile(long time) {
        setCount(1);
        setTotalTime(time);
    }

    public OperationProfile(long totalTime, long count) {
        this.totalTime = totalTime;
        this.count = count;
    }

    public long getTotalTime() {
        return totalTime;
    }
    
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
    
    public long getCount() {
        return count;
    }
    
    public void setCount(long count) {
        this.count = count;
    }
}

