/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.blazeds;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.common.util.RunnableWithParam;

public class MessageBrokerMappingEvaluator implements RunnableWithParam<Boolean, HttpServletRequest> {

	@Override
	public Boolean run(HttpServletRequest param) {
		// when the mapping will be on /servlet/*, the next commented line should be used
//		return param.getPathInfo().startsWith("/messagebroker");
		return param.getServletPath().endsWith("/messagebroker");
	}

}