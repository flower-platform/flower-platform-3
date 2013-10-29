/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation version 3.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
* 
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.editor.model.controller {
	
	import flash.display.DisplayObject;
	import flash.geom.Rectangle;
	
	import mx.core.ClassFactory;
	
	import org.flowerplatform.editor.model.content_assist.NotationDiagramContentAssistProvider;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexutil.content_assist.ContentAssistListTextAreaSkin;
	import org.flowerplatform.flexutil.text.AutoGrowSkinnableTextBaseSkin;
	
	import spark.components.TextArea;
	import spark.components.TextInput;
	import spark.components.supportClasses.SkinnableTextBase;
	import spark.components.supportClasses.StyleableTextField;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class InplaceEditorController extends ControllerBase implements IInplaceEditorController {
		
		public var rendererClass:Class;
		
		public function InplaceEditorController(diagramShell:DiagramShell) {
			super(diagramShell);		
		}
		
		public function canActivate(model:Object):Boolean	{		
			return true;
		}
		
		public function activate(model:Object):void {
			var renderer:DisplayObject = DisplayObject(diagramShell.getRendererForModel(model));
			var textField:SkinnableTextBase = new rendererClass();
			textField.setStyle("skinClass", AutoGrowSkinnableTextBaseSkin);
			
			// TODO CC: to reactivate
//			var skinClassFactory:ClassFactory = new ClassFactory();
//			skinClassFactory.generator = ContentAssistListTextAreaSkin;
//			skinClassFactory.properties = new Object();
//			skinClassFactory.properties.contentAssistProvider = new NotationDiagramContentAssistProvider(Node(model).id);
//			textField.setStyle("skinFactory", skinClassFactory);
			
			diagramShell.diagramRenderer.addElement(textField);
			
			var bounds:Rectangle = renderer.getBounds(DisplayObject(diagramShell.diagramRenderer));
			textField.x = bounds.x + 2;
			textField.y = bounds.y;
			
			if (textField is TextInput) {
				textField.minWidth = bounds.width; // resize width if long text
			} else {
				textField.width = bounds.width;
			}			
			textField.minHeight = bounds.height;
			
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_getInplaceEditorText(Node(model).id, function(text:String):void {
				if (text != null) {
					textField.text = text;
					textField.selectRange(text.length, text.length);
				}
			});
			textField.callLater(textField.setFocus);
			
			diagramShell.modelToExtraInfoMap[model].inplaceEditor = textField;
		}
		
		public function commit(model:Object):void {		
			var textField:SkinnableTextBase = diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_setInplaceEditorText(Node(model).id, textField.text);
			
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function abort(model:Object):void {
			// here can be placed a warning
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			var textField:SkinnableTextBase = diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			diagramShell.diagramRenderer.removeElement(textField);
			
			delete diagramShell.modelToExtraInfoMap[model].inplaceEditor;			
		}		
	}
}