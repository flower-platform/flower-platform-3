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

	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class DragToCreateRelationController extends ControllerBase implements IDragToCreateRelationController {
	
		public function DragToCreateRelationController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			trace ("activate " + model);
		}
		
		public function drag(model:Object):void {
			trace("update");
		}
		
		public function drop(model:Object):void {
			trace("drop " + model);
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
				.service_addNewConnection(diagramShell.mainTool.context.model.id, model.id);
			
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			trace("deactivate");
		}
	}
}