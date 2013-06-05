package org.flowerplatform.communication;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.common.util.RunnableWithParam;

/**
 * @author Cristi
 */
public class ServletMapping {
	public int priority;
	public RunnableWithParam<Boolean, HttpServletRequest> mappingEvaluator;
	public Servlet servlet;
	
}
