package org.flowerplatform.flexdiagram {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IInvalidating;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	import mx.messaging.events.ChannelEvent;
	
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	import spark.components.supportClasses.ItemRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class DiagramShell {
		
		private var _modelToExtraInfoMap:Dictionary = new Dictionary();
		
		private var _rootModel:Object;
		
		private var _diagramRenderer:IVisualElementContainer;

		private var _mainSelectionIndex:int = -1;
		private var _selectedItems:ParentAwareArrayList = new ParentAwareArrayList(null);
		
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
		
		public function get selectedItems():ParentAwareArrayList {
			return _selectedItems;
		}
		
		public function set mainSelectionIndex(value:int):void {
			if (value < 0 || value >= _selectedItems.length) {
				throw new Error("mainSelectionIndex has to be between selectedItems");
			}
			if (_mainSelectionIndex != value) {
				var model:Object;
				
				if (_mainSelectionIndex != -1) {
					model = _selectedItems.getItemAt(_mainSelectionIndex);					
					getControllerProvider(model).getSelectionController(model).setSelectedState(model, getRendererForModel(model), true, false);
				}
				// mark the new main selection 
				_mainSelectionIndex = value;
				
				model = _selectedItems.getItemAt(_mainSelectionIndex);				
				getControllerProvider(model).getSelectionController(model).setSelectedState(model, getRendererForModel(model), true, true);				
			}
		}
		
		public function get mainSelectionIndex():int {
			return _mainSelectionIndex;
		}
		
		public function DiagramShell() {
			selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangeHandler);
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
			
			var selectionController:ISelectionController = controllerProvider.getSelectionController(model);
			if (selectionController != null) {
				selectionController.associatedModelToSelectionRenderer(model, renderer);
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
						
			var selectionController:ISelectionController = controllerProvider.getSelectionController(model);
			if (selectionController != null) {
				selectionController.unassociatedModelFromSelectionRenderer(model, renderer);
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
		
		private function selectionChangeHandler(event:CollectionEvent):void {
			var model:Object = event.items[0];
			var selectionController:ISelectionController = getControllerProvider(model).getSelectionController(model);
			if (selectionController != null) {				
				if (event.kind == CollectionEventKind.ADD) {
					selectionController.setSelectedState(model, getRendererForModel(model), true, true);					
				} else if (event.kind == CollectionEventKind.REMOVE) {
					if (selectedItems.length == 0) { // all items where removed, so reset mainSelectionIndex
						_mainSelectionIndex = -1;						
					}				
					selectionController.setSelectedState(model, getRendererForModel(model), false, false);
				} else if (event.kind == CollectionEventKind.REPLACE) {
					model = PropertyChangeEvent(event.items[0]).oldValue;					
					selectionController.setSelectedState(model, getRendererForModel(model), false, false);
				}
			}
		}
		
		public function addToSelection(model:Object, isMainSelection:Boolean):void {
			if (selectedItems.getItemIndex(model) == -1) {
				selectedItems.addItem(model);
			}
			if (isMainSelection) {
				mainSelectionIndex = selectedItems.getItemIndex(model);
			}
		}
		
		public function removeFromSelection(model:Object):void {
			var index:int = selectedItems.getItemIndex(model);
			if (index < 0 || index >= selectedItems.length) {
				throw new Error("Attempting to remove an index of a non existent model");
			}
		
			selectedItems.removeItemAt(index);
			
			switch (selectedItems.length) {
				case 0:
					_mainSelectionIndex = -1;
					break;
				case index:
					_mainSelectionIndex = selectedItems.length - 1;
					var model:Object = selectedItems.getItemAt(_mainSelectionIndex);
					var selectionController:ISelectionController = getControllerProvider(model).getSelectionController(model);
					selectionController.setSelectedState(model, getRendererForModel(model), true, true);
					break;
				default:
					_mainSelectionIndex--;
			}
		}
		
		public function isMainSelection(model:Object):Boolean {
			if (_mainSelectionIndex == -1) {
				return false;
			}
			return selectedItems.getItemIndex(model) == _mainSelectionIndex;				
		}
	}
}