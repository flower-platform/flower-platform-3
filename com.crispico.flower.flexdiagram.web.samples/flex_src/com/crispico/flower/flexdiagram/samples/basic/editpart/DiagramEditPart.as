package com.crispico.flower.flexdiagram.samples.basic.editpart {
	import com.crispico.flower.flexdiagram.AbsoluteLayoutEditPart;
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IEditPartWithOrderedElements;
	import com.crispico.flower.flexdiagram.IScrollableEditPart;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicModel;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.events.CollectionEvent;

	public class DiagramEditPart extends AbsoluteLayoutEditPart implements IScrollableEditPart, IEditPartWithOrderedElements {

		private const PREFERRED_DISPLAY_OBJECTS_MAX_NUMBER:int = 100;
		
		public function DiagramEditPart(model:Object, viewer:DiagramViewer) {
			super(model, viewer, PREFERRED_DISPLAY_OBJECTS_MAX_NUMBER);
		}
		
		override public function activate():void {
			super.activate();
			ArrayCollection(model).addEventListener(CollectionEvent.COLLECTION_CHANGE, collectionChangeHandler);
		}
		
		override public function deactivate():void {
			ArrayCollection(model).removeEventListener(CollectionEvent.COLLECTION_CHANGE, collectionChangeHandler);
			super.deactivate();
		}
		
		private function collectionChangeHandler(event:CollectionEvent):void {
			refresh();
		}
		
		override protected function getModelHolderCollections():Array {
			return [model];
		}
		
		override public function getModelFromModelHolder(modelHolder:Object):Object {
			return modelHolder;
		}
		
		override public function isEditPartAccepted(editParts:ArrayCollection):Boolean {
			return true;
		}
		
		override public function acceptEditPart(editParts:ArrayCollection, deltaX:int, deltaY:int):void {
			for (var i:int = 0; i < editParts.length; i++) {
				var ep:EditPart = EditPart(editParts.getItemAt(i));
				if (ep.getModel() is BasicModel) {
					var basicModel:BasicModel = BasicModel(ep.getModel());
					basicModel.x += deltaX;
					basicModel.y += deltaY;
				}
			}
		}
		
		override public function acceptNewEditPart(editPart:EditPart, x:int, y:int, width:int, height:int, additionalInfo:Object = null):void {
//			if (editPart is IAbsolutePositionEditPart){
//				var createNodeCommand:CreateNodeCommand = new CreateNodeCommand(View(editPart.getModel()).viewType, 
//					View(getModel()), x, y, width, height, additionalInfo);
//				
//				if (editPart is NoteEditPart) {
//					BaseFlowerDiagramEditor.instance.sendObject(createNodeCommand);		
//				} else {
//					new DiagramCreateNewElementDialogController(createNodeCommand).invokeRequestDiagramPropertiesCommand();
//				}
//			} else if (editPart is ConnectionEditPart) {
//				var createEdgeCommand:CreateEdgeCommand = new CreateEdgeCommand(View(editPart.getModel()).viewType,
//					View(ConnectionEditPart(editPart).getSource().getModel()).id,
//					View(ConnectionEditPart(editPart).getTarget().getModel()).id,
//					additionalInfo);
//				BaseFlowerDiagramEditor.instance.sendObject(createEdgeCommand);
//			}
		}

//		/**
//		 * Besides the super functionality, the method updates the Context Menu position.
//		 * A callLater is needed because some components aren't yet updated 
//		 * <p>
//		 * Example : when zooming, the scrollbar position isn't set yet so
//		 * the CM position will be updated early and not displayed correctly.
//		 * @flowerModelElementId _FwQxANVDEd-_pe20qTUaAg
//		 * @author Cristina
//		 */
//		override public function refreshVisualChildren():void {
//			super.refreshVisualChildren();			
//			UIComponent(getFigure()).callLater(ContextMenuManager.INSTANCE.updatePosition, [getViewer()]);
//		}
		
		public function handleExternalScroll (deltaX:int, deltaY:int):void {
//			var fig:RootFigure = RootFigure(getFigure());
//			var scrollPositionChanged:Boolean = false;
//			
//			if (fig.horizontalScrollBar != null) {
//				var newPosX:Number = fig.horizontalScrollPosition + 
//					Math.ceil(deltaX * fig.getScaleFactor());
//				
//				// checking if new position between min and max scroll positions
//				if (newPosX < 0) {
//					newPosX = 0;
//				} else if (newPosX > fig.horizontalScrollBar.maxScrollPosition) {
//					newPosX = fig.horizontalScrollBar.maxScrollPosition;
//				} 
//				fig.horizontalScrollPosition = newPosX;
//				scrollPositionChanged = true;	
//			}
//			if (fig.verticalScrollBar != null) {
//				var newPosY:Number = fig.verticalScrollPosition +
//					Math.ceil(deltaY * fig.getScaleFactor());
//				
//				if (newPosY < 0) {
//					newPosY = 0;
//				}
//				else if (newPosY > fig.verticalScrollBar.maxScrollPosition) {
//					newPosY = fig.verticalScrollBar.maxScrollPosition;
//				}
//				fig.verticalScrollPosition = newPosY;
//				scrollPositionChanged = true;
//			}
//			
//			// track changes to scroll position because calling refreshVisualChildren can be expensive
//			if (scrollPositionChanged) {
//				refreshVisualChildren();
//			}
		}
		
		/**
		 * Sends a MoveElementCommand to reposition each child to the new index.
		 */
		public function repositionChildren(children:Array, newIndexList:Array):void {
//			
//			if (children.length == 1) {
//				BaseFlowerDiagramEditor.instance.sendObject(new MoveElementCommand(EditPart(children[0]).getModel().id, newIndexList[0]));
//				
//			} else if (children.length > 1) {
//				var composedComand:FlexToJavaCompoundCommand = new FlexToJavaCompoundCommand();
//				for (var i:int = 0; i < children.length; i++) {
//					var newIndex:int = newIndexList.length == 1 ? newIndexList[0] : newIndexList[i];
//					composedComand.append(new MoveElementCommand(EditPart(children[i]).getModel().id, newIndex));
//				}
//				BaseFlowerDiagramEditor.instance.sendObject(composedComand);
//			}
		}
		
		/**
		 * Retrieves the ArrayCollection that contains the model/model holder associated to the <code>childEditPart</code>.
		 */
		public function getModelHolderList(childEditPart:EditPart):ListCollectionView {
			return ArrayCollection(model);			
		}
		
		/**
		 * Returns <code>true</code> if the given EditParts intersect.
		 */
		public function intersects(child1:EditPart, child2:EditPart):Boolean {
//			if (child1 is IAbsolutePositionEditPart && child2 is IAbsolutePositionEditPart) {
//				var bounds:Array = IAbsolutePositionEditPart(child1).getBoundsRect();
//				var r1:Rectangle = new Rectangle(bounds[0], bounds[1], bounds[2], bounds[3]);
//				
//				bounds = IAbsolutePositionEditPart(child2).getBoundsRect();
//				return r1.intersects(new Rectangle(bounds[0], bounds[1], bounds[2], bounds[3]));
//				
//			} else if (child1 is ConnectionEditPart && child2 is ConnectionEditPart) {
//				// two connection figures intersect only if two of their segments intersect
//				var conFig1:ConnectionFigure = child1.getFigure() as ConnectionFigure;
//				var conFig2:ConnectionFigure = child2.getFigure() as ConnectionFigure;
//				
//				for (var i:int = 0; i < conFig1.getNumberOfSegments(); i++) {
//					var segment1:ConnectionSegment = conFig1.getSegmentAt(i);
//					
//					for (var j:int = 0; j < conFig2.getNumberOfSegments(); j++) {
//						var segment2:ConnectionSegment = conFig2.getSegmentAt(j);
//						
//						if (segment1.intersectsSegment(new Point(segment2.sourcePoint.x, segment2.sourcePoint.y), 
//							new Point(segment2.targetPoint.x, segment2.targetPoint.y)) != null) {
//							return true;
//						}
//					}
//				}
//				return false;					
//			}
//			
			return false;
		}	
		
	}
}