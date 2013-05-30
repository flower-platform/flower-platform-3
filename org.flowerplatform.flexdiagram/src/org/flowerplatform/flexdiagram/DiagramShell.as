package org.flowerplatform.flexdiagram {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IInvalidating;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import spark.components.supportClasses.ItemRenderer;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class DiagramShell {
		
		private var _modelToExtraInfoMap:Dictionary = new Dictionary();
		
		private var _rootModel:Object;
		
		private var _diagramRenderer:IVisualElementContainer;

		public function get modelToExtraInfoMap():Dictionary {
			return _modelToExtraInfoMap;
		}
		
		public function get rootModel():Object {
			return _rootModel;
		}
		
		public function set rootModel(value:Object):void {
			if (rootModel != null) {
				unassociateModelFromRenderer(rootModel, IVisualElement(diagramRenderer), true);
			}
			_rootModel = value;
			if (rootModel != null) {
				addInModelMapIfNecesssary(rootModel);
				associateModelToRenderer(rootModel, IVisualElement(diagramRenderer), null);
			}
		}
		
		public function get diagramRenderer():IVisualElementContainer {
			return _diagramRenderer;
		}
		
		public function set diagramRenderer(value:IVisualElementContainer):void {
			_diagramRenderer = value;
		}
		
		public function getControllerProvider(model:Object):IControllerProvider {
			throw new Error("Should be implemented by subclass");
		}
		
		public function addInModelMapIfNecesssary(model:Object, controllerProvider:IControllerProvider = null):Boolean {
			if (controllerProvider == null) {
				controllerProvider = getControllerProvider(model);
			}
			if (modelToExtraInfoMap[model] == null) {
				var extraInfo:Object = controllerProvider.getModelExtraInfoController(model).createExtraInfo(model);
				modelToExtraInfoMap[model] = extraInfo;
				return true;
			}
			return false;
		}
		
		public function associateModelToRenderer(model:Object, renderer:IVisualElement, controllerProvider:IControllerProvider = null):void {
			if (controllerProvider == null) {
				controllerProvider = getControllerProvider(model);
			}
			
			// update the renderer in model map
			controllerProvider.getModelExtraInfoController(model).setRenderer(model, modelToExtraInfoMap[model], renderer);
			
			if (renderer is IInvalidating) {
				var invalidating:IInvalidating = IInvalidating(renderer);
				invalidating.invalidateDisplayList();
				invalidating.invalidateProperties();
				invalidating.invalidateSize();
			}
			if (renderer is IVisualChildrenRefreshable) {
				IVisualChildrenRefreshable(renderer).shouldRefreshVisualChildren = true;
			}
			if (renderer is IDiagramShellAware) {
				IDiagramShellAware(renderer).diagramShell = this;
			}

			IDataRenderer(renderer).data = model;
			
			var rendererController:IRendererController = controllerProvider.getRendererController(model);
			if (rendererController != null) {
				// is null just for the diagram
				rendererController.associatedModelToRenderer(model, renderer);
			}
			
			var modelChildrenController:IModelChildrenController = controllerProvider.getModelChildrenController(model);
			if (modelChildrenController != null) {
				// "leaf" models don't have children, i.e. no provider
				modelChildrenController.beginListeningForChanges(model);
			}
		}
		
		public function unassociateModelFromRenderer(model:Object, renderer:IVisualElement, modelIsDisposed:Boolean, controllerProvider:IControllerProvider = null):void {
			if (controllerProvider == null) {
				controllerProvider = getControllerProvider(model);
			}
			
			var modelChildrenController:IModelChildrenController = controllerProvider.getModelChildrenController(model);
			if (modelChildrenController != null) {
				var children:IList = modelChildrenController.getChildren(model);
				for (var i:int = 0; i < children.length; i++) {
					var childModel:Object = children.getItemAt(i);
					unassociateModelFromRenderer(childModel, getRendererForModel(childModel), modelIsDisposed);
				}
				// "leaf" models don't have children, i.e. no provider
				modelChildrenController.endListeningForChanges(model);
			}

			// update the renderer in model map
			controllerProvider.getModelExtraInfoController(model).setRenderer(model, modelToExtraInfoMap[model], null);
			
			if (renderer != null) {
				IDataRenderer(renderer).data = null;
				if (renderer is IDiagramShellAware) {
					IDiagramShellAware(renderer).diagramShell = null;
				}
			}

			var rendererController:IRendererController = controllerProvider.getRendererController(model);
			if (rendererController != null) {
				// is null just for the diagram
				rendererController.unassociatedModelFromRenderer(model, renderer, modelIsDisposed);
			}
			
			if (modelIsDisposed) {
				delete modelToExtraInfoMap[model];
			}
		}
		
		public function getRendererForModel(model:Object):IVisualElement {
			return getControllerProvider(model).getModelExtraInfoController(model).getRenderer(modelToExtraInfoMap[model]);
		}
		
		public function shouldRefreshVisualChildren(model:Object):void {
			var renderer:IVisualElement = getRendererForModel(model);
			if (renderer == null) {
				return;
			}
			
			if (renderer is IInvalidating) {
				var invalidating:IInvalidating = IInvalidating(renderer);
				invalidating.invalidateDisplayList();
				invalidating.invalidateProperties();
				invalidating.invalidateSize();
			}
			if (renderer is IVisualChildrenRefreshable) {
				IVisualChildrenRefreshable(renderer).shouldRefreshVisualChildren = true;
			}
		
		}
		
	}
}