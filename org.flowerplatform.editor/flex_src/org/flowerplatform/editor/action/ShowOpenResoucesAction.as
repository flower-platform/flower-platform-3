package org.flowerplatform.editor.action {
	
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.open_resources_view.OpenResourcesViewProvider;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	
	/**
	 * @author 
	 */ 
	public class ShowOpenResoucesAction extends ActionBase {
		
		public function ShowOpenResoucesAction(){
			super();
			label = EditorPlugin.getInstance().getMessage("action.properties");
			icon = EditorPlugin.getInstance().getResourceUrl("images/open_resources_view.png");
			orderIndex = 500;
		}
			
		override public function get visible():Boolean {
			return true;
		}
		
		override public function run():void {	
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(OpenResourcesViewProvider.ID)
				.setWidth(500)
				.setHeight(300)
				.show();
		}
	}
}