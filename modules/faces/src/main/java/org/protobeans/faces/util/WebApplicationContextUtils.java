package org.protobeans.faces.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class WebApplicationContextUtils {
	private static volatile ApplicationContext ctx;
	
	@Autowired
	private ApplicationContext _ctx; 
	
	@PostConstruct
	public void init() {
		ctx = _ctx;
	}
	
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
}
