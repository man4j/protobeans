package org.protobeans.hibernate.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;
import org.protobeans.core.bean.ProtobeansContext;
import org.protobeans.core.model.QueryStatInfo;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateStatsService {
    @Autowired
    private EntityManagerFactory emf;
    
    @Autowired
    private ProtobeansContext protobeansContext;
    
    @SuppressWarnings("resource")
    @PostConstruct
    public void init() {
        Supplier<List<QueryStatInfo>> statsSupplier = () -> {
            SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
            
            Statistics statistics = sessionFactory.getStatistics();
            
            List<QueryStatInfo> stats = new ArrayList<>();
            
            for (String query : statistics.getQueries()) {
                QueryStatistics queryStatistics = statistics.getQueryStatistics(query);
                
                stats.add(new QueryStatInfo(query, queryStatistics.getExecutionMinTime(), queryStatistics.getExecutionMaxTime(), queryStatistics.getExecutionAvgTime(), queryStatistics.getExecutionCount()));
            }
            
            Collections.sort(stats, Comparator.comparing(QueryStatInfo::getAvgTime).reversed());
            
            return stats;
        };
        
        protobeansContext.putValue(QueryStatInfo.QUERY_STATS_KEY, statsSupplier);
    }
}
