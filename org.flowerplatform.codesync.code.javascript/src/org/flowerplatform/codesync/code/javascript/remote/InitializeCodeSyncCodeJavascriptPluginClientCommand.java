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
package org.flowerplatform.codesync.code.javascript.remote;

import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;
import org.flowerplatform.communication.command.AbstractClientCommand;

/**
 * @author Mariana Gheorghe
 */
public class InitializeCodeSyncCodeJavascriptPluginClientCommand extends
		AbstractClientCommand {

	private Object availableTemplates;
	
	public Object getAvailableTemplates() {
		return availableTemplates;
	}

	public void setAvailableTemplates(Object availableTemplates) {
		this.availableTemplates = availableTemplates;
	}
	
	public InitializeCodeSyncCodeJavascriptPluginClientCommand() {
		super();
		availableTemplates = CodeSyncCodeJavascriptPlugin.getInstance().getAvailableTemplates();
	}

}
