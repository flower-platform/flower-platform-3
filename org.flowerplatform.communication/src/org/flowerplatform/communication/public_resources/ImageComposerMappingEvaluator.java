package org.flowerplatform.communication.public_resources;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.common.util.RunnableWithParam;

public class ImageComposerMappingEvaluator implements RunnableWithParam<Boolean, HttpServletRequest> {

	@Override
	public Boolean run(HttpServletRequest param) {
		return param.getPathInfo().startsWith(ImageComposerServlet.PATH_PREFIX);
	}

}
