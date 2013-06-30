package org.flowerplatform.communication.transferable_object {
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.mx_internal;
	import mx.utils.DescribeTypeCache;
	
	import org.flowerplatform.flexutil.Utils;
	
	use namespace mx_internal;
	
	/**
	 * @author Cristi
	 */
	public class AbstractTransferableObjectRegistry {
		
		private var _registry:Dictionary = new Dictionary();
		
		protected function getIdForObject(object:IEventDispatcher):Object {
			throw new Error("This method should be implemented");
		}

		protected function copyProperties(source:IEventDispatcher, dest:IEventDispatcher, postProcessOnly:Boolean):void {
			var classInfo:XML = DescribeTypeCache.describeType(dest).typeDescription;
			for each (var v:XML in classInfo..accessor) {
				if (v.@name != null && v.@access != 'readonly') {
					copyProperty(source, dest, v.@name, postProcessOnly);
				}
			}
		}
		
		protected function copyProperty(source:Object, dest:Object, propertyName:String, postProcessOnly:Boolean):void {
			if (!postProcessOnly) {
				dest[propertyName] = source[propertyName];
			}
		}
		
		mx_internal function get registry():Dictionary {
			return _registry;
		}
	
		public function getObjectById(id:Object):IEventDispatcher {
			return registry[id];
		}
		
		public function clearRegistry():void {
			_registry = new Dictionary();
		}
		
		/**
		 * The method receives the <code>TransferableObject</code>
		 * that needs to be updated.
		 */
		public function updateObject(object:IEventDispatcher): void {
			var oId:Object = getIdForObject(object);
			var oldObject:IEventDispatcher = registry[oId];
			
			if (oldObject == null){
				registry[oId] = object;
				copyProperties(null, object, true);
			} else {
				copyProperties(object, oldObject, false);
				try {
					oldObject.dispatchEvent(new TransferableObjectUpdatedEvent(oldObject));
				} catch (e:Object) {
					// TODO CS/CS3 properly log error
					Alert.show("Error while dispatching object updated event.");
				}
			}
		}
		
		/**
		 * The method receives the ID of the <code>TransferableObject</code>
		 * that needs to be disposed.
		 */
		public function removeAndDisposeObject(objectID:Object):void {
			var oId:Object = objectID;
			var oldObject:IEventDispatcher = registry[oId];
			if (oldObject != null) {
				try {
					disposeObject(oldObject);
				} catch (e:Object) {
					// TODO CS/CS3 properly log error
					Alert.show("Error while disposing or dispatching object disposed event.");
				}
				removeObject(oId);
			}
		}
		
		protected function disposeObject(object:IEventDispatcher):void {
			object.dispatchEvent(new TransferableObjectDisposedEvent(object));
			if (object is IDisposable) {
				IDisposable(object).dispose();			
			}
		}
		
		protected function removeObject(objectID:Object):void {
			delete registry[objectID];			
		}
		
		public function disposeTransferableObjectHierarchy(object:IEventDispatcher):void {
			if (object == null ||
				object is IDontDisposeDuringDisposeHierarchy && 
				!IDontDisposeDuringDisposeHierarchy(object).shouldDispose()) {
				return;
			}
			// only the removal; disposal is at the end, because it sets
			// the _RH fields to null, which is not wanted at this moment
			removeObject(getIdForObject(object));
			
			var classInfo:XML = DescribeTypeCache.describeType(object).typeDescription;
			for each (var v:XML in classInfo..accessor) {
				
				if (v.@name == null || !Utils.endsWith(v.@name, "_RH")) {
					continue;
				}
				
				var value:Object = object[v.@name];
				if (value is AbstractReferenceHolder) {
					// get the reference directly from the registry (i.e. don't use the cached value);
					// this way we avoid processing parent elements (that would generate stack overflow).
					// this is possible because the first step of this method is to remove from the registry
					var referencedObject:IEventDispatcher = getObjectById(AbstractReferenceHolder(value).referenceId);
					if (referencedObject != null) {
						disposeTransferableObjectHierarchy(referencedObject);
					}
				} else if (value is ArrayCollection) {
					for each (var child:AbstractReferenceHolder in ArrayCollection(value)) {
						referencedObject = getObjectById(AbstractReferenceHolder(child).referenceId);
						if (referencedObject != null) {
							disposeTransferableObjectHierarchy(referencedObject);
						}
					}
				}
			}
			
			// dispose here, because this method will put to null the _RH fields
			disposeObject(object);
		}
		
		// TODO CS/CS3 in caz de eroare, imbunatatit logingul
		public function updateObjects(objectsToUpdate:ArrayCollection):void {
			for (var i:int = 0; i < objectsToUpdate.length; i++) {
				try {
					updateObject(IEventDispatcher(objectsToUpdate.getItemAt(i)));
				} catch (e:Object) {
					Alert.show("Error in TransferObjectsCommand.processObjectsToUpdate()");
					if (e is Error) {
						trace(e.getStackTrace());
					}
				}
			}
		}
		
		public function removeAndDisposeObjects(objectsIdsToDispose:ArrayCollection):void {
			for (var i:int = 0; i < objectsIdsToDispose.length; i++) {
				try {
					removeAndDisposeObject(objectsIdsToDispose.getItemAt(i));
				} catch (e:Object) {
					Alert.show("Error in TransferObjectsCommand.processObjectsToDispose()");
					if (e is Error) {
						trace(e.getStackTrace());
					}
				} 
			}
		}
	}
}