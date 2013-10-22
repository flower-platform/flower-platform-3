/**/
package org.flowerplatform.flexutil.layout.event
{
	
	
	import flash.events.Event;
	
	/**
	 * Dispatched by <code>EditorSupport</code> each time the dirty state is updated.
	 * 
	 * @author Mariana
	 */ 
	public class DirtyStateUpdatedEvent extends Event {
		public static const DIRTY_STATE_UPDATED:String = "DirtyStateUpdated";
		
		public var editorStatefulClient:Object;
			
		public function DirtyStateUpdatedEvent(editorStatefulClient:Object) {
			super(DIRTY_STATE_UPDATED);
			
			this.editorStatefulClient = editorStatefulClient;
		}
	}
}
