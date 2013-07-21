package org.flowerplatform.editor.model.remote.scenario {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ScenarioTreeStatefulClient extends GenericTreeStatefulClient {
		
		public var diagramStatefulClient:NotationDiagramEditorStatefulClient;
		
		public function ScenarioTreeStatefulClient() {
			super();
			
			statefulServiceId = "diagramEditorStatefulService";
		}
		
		override public function openNode(node:Object, resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			var path:ArrayCollection;
			if (node is TreeNode && node != null) {
				path = TreeNode(node).getPathForNode();
			} else {
				path = ArrayCollection(node);
			}
			
			context["diagramEditableResourcePath"] = diagramStatefulClient.editableResourcePath;
			diagramStatefulClient.service_openScenarioNode(path, context);
		}
		
	}
}