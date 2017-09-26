package org.protobeans.mvc.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

public class MvcFilterChainBean {
    private List<Filter> filters = new ArrayList<>();
    
    public MvcFilterChainBean addFilter(Filter filter) {
        filters.add(filter);
        
        return this;
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
