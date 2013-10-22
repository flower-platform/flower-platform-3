package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.properties.PropertiesPlugin;
	import org.flowerplatform.properties.ui.PropertiesList;
	import org.flowerplatform.properties.ui.PropertiesViewProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ShowPropertiesAction extends ActionBase {
		
		public function ShowPropertiesAction(){
			super();
			label = EditorModelPlugin.getInstance().getMessage("action.properties");
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/properties.gif");
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