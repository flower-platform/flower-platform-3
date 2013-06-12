package org.flowerplatform.blazeds;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.common.util.RunnableWithParam;

public class MessageBrokerMappingEvaluator implements RunnableWithParam<Boolean, HttpServletRequest> {

	@Override
	public Boolean run(HttpServletRequest param) {
		// when the mapping will be on /servlet/*, the next commented line should be used
//		return param.getPathInfo().startsWith("/messagebroker");
		return param.getServletPath().startsWith("/messagebroker");
	}

}
