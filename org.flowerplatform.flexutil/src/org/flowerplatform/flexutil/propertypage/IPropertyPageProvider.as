package org.flowerplatform.flexutil.propertypage {
	import mx.core.IVisualElement;

	public interface IPropertyPageProvider {
		
		function getLabel():String;
		function getPage():IPropertyPage;
		
	}
}