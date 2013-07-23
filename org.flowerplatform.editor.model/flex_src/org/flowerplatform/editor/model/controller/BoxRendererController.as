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
	import mx.core.IVisualElement;
	
	import org.flowerplatform.editor.model.renderer.BoxRenderer;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BoxRendererController extends ClassReferenceRendererController {
		
		public function BoxRendererController(diagramShell:DiagramShell) {
			super(diagramShell, BoxRenderer);
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
//			IEventDispatcher(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {
//			if (isModelDisposed) {
//				if (renderer != null) {
//					IVisualElementContainer(renderer.parent).removeElement(renderer);
//				}
//				IEventDispatcher(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
//			} else {
//				// weak referenced. In theory, this is not needed, but to be sure...
//				// The only case where it would make sense: if the model children controller fails to inform us of a disposal;
//				// but in this case, the stray model may be as well left on the diagram 
//				IEventDispatcher(model).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler, false, 0, true);
//			}
		}
		
//		private function modelChangedHandler(event:PropertyChangeEvent):void {
//			if (event.property == "x" || event.property == "y" || event.property == "height" || event.property == "width") {
//				diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
//			}
//		}
	}
}