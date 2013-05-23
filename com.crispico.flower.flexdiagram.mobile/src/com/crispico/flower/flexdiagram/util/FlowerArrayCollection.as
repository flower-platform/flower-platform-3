package com.crispico.flower.flexdiagram.util
{
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;

	/**
	 * This custom ArrayCollection contains a custom ArrayList 
	 * with the purpose of adding new functionalities like:
	 * <ul>
	 * 	<li> add multiple items with only one CollectionEventKind.ADD event dispatched.
	 * 		this is done by the same method #addAll or #addAllAt which delegates 
	 * 		to the custom ArrayList#addAllAt method. 
	 * </ul>
	 *  
	 * @private
	 * 
	 * @author Sorin
	 */ 	
	public class FlowerArrayCollection extends ArrayCollection {
		
		public function FlowerArrayCollection(source:Array = null) {
			super(source);
		}
		
		/**
		 * This method was extended with the purpose of creating our custom ArrayList.
		 * @private
		 */
		override public function set source(s:Array):void {
		    list = new FlowerArrayList(s);
		}
		
		override public function addAllAt(addList:IList, index:int):void {
			ArrayList(list).addAllAt(addList, index);
		}
	}
}

import mx.collections.IList;
import mx.collections.ArrayList;
import mx.events.CollectionEventKind;
import mx.events.PropertyChangeEvent;
import mx.events.CollectionEvent;
import mx.resources.ResourceManager;
	
/**
 * This class is a custom <code>ArrayList</code> which is exposed by upper custom ArrayCollection and
 * adds the folowing functionalities:
 * <ul>
 * 	<li> add multiple items with only one CollectionEventKind.ADD event dispatched. 
 * </ul>
 * @private
 */ 
class FlowerArrayList extends ArrayList {
	
	public function FlowerArrayList(source:Array = null) {
		super(source);
	}
	    
    /**
     * The meaning of the parameters is the same as for the super.addItemAt method,
     * with the exception that it does not add one item but it adds multiple items.
     *   
      * @see mx.collections.ArrayList#addItemAt
     */
    override public function addAllAt(addList:IList, index:int):void {
        if (index < 0 || index > length) {
			var message:String = ResourceManager.getInstance().getString(
				"collections", "outOfBounds", [ index ]);
        	throw new RangeError(message);
		}
		var items:Array = addList.toArray();
		for (var i:int = 0; i < items.length; i++) {
			var item:Object = items[i];
	  	    source.splice(index + i, 0, item);
	  	    // Similary for each element start tracking updates.
    	    startTrackUpdates(item); 
		}
  		// the following dispatched a single event for all items.
        almostCopiedInternalDispatchEvent(CollectionEventKind.ADD, items, index);
    }
    
	/**
	 * The following method is almost copy pasted from the ArrayList.
	 * This was done because it was private. Almost because <code>ArrayList#_dispatchEvents</code>
	 * is private and cannot accesed, so it ignores it and just dispatches the events.
	 *
	 * @see mx.collections.ArrayList#internalDispatchEvent
	 */
	private function almostCopiedInternalDispatchEvent(kind:String, item:Object = null, location:int = -1):void {
		if (hasEventListener(CollectionEvent.COLLECTION_CHANGE)) {
	        var event:CollectionEvent = new CollectionEvent(CollectionEvent.COLLECTION_CHANGE);
		        event.kind = kind;
		        event.items.push(item);
		        event.location = location;
		        dispatchEvent(event);
		}

    	// now dispatch a complementary PropertyChangeEvent
    	if (hasEventListener(PropertyChangeEvent.PROPERTY_CHANGE) && (kind == CollectionEventKind.ADD || kind == CollectionEventKind.REMOVE)) {
    		var objEvent:PropertyChangeEvent = new PropertyChangeEvent(PropertyChangeEvent.PROPERTY_CHANGE);
    		objEvent.property = location;
    		if (kind == CollectionEventKind.ADD)
    			objEvent.newValue = item;
    		else
    			objEvent.oldValue = item;
    		dispatchEvent(objEvent);
	    }
	}	    
}
