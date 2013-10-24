package org.flowerplatform.properties.action {
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.properties.PropertiesPlugin;
	import org.flowerplatform.properties.PropertiesViewProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ShowPropertiesAction extends ActionBase {
		
		public function ShowPropertiesAction(){
			super();
			label = PropertiesPlugin.getInstance().getMessage("action.properties");
			icon = PropertiesPlugin.getInstance().getResourceUrl("images/properties.gif");
			orderIndex = 500;
		}
			
		override public function get visible():Boolean {
			return true;
		}
		
		override public function run():void {	
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(PropertiesViewProvider.ID)
				.setWidth(500)
				.setHeight(300)
				.show();
		}
	}
}