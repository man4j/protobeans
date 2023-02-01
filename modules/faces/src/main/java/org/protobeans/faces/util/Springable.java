package org.protobeans.faces.util;

import java.io.Serializable;

import org.protobeans.faces.config.ProtobeansFacesConfig;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class Springable implements Serializable {    
    public Springable() {
	    AutowireCapableBeanFactory ctx = ProtobeansFacesConfig.springContext.getAutowireCapableBeanFactory();
	    
	    ctx.autowireBean(this);
	}
}
