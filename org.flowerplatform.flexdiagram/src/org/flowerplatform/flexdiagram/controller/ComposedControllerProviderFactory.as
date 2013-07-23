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
package org.flowerplatform.flexdiagram.controller {
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.ISelectOrDragToCreateElementController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ComposedControllerProviderFactory {
		
		public var visualChildrenControllerClass:ControllerFactory;
		public var modelExtraInfoControllerClass:ControllerFactory;
		public var modelChildrenControllerClass:ControllerFactory;
		public var absoluteLayoutRectangleControllerClass:ControllerFactory;
		public var rendererControllerClass:ControllerFactory;
		public var selectionControllerClass:ControllerFactory;
		
		public var inplaceEditorControllerClass:ControllerFactory;
		public var resizeControllerClass:ControllerFactory;
		public var dragToCreateRelationControllerClass:ControllerFactory;
		public var dragControllerClass:ControllerFactory;
		public var selectOrDragToCreateElementControllerClass:ControllerFactory;
		
		public function createComposedControllerProvider(shell:DiagramShell):ComposedControllerProvider {
			var result:ComposedControllerProvider = new ComposedControllerProvider();
			if (visualChildrenControllerClass != null) {
				result.visualChildrenController = visualChildrenControllerClass.newInstance(shell);
			}
			if (modelExtraInfoControllerClass != null) {
				result.modelExtraInfoController = modelExtraInfoControllerClass.newInstance(shell);
			}
			if (modelChildrenControllerClass != null) {
				result.modelChildrenController = modelChildrenControllerClass.newInstance(shell);
			}
			if (absoluteLayoutRectangleControllerClass != null) {
				result.absoluteLayoutRectangleController = absoluteLayoutRectangleControllerClass.newInstance(shell);
			}
			if (rendererControllerClass != null) {
				result.rendererController = rendererControllerClass.newInstance(shell);
			}
			if (selectionControllerClass != null) {
				result.selectionController = selectionControllerClass.newInstance(shell);
			}
			
			if (inplaceEditorControllerClass != null) {
				result.inplaceEditorController = inplaceEditorControllerClass.newInstance(shell);
			}
			if (resizeControllerClass != null) {
				result.resizeController = resizeControllerClass.newInstance(shell);
			}
			if (dragToCreateRelationControllerClass != null) {
				result.dragToCreateRelationController = dragToCreateRelationControllerClass.newInstance(shell);
			}
			if (dragControllerClass != null) {
				result.dragController = dragControllerClass.newInstance(shell);
			}
			if (selectOrDragToCreateElementControllerClass != null) {
				result.selectOrDragToCreateElementController = selectOrDragToCreateElementControllerClass.newInstance(shell);
			}
			return result;
		}

	}
}