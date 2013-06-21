package org.flowerplatform.emf_model.notation {
	
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;

	[Bindable]
	[RemoteClass]
	public class Node extends View {
		public var layoutConstraint_RH:ReferenceHolder;
	}
}