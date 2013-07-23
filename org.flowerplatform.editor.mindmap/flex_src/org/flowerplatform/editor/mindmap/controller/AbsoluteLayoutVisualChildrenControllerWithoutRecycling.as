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
package org.flowerplatform.editor.mindmap.controller {
	import flash.display.Graphics;
	import flash.display.Sprite;
	import flash.geom.Rectangle;
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IUIComponent;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.messaging.errors.NoChannelAvailableError;
	
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.renderer.IAbsoluteLayoutRenderer;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class AbsoluteLayoutVisualChildrenControllerWithoutRecycling extends ControllerBase implements IVisualChildrenController {
		
		public function AbsoluteLayoutVisualChildrenControllerWithoutRecycling(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		// TODO the commented code was code that handled the order (i.e. depth), in the previous FD implementation/Flex 3
		public function refreshVisualChildren(parentModel:Object):void {
			// log related
			var logTsStart:Number = new Date().time;
			var logNewModels:int = 0;
			var logRenderersReused:int = 0;
			var logReusableRenderersCreated:int = 0;
			var logNonReusableRenderersCreated:int = 0;
			var logReusableRenderersRemoved:int = 0;
			
			// I have preffixed the variables with "parent" and "child", to avoid making mistakes and
			// using one instead of the other. It helped!
			var parentControllerProvider:IControllerProvider = diagramShell.getControllerProvider(parentModel);
			var parentRenderer:IVisualElementContainer = IVisualElementContainer(parentControllerProvider.getModelExtraInfoController(parentModel).getRenderer(diagramShell.modelToExtraInfoMap[parentModel]));

			var scrollRect:Rectangle = IAbsoluteLayoutRenderer(parentRenderer).getViewportRect();
			var noNeedToRefreshRect:Rectangle = IAbsoluteLayoutRenderer(parentRenderer).noNeedToRefreshRect;
			if (!IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren
				&& noNeedToRefreshRect != null && noNeedToRefreshRect.containsRect(scrollRect)) {
				return;
			}
			
			// holds the renderers to reuse
			// key: provided by the controller, usually figure class; value: a Vector of renderers (IVisualElement)
			var renderersToReuse:Dictionary = new Dictionary();
			var modelsToAdd:Vector.<Object> = new Vector.<Object>();
			var visibleModelsCounter:int = 0;
			
			// These values are computed based on the dimensions of all children 
			// (including those that are not displayed, because out of viewable area)
			var horizontalScrollPositionMin:Number = int.MAX_VALUE;
			var horizontalScrollPositionMax:Number = int.MIN_VALUE;
			var verticalScrollPositionMin:Number = int.MAX_VALUE;
			var verticalScrollPositionMax:Number = int.MIN_VALUE;
			
			// These values are computed based on the children that are not visible
			var horizontalNoNeedToRefreshLeft:int = int.MIN_VALUE;
			var horizontalNoNeedToRefreshRight:int = int.MAX_VALUE;
			var verticalNoNeedToRefreshTop:int = int.MIN_VALUE;
			var verticalNoNeedToRefreshBottom:int = int.MAX_VALUE;

			var children:IList = parentControllerProvider.getModelChildrenController(parentModel).getChildren(parentModel);
			
			for (var i:int = 0; i < children.length; i++) {
				var childModel:Object = children.getItemAt(i);
				var childControllerProvider:IControllerProvider = diagramShell.getControllerProvider(childModel);
				var childAbsoluteLayoutRectangleController:IAbsoluteLayoutRectangleController = childControllerProvider.getAbsoluteLayoutRectangleController(childModel);
				var childRendererController:IRendererController = childControllerProvider.getRendererController(childModel);
				var childRenderer:IVisualElement = childControllerProvider.getModelExtraInfoController(childModel).getRenderer(diagramShell.modelToExtraInfoMap[childModel]);

				diagramShell.addInModelMapIfNecesssary(childModel, childControllerProvider);
			
				if (childAbsoluteLayoutRectangleController != null) {
					// a child that participates to renderer recycling logic
					var crtRect:Rectangle = childAbsoluteLayoutRectangleController.getBounds(childModel);
					
					// updates the new scroll bounds, based on the dimensions of the current child
					if (crtRect.x + crtRect.width > horizontalScrollPositionMax) {
						horizontalScrollPositionMax = crtRect.x + crtRect.width;
					}
					if (crtRect.x < horizontalScrollPositionMin) {
						horizontalScrollPositionMin = crtRect.x;
					}
					if (crtRect.y + crtRect.height > verticalScrollPositionMax) {
						verticalScrollPositionMax = crtRect.y + crtRect.height;
					}
					if (crtRect.y < verticalScrollPositionMin) {
						verticalScrollPositionMin = crtRect.y;
					}

					if (childRenderer == null) { 
						// the model should be visible and it is not
						modelsToAdd.push(childModel);
					}
				}
			}

			if (children.length == 0) {
				horizontalScrollPositionMin = 0;
				horizontalScrollPositionMax = 0;
				verticalScrollPositionMin = 0;
				verticalScrollPositionMax = 0;
			}
			IAbsoluteLayoutRenderer(parentRenderer).setContentRect(new Rectangle(horizontalScrollPositionMin, verticalScrollPositionMin, horizontalScrollPositionMax - horizontalScrollPositionMin, verticalScrollPositionMax - verticalScrollPositionMin));
			
			for (i = 0; i < modelsToAdd.length; i++) {
				childModel = modelsToAdd[i];
				childControllerProvider = diagramShell.getControllerProvider(childModel);
				childRendererController = childControllerProvider.getRendererController(childModel);

				childRenderer = childRendererController.createRenderer(childModel);
				parentRenderer.addElement(childRenderer);
	
				diagramShell.associateModelToRenderer(childModel, childRenderer, childControllerProvider);				
			}
		
			IVisualChildrenRefreshable(parentRenderer).shouldRefreshVisualChildren = false;
			IAbsoluteLayoutRenderer(parentRenderer).noNeedToRefreshRect = new Rectangle(horizontalNoNeedToRefreshLeft, verticalNoNeedToRefreshTop, horizontalNoNeedToRefreshRight - horizontalNoNeedToRefreshLeft, verticalNoNeedToRefreshBottom - verticalNoNeedToRefreshTop);
		}	
	}
	
}