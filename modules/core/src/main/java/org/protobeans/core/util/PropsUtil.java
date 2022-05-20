package org.protobeans.core.util;

public class PropsUtil {
	public static String getProp(String prop) {
		String value = System.getProperty(prop);
		
		if (value == null) {
			value = System.getenv(prop);
		}
		
		return value;
	}
}
