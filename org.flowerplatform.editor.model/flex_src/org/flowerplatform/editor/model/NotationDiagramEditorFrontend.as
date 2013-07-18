package org.flowerplatform.editor.model
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.tool.DragToCreateRelationTool;
	import org.flowerplatform.flexdiagram.tool.DragTool;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ResizeTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	
	import spark.components.Button;
	import spark.components.TextInput;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class NotationDiagramEditorFrontend extends DiagramEditorFrontend {
				
		override protected function createChildren():void {			
			super.createChildren();
			
			var testButton:Button = new Button();
			resourceStatusBar.addChild(testButton);
			testButton.label = "Test/Rename class";
			
			var textInput:TextInput = new TextInput();
			resourceStatusBar.addChild(textInput);
			textInput.text = "MyTest";
			
			testButton.addEventListener(MouseEvent.CLICK, function (event:Event):void {
				NotationDiagramEditorStatefulClient(editorStatefulClient).service_setInplaceEditorText(View(diagramShell.selectedItems.getItemAt(0)).id, textInput.text);
			});
			
			testButton = new Button();
			resourceStatusBar.addChild(testButton);
			testButton.label = "Test/Expand attributes";
			testButton.addEventListener(MouseEvent.CLICK, function (event:Event):void {
				NotationDiagramEditorStatefulClient(editorStatefulClient).service_expandCompartment(View(diagramShell.selectedItems.getItemAt(0)).id);
			});
		}
		
		override protected function getDiagramShellInstance():DiagramShell {
			var diagramShell:NotationDiagramShell = new NotationDiagramShell();
			diagramShell.editorStatefulClient = DiagramEditorStatefulClient(editorStatefulClient);
			
			diagramShell.registerTools([
				ScrollTool, SelectOnClickTool, InplaceEditorTool, ResizeTool, 
				DragToCreateRelationTool, DragTool/*, SelectOrDragToCreateElementTool*/, ZoomTool]);
			return diagramShell;
		}
		
	}
}