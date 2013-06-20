package org.flowerplatform.flexdiagram.tool {
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IWakeUpableTool {	
		
		function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean;		
	
	}
}