package org.flowerplatform.emf_model.notation {
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;

	[Bindable]
	[RemoteClass]
	public class Edge extends View {
		public var source_RH:ReferenceHolder;
		public var target_RH:ReferenceHolder;
	}
}