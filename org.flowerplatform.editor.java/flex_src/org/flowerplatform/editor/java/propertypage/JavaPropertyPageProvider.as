package org.flowerplatform.editor.java.propertypage
{
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.propertypage.IPropertyPage;
	import org.flowerplatform.flexutil.propertypage.IPropertyPageProvider;
	
	public class JavaPropertyPageProvider implements IPropertyPageProvider {
				
		public function getLabel():String{
			return "Java";
		}
		
		public function getPage():IPropertyPage {
			return new JavaPropertyPage();
		}
	}
}