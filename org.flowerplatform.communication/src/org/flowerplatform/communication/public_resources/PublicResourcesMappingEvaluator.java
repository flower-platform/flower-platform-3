package org.flowerplatform.communication.public_resources;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.common.util.RunnableWithParam;

/**
 * @author Cristi
 */
public class PublicResourcesMappingEvaluator implements
		RunnableWithParam<Boolean, HttpServletRequest> {

	@Override
	public Boolean run(HttpServletRequest param) {
		return param.getPathInfo().startsWith(PublicResourcesServlet.PATH_PREFIX);
	}

}
