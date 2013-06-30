package org.flowerplatform.communication.transferable_object {
	
	
	[RemoteClass(alias="org.flowerplatform.blazeds.custom_serialization.ReferenceHolder")]
	public class ReferenceHolder extends AbstractReferenceHolder {
	
		public var registry:LocalIdTransferableObjectRegistry;
		
		override protected function getReferencedObjectFromRegistry(id:Object):Object {
			if (registry == null) {
				throw new Error("Trying to access LocalIdReferenceHolder.referencedObject, but the registry is null");
			}
			return registry.getObjectById(id);	
		}
		
		public function get referenceIdAsString():String {
			return String(referenceId);
		}
	}
}