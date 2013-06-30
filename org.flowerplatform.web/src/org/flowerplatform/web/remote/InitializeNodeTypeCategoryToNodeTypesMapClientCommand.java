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
