package com.crispico.flower.flexdiagram.connection {
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.event.UpdateConnectionEndsEvent;
	import com.crispico.flower.flexdiagram.ui.ConnectionFigure;
	import com.crispico.flower.flexdiagram.ui.ConnectionLabelFigure;
	
	import flash.events.Event;
	import flash.geom.Point;
	
	/**
	 * The editPart for the connection labels. The editPart is selectable, so an 
	 * implementation for the <code>isSelectable()</code> and 
	 * <code>updateSelectedState()</code> methods is provided.
	 * 
	 * @author Georgi
	 * @flowerModelElementId _hE3UcABJEd-COsJ6v0d3bA
	 */
	public class ConnectionLabelEditPart extends EditPart {
		
		protected const SOURCE_UP:int = 0;
		
		protected const SOURCE_DOWN:int = 1;
		
		protected const TARGET_UP:int = 2;
		
		protected const TARGET_DOWN:int = 3;
		
		protected const MIDDLE_UP:int = 4;
		
		protected const MIDDLE_DOWN:int = 5;
		
		private const HORIZONTAL_LINE:int = -1;
		
		private const VERTICAL_LINE:int = 1;
		
		private const ALMOST_HORIZONTAL_LINE:int = 0;

		public function ConnectionLabelEditPart(model:Object, viewer:DiagramViewer) {
			super(model, viewer);
		}
		
		/**
		 * @flowerModelElementId _-DPLsAvjEd-p6bQBEYsbUw
		 */
		override public function getFigureClass():Class {
			return ConnectionLabelFigure;
		}
	
		/**
		 * Override the <code>activate()</code> method from superclass in order
		 * to add a <code>UpdateConnectionEndsEvent</code> listener on the connectionEditPart
		 * attached (in order to receive notification when the connection changes its position
		 * so the label's position should be recomputed)
		 */
		override public function activate():void {
			super.activate();
			parent.addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updatePositionHandler);
		}
	
		/**
		 * Override the <code>deactivate()</code> method from superclass in order
		 * to remove the listeners added in the <code>activate()</code> method.
		 */
		override public function deactivate():void {
			parent.removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updatePositionHandler);
			super.deactivate();
		}
		
		/**
		 * 
		 * <p>If the connection's orientation changes (an <code>UpdateConnectionEndsEvent</code> is 
		 * sent) the label's position is recomputed. The event is send when the source/target of
		 * the connection is changed, so we need to make sure that the connection figure is valid
		 * (has a source and a target editPart) in order to compute the correct values for
		 * the label.
		 *  
		 * <p>The method computes some information about the connection in order
		 * to provide it to the <code>getLabelPosition()</code> method.
		 * <ul>
		 * 	<li> If the connection intersects the vertical/horizontal edge of the
		 * 	source/target figure, 'isHorizontal' flag will be set to false/true.
		 * 	There is a special case which concerns the label's whose position is
		 * 	"MIDDLE". In this case, 'isHorizontal' flag is computed by the slope of
		 * 	the associated segment.
		 * 	<li> The 'refPoint' variable represents the reference point for the label.
		 * 	This could be the sourcePoint/targetPoint/midPoint of the connection.
		 * 	<li> The 'isTopOrLeft' flag is computed based on the information given 
		 * 	by the <code>getPositionType()</code> method; this flag is set for the 
		 * 	labels whose position constant is UP.
		 * 	<li> The 'alignTopOrLeft' flag is computed by the 
		 * 	<code>ClipUtils.computeEdgeIntersectionProperty()</code> method.
		 * </ul>
		 * @flowerModelElementId _38BMEBCUEd-bY9BcYhuW0g
		 */
		protected function updatePositionHandler(event:Event=null):void {
			var fig:ConnectionLabelFigure = ConnectionLabelFigure(getFigure());
			if (fig != null && ConnectionFigure(parent.getFigure()) != null && 
				ConnectionEditPart(parent).getSource() != null && 
				ConnectionEditPart(parent).getTarget() != null) {
					var connectionFigure:ConnectionFigure = ConnectionFigure(parent.getFigure());
					// this will be :
					// ALMOST_HORIZONTAL_LINE when the line is very close to horizontal
					// HORIZONTAL_LINE when horizontal
					// VERTICAL_LINE when vertical 
					var lineType:int; 
					var isTopOrLeft:Boolean;
					var refPoint:BindablePoint;
					var posType:int = getPositionType();
					var segmId:int;
					var array:Array;
					var alignTopOrLeft:Boolean;
					var slope:Number;
					
					switch (posType) {
						case SOURCE_UP:
							segmId = 0;
							isTopOrLeft = true;
							refPoint = connectionFigure._sourcePoint;
							array = ClipUtils.computeEdgeIntersectionProperty(
								ConnectionEditPart(parent).getSource().getConnectionAnchorRect(), refPoint);
							break;
						case SOURCE_DOWN:
							segmId = 0;
							isTopOrLeft = false;
							refPoint = connectionFigure._sourcePoint;
							array = ClipUtils.computeEdgeIntersectionProperty(
								ConnectionEditPart(parent).getSource().getConnectionAnchorRect(), refPoint);
							break;
						case TARGET_UP:
							segmId = connectionFigure.getNumberOfSegments() - 1;
							isTopOrLeft = true;
							refPoint = connectionFigure._targetPoint;
							array = ClipUtils.computeEdgeIntersectionProperty(
									ConnectionEditPart(parent).getTarget().getConnectionAnchorRect(), refPoint);
							break;
						case TARGET_DOWN:
							segmId = connectionFigure.getNumberOfSegments() - 1;
							isTopOrLeft = false;
							refPoint = connectionFigure._targetPoint;
							array = ClipUtils.computeEdgeIntersectionProperty(
								ConnectionEditPart(parent).getTarget().getConnectionAnchorRect(), refPoint);
							break;
						case MIDDLE_UP:
							var arr:Array = connectionFigure.getPointFromDistance() as Array;
							refPoint = arr[0];
							segmId = arr[1];
							slope = connectionFigure.getSegmentAt(segmId).getSegmentSlope();
							if (Math.abs(slope) <= 0.1) {
								lineType = ALMOST_HORIZONTAL_LINE; // almost perfectly horizontal
							} else if (Math.abs(slope) < 1) { 
								lineType = HORIZONTAL_LINE;
							} else {
								lineType = VERTICAL_LINE;
							}
							if (lineType != VERTICAL_LINE) {
								alignTopOrLeft = false;
								if (slope < 0)
									isTopOrLeft = false;
								else
									isTopOrLeft = true;
							} else {
								isTopOrLeft = true;
								if (slope < 0)
									alignTopOrLeft = true;
								else 
									alignTopOrLeft = false;
							}
							break;
					}
					fig.validateProperties();
					fig.invalidateSize();
					if (posType != MIDDLE_UP) {
						lineType = array[0] == true ? HORIZONTAL_LINE : VERTICAL_LINE;
						alignTopOrLeft = array[1];
					} 
					var p:Point = getLabelPosition(refPoint, fig.textWidth + 5, fig.textHeight + 5, lineType, isTopOrLeft, alignTopOrLeft);
					fig.x = p.x;
					fig.y = p.y;
					fig.invalidateDisplayList();
				}
		}
		
		/**
		 * <p>The constant represent the place where is label is displayed. The first part
		 * of the constant name represents that the label should be placed:
		 * <ul>
		 * 		<li> near the source of the connection
		 * 		<li> near the target of the connection
		 * 		<li> in the middle of the connection
		 * </ul>
		 * The second part of the constant name represents that the label should be placed:
		 * <ul>
		 * 		<li> on the top/bottom side, if the labels' alignment is vertical
		 * 		<li> on the left/right side, if the labels' alignment is horizontal
		 * </ul>
		 * 
		 * <p>The method must be implemented by its subclasses
		 * in order to provide an identifier for its position
		 * using information from the model.
		 */
		protected function getPositionType():int {
			throw new Error("getPositionType() should be implemented");
		}
		
		/**
		 * The method computes the (x,y) coordinated for the label. It is called by
		 * the <code>computeLabelPosition()</code> method. 
		 */
		private function getLabelPosition(referencePoint:BindablePoint, labelWidth:int, labelHeight:int, 
			lineType:int, isTopOrIsLeft:Boolean, alignLeftOrTop:Boolean):Point {
			var resultPoint:Point = new Point();
			if (lineType != VERTICAL_LINE) {
				if (!alignLeftOrTop)
					resultPoint.x = (lineType == ALMOST_HORIZONTAL_LINE) ? referencePoint.x - labelWidth / 2 // center the label on the line when the line is almost horizontal
													  : referencePoint.x + 5; //shadow correction
				else
					resultPoint.x = referencePoint.x - labelWidth;
				if (isTopOrIsLeft) 
					resultPoint.y = referencePoint.y - labelHeight;
				else
					resultPoint.y = referencePoint.y;
			} else {
				if (isTopOrIsLeft) 
					resultPoint.x = referencePoint.x - labelWidth;
				else 
					resultPoint.x = referencePoint.x;
				if (!alignLeftOrTop) 
					resultPoint.y = referencePoint.y;
				else 
					resultPoint.y = referencePoint.y - labelHeight;
			}
			return resultPoint;
		}
		
		/**
		 * The editPart is selectable.
		 */
		override public function isSelectable():Boolean {
			return true;
		}
		
		/**
		 * Override updateSelectedState because we need to refresh
		 * the children when selected state is changed.
		 */
		override public function updateSelectedState(isSelected:Boolean, isMainSelection:Boolean):void {
			var oldSelected:Boolean = getSelected();
			super.updateSelectedState(isSelected, isMainSelection);
			if (oldSelected != isSelected) {
				refreshVisualDetails(false);
			}
		}
	}
}