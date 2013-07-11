package org.flowerplatform.web.common.projects.properties {
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IPropertyPage extends IVisualElement {
		
		function setSelectedNode(value:Object):void;
		function okHandler():void;
		function cancelHandler():void;
		
	}
}