package org.protobeans.faces.config.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.io.Serializable;

public class Springable implements Serializable {    
	@PostConstruct
    protected void init() {
	    AutowireCapableBeanFactory ctx = WebApplicationContextUtils.getApplicationContext().getAutowireCapableBeanFactory();
	    
	    ctx.autowireBean(this);
	}
}
