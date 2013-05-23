package com.crispico.flower.flexdiagram.ui {
	import com.crispico.flower.flexdiagram.IConnectableEditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.connection.BindablePoint;
	import com.crispico.flower.flexdiagram.tool.SelectMoveResizeTool;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	import mx.managers.CursorManager;
	
	
	/**
	 * A component that displayes the two anchors (source 
	 * and target) and the bendpoints anchors for a 
	 * connection. 
	 * 
	 * The "target" figure is a <code>ConnectionFigure</code> 
	 * and it is received in <code>activate()</code> call. The
	 * anchors should listen the changes in the figure's 
	 * bendpoints and source and target points.
	 * 
	 * @author Georgi 
	 * @flowerModelElementId _b3SsAL8REd6XgrpwHbbsYQ
	 */
	public class ConnectionSelectionAchors extends AbstractSelectionAnchors {
		
		/**
		 * The mouse cursor icon when the mouse is over an anchor.
		 */
		[Embed(source="/icons/crossCursor.gif")]
        [Bindable]
		protected var crossCursor:Class;

		/** 
		 * Source anchor for the connection figure.
		 */
		protected var sourceAnchor:ResizeAnchor;
		
		/**
		 * Target anchor for the connection figure.
		 */
		protected var targetAnchor:ResizeAnchor;
		
		/**
		 * Bendpoints anchors for the connection figure.
		 * Map [BindablePoint] = ResizeAnchor. 
		 */
		protected var bendpointsAnchorsMap:Dictionary = new Dictionary();
		
		/**
		 * @flowerModelElementId _rnUeMEAqEeCEz721UNEmHg
		 */
		protected var createRelationAnchor:RelationAnchor;
		
		public static const SOURCE_ANCHOR:String = "SOURCE_ANCHOR";
		
		public static const TARGET_ANCHOR:String = "TARGET_ANCHOR";
		
		public static const BENDPOINT_ANCHOR:String = "BENDPOINT_ANCHOR";
		
		/**
		 * @flowerModelElementId _b3cdEb8REd6XgrpwHbbsYQ
		 */
		public function ConnectionSelectionAchors() {
			sourceAnchor = new ResizeAnchor(SOURCE_ANCHOR);
			addChild(sourceAnchor);
			
			targetAnchor = new ResizeAnchor(TARGET_ANCHOR);
			addChild(targetAnchor);
			
			createRelationAnchor = new RelationAnchor();
			addChild(createRelationAnchor);
		}
		
		/**
		 * Set the main selection flag an update the anchors.
		 */
		override public function setMainSelection(value:Boolean):void {
			var oldValue:Boolean = isMainSelection;
			isMainSelection = value;
			if (oldValue != value) {
				sourceAnchor.invalidateDisplayList();
				targetAnchor.invalidateDisplayList();
				for (var obj:Object in bendpointsAnchorsMap)
					ResizeAnchor(bendpointsAnchorsMap[obj]).invalidateDisplayList();
			}
		}
		
		/**
		 * Called from <code>activate</code>. Adds a <code>ResizeAnchor</code>
		 * for every point received from <code>ConnectionFigure</code>.
		 */
		private function updateDictionary(points:ArrayCollection):void {
			var i:int;
			for (i = 0; i < points.length; i++) {
				BindablePoint(points[i]).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, handleTargetMove);
				var anchor:ResizeAnchor = new ResizeAnchor(BENDPOINT_ANCHOR);
				bendpointsAnchorsMap[points[i]] = anchor;
				addChild(anchor);
				anchor.invalidateDisplayList();
			}
		}
		
		/**
		 * The method clears the map. It is called from <code>deactivate()</code>.
		 */
		private function clearDictionary():void {
			for (var obj:Object in bendpointsAnchorsMap) {
				BindablePoint(obj).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, handleTargetMove);		
				var anchor:ResizeAnchor = ResizeAnchor(bendpointsAnchorsMap[obj]);
				removeChild(anchor);
				delete bendpointsAnchorsMap[obj];
			}
		}
		
		/**
		 * Register listeners for source and target point. Any change
		 * will lead to an update for the associated anchor.
		 * Register a CollectionEvent handler for the points collection and a
		 * PropertyChangeEvent handler for every point in the collection.
		 * @flowerModelElementId _l4a3oMliEd6hMdHnTBcXLQ
		 */
		override public function activate(target:IFigure):void {
			super.activate(target);
			DisplayObject(target.getEditPart().getViewer().getRootEditPart().getFigure()).addEventListener(MouseEvent.MOUSE_OVER, changeCursor);
			DisplayObject(target.getEditPart().getViewer().getRootEditPart().getFigure()).addEventListener(MouseEvent.MOUSE_OUT, changeCursor);
			var cf:ConnectionFigure = getConnectionFigure();
			BindablePoint(cf._sourcePoint).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, handleTargetMove);
			BindablePoint(cf._targetPoint).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, handleTargetMove);
			updateDictionary(cf.points);
			cf.points.addEventListener(CollectionEvent.COLLECTION_CHANGE, collectionChangeHandler);
			
			// make createRelationAnchor visible only if target is connectable
			if ( !(target.getEditPart() is IConnectableEditPart)) {
				createRelationAnchor.visible = false;
			}
			
			handleTargetMove(null);
		}
		
		/**
		 * If a new bendpoint is added to the connection, 
		 * a CollectionChangeEvent is fired, and the anchors
		 * are updated.
		 */
		private function collectionChangeHandler(event:CollectionEvent):void {
			var i:int;
			var anchor:ResizeAnchor;
			if (event.kind == CollectionEventKind.ADD) { // new point was added
				for (i = 0; i < event.items.length; i++ ){
					BindablePoint(event.items[i]).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, handleTargetMove);
					anchor = new ResizeAnchor(BENDPOINT_ANCHOR);
					bendpointsAnchorsMap[event.items[i]] = anchor;
					addChild(anchor);
				}
				handleTargetMove();
			} else if (event.kind == CollectionEventKind.REMOVE) { // point was removed
				for (i = 0; i < event.items.length; i++ ){
					BindablePoint(event.items[i]).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, handleTargetMove);
					anchor = ResizeAnchor(bendpointsAnchorsMap[event.items[i]]);
					delete bendpointsAnchorsMap[event.items[i]];
					removeChild(anchor);
				}
				handleTargetMove();
			}
		}
		
		/**
		 * Unregister the listeners.
		 */
		override public function deactivate():void {
			DisplayObject(target.getEditPart().getViewer().getRootEditPart().getFigure()).removeEventListener(MouseEvent.MOUSE_OVER, changeCursor);
			DisplayObject(target.getEditPart().getViewer().getRootEditPart().getFigure()).removeEventListener(MouseEvent.MOUSE_OUT, changeCursor);
			BindablePoint(ConnectionFigure(target)._sourcePoint).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, handleTargetMove);
			BindablePoint(ConnectionFigure(target)._targetPoint).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, handleTargetMove);
			var cf:ConnectionFigure = getConnectionFigure();
			clearDictionary();
			cf.points.removeEventListener(CollectionEvent.COLLECTION_CHANGE, collectionChangeHandler);
			super.deactivate();
		}

		/**
		 * Update anchor position based on the points' positions.
		 * @flowerModelElementId _b3cdN78REd6XgrpwHbbsYQ
		 */
		protected function handleTargetMove(event:Event=null):void {
			sourceAnchor.x = getConnectionFigure()._sourcePoint.x;
			sourceAnchor.y = getConnectionFigure()._sourcePoint.y;
			sourceAnchor.invalidateDisplayList();
			
			targetAnchor.x = getConnectionFigure()._targetPoint.x;
			targetAnchor.y = getConnectionFigure()._targetPoint.y;
			targetAnchor.invalidateDisplayList();
			
			// compute the connection anchor point so that it stays closer to the middle of the segment but
			// with a small displacement so that it won't overlap with eventual labels
			var middleSegment:ConnectionSegment = getConnectionFigure().getSegmentAt(getConnectionFigure().getPointFromDistance()[1]);
			
			var middlePoint:Array = getConnectionFigure().getMiddlePointRect();
			var dx:int = middlePoint[0] - middleSegment.sourcePoint.x;
			var dy:int = middlePoint[1] - middleSegment.sourcePoint.y;
			
			// if middle is closer to source then pick target to compute displacement
			if (dx < middleSegment.targetPoint.x - middlePoint[0]) {
				dx = middleSegment.targetPoint.x - middlePoint[0];
			}
			
			if (dy < middleSegment.targetPoint.y - middlePoint[1]) {
				dy = middleSegment.targetPoint.y - middlePoint[1];
			}
			
			// move a little back the anchor because usually Connection Labels are displayed on center
			// and other relations are linked in center too
			createRelationAnchor.x = middlePoint[0] + int(dx / 2);
			createRelationAnchor.y = middlePoint[1] + int(dy / 2);
			
			for (var obj:Object in bendpointsAnchorsMap) {
				var anchor:ResizeAnchor = ResizeAnchor(bendpointsAnchorsMap[obj]);
				if (anchor != null) {
					anchor.x = BindablePoint(obj).x;
					anchor.y = BindablePoint(obj).y;
					anchor.invalidateDisplayList();
				}
			}
		}
		
		/**
		 * If a <code>ResizeAnchor</code> of a <code>ConnectionSelectionAnchors</code>
		 * fired the mouse over event and no drag operation is in progress, 
		 * update the cursor.
		 * Otherwise, if any other object but a <code>ResizeAnchor</code> fired
		 * the event and no drag operation is in progress, remove cursor icon.
		 */
		protected function changeCursor(event:MouseEvent):void {
			if (event.target is ResizeAnchor){ // the object under mouse is a ResizeAnchor
				if (DisplayObject(event.target).parent is ConnectionSelectionAchors && target.getEditPart().getViewer().getActiveTool() is SelectMoveResizeTool
					&& !SelectMoveResizeTool(target.getEditPart().getViewer().getActiveTool()).isDragInProgress()){ // there is no drag&drop operation in progress
						 // if the anchor belongs to a ConnectionFigure
					cursorManager.removeAllCursors();
					cursorManager.setCursor(crossCursor, 2, -16, -16);// center the icon's image (32x32) over the anchor
				}
			} else { // the object under mouse is not a ResizeAnchor
				if (target.getEditPart() != null && target.getEditPart().getViewer().getActiveTool() is SelectMoveResizeTool)
					if (!SelectMoveResizeTool(target.getEditPart().getViewer().getActiveTool()).isDragInProgress()) // there is no drag&drop operation in progress
						cursorManager.removeAllCursors();
			}
		}
		
		/**
		 * The method returns the BindablePoint connected to the
		 * current anchor received as parameter.
		 */
		public function getBindablePoint(anchor:ResizeAnchor):BindablePoint {
			for (var obj:Object in bendpointsAnchorsMap)
				if (bendpointsAnchorsMap[obj] == anchor)
					return BindablePoint(obj);
			return null;
		}
		
	}
}