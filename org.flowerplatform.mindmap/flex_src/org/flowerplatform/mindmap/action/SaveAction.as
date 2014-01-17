package org.flowerplatform.mindmap.action {
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.mindmap.MindMapPlugin;
	import org.flowerplatform.mindmap.controller.NodeController;
	import org.flowerplatform.mindmap.remote.Node;
	
	public class SaveAction extends ActionBase {
		
		public function SaveAction() {			
			label = "Save";
			preferShowOnActionBar = true;
		}
				
		override public function get visible():Boolean {			
			return true;
		}
		
		override public function run():void {
			MindMapPlugin.getInstance().service.save();
		}
				
	}
}