package org.flowerplatform.editor.model.remote;

import java.util.Map;

import org.flowerplatform.communication.command.AbstractClientCommand;

public class ViewDetailsUpdate {

	private Object viewId;
	
	private Map<String, Object> viewDetails;

	public Object getViewId() {
		return viewId;
	}

	public void setViewId(Object viewId) {
		this.viewId = viewId;
	}

	public Map<String, Object> getViewDetails() {
		return viewDetails;
	}

	public void setViewDetails(Map<String, Object> viewDetails) {
		this.viewDetails = viewDetails;
	}
	
}
