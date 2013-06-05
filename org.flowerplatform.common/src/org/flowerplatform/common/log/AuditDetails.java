package org.flowerplatform.common.log;

import org.slf4j.Logger;

/**
 * @author Mariana
 * @author Cristi
 */
public class AuditDetails {
	
	private Logger logger;
	private long time;
	private long duration;
	private String auditCategory;
	private Object param0;
	private Object param1;
	private Object param2;
	
	public AuditDetails(Logger logger, String auditCategory, Object... params) {
		time = System.currentTimeMillis();
		this.auditCategory = auditCategory;
		this.logger = logger;
		if (params.length > 3) {
			throw new IllegalArgumentException("Too much parameters; max is 3");
		} 
		
		if (params.length > 2) {
			param2 = params[2];
		}
		if (params.length > 1) {
			param1 = params[1];
		}
		if (params.length > 0) {
			param0 = params[0];
		}
	}

	public void measureDuration() {
		duration = System.currentTimeMillis() - time;
	}
	
	public Object getParam0() {
		return param0;
	}

	public Object getParam1() {
		return param1;
	}

	public Object getParam2() {
		return param2;
	}

	public long getTime() {
		return time;
	}

	public long getDuration() {
		return duration;
	}
	
	public String getAuditCategory() {
		return auditCategory;
	}

	public Logger getLogger() {
		return logger;
	}
}