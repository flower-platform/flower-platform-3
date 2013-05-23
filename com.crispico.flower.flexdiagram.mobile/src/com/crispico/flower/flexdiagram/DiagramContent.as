package com.crispico.flower.flexdiagram {
	
	import flash.geom.Rectangle;
	
	import mx.containers.Canvas;
	import mx.core.ScrollPolicy;
	
	import spark.components.Group;

	/**
	 * The default container to be used as a figure for a diagram. The associated
	 * EditPart should extend AbsoluteEditPart.
	 * 
	 * <p>
	 * When scroll or resize events are detected, refreshVisualChildren() is called, "bootstrapping"
	 * the figure optimization mechanism.
	 * 
	 * @author cristi
	 * @flowerModelElementId _Ie3k0MVeEd6x1dpkaVcaXg
	 */
	[SecureSWF(rename="off")]
	public class DiagramContent extends Group implements IFigure {
		
		protected var editPart:EditPart;
		
		/**
		 * The actual width for this component that should be known by the scrollbar in order 
		 * to display correctly. This is necessary because the figure marking the right most point of the 
		 * DiagramContent might not be on the screen. 
		 * 
		 * @author Luiza
		 * @flowerModelElementId _ilHn8EstEeCbK9Qdqt9oUA
		 */
		private var _maxScrollWidth:Number = 0;
		
		/**
		 * The actual height for this component that should be known by the scrollbar in order 
		 * to display correctly. This is necessary because the figure marking the buttom most point of the 
		 * DiagramContent might not be on the screen. 
		 * 
		 * @author Luiza
		 * @flowerModelElementId _ilJdIEstEeCbK9Qdqt9oUA
		 */
		private var _maxScrollHeight:Number = 0;
		
		/**
		 * @flowerModelElementId _Ie3k2MVeEd6x1dpkaVcaXg
		 */
		public function DiagramContent() {
			super();
			// TODO CC: mobile support
//			horizontalScrollPolicy = ScrollPolicy.OFF;
//			verticalScrollPolicy = ScrollPolicy.OFF;
		}
		
		/**
		 * @flowerModelElementId _IfAuwsVeEd6x1dpkaVcaXg
		 */
		public function getEditPart():EditPart {
			return editPart;
		}
		
		/**
		 * @flowerModelElementId _IfAuxsVeEd6x1dpkaVcaXg
		 */
		public function setEditPart(newEditPart:EditPart):void{
			this.editPart = newEditPart;
		}
		
		/**
		 * 
		 * It corrects the computed measuredWidth and measuredHeight if they are smaller then _maxScrollWidth and _maxScrollHeight.
		 * There are two similar cases for both measuredWidth and measuredHeight:
		 * 
		 * <ul>
		 * 	<li>the computed measuredWidth is smaller then _maxScrollWidth (this happens if the right most EditPart is not visible => its figure is not on the screen);
		 *  so measuredWidth must be set to the _maxScrollWidth computed by AbsoluteLayoutEditPart in refreshVisualChildren()
		 * 
		 * 	<li>the computed measuredWidth is equal or bigger then _maxScrollWidth (this happens if the right most EditParts is not an IAbsolutePositionEditPart and its
		 * figure is not reused or if right most EditPart is visible) then the measuredWidth is the real one. In this case cancel _maxScrollWidth because is no longer usefull.
		 * </ul>
		 * 
		 * Also increase the dimensions of the <code>DiagramContent</code> with 5 pixels. This modification is required because, during the zoom
		 * process the visual components that are close to the edges are sometimes trucated.
		 */
		override protected function measure():void {
			super.measure();
			
			if (measuredHeight < _maxScrollHeight) {
				// if the bottom most figure is not on the screen then the computed size is not correct (is smaller)
				// so replace it with the one determined by AbsoluteLayoutEditPart
				measuredHeight = _maxScrollHeight;
			} else {
				// if a figure was moved causing the diagrama to increase size then this value is no longer usefull 
				// the bottom most figure is visibile on the screen
				_maxScrollHeight = 0;
			}
			
			if (measuredWidth < _maxScrollWidth) {
				// if the right most figure is not on the screen then the computed size is not correct (is smaller)
				// so replace it with the one determined by AbsoluteLayoutEditPart
				measuredWidth = _maxScrollWidth;
			} else {
				// if a figure was moved causing the diagrama to increase size then this value is no longer usefull 
				// the right most figure is visibile on the screen in this case
				_maxScrollWidth = 0;
			}			
			
			measuredHeight += 5 / scaleX;
			measuredWidth += 5 / scaleY;
		}
		
		/**
		 * Resets the _maxScrollWidth and _maxScrollHeight. If the two values are larger then the computed measuredWidth and measuredHeight
		 * then the last two will be replaced with the received values. 
		 * 
		 * @flowerModelElementId _ilL5YUstEeCbK9Qdqt9oUA
		 */
		public function setMaxScrollDimensions(maxWidth:Number, maxHeight:Number):void {			
			_maxScrollWidth = maxWidth;
			_maxScrollHeight = maxHeight;
			invalidateSize();
		}
	}
}