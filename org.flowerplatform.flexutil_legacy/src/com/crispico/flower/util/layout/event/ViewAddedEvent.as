package com.crispico.flower.util.layout.event {
	import flash.events.Event;
	
	import mx.core.UIComponent;
	
	/**
	 * @author Cristi
	 */
	public class ViewAddedEvent extends Event {
		
		public static const VIEW_ADDED:String = "view_added";
		
		private var _view:UIComponent;
		
		public function ViewAddedEvent(view:UIComponent) {
			super(VIEW_ADDED);
			_view = view;
		}

		public function get view():UIComponent {
			return _view;
		}

	}
}