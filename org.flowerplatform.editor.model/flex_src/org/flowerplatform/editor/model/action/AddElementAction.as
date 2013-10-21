package org.flowerplatform.editor.model.action {
	
	import flash.profiler.showRedrawRegions;
	
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * Added for tests.
	 * MUST BE REMOVED.
	 * @author Cristina Constantinescu
	 */ 
	public class AddElementAction extends NewModelAction  {
		
		public function AddElementAction() {
			super();
			label = "Add Element";
			parentId = "new";
			preferShowOnActionBar = true;
		}
				
		override public function get visible():Boolean {			
			return selection == null || selection.length == 0;
		}
		
//		override public function run():void {
//			super.run();
//			if (context != null) { 
//				if (context.hasOwnProperty("rectangle")) { // exists for drag to create on web
//					trace(context.rectangle);
//				} else { // context x, y exists on mobile
//				}
//			}
//		}
		
	}
}