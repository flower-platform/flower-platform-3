/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation version 3.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
* 
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.editor.event {
	
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
