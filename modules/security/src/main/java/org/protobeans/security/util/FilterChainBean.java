package org.protobeans.security.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

public class FilterChainBean {
    private List<Filter> filters = new ArrayList<>();
    
    public FilterChainBean addFilter(Filter filter) {
        filters.add(filter);
        
        return this;
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
