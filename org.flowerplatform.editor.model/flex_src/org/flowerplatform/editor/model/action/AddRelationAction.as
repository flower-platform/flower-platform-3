package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AddRelationAction extends ActionBase implements IDiagramShellAware {
		
		private var _diagramShell:DiagramShell;
		
		public function AddRelationAction() {
			super();
			label = EditorModelPlugin.getInstance().getMessage("action.addRelation");
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/obj16/Association_none.gif");
			orderIndex = 110;
			parentId = "new";
		}
		
		public function get diagramShell():DiagramShell {		
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}
				
		override public function get visible():Boolean {	
			// single model selected, diagram excluded, and context not null 
			// (the action must be visible when dragToCreateRelationTool active)
			return selection.length == 1 && !(selection.getItemAt(0) is DiagramRenderer) && context != null;
		}
		
		override public function run():void {
			if (!context.hasOwnProperty("sourceId")) {
				throw new Error("Source model id hasn't been set!");	
			}
			if (!context.hasOwnProperty("targetId")) {
				throw new Error("Target model id hasn't been set!");	
			}
			NotationDiagramEditorStatefulClient(NotationDiagramShell(_diagramShell).editorStatefulClient)
				.service_addNewConnection(context.sourceId, context.targetId);
		}
		
	}
}