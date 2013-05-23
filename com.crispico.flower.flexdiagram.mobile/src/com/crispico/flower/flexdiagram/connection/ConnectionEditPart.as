package  com.crispico.flower.flexdiagram.connection {

	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.event.UpdateConnectionEndsEvent;
	import com.crispico.flower.flexdiagram.ui.ConnectionFigure;
	import com.crispico.flower.flexdiagram.ui.ConnectionSegment;
	import com.crispico.flower.flexdiagram.ui.ConnectionSelectionAchors;
	import com.crispico.flower.flexdiagram.ui.IDraggable;
	import com.crispico.flower.flexdiagram.ui.ResizeAnchor;
	
	import flash.display.DisplayObject;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	/**
	 * 
	 * An EditPart for a connection. When this EditPart is activated  
	 * (<code>activate()</code> called), or when ends change, the 
	 * ConnectionEditPart begins to listen the source/target
	 * EditParts. When <code>UpdateConnectionEndsEvent</code> is received from
	 * them (by <code>updateConnectionEnds ()</code>), the figure is 
	 * updated (one of its end points).
	 * 
	 * <p>
	 * A connection doesn't make sense without the ends. However, if the 
	 * ConnectionEditPart reaches this state (one or both of the ends) are null, 
	 * the connection will position itself in (0, 0) and (50, 50) for source and
	 * target ends respectively. This is a warning mechanism: if somehow the model
	 * is more or less corupt (allowing this state) we'll see in the editor that 
	 * the connections (or more precisely the underlying model) have a problem. 
	 * This is usefull during testing.<br> 
	 * Please <b>note</b> that once arrived in this state the connection cannot get 
	 * back to it's normal state and probably additionnal errors will occur.
	 *
	 * <p>
	 * The underlying model probably has information related to the intermediary
	 * points. The subclasses that "know" the model should listen to them and modify
	 * the figure's intermediary points when a change occurs.
	 *
	 * <p>
	 * Subclasses would/should normally override the following methods (besides the 
	 * ones form <code>EditPart</code>:
	 * <ul>
	 * 		<li><code>getSourceModel()</code>
	 * 		<li><code>getTargetModel()</code>
	 * 		<li>should call <code>sourceModelChanged()</code> and
	 * 			<code>targetModelChanged()</code> when the model changes
	 * 			(i.e. the connection is "reassociated").
	 * 		<li>should listen to the model and add/remove/modify bendpoints
	 * 			using <code>addVisualBendpoint()</code>, <code>removeVisualBendpoint</code>
	 * 			and <code>modifyVisualBendpoint()</code>; the first 2 methods might be
	 * 			overridden to register/unregister listeners for individual bendpoint change
	 * 			notification.
	 * </ul>
	 * 
	 * 
	 * @author Cristi
	 * @author Georgi
	 * @flowerModelElementId _bxov8L8REd6XgrpwHbbsYQ
	 */
	public class ConnectionEditPart extends EditPart {
		
		/**
		 * @flowerModelElementId _bxx5478REd6XgrpwHbbsYQ
		 */
		protected var source:EditPart;
		
		/**
		 * @flowerModelElementId _bxx55r8REd6XgrpwHbbsYQ
		 */
		protected var target:EditPart;
		
		/**
		 * 
		 * The visual bendpoints of the connection. This list is "in sync"
		 * with the bendpoints from the figure
		 * @flowerModelElementId _bxx56b8REd6XgrpwHbbsYQ
		 */ 
		protected var visualBendpoints:ArrayCollection = new ArrayCollection();
		
		/**
		 * 
		 * The map contains a model as key and its associated 
		 * <code>BindablePoint</code> as value
		 * @flowerModelElementId _bxx57r8REd6XgrpwHbbsYQ
		 */
		protected var modelToBindablePointMap:Dictionary = new Dictionary();
		
		/**
		 * 
		 * The anchors that belong to the current figure
		 * @flowerModelElementId _bxx5878REd6XgrpwHbbsYQ
		 */
		protected var anchors:ConnectionSelectionAchors;
		
		/**
		 * 
		 * x position of the point that is currently being
		 * moved
		 * @flowerModelElementId _bxx5-b8REd6XgrpwHbbsYQ
		 */
		protected var xOnBeginDrag:int;
		
		/**
		 * 
		 * y position of the point that is currently being
		 * moved
		 * @flowerModelElementId _bxx5_b8REd6XgrpwHbbsYQ
		 */
		protected var yOnBeginDrag:int;	
		
		/**
		 * @flowerModelElementId _bx7q5L8REd6XgrpwHbbsYQ
		 */
		public function ConnectionEditPart(model:Object, viewer:DiagramViewer) {
			super(model, viewer);
		}
		
		/**
		 * @flowerModelElementId _bx7q6b8REd6XgrpwHbbsYQ
		 */
		public function getSource():EditPart {
			return source;
		}
		
		/**
		 * @flowerModelElementId _bx7q7b8REd6XgrpwHbbsYQ
		 */
		public function setSource(editPart:EditPart):void {
			source = editPart;
		}
		
		/**
		 * @flowerModelElementId _bx7q8r8REd6XgrpwHbbsYQ
		 */
		public function getTarget():EditPart {
			return target;
		}
		
		/**
		 * @flowerModelElementId _bx7q9r8REd6XgrpwHbbsYQ
		 */
		public function setTarget(editPart:EditPart):void {
			target = editPart;
		}
		
		/**
		 * 
		 * Should be implemented by subclasses and return the model element
		 * that that represents the connection's source. It is used in <code>
		 * activate()</code> to find the corresponding EditPart
		 * @flowerModelElementId _bx7q-78REd6XgrpwHbbsYQ
		 */
		public function getSourceModel():Object {
			throw new Error("ConnectionEditPart.getSourceModel() should be implemented.");
		}
		
		/**
		 * 
		 * Should be implemented by subclasses and return the model element
		 * that that represents the connection's target. It is used in <code>
		 * activate()</code> to find the corresponding EditPart
		 * @flowerModelElementId _bx7rAL8REd6XgrpwHbbsYQ
		 */
		public function getTargetModel():Object {
			throw new Error("ConnectionEditPart.getTargetModel() should be implemented.");
		}
		
		/**
		 * @flowerModelElementId _bx7rBb8REd6XgrpwHbbsYQ
		 */
		override public function getFigureClass():Class {
			return ConnectionFigure;
		}

		/**
		 * 
		 * Initializes the [EditPart.source/targetConnections - source/target] associations. 
		 * Registers as listener for <code>UpdateConnectionsEvent</code>.
		 * EditParts should exist for the source/target models
		 * @flowerModelElementId _bx7rCb8REd6XgrpwHbbsYQ
		 */
		override public function activate():void {
			super.activate();
			sourceModelChanged(getSourceModel());
			targetModelChanged(getTargetModel());
		}
		
		/**
		 * 
		 * Resets the [EditPart.source/targetConnections - source/target] associations.
		 * Unregisters the listener for <code>UpdateConnectionsEvent</code>.
		 * Clears the bendpoints array
		 * @flowerModelElementId _byFb4L8REd6XgrpwHbbsYQ
		 */
		override public function deactivate():void {
			sourceModelChanged(null, true);
			targetModelChanged(null, true);
			super.deactivate();
		}
		
		/**
		 * @flowerModelElementId _byFb5b8REd6XgrpwHbbsYQ
		 */
		protected function sourceModelChanged(sourceModel:Object, deactivating:Boolean=false):void {
			if (source != null) {
				source.getSourceConnections().removeItemAt(source.getSourceConnections().getItemIndex(this));
				// this verification is necessary because
				// if target == source then when removing the listener from source it will be removed also from target
				if (target != source) {
					source.removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
				}
				source = null;
			}
			if (sourceModel != null) {
				source = viewer.getModelToEditPartMap()[sourceModel];
				if (source != null) {
					source.getSourceConnections().addItem(this);
					// if target == source it means the source has already set this listener
					// so it isn't necessary to add it again
					if (target != source) {
						source.addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
					}
				}
			}
			if (figure != null && !deactivating)
				updateConnectionEndsHandler(null);
			//no need to call the function if the source is null
			if (source != null)
				connectSameElement();
		}
		
		/**
		 * @flowerModelElementId _byFb7L8REd6XgrpwHbbsYQ
		 */
		protected function targetModelChanged(targetModel:Object, deactivating:Boolean=false):void {
			if (target != null) {				
				target.getTargetConnections().removeItemAt(target.getTargetConnections().getItemIndex(this));
				if (target != source) {
					target.removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);				
				}
				target = null;
			}
			if (targetModel != null) {
				target = viewer.getModelToEditPartMap()[targetModel];
				if (target != null) {					
					target.getTargetConnections().addItem(this);
					if (target != source) {
						target.addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
					}
				}
			}
			if (figure != null && !deactivating)
				updateConnectionEndsHandler(null);
			//no need to call the function if the target is null
			if (target != null)
				connectSameElement();
		}
		
		/**
		 * 
		 * The method is called whenever the source/target of the connection
		 * changes: <code>sourceModelChanged</code> and <code>targetModelChanged</code>.
		 * Also, the method is called from <code>setFigure</code> method (which is 
		 * called when the connection is first drawn)
		 * 
		 * @author Daniela
		 * @flowerModelElementId _WHefcMoHEd6eRqxgQwQuPg
		 */
		protected function connectSameElement():void {
			if (getSource() == getTarget()) {
				if (visualBendpoints.length == 0) {
					// add three inflexion points
					if (getFigure() != null) {
						var p:BindablePoint = getConnectSameElementReferencePoint();
						
						var threePointsDerivedfromReferencePoint:ArrayCollection = new ArrayCollection();
						threePointsDerivedfromReferencePoint.addItem(new BindablePoint(p.x + 31, p.y));
						threePointsDerivedfromReferencePoint.addItem(new BindablePoint(p.x + 31, p.y - 15));
						threePointsDerivedfromReferencePoint.addItem(new BindablePoint(p.x + 7, p.y - 15));
						
						var pointsIndexes:ArrayCollection = new ArrayCollection([0, 1, 2]);
						
						UIComponent(getFigure()).callLater(addMoreModelBendpointsOnce, [threePointsDerivedfromReferencePoint, pointsIndexes, true]);
					}
				}
			}
		}
		
		/**
		 * This function is used by the connectSameElement function
		 * 
		 * Subclasses can override this method in order to specific position the
		 * self connection relative to the figure of the source element that is connected
		 * 
		 * By default the self connection is fixed.
		 * 
		 * @see AbstractConnectionEditPart#getConnectSameElementReferencePoint()
		 * @author Daniela
		 */ 
		protected function getConnectSameElementReferencePoint():BindablePoint {
			return ConnectionFigure(getFigure())._sourcePoint;
		}
				
		/**
		 * 
		 * Called by subclasses when the bendpoint model list changes and a new bendpoint
		 * is added
		 * @flowerModelElementId _byFb878REd6XgrpwHbbsYQ
		 */ 
		protected function addVisualBendpoint(index:int, model:Object, x:int, y:int):void {
			var newPoint:BindablePoint = new BindablePoint(x, y);
			visualBendpoints.addItemAt(newPoint, index);
			if (model != null)
				modelToBindablePointMap[model] = newPoint;
			ConnectionFigure(getFigure()).addPointAt(newPoint, index);
			if (index  == 0 || index  ==  visualBendpoints.length - 1)
				updateConnectionEndsHandler(null);
		}
		
		/**
		 * 
		 * Called by subclasses when the bendpoint model list changes and a bendpoint is
		 * removed
		 * @flowerModelElementId _byFb_L8REd6XgrpwHbbsYQ
		 */ 
		protected function removeVisualBendpoint(index:int):void {
			if (index >= 0 && index < visualBendpoints.length) {
				var bp:BindablePoint = BindablePoint(ConnectionFigure(getFigure()).points.getItemAt(index));
				var model:Object = getModelForBindablePoint(bp);
				visualBendpoints.removeItemAt(index);
				ConnectionFigure(getFigure()).removePointAt(index);
				if (model != null) 
					delete modelToBindablePointMap[model];
				if (index  == 0 || index  ==  visualBendpoints.length - 1)
					updateConnectionEndsHandler(null);
			}
		}	
		
		/**
		 * 
		 * Called by subclasses when an element from the bendpoint model list changes
		 * @flowerModelElementId _byFcAr8REd6XgrpwHbbsYQ
		 */ 
		protected function modifyVisualBendpoint(model:Object, x:int, y:int):void {
			var bp:BindablePoint = BindablePoint(modelToBindablePointMap[model]);
			var index:int = -1;
			if (bp != null) {
				bp.x = x;
				bp.y = y;
				index =  ConnectionFigure(figure).getPointIndex(bp);
				if (index  == 0 || index  ==  visualBendpoints.length - 1)
					updateConnectionEndsHandler(null);
			}
		}
			
		/**
		 * 
		 * Receives <code>UpdateConnectionsEvent</code> and updates the
		 * associated figure.
		 * 
		 * @@see UpdateConnectionEndsEvent
		 * @@see EditPart.dispatchUpdateConnectionEndsEvent()
		 * 
		 * Calls <code>ClipUtils.computeClipBindablePoint()</code> to 
		 * compute the clip rectangle
		 * @flowerModelElementId _byOl078REd6XgrpwHbbsYQ
		 */ 
		protected function updateConnectionEndsHandler(event:UpdateConnectionEndsEvent):void {		
			var rect:Array;
			var resultRect:Array;
			var auxRect:Array;
			
			// <code>ClipUtils.computeClipBindablePoint()</code> receives two parameters:
			// a rectangle and a point (the middle point for the other rectangle).
			// referencePoint is the auxiliary point used as second parameter given
			// to <code>ClipUtils.computeClipBindablePoint()</code> method.
			var referencePoint:BindablePoint = new BindablePoint(0,0);
			
			// used to avoid the problems that could occur if the lines
			// are processed in a wrong order 
			if (figure == null) 
				return; 
			
			// if there are no bendpoints, the line needs to update both of its ends at
			// every call
			if (event == null || event.editPart == source || visualBendpoints.length == 0) {
				if (source != null)
					rect = source.getConnectionAnchorRect();
				else
					rect = [0, 0, 0, 0];
				if (visualBendpoints.length > 0) 
					referencePoint = visualBendpoints[0];
				else {
					if (target != null)
						auxRect = target.getConnectionAnchorRect();
					else
						auxRect = [50, 50, 50, 50];
					referencePoint.x = auxRect[0] + auxRect[2] / 2;
					referencePoint.y = auxRect[1] + auxRect[3] / 2; 
				} 
				resultRect = ClipUtils.computeClipBindablePoint(rect, referencePoint);
				ConnectionFigure(figure)._sourcePoint.x = resultRect[0];
				ConnectionFigure(figure)._sourcePoint.y = resultRect[1];
			}
			if (event == null || event.editPart == target || visualBendpoints.length == 0) {
				if (target != null)
					rect = target.getConnectionAnchorRect();
				else
					rect = [50, 50, 50, 50];
				if (visualBendpoints.length > 0)
					referencePoint = visualBendpoints[visualBendpoints.length - 1];
				else {
					if (source != null)
						auxRect = source.getConnectionAnchorRect();
					else
						auxRect = [0, 0, 0, 0];
					referencePoint.x = auxRect[0] + auxRect[2] / 2;
					referencePoint.y = auxRect[1] + auxRect[3] / 2; 
				} 
				resultRect = ClipUtils.computeClipBindablePoint(rect, referencePoint);
				ConnectionFigure(figure)._targetPoint.x = resultRect[0];
				ConnectionFigure(figure)._targetPoint.y = resultRect[1];
			}
			// notifies other <code>ConnectionEditPart</code> that have
			// the current editPart as source or target
			super.dispatchUpdateConnectionEndsEvent(null);
		}

		/**
		 * Calls <code>updateConnectionEnds </code> to initialize the ends
		 * @flowerModelElementId _byOl2b8REd6XgrpwHbbsYQ
		 */ 
		override public function setFigure(newFigure:IFigure):void {
			super.setFigure(newFigure);
			updateConnectionEndsHandler(null);
		}
		
		/**
		 * 
		 * Returns a point (x, y, 0, 0) representing the middle
		 * of the connection
		 * @flowerModelElementId _byOl4L8REd6XgrpwHbbsYQ
		 */
		override public function getConnectionAnchorRect():Array {
			return ConnectionFigure(figure).getMiddlePointRect();
		}
		
		/**
		 * 
		 * The editPart of a connection is selectable
		 * @flowerModelElementId _byOl5b8REd6XgrpwHbbsYQ
		 */
		override public function isSelectable():Boolean {
			return true;
		}

		/**
		 * 
		 * The editPart of a connection is draggable. Each one of its ends can 
		 * be moved to another editPart (if the targetEditPart accepts it) and
		 * each bendpoint can be moved anywhere on the diagram
		 * @flowerModelElementId _byOl6r8REd6XgrpwHbbsYQ
		 */
		override public function isDraggable():Boolean {
			return true;
		}

		/**
		 * 
		 * Decide wheather to create a new selection component or to
		 * update the existing one based on the received arguments
		 * @flowerModelElementId _byOl778REd6XgrpwHbbsYQ
		 */
		override public function updateSelectedState(isSelected:Boolean, isMainSelection:Boolean):void {
			super.updateSelectedState(isSelected, isMainSelection);
			if (isSelected){
				if (anchors == null) { // the selection component was not created yet
					// create new selection component and set main selection
					anchors = new ConnectionSelectionAchors();
					anchors.setMainSelection(isMainSelection);
					anchors.activate(getFigure());
				} else { // the associated editpart is selected => update main selection if needed
					if (isMainSelection != anchors.getMainSelection())
						anchors.setMainSelection(isMainSelection);
				}
			} else { // the associated editpart is not selected => distroy the selection component
				if (anchors != null) {
					anchors.deactivate();
					anchors = null; 
				}
			}
		}
		
		/**
		 * 
		 *  If one of the connection's ends generated this event, the drop operation
		 *  must ensure that the targetEditPart accept it (in this case, <code>beginDrag()</code>
		 *  returns true). Otherwise, any bendpoint can change its position without asking
		 *  any other editPart.
		 *  
		 *  If isInitiator is true the selection is modified to contain only the currently
		 *  selected line (its EditPart).
		 *
		 * @flowerModelElementId _byOl9r8REd6XgrpwHbbsYQ
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
			} else if (draggable is ConnectionSegment) {
				// obtain the ConnectionFigure that holds the segment
				var cf:ConnectionFigure = ConnectionFigure(ConnectionSegment(draggable).parent);
				var index:int = cf.getSegmentIndex(ConnectionSegment(draggable));
				addVisualBendpoint(index, null, x, y);
			}
			return false;
		}
		
		/**
		 * 
		 * <p>If the source anchor fired a drag event, the sourcePoint of the
		 * connection is updated. 
		 * 
		 * <p>If the target anchor fired the event, the targetPoint of the connection 
		 * is updated.
		 * 
		 * <p>If a bendpoint anchor fired the event, get the associated <code>BindablePoint</code>
		 * and update its position by calling <code>modifyBendpoint</code>. 
		 * 
		 * <p>If <code>SegmentFigure</code> fired the event, an add operation is in progress,
		 * and the currentPoint needs to be updated
		 * 
		 * @author Ioana: An update event should also be sent for the end points of the segment
		 * @flowerModelElementId _byYW0L8REd6XgrpwHbbsYQ
		 */
		override public function drag(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
			var dx:int = (xOnBeginDrag + deltaX) > 0 ? (xOnBeginDrag + deltaX) : 0;
			var dy:int = (yOnBeginDrag + deltaY) > 0 ? (yOnBeginDrag + deltaY) : 0;
			if (draggable is ResizeAnchor) {
				if (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.SOURCE_ANCHOR) {
					// source anchor for the connection is moved 
					ConnectionFigure(draggable.getEditPart().getFigure())._sourcePoint.x = dx;
					ConnectionFigure(draggable.getEditPart().getFigure())._sourcePoint.y = dy;
					dispatchUpdateConnectionEndsEvent(null);
				} else if (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.TARGET_ANCHOR) {
					// target anchor for the connection is moved
					ConnectionFigure(draggable.getEditPart().getFigure())._targetPoint.x = dx;
					ConnectionFigure(draggable.getEditPart().getFigure())._targetPoint.y = dy; 
					dispatchUpdateConnectionEndsEvent(null);
				} else if (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.BENDPOINT_ANCHOR) {
					// a bendpoint of the connection is moved
					var csa:ConnectionSelectionAchors = ConnectionSelectionAchors(DisplayObject(draggable).parent);
					var bp:BindablePoint = csa.getBindablePoint(ResizeAnchor(draggable));
					var index:int = -1;
					var model:Object = getModelForBindablePoint(bp);
					if (bp != null)
						index = ConnectionFigure(draggable.getEditPart().getFigure()).getPointIndex(bp);
					if (index != -1 && index < ConnectionFigure(draggable.getEditPart().getFigure()).points.length && model != null) {
						modifyVisualBendpoint(model, dx, dy);
					}
				}
			} else if (draggable is ConnectionSegment) {
				// the new added bendpoint is moved
				ConnectionSegment(draggable).targetPoint.x = dx;
				ConnectionSegment(draggable).targetPoint.y = dy;
			} 
		}
		
		/**
		 * 
		 * If the event was fired by a <code>ResizeAnchor</code>, a modify operation is in 
		 * progress and <code>modifyBendpoint></code> should be called. Otherwise, an add
		 * operation is in progress and <code>addBendpoint</code> should be called.
		 * The <code>drop()</code> method calls <code>abordDrag()</code>
		 * @flowerModelElementId _byYW2b8REd6XgrpwHbbsYQ
		 */
		override public function drop(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
			var index:int;
			var bp:BindablePoint;
			if (draggable is ResizeAnchor && (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.SOURCE_ANCHOR ||
				ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.TARGET_ANCHOR)) {
				updateConnectionEndsHandler(null);
			} else if (draggable is ResizeAnchor && ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.BENDPOINT_ANCHOR) {
				var csa:ConnectionSelectionAchors = ConnectionSelectionAchors(DisplayObject(draggable).parent);
				bp = csa.getBindablePoint(ResizeAnchor(draggable));
				index = -1;
				if (bp != null) {
					index = ConnectionFigure(draggable.getEditPart().getFigure()).getPointIndex(bp);
					if (index != -1){
						if (modifyOrDeleteBendpoint(bp, index))
							removeModelBendpoint(index);
						else 
							modifyModelBendpoint(bp.x, bp.y, index);
					}
				}
			} else if (draggable is ConnectionSegment) {
				index = -1;
				bp = ConnectionSegment(draggable).targetPoint;
				index = ConnectionFigure(draggable.getEditPart().getFigure()).getPointIndex(bp);
				addModelBendpoint(bp.x, bp.y, index);
			}
		}
		
		/**
		 * 
		 * The function returns true if the current point needs 
		 * to be deleted (the point, its successor and its predecessor are
		 * collinear). Otherwise, the point needs to be updated
		 * @flowerModelElementId _byYW6r8REd6XgrpwHbbsYQ
		 */
		private function modifyOrDeleteBendpoint(bp:BindablePoint, index:int):Boolean {
			var leftPoint:BindablePoint;
			var rightPoint:BindablePoint;
			var cf:ConnectionFigure = ConnectionFigure(getFigure());
			if (index == 0) // first bendpoint
				leftPoint = cf._sourcePoint;
			else
				leftPoint = BindablePoint(cf.points.getItemAt(index - 1));
		
			if (index == cf.points.length - 1) //last bendpoint
				rightPoint = cf._targetPoint;
			else
				rightPoint = BindablePoint(cf.points.getItemAt(index + 1));
			
			var angle:Number = ClipUtils.computeAngle(leftPoint, bp, rightPoint);
			// 17*PI/18 = 170 degrees
			if (Math.PI*17/18 <= angle && angle <= Math.PI) // delete
				return true;	
			else  // modify
				return false; 
		}
		
		/**
		 * 
		 * If the current action was aborded, the connection figure will be
		 * updated with the position/dimension from the model
		 * @flowerModelElementId _byYW8b8REd6XgrpwHbbsYQ
		 */
		override public function abortDrag(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
			var index:int = -1;
			var bp:BindablePoint;
			if (draggable is ResizeAnchor) {
				var csa:ConnectionSelectionAchors = ConnectionSelectionAchors(DisplayObject(draggable).parent);
				bp = csa.getBindablePoint(ResizeAnchor(draggable));
				var model:Object = getModelForBindablePoint(bp);
				if (bp != null)
					index = ConnectionFigure(draggable.getEditPart().getFigure()).getPointIndex(bp);
				modifyVisualBendpoint(model, deltaX, deltaY);
			} else if (draggable is ConnectionSegment) {
				bp = ConnectionSegment(draggable).targetPoint;
				index = ConnectionFigure(draggable.getEditPart().getFigure()).getPointIndex(bp);
				removeVisualBendpoint(index);
			}
			updateConnectionEndsHandler(null);
		}
		
		/**
		 * 
		 * <p>If the draggable object is the source anchor for the current connection, call
		 * <code>isSourceConnectionEditPartAccepted()</code> for the targetEditPart. 
		 * 
		 * <p>If the draggable object is the target anchor for the current connection, call
		 * <code>isTargetConnectionEditPartAccepted()</code> for the targetEditPart
		 * @flowerModelElementId _byhgyL8REd6XgrpwHbbsYQ
		 */
		override public function dragOverTargetEditPart(draggable:IDraggable, targetEditPart:EditPart, editParts:ArrayCollection):Boolean {
			if (draggable is ResizeAnchor){
				if (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.SOURCE_ANCHOR)
					return targetEditPart.isSourceConnectionEditPartAccepted(editParts);
				if (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.TARGET_ANCHOR)
					return targetEditPart.isTargetConnectionEditPartAccepted(editParts);
			} 
			return false;
		}
		
		/**
		 * 
		 * <p>If the draggable object is the source anchor for the current connection, call
		 * <code>acceptSourceConnectionEditPartAccepted()</code> for the targetEditPart. 
		 * 
		 * <p>If the draggable object is the target anchor for the current connection, call
		 * <code>acceptTargetConnectionEditPartAccepted()</code> for the targetEditPart
		 * @flowerModelElementId _byhg0L8REd6XgrpwHbbsYQ
		 */
		override public function dropOverEditPart(draggable:IDraggable, targetEditPart:EditPart, editParts:ArrayCollection, deltaX:int, deltaY:int):void {
			if (draggable is ResizeAnchor){
				if (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.SOURCE_ANCHOR)
					if (targetEditPart.isSourceConnectionEditPartAccepted(editParts))
						targetEditPart.acceptSourceConnectionEditPart(editParts);
				if (ResizeAnchor(draggable).getType() == ConnectionSelectionAchors.TARGET_ANCHOR)
					if (targetEditPart.isTargetConnectionEditPartAccepted(editParts))
						targetEditPart.acceptTargetConnectionEditPart(editParts);
			} 
		}
		
		/**
		 * 
		 * Abstract method. Subclasses must implement it in order to add 
		 * a bendpoint to the connection's model
		 * @flowerModelElementId _byrRxL8REd6XgrpwHbbsYQ
		 */	
		protected function addModelBendpoint(x:int, y:int, index:int, mergeWithPrevious:Boolean = false):void {
		}
		
		/**
		 * Abstract method. Subclasses must implement it in order to add more 
		 * bendpoints once to the connection's model
		 * 
		 * @author Daniela
		 */	
		protected function addMoreModelBendpointsOnce(points:ArrayCollection, pointsIndexes:ArrayCollection, mergeWithPrevious:Boolean = false):void {
		}
		
		/**
		 * 
		 * Abstract method. Subclasses must implement it in order to
		 * remove the bendpoint from the connection's model
		 * @flowerModelElementId _byrRzL8REd6XgrpwHbbsYQ
		 */ 
		protected function removeModelBendpoint(index:int):void {
		}
		
		/**
		 * 
		 * Abstract method. Subclasses must implement it in order to modify
		 * the bendpoint to the connection's model
		 * @flowerModelElementId _byrR0r8REd6XgrpwHbbsYQ
		 */	
		protected function modifyModelBendpoint(x:int, y:int, index:int):void {
		}
		
		/**
		 * 
		 * Returns the model for the given <code>BindablePoint</code> from the map
		 * @flowerModelElementId _by1Cw78REd6XgrpwHbbsYQ
		 */
		protected function getModelForBindablePoint(bp:BindablePoint):Object {
			for (var obj:Object in modelToBindablePointMap)
				if (modelToBindablePointMap[obj] == bp) 
					return obj;
			return null;
		}
			
		/**
		 * The method must be overriden by subclasses in order to set the 
		 * visual properties for the connection (e.g. dash/continuous line)
		 * 
		 * @flowerModelElementId _P9BAYL_XEd6MgcWPi_drlA
		 */
		public function setConnectionVisualProperties(figure: IFigure):void {
		}
		
		/**
		 * The method overrides <code>unsetFigure()</code> method
		 * from superclass because the associated figures for the <code>EditPart</code> 
		 * children of a <code>ConnectionEditPart</code> are not graphical children for
		 * the connection;
		 * 
		 * Dana: When unseting the figure we need also to remove the visual bendpoints.
		 * @flowerModelElementId _Fmu6kBciEd-0Bak4zARqXA
		 */
		override public function unsetFigure():void {
			var i:int = visualBendpoints.length;
			
			while (i > 0) {
				removeVisualBendpoint(0);
				i--;
			}
			
			if (figure != null) {
				figure.setEditPart(null);
				figure = null;
			}
		}
		
		/**
		 * The method overrides the <code>childRemoved()</code> method from superclass
		 * in order to remove its children without taking in consideration the 
		 * 'recycleFigureNeeded' flag.
		 * @flowerModelElementId _Fmu6kxciEd-0Bak4zARqXA
		 */
		override public function childRemoved(child:EditPart, recycleFigureNeeded:Boolean=true):void {
			super.childRemoved(child, true);
		}
	}
}