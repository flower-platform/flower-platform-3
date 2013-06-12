package  org.flowerplatform.communication.tree.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	
	/**
	 * @author Cristina
	 * @flowerModelElementId _UjSwUA70EeKbvNML8mcTuA
	 */
	[RemoteClass]
	public class GenericTreeStatefulClientLocalState implements IStatefulClientLocalState {
		
		/**
		 * @flowerModelElementId _jhrbMA70EeKbvNML8mcTuA
		 */
		public var openNodes:ArrayCollection;
		
		/**
		 * @flowerModelElementId _T5C_cBEjEeKYjqFAQECmkA
		 */
		public var clientContext:Object;
		
		public var statefulContext:Object;
	}
	
}