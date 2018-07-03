package org.protobeans.mvc.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

public class FilterBean {
    private List<Filter> filters = new ArrayList<>();
    
    public FilterBean(Filter filter) {
        this.filters.add(filter);
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
