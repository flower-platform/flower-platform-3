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
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.renderer.ConnectionRenderer;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.renderer.connection.BindablePoint;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class DragToCreateRelationController extends ControllerBase implements IDragToCreateRelationController {
	
		public function DragToCreateRelationController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			// create temp connection
			var connection:ConnectionRenderer = new ConnectionRenderer();
			var modelRenderer:IVisualElement = diagramShell.getRendererForModel(model);
			var rect:Rectangle = DisplayObject(modelRenderer).getBounds(DisplayObject(diagramShell.diagramRenderer));
			var x:int = rect.x + rect.width / 2;
			var y:int = rect.y + rect.height / 2;
			connection._sourcePoint.x = connection._targetPoint.x = x;
			connection._sourcePoint.y = connection._targetPoint.y = y;
			
			// add to map
			diagramShell.modelToExtraInfoMap[model].tempConnection = connection;
			
			// add to diagram
			diagramShell.diagramRenderer.addElement(connection);
		}
		
		public function drag(model:Object, deltaX:Number, deltaY:Number):void {
			var connection:ConnectionRenderer = diagramShell.modelToExtraInfoMap[model].tempConnection;
			
			connection._targetPoint.x = deltaX; 
			connection._targetPoint.y = deltaY;
		}
		
		public function drop(model:Object):void {
			if (model != null) {
				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
					.service_addNewRelation(diagramShell.mainTool.context.model.id, model.id);
			}
			
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			var connection:ConnectionRenderer = diagramShell.modelToExtraInfoMap[model].tempConnection;
			diagramShell.diagramRenderer.removeElement(connection);
			
			delete diagramShell.modelToExtraInfoMap[model].tempConnection;
		}
	}
}