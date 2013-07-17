package org.flowerplatform.editor.mindmap.controller {
	import flash.display.DisplayObject;
	import flash.geom.Rectangle;
	
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	
	import spark.components.TextInput;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapNodeInplaceEditorController extends ControllerBase implements IInplaceEditorController {
		
		public function MindMapNodeInplaceEditorController(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
				
		public function activate(model:Object):void {
			var renderer:DisplayObject = DisplayObject(diagramShell.getRendererForModel(model));
			var textField:TextInput = new TextInput();
			
			diagramShell.diagramRenderer.addElement(textField);
			
			var bounds:Rectangle = renderer.getBounds(DisplayObject(diagramShell.diagramRenderer));
			textField.x = bounds.x + 2;
			textField.y = bounds.y;
			textField.width = bounds.width;
			textField.height = bounds.height;
			textField.text = MindMapNode(model).viewDetails.text;
			textField.callLater(textField.setFocus);
			
			diagramShell.modelToExtraInfoMap[model].inplaceEditor = textField;
		}
		
		public function commit(model:Object):void {		
			var textField:TextInput = diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			MindMapNode(model).viewDetails.text = textField.text;
			
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function abort(model:Object):void {
			// here can be placed a warning
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			var textField:TextInput = diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			diagramShell.diagramRenderer.removeElement(textField);
			
			delete diagramShell.modelToExtraInfoMap[model].inplaceEditor;			
		}
	}
	
}