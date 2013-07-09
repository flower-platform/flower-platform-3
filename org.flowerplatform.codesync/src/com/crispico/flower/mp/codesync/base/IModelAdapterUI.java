package com.crispico.flower.mp.codesync.base;

import java.util.List;

public interface IModelAdapterUI {

	public boolean hasChildren(Object modelElement);

	public List<?> getChildren(Object modelElement);

	public String getLabel(Object modelElement);

	public List<String> getIconUrls(Object modelElement);
	
}