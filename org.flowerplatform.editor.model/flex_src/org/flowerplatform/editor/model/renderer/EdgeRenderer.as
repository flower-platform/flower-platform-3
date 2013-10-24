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
package org.flowerplatform.editor.model.renderer {
	
	import flash.events.Event;
	
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.controller.renderer.ConnectionRendererController;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionRenderer;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class EdgeRenderer extends ConnectionRenderer {
		
		override public function set data(value:Object):void {
			if (data != null) {
				View(data).removeEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
			}
			super.data = value;
			if (data != null) {
				View(data).addEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
				
				if (View(data).viewDetails != null) {
					viewDetailsUpdatedHandler(null);
				}
			}
		}
		
		protected function viewDetailsUpdatedHandler(event:Event):void {
			addOrUpdateMiddleLabel(ConnectionRendererController(diagramShell.getControllerProvider(data).getRendererController(data)));
		}
		
		override protected function getConnectionLabel():String {
			if (data && View(data).viewDetails) {
				return View(data).viewDetails.scenarioInteractionLabel;
			}
			return null;
		}
	}
}