package org.flowerplatform.communication.transferable_object {
	
	
	/**
	 * See the Java equivalent class.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _zIr9IMK6Ed6-yKDFMO4rZg
	 */ 
	[Bindable]
	[SecureSWF(rename="off")]
	public class AbstractReferenceHolder {
	
		protected var _referenceId:Object;
		
		protected var cachedReferencedObject:TransferableObject;

		[SecureSWF(rename="off")]
		public function get referenceId():Object {
			return _referenceId;
		}

		[SecureSWF(rename="off")]
		public function set referenceId(referenceId:Object):void {
			_referenceId = referenceId;
		}

		[SecureSWF(rename="off")]
		public function get referencedObject():TransferableObject {
			if (cachedReferencedObject == null) {
				cachedReferencedObject = TransferableObject(getReferencedObjectFromRegistry(referenceId));
			}
			return cachedReferencedObject;
		}
		
		protected function getReferencedObjectFromRegistry(id:Object):Object {
			throw new Error("This method should be implemented");
		}
		
		/**
		 * Use this function to compare for equality (e.g. after a property change event).
		 * It allows null as arguments.
		 */ 
		public static function equals(o1:AbstractReferenceHolder, o2:AbstractReferenceHolder):Boolean {
			if (o1 == null && o2 == null)
				return true;
			else if (o1 == null || o2 == null)
				return false;
			else
				return o1.referenceId == o2.referenceId;
		}
		
		public function toString():String {
			return "[RH:" + referenceId + "]";
		}
		
	}
}
