package org.protobeans.mvc.controller;

import java.util.List;
import java.util.function.Supplier;

import org.protobeans.core.bean.ProtobeansContext;
import org.protobeans.core.model.CouchDbQueryStatInfo;
import org.protobeans.core.model.HibernateQueryStatInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/protobeans")
public class StatsController {
    @Autowired
    private ProtobeansContext context;
    
    @GetMapping("/stats/hibernate")
    String getHibernateQueryStats(Model model) {
        Supplier<List<HibernateQueryStatInfo>> statsSupplier = context.getValue(HibernateQueryStatInfo.QUERY_STATS_KEY);
        
        model.addAttribute("queryList", statsSupplier.get());
        
        return "/hibernateQueryStats";
    }
    
    @GetMapping("/stats/couchdb")
    String getCouchDbQueryStats(Model model) {
        Supplier<List<CouchDbQueryStatInfo>> statsSupplier = context.getValue(CouchDbQueryStatInfo.QUERY_STATS_KEY);
        
        model.addAttribute("queryList", statsSupplier.get());
        
        return "/couchdbQueryStats";
    }
}
