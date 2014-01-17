package org.flowerplatform.mindmap.action {
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.mindmap.MindMapPlugin;
	import org.flowerplatform.mindmap.controller.NodeController;
	import org.flowerplatform.mindmap.remote.Node;
	
	public class ReloadAction extends ActionBase {
		
		private var editorFrontend:MindMapEditorFrontend;
		
		public function ReloadAction(editorFrontend:MindMapEditorFrontend) {
			this.editorFrontend = editorFrontend;
			label = "Reload";
			preferShowOnActionBar = true;
		}
				
		override public function get visible():Boolean {			
			return true;
		}
		
		override public function run():void {
			MindMapPlugin.getInstance().service.reload(this, reloadCallbackHandler);
		}
		
		private function reloadCallbackHandler(result:Object):void {
			var diagramShell:MindMapDiagramShell = MindMapDiagramShell(editorFrontend.diagramShell);
			
			var rootModel:Object = diagramShell.getControllerProvider(diagramShell.rootModel).getModelChildrenController(diagramShell.rootModel).getChildren(diagramShell.rootModel).getItemAt(0);
			NodeController(IMindMapControllerProvider(diagramShell.getControllerProvider(rootModel)).getMindMapModelController(rootModel)).disposeModel(rootModel);
			
			editorFrontend.requestRootModel();
		}
		
	}
}