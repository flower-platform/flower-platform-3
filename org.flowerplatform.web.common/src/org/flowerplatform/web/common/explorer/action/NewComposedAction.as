package org.flowerplatform.web.common.explorer.action {
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NewComposedAction extends org.flowerplatform.flexutil.action.ComposedAction {
		
		public function NewComposedAction() {
			super();
			id = "new";
			label = WebCommonPlugin.getInstance().getMessage("new.action");
			icon = WebCommonPlugin.getInstance().getResourceUrl("/images/common/add.png");
			orderIndex = 100;
		}
	}
}