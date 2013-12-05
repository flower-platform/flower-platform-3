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
package org.flowerplatform.codesync.regex.remote.command;

import org.flowerplatform.communication.command.AbstractClientCommand;

/**
 * @author Cristina Constantinescu
 */
public class RegexCommand extends AbstractClientCommand {

	public static final String REFRESH_CONFIGS = "refreshConfigs";
	public static final String REFRESH_MACROS = "refreshMacros";
	public static final String REFRESH_PARSERS = "refreshParsers";
	
	private String operation;
		
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
		
}
