package org.protobeans.couchdb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.protobeans.core.bean.ProtobeansContext;
import org.protobeans.core.model.CouchDbQueryStatInfo;
import org.springframework.beans.factory.annotation.Autowired;

import com.equiron.acc.CouchDbOperationStats;
import com.equiron.acc.OperationProfile;

public class CouchDbStatsService {
    @Autowired
    private ProtobeansContext protobeansContext;
    
    @PostConstruct
    public void init() {
        Supplier<List<CouchDbQueryStatInfo>> statsSupplier = () -> {
            List<CouchDbQueryStatInfo> stats = new ArrayList<>();
            
            for (OperationProfile profile : CouchDbOperationStats.getOperationsMap().values()) {
                stats.add(new CouchDbQueryStatInfo(profile.getOperationType().toString(), profile.getOperationInfo(), profile.getCount(), profile.getAvgOperationTime(), profile.getMaxOperationTime(), profile.getMinOperationTime(), profile.getTotalTime()));
            }
            
            Collections.sort(stats, Comparator.comparing(CouchDbQueryStatInfo::getAvgOperationTime).reversed());
            
            return stats;
        };
        
        protobeansContext.putValue(CouchDbQueryStatInfo.QUERY_STATS_KEY, statsSupplier);
    }
}
