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
	
	import flash.geom.Rectangle;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.olap.aggregators.MaxAggregator;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.remote.command.MoveResizeServerCommand;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	import org.flowerplatform.flexdiagram.ui.ResizeAnchor;
	
	public class ResizeController extends ControllerBase implements IResizeController {
		
		public function ResizeController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			var bounds:Rectangle = diagramShell.getControllerProvider(model).getAbsoluteLayoutRectangleController(model).getBounds(model);
			var resizePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			
			resizePlaceHolder.x = bounds.x;
			resizePlaceHolder.y = bounds.y;
			resizePlaceHolder.width = bounds.width;
			resizePlaceHolder.height = bounds.height;
			
			diagramShell.modelToExtraInfoMap[model].resizePlaceHolder = resizePlaceHolder;
			diagramShell.diagramRenderer.addElement(resizePlaceHolder);
			
			diagramShell.modelToExtraInfoMap[model].initialX = resizePlaceHolder.x;
			diagramShell.modelToExtraInfoMap[model].initialY = resizePlaceHolder.y;
			diagramShell.modelToExtraInfoMap[model].initialWidth = resizePlaceHolder.width;
			diagramShell.modelToExtraInfoMap[model].initialHeight = resizePlaceHolder.height;
		}
			
		public function drag(model:Object, deltaX:Number, deltaY:Number, type:String):void {	
			var resizePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].resizePlaceHolder;
			
			var newX:int = resizePlaceHolder.x, newY:int = resizePlaceHolder.y;
			var newWidth:int = resizePlaceHolder.width, newHeight:int = resizePlaceHolder.height;
			switch(type) {
				case ResizeAnchor.LEFT_UP: 
					newX = diagramShell.modelToExtraInfoMap[model].initialX + deltaX;
					newY = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;					
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth - (newX - diagramShell.modelToExtraInfoMap[model].initialX);
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight - (newY - diagramShell.modelToExtraInfoMap[model].initialY);
					break;
				case ResizeAnchor.LEFT_MIDDLE:
					newX = diagramShell.modelToExtraInfoMap[model].initialX + deltaX;
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth - (newX - diagramShell.modelToExtraInfoMap[model].initialX);
					break;
				case ResizeAnchor.LEFT_DOWN:
					newX = diagramShell.modelToExtraInfoMap[model].initialX + deltaX; 
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth - (newX - diagramShell.modelToExtraInfoMap[model].initialX);
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight + deltaY;
					break;
				case ResizeAnchor.MIDDLE_DOWN:
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight + deltaY;
					break;
				case ResizeAnchor.MIDDLE_UP:
					newY = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight - (newY - diagramShell.modelToExtraInfoMap[model].initialY);
					break;
				case ResizeAnchor.RIGHT_DOWN:
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth + deltaX;
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight + deltaY;
					break;
				case ResizeAnchor.RIGHT_MIDDLE:
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth + deltaX;
					break;
				case ResizeAnchor.RIGHT_UP:
					newY = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth + deltaX;
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight - (newY - diagramShell.modelToExtraInfoMap[model].initialY);
					break;
			}

			var renderer:IVisualElement = diagramShell.getRendererForModel(model);
			resizePlaceHolder.x = newX;
			resizePlaceHolder.y = newY;
			resizePlaceHolder.width = Math.max(newWidth, UIComponent(renderer).minWidth);
			resizePlaceHolder.height = Math.max(newHeight, UIComponent(renderer).minHeight);
		}
		
		public function drop(model:Object):void {	
			var resizePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].resizePlaceHolder;
									
			var command:MoveResizeServerCommand = new MoveResizeServerCommand();
			command.id = Node(model).layoutConstraint_RH.referenceIdAsString;
			command.newWidth = resizePlaceHolder.width;
			command.newHeight = resizePlaceHolder.height;
			command.newX = resizePlaceHolder.x;
			command.newY = resizePlaceHolder.y;						
			NotationDiagramShell(diagramShell).editorStatefulClient.attemptUpdateContent(null, command);
		}
		
		public function deactivate(model:Object):void {
			diagramShell.diagramRenderer.removeElement(diagramShell.modelToExtraInfoMap[model].resizePlaceHolder);			
			delete diagramShell.modelToExtraInfoMap[model].resizePlaceHolder;
			
			delete diagramShell.modelToExtraInfoMap[model].initialX;
			delete diagramShell.modelToExtraInfoMap[model].initialY;
			delete diagramShell.modelToExtraInfoMap[model].initialWidth;
			delete diagramShell.modelToExtraInfoMap[model].initialHeight;
		}
	}
}