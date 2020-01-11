package org.protobeans.core.model;

public class HibernateQueryStatInfo {
    public static final String QUERY_STATS_KEY = "QUERY_STATS";
    
    private String query;
    
    private long minTime;
    
    private long maxTime;
    
    private long avgTime;
    
    private long count;

    public HibernateQueryStatInfo(String query, long minTime, long maxTime, long avgTime, long count) {
        this.query = query;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.avgTime = avgTime;
        this.count = count;
    }
    
    public String getQuery() {
        return query;
    }

    public long getMinTime() {
        return minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getAvgTime() {
        return avgTime;
    }

    public long getCount() {
        return count;
    }
}
