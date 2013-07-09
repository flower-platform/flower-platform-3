package org.flowerplatform.flexutil.propertypage {
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	public interface IPropertyPage extends IVisualElement {
		
		function setSelectedNode(value:Object):void;
		function okHandler():void;
		function cancelHandler():void;
		
	}
}