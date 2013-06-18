package org.flowerplatform.editor.remote {
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	
	[RemoteClass]
	public class EditorStatefulClientLocalState implements IStatefulClientLocalState {

		public var editableResourcePath:String;
		
		/**
		 * Not used on Flex, but present to avoid warnings from
		 * serialization
		 */ 
		public var forcingSubscriptionFromServer:Boolean;
	
	}
}