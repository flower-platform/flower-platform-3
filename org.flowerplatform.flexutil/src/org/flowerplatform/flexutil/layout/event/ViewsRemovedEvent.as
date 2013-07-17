package org.flowerplatform.flexutil.layout.event
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;

	/**
	 * Dispatched when one or multiple views are removed from workbench.
	 * 
	 * @author Cristina
	 */ 
	public class ViewsRemovedEvent extends Event {
		
		public static const VIEWS_REMOVED:String = "views_removed";
		
		private var _removedViews:ArrayCollection; /* of UIComponent */
		
		public var dontRemoveViews:ArrayCollection; /* of UIComponent */
		
		public function ViewsRemovedEvent(removedViews:ArrayCollection) {
			super(VIEWS_REMOVED);
			
			_removedViews = removedViews;
			dontRemoveViews = new ArrayCollection();
		}
		
		/**
		 * The <code>ViewLayoutData</code> corresponding to the view
		 * that is being closed.
		 */ 
		public function get removedViews():ArrayCollection {
			return _removedViews;
		}
	}
	
}