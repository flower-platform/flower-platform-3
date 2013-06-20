package org.flowerplatform.web.common.security.ui {
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Mariana
	 */
	public class UserFormViewProvider implements IViewProvider {
		
		public static const ID:String = "userForm"; 
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return null;
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return null;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}