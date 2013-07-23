package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Node;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ExpandOperationsCompartmentAction extends ExpandCompartmentAction {
		
		public function ExpandOperationsCompartmentAction() {
			super();
			
			label = "Expand Operations";
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_expandCompartment_operations(node.id);
		}
	}
}