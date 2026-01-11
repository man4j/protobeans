package org.protobeans.mvc.interceptor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}


