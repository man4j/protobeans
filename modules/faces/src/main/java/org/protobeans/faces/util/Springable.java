package org.protobeans.faces.util;

import java.io.Serializable;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class Springable implements Serializable {    
    public Springable() {
	    AutowireCapableBeanFactory ctx = WebApplicationContextUtils.getApplicationContext().getAutowireCapableBeanFactory();
	    
	    ctx.autowireBean(this);
	}
}
