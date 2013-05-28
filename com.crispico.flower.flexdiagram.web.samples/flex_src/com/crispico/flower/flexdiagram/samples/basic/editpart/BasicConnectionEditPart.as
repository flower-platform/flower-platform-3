package com.crispico.flower.flexdiagram.samples.basic.editpart {
	import com.crispico.flower.flexdiagram.AbsolutePositionEditPartUtils;
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.connection.ConnectionEditPart;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicConnection;
	import com.crispico.flower.flexdiagram.ui.ConnectionEnd;
	import com.crispico.flower.flexdiagram.ui.ConnectionFigure;
	import com.crispico.flower.flexdiagram.ui.ConnectionSegment;
	import com.crispico.flower.flexdiagram.ui.ConnectionSelectionAchors;
	import com.crispico.flower.flexdiagram.ui.IDraggable;
	import com.crispico.flower.flexdiagram.ui.ResizeAnchor;
	
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.core.Container;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.PropertyChangeEvent;
	
	public class BasicConnectionEditPart extends ConnectionEditPart {
		public function BasicConnectionEditPart(model:Object, viewer:DiagramViewer) {
			super(model, viewer);
		}
		
		override public function refreshVisualDetails(calledDuringFigureSet:Boolean):void {
			ConnectionFigure(getFigure()).targetEndType = ConnectionEnd.CLOSED_ARROW;
		}
		
		override public function getSourceModel():Object {
			return BasicConnection(getModel()).source;
		}
		
		override public function getTargetModel():Object {
			return BasicConnection(getModel()).target;
		}
		
		override public function activate():void {
			super.activate();
			BasicConnection(getModel()).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelPropertyChangeHandler);
		}
		
		override public function deactivate():void {
			BasicConnection(getModel()).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelPropertyChangeHandler);
			super.deactivate();
		}
		
		private function modelPropertyChangeHandler(event:PropertyChangeEvent):void {
			if (event.property == "source" && event.oldValue != event.newValue) {
				sourceModelChanged(getSourceModel());
			}
			if (event.property == "target" && event.oldValue != event.newValue) {
				targetModelChanged(getTargetModel());
			}
			if (event.property == "children") {
				refreshChildren();
				refreshVisualChildren();
			}
		}
		/**
		 * Cancel the bend points logic
		 */
		override public function beginDrag(draggable:IDraggable, x:int, y:int, isInitiator:Boolean):Boolean {
			// if initiator then reset the selection
			if (isInitiator) {
				// remove everything from selection except the line
				var viewer:DiagramViewer = draggable.getEditPart().getViewer();
				var selLength:int = viewer.getSelectedElements().length;
				for (var i:int = selLength - 1; i >= 0; i--) {
					if (viewer.getSelectedElements().getItemAt(i) != draggable.getEditPart())
						viewer.removeFromSelectionAtIndex(i);
				}
			}
			xOnBeginDrag = x;
			yOnBeginDrag = y;
			if (draggable is ResizeAnchor) {
				if (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.SOURCE_ANCHOR ||
					ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.TARGET_ANCHOR) {
					return true; // move source/target anchor for a connection
				} else {
					return false; // move a bendpoint
				}
			} 
//			else if (draggable is ConnectionSegment) {
//				// obtain the ConnectionFigure that holds the segment
//				var cf:ConnectionFigure = ConnectionFigure(ConnectionSegment(draggable).parent);
//				var index:int = cf.getSegmentIndex(ConnectionSegment(draggable));
//				addVisualBendpoint(index, null, x, y);
//			}
			return false;
		}		

		override public function drag(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
			if (draggable is ConnectionSegment) {
				return;
			}
			super.drag(draggable, deltaX, deltaY, isInitiator);
		}
		
		override protected function getModelHolderCollections():Array {
			return [BasicConnection(model).children];
		}
		
		override public function getModelFromModelHolder(modelHolder:Object):Object {
			return modelHolder;
		}
		
		override public function refreshVisualChildren():void {
			// parentContainer is the diagram
			var container:IVisualElementContainer = IVisualElementContainer(getViewer().getRootEditPart().getFigure());
			var currentFigure:IFigure = null;
			var editPartsToAdd:ArrayCollection = new ArrayCollection();
			for (var i:int = 0; i < children.length; i++) {
				var ep:EditPart = EditPart(children[i]);
				if (ep.getFigure() == null) { // the figure is not visible
					currentFigure = ep.createOrGetRecycledFigure()
					ep.setFigure(currentFigure);
					AbsolutePositionEditPartUtils.addChildFigure(container, IVisualElement(currentFigure));
				} 					
			}
		}
	}

}