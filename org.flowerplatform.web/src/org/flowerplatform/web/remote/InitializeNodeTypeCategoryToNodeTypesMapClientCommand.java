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
package org.flowerplatform.web.remote;

import java.util.List;
import java.util.Map;

import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.web.WebPlugin;

public class InitializeNodeTypeCategoryToNodeTypesMapClientCommand extends AbstractClientCommand {
	private Map<String, List<String>> nodeTypeCategoryToNodeTypesMap;

	public Map<String, List<String>> getNodeTypeCategoryToNodeTypesMap() {
		return nodeTypeCategoryToNodeTypesMap;
	}

	public void setNodeTypeCategoryToNodeTypesMap(Map<String, List<String>> nodeTypeCategoryToNodeTypesMap) {
		this.nodeTypeCategoryToNodeTypesMap = nodeTypeCategoryToNodeTypesMap;
	}

	public InitializeNodeTypeCategoryToNodeTypesMapClientCommand() {
		super();
		nodeTypeCategoryToNodeTypesMap = WebPlugin.getInstance().getNodeTypeCategoryToNodeTypesMap();
	}
	
	
}