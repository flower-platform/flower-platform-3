package org.flowerplatform.editor.java.propertypage {	
	import org.flowerplatform.web.common.projects.properties.IPropertyPage;
	import org.flowerplatform.web.common.projects.properties.IPropertyPageProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class JavaPropertyPageProvider implements IPropertyPageProvider {
				
		public function getLabel():String{
			return "Java";
		}
		
		public function getPage():IPropertyPage {
			return new JavaPropertyPage();
		}
	}
}