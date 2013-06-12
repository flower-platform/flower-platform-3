package org.flowerplatform.flexdiagram {
	import flash.ui.Multitouch;
	import flash.ui.MultitouchInputMode;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.IInvalidating;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	import org.flowerplatform.flexdiagram.tool.DragToCreateRelationTool;
	import org.flowerplatform.flexdiagram.tool.DragTool;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ResizeTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.SelectOrDragToCreateElementTool;
	import org.flowerplatform.flexdiagram.tool.Tool;
	import org.flowerplatform.flexdiagram.tool.WakeUpTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class DiagramShell {
		
		private var _modelToExtraInfoMap:Dictionary = new Dictionary();
		
		private var _rootModel:Object;
		
		private var _diagramRenderer:IVisualElementContainer;

		private var _mainSelectedItem:Object;
		private var _selectedItems:ParentAwareArrayList = new ParentAwareArrayList(null);
		
		private var _defaultTool:Tool;
		
		private var _mainTool:Tool;
		
		public var tools:Dictionary = new Dictionary();
		
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
		
		public function set mainSelectedItem(value:Object):void {
			if (_mainSelectedItem != value) {			
				if (_mainSelectedItem != null) {				
					getControllerProvider(_mainSelectedItem).getSelectionController(_mainSelectedItem).
						setSelectedState(_mainSelectedItem, getRendererForModel(_mainSelectedItem), true, false);
				}
				// mark the new main selection 
				_mainSelectedItem = value;
							
				if (_mainSelectedItem != null) {
					getControllerProvider(_mainSelectedItem).getSelectionController(_mainSelectedItem).
						setSelectedState(_mainSelectedItem, getRendererForModel(_mainSelectedItem), true, true);			
				}
			}
		}
		
		public function get mainSelectedItem():Object {			
			return _mainSelectedItem;
		}
				
		[Bindable]
		public function get mainTool():Tool {			
			return _mainTool;
		}
		
		public function set mainTool(value:Tool):void {
			if (_mainTool != value) {
				if (_mainTool != null) {
					_mainTool.deactivateAsMainTool();					
				}
				
				_mainTool = value;
				
				if (mainTool != null) {
					_mainTool.activateAsMainTool();					
				}
			}
		}
		
		public function mainToolFinishedItsJob():void {
			mainTool = _defaultTool;
		}
		
		public function DiagramShell() {
			selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangeHandler);
			
			var wakeUpTool:WakeUpTool = new WakeUpTool(this);
			_defaultTool = wakeUpTool;
			
			tools[WakeUpTool.ID] = wakeUpTool;
			tools[ScrollTool.ID] = new ScrollTool(this);
			tools[SelectOnClickTool.ID] = new SelectOnClickTool(this);
			tools[InplaceEditorTool.ID] = new InplaceEditorTool(this);
			tools[ResizeTool.ID] = new ResizeTool(this);
			tools[DragToCreateRelationTool.ID] = new DragToCreateRelationTool(this);
			tools[DragTool.ID] = new DragTool(this);
			tools[SelectOrDragToCreateElementTool.ID] = new SelectOrDragToCreateElementTool(this);
			tools[ZoomTool.ID] = new ZoomTool(this);
			
			Multitouch.inputMode = MultitouchInputMode.GESTURE;
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
			
			if (selectedItems.getItemIndex(mainSelectedItem) == -1) {
				// set main selected item to last item from list
				mainSelectedItem = selectedItems.length == 0 ? null : selectedItems.getItemAt(selectedItems.length  - 1);					
			}
		
			if (selectionController != null) {				
				if (event.kind == CollectionEventKind.ADD) {
					mainSelectedItem = model;
					//selectionController.setSelectedState(model, getRendererForModel(model), true, _mainSelectedItem == model);					
				} else if (event.kind == CollectionEventKind.REMOVE ||  event.kind == CollectionEventKind.REPLACE) {
					
					if (event.kind == CollectionEventKind.REPLACE) {
						model = PropertyChangeEvent(event.items[0]).oldValue;	
					}				
					selectionController.setSelectedState(model, getRendererForModel(model), false, false);
				}
			}
		}
		
		public function activateTools():void {
			for (var key:Object in tools) {
				Tool(tools[key]).activateDozingMode();
			}
			if (mainTool == null) {
				mainTool = _defaultTool;
			}
			mainTool.activateAsMainTool();
		}		
	
		public function deactivateTools():void {
			mainTool.deactivateAsMainTool();
			
			for (var key:Object in tools) {
				Tool(tools[key]).deactivateDozingMode();
			}			
		}
		
	}
}