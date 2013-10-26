package org.flowerplatform.properties.remote;

import java.util.List;

public class Properties {
	private String label;
	private String icon;
	private List<Property> propertiesList;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public List<Property> getPropertiesList() {
		return propertiesList;
	}
	public void setPropertiesList(List<Property> propertiesList) {
		this.propertiesList = propertiesList;
	}
	
}
