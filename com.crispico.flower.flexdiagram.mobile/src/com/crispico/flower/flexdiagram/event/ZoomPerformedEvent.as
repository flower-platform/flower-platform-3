package com.crispico.flower.flexdiagram.event {
	import flash.events.Event;
	
	/**
	 * The event is dispatched when the <code>RootFigure</code> changes
	 * its scaleX or scaleY property. It is used to notify all graphical
	 * figures on the diagram. It is needed for figures that display text
	 * to truncate it or not depending on the scale factor.
	 * 
	 * @author Georgi
	 * @flowerModelElementId _LWrW8MllEd6C8JWSqvaxfA
	 */
	public class ZoomPerformedEvent extends Event {
		
		public static const ZOOM_PERFORMED:String = "ZoomPerformed";
		
		public var zoomPercent:Number;
		
		public function ZoomPerformedEvent(zoomPercent:Number = 1) {
			super(ZOOM_PERFORMED);
			this.zoomPercent = zoomPercent;
		}
	}
}