package org.flowerplatform.web.common.communication {
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Mariana
	 */
	public class AuthenticationViewProvider implements IViewProvider {
		
		public static const ID:String = "authenticationView"; 
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			return new AuthenticationView();
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return "Login";
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return WebCommonPlugin.getInstance().getResourceUrl("images/login.png");
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}