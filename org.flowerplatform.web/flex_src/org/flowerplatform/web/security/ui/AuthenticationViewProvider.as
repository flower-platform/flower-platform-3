package org.flowerplatform.web.security.ui 
{
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.web.common.communication.AuthenticationViewProvider;
	
	/**
	 * @author Mariana
	 */
	public class AuthenticationViewProvider extends org.flowerplatform.web.common.communication.AuthenticationViewProvider {
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			return new AuthenticationPopup();
		}
		
	}
}