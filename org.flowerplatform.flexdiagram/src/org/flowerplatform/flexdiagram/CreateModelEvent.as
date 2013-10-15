package org.flowerplatform.flexdiagram {
	import flash.events.Event;
	
	/**
	 * Dispatched by DiagramRenderer to show model creation options (e.g. a menu with actions).
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class CreateModelEvent extends Event	{
		
		public static const SHOW_CREATE_OPTIONS:String = "SHOW_CREATE_OPTIONS";
		
		public var context:Object;
		
		/**
		 * If true, the tool will not be deactivated after dispatching this event. 
		 */ 
		public var finishToolJobAfter:Boolean;
		
		public function CreateModelEvent(context:Object = null, finishToolJobAfter:Boolean = false)	{
			super(SHOW_CREATE_OPTIONS);
			this.context = context;
			this.finishToolJobAfter = finishToolJobAfter;
		}
		
	}
}