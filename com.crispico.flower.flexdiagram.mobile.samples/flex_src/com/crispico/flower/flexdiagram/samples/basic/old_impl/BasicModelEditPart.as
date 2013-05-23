package com.crispico.flower.flexdiagram.samples.basic.old_impl {
	import com.crispico.flower.flexdiagram.AbsolutePositionEditPartUtils;
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IAbsolutePositionEditPart;
	import com.crispico.flower.flexdiagram.IConnectableEditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.event.BoundsRectChangedEvent;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicConnection;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicModel;
	import com.crispico.flower.flexdiagram.ui.AbsolutePositionSelectionAnchors;
	import com.crispico.flower.flexdiagram.ui.IDraggable;
	import com.crispico.flower.flexdiagram.ui.MoveResizePlaceHolder;
	import com.crispico.flower.flexdiagram.ui.ResizeAnchor;
	
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	
	public class BasicModelEditPart extends EditPart implements IAbsolutePositionEditPart, IConnectableEditPart {
		
		protected var selection:AbsolutePositionSelectionAnchors;	
		protected var moveResizePlaceHolder:MoveResizePlaceHolder = null; 
		protected var xOnBeginDrag:int;
		protected var yOnBeginDrag:int;	
		protected var widthOnBeginDrag:int;
		protected var heightOnBeginDrag:int;	
		
		public function get basicModel():BasicModel {
			return BasicModel(model);
		}
		
		public function get basicModelFigure():BasicModelFigure {
			return BasicModelFigure(figure);
		}
		
		public function BasicModelEditPart(model:Object, viewer:DiagramViewer) {
			super(model, viewer);
		}
		
		override public function getFigureClass():Class	{
			return BasicModelFigure;
		}
		
		override public function refreshVisualDetails(calledDuringFigureSet:Boolean):void {
			super.refreshVisualDetails(calledDuringFigureSet);
			basicModelFigure.x = basicModel.x;
			basicModelFigure.y = basicModel.y;
			basicModelFigure.width = basicModel.width;
			basicModelFigure.height = basicModel.height;
		}
		
		override public function isSelectable():Boolean {
			return true;
		}

		override public function isDraggable():Boolean {
			return true;
		}
		
		override public function activate():void {
			super.activate();
			basicModel.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelPropertyChangeHandler);
		}
		
		override public function deactivate():void {
			basicModel.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelPropertyChangeHandler);
			super.deactivate();
		}

		private function modelPropertyChangeHandler(event:PropertyChangeEvent):void {
			// if a figure exist, there is no need to dispatch updateConn... because
			// we are already listening the figure
			if (figure != null)
				refreshVisualDetails(false); 
			else {
				dispatchUpdateConnectionEndsEvent(null);
				dispatchEvent(new BoundsRectChangedEvent(this));
			}
		}
		
		/**
		 * Delegates to <code>AbsolutePositionEditPartUtils</code>
		 */
		override public function updateSelectedState(isSelected:Boolean, isMainSelection:Boolean):void {
			super.updateSelectedState(isSelected, isMainSelection);
			AbsolutePositionEditPartUtils.updateSelectedState(this, isSelected, isMainSelection);
			// tell the children that their parent has changed selected status.
			for each (var editPart:EditPart in getChildren()) {
				editPart.updateSelectedState(isSelected, isMainSelection);
			} 
		}
		
		/**
		 * Getter for the component that handles the anchors
		 */
		public function getSelectionComponent():AbsolutePositionSelectionAnchors {
			return selection;
		}
		
		/**
		 * Setter for the component that handles the anchors
		 */
		public function setSelectionComponent(selection:AbsolutePositionSelectionAnchors):void {
			this.selection = selection;
		}
		
		/**
		 * Creates the selection Anchors for this EditPart.
		 */ 
		public function createNewSelectionComponent():AbsolutePositionSelectionAnchors {
			return new AbsolutePositionSelectionAnchors();
		}

		public function getBoundsRect():Array {
			return [basicModel.x, basicModel.y, basicModel.width, basicModel.height];
		}
		
		protected function createMoveResizePlaceHolder():MoveResizePlaceHolder {
			return new MoveResizePlaceHolder();
		}
		
		override public function beginDrag(draggable:IDraggable, x:int, y:int, isInitiator:Boolean):Boolean {
			if (moveResizePlaceHolder == null) {
				moveResizePlaceHolder = createMoveResizePlaceHolder();
				moveResizePlaceHolder.x = DisplayObject(getFigure()).x;
				moveResizePlaceHolder.y = DisplayObject(getFigure()).y;
				moveResizePlaceHolder.width = DisplayObject(getFigure()).width;
				moveResizePlaceHolder.height = DisplayObject(getFigure()).height;	
				AbsolutePositionEditPartUtils.addChildFigure(IVisualElementContainer(viewer.getRootEditPart().getFigure()), moveResizePlaceHolder);
				// update x, y, width, height at the start of the move/resize operation
				xOnBeginDrag = moveResizePlaceHolder.x;
				yOnBeginDrag = moveResizePlaceHolder.y;
				widthOnBeginDrag = moveResizePlaceHolder.width;
				heightOnBeginDrag = moveResizePlaceHolder.height;
				// force an update for _acceptedMinHeight and _acceptedMinWidth
				// so that at resize the Rectangle Mask knows its min with/height
//				AbstractAbsolutePositionFigure(getFigure()).computeAcceptedMinSize();
			}
			if (draggable is ResizeAnchor)
				return false;
			else 
				return true;
		}

		override public function drag(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
			// resize operation
			if (draggable is ResizeAnchor) {
//				var type:String = ResizeAnchor(draggable).getType();
//				if (draggable.getEditPart() is IAbsolutePositionEditPart)
//					computeResizeProperties(moveResizePlaceHolder, type, deltaX, deltaY);
				
				// move operation
			} else { 
				
				// the element cannot have a negative coordinates so apply max function				
				var newCoords:Array = snapToGrid(Math.max(0, xOnBeginDrag + deltaX), Math.max(0, yOnBeginDrag + deltaY));
				moveResizePlaceHolder.x = newCoords[0];
				moveResizePlaceHolder.y = newCoords[1];					
			}
		}

		override public function abortDrag(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
			if (moveResizePlaceHolder != null) {
				AbsolutePositionEditPartUtils.removeChildFigure(IVisualElementContainer(viewer.getRootEditPart().getFigure()), moveResizePlaceHolder);
				moveResizePlaceHolder = null;
			}
		}
		
		override public function drop(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
			abortDrag(draggable, deltaX, deltaY, isInitiator);
		}
		
		override public function getConnectionAnchorRect():Array {
			return AbsolutePositionEditPartUtils.getConnectionAnchorRect(this);	
		}
		
		override public function setFigure(newFigure:IFigure):void {
			super.setFigure(newFigure);
			AbsolutePositionEditPartUtils.setFigure(this, newFigure);
		}
		
		/**
		 * Delegates to <code>AbsolutePositionEditPartUtils</code>
		 * @flowerModelElementId _cWJLR78REd6XgrpwHbbsYQ
		 */ 
		override public function unsetFigure():void {
			AbsolutePositionEditPartUtils.unsetFigure(this);
			super.unsetFigure();
		}
		
		public function getAcceptedIncommingConnectionEditParts():ArrayCollection {
			return new ArrayCollection([BasicConnectionEditPart]);
		}
		
		public function getAcceptedOutgoingConnectionEditParts():ArrayCollection {
			return new ArrayCollection([BasicConnectionEditPart]);
		}
		
		override public function acceptSourceConnectionEditPart(editParts:ArrayCollection):void	{
			BasicConnection(editParts[0].getModel()).source = basicModel;
		}
		
		override public function acceptTargetConnectionEditPart(editParts:ArrayCollection):void	{
			BasicConnection(editParts[0].getModel()).target = basicModel;
		}
		
		override public function isSourceConnectionEditPartAccepted(editParts:ArrayCollection):Boolean {
			return true;
		}
		
		override public function isTargetConnectionEditPartAccepted(editParts:ArrayCollection):Boolean {
			return true;
		}
		
		
	}
}