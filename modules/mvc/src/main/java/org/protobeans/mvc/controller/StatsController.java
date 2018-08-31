package org.protobeans.mvc.controller;

import java.util.List;
import java.util.function.Supplier;

import org.protobeans.core.bean.ProtobeansContext;
import org.protobeans.core.model.QueryStatInfo;
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
    
    @GetMapping("/stats/query")
    String getQueryStats(Model model) {
        Supplier<List<QueryStatInfo>> statsSupplier = context.getValue(QueryStatInfo.QUERY_STATS_KEY);
        
        model.addAttribute("queryList", statsSupplier.get());
        
        return "/queryStats";
    }
}
