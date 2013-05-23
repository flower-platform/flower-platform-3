package  com.crispico.flower.flexdiagram.event {
	import com.crispico.flower.flexdiagram.EditPart;
	
	import flash.events.Event;

	/**
	 * Dispatched (only if a figure doesn't exist i.e. the EditPart is not 
	 * "visible") by EditParts that implement <code>IAbsolutePositionEditPart</code>
	 * when the model changes and the bounds rectangle is updated. Received by
	 * <code>AbsoluteLayoutEditPart</code> that updates the display. It can be
	 * listened as well by sub-EditParts of <code>IAbsolutePositionEditPart</code>
	 * that have connections. When these EditParts don't have a figure, they should
	 * rely on their parent to "estimate" the position for the ends.
	 * 
	 * @see AbsoluteLayoutEditPart.boundsRectChangedHandler()
	 * @see AbsoluteLayoutEditPart.refreshChildren()
	 * 
	 * @author Cristi
	 */
	public class BoundsRectChangedEvent extends Event {
	
		public static const BOUNDS_RECT_CHANGED:String = "BoundsRectChangedEvent";
		
		public var editPart:EditPart;
		
		public function BoundsRectChangedEvent(editPart:EditPart):void {
			super(BOUNDS_RECT_CHANGED);
			this.editPart = editPart;
		}

	}
}