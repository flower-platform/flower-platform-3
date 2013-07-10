package org.flowerplatform.web.common.projects.properties {
	import mx.core.IVisualElement;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IPropertyPageProvider {
		
		function getLabel():String;
		function getPage():IPropertyPage;		
		
	}
}