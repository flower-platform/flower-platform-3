package com.crispico.flower.util.layout.event {
	import flash.events.Event;
	
	/**
	 * Dispathed by <code>Workbench</code> each time the layout data structure changes.
	 * 
	 * @see Workbench
	 * @author Cristina
	 * @flowerModelElementId _IGWRMFMoEeG1ioU_PGdg8A
	 */ 
	public class LayoutDataChangedEvent extends Event {
		
		public static const LAYOUT_DATA_CHANGED:String="layoutData_changed";
		
		public function LayoutDataChangedEvent() {
			super(LAYOUT_DATA_CHANGED);
		}

	}
}