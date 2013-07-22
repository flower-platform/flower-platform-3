package org.flowerplatform.editor.model
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import spark.components.Button;
	import spark.components.TextInput;
	
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.scenario.ScenarioTreeStatefulClient;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.tool.DragToCreateRelationTool;
	import org.flowerplatform.flexdiagram.tool.DragTool;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ResizeTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	import org.flowerplatform.flexutil.popup.IAction;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class NotationDiagramEditorFrontend extends DiagramEditorFrontend {
				
		override protected function createChildren():void {			
			super.createChildren();
			
//			var testButton:Button = new Button();
//			resourceStatusBar.addChild(testButton);
//			testButton.label = "Test/Rename class";
//			
//			var textInput:TextInput = new TextInput();
//			resourceStatusBar.addChild(textInput);
//			textInput.text = "MyTest";
//			
//			testButton.addEventListener(MouseEvent.CLICK, function (event:Event):void {
//				NotationDiagramEditorStatefulClient(editorStatefulClient).service_setInplaceEditorText(View(diagramShell.selectedItems.getItemAt(0)).id, textInput.text);
//			});
//			
//			testButton = new Button();
//			resourceStatusBar.addChild(testButton);
//			testButton.label = "Test/Expand attributes";
//			testButton.addEventListener(MouseEvent.CLICK, function (event:Event):void {
//				NotationDiagramEditorStatefulClient(editorStatefulClient).service_expandCompartment_attributes(View(diagramShell.selectedItems.getItemAt(0)).id);
//			});
//			
//			testButton = new Button();
//			resourceStatusBar.addChild(testButton);
//			testButton.label = "Test/Expand operations";
//			testButton.addEventListener(MouseEvent.CLICK, function (event:Event):void {
//				NotationDiagramEditorStatefulClient(editorStatefulClient).service_expandCompartment_operations(View(diagramShell.selectedItems.getItemAt(0)).id);
//			});
//			
//			testButton = new Button();
//			resourceStatusBar.addChild(testButton);
//			testButton.label = "Test/Add Connection";
//			testButton.addEventListener(MouseEvent.CLICK, function (event:Event):void {
//				NotationDiagramEditorStatefulClient(editorStatefulClient).service_addNewConnection(
//					View(diagramShell.selectedItems.getItemAt(0)).id,
//					View(diagramShell.selectedItems.getItemAt(1)).id);
//			});
			
			var scenarioTree:GenericTreeList = new GenericTreeList();
			scenarioTree.percentHeight = 100;
			scenarioTree.right = 0;
			var treeClient:ScenarioTreeStatefulClient = new ScenarioTreeStatefulClient();
			treeClient.diagramStatefulClient = NotationDiagramEditorStatefulClient(editorStatefulClient);
			treeClient.diagramStatefulClient.scenarioTreeStatefulClient = treeClient;
			scenarioTree.statefulClient = treeClient;
			treeClient.treeList = scenarioTree;
			editor.addChild(scenarioTree);
			
			var root:TreeNode = new TreeNode();
			root.children = new ArrayCollection();
			root.hasChildren = true;
			scenarioTree.rootNode = root;
			scenarioTree.statefulClient.openNode(root);
		}
		
		override protected function getDiagramShellInstance():DiagramShell {
			var diagramShell:NotationDiagramShell = new NotationDiagramShell();
			diagramShell.editorStatefulClient = DiagramEditorStatefulClient(editorStatefulClient);
			
			diagramShell.registerTools([
				ScrollTool, SelectOnClickTool, InplaceEditorTool, ResizeTool, 
				DragToCreateRelationTool, DragTool/*, SelectOrDragToCreateElementTool*/, ZoomTool]);
			return diagramShell;
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		override public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			var actions:Vector.<IAction> = EditorModelPlugin.getInstance().notationDiagramClassFactoryActionProvider.getActions(selection);
			if (actions != null) {
				for each (var action:IAction in actions) {
					result.push(action);
				}
			}		
			return result;
		}
		
		
		
	}
}