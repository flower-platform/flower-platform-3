package org.flowerplatform.web.security.ui
{
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.web.common.security.ui.UserFormViewProvider;
	
	/**
	 * @author Mariana
	 */
	public class UserFormViewProvider extends org.flowerplatform.web.common.security.ui.UserFormViewProvider {
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			return new UserForm();
		}
	}
}