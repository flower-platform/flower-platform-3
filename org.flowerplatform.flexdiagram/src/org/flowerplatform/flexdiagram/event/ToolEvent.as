package org.flowerplatform.flexdiagram.event {
	import flash.events.Event;
	
	import org.flowerplatform.flexdiagram.tool.Tool;
	
	/**
	 * Dispatched by the viewer to notify about events in the lifecycle of tools
	 * that work in exclusive mode (i.e. not the ones that are permanent tools).
	 * 
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class ToolEvent extends Event {
		
		public static const EXCLUSIVE_TOOL_ACTIVATED:String = "ExclusiveToolActivated";
		
		public static const EXCLUSIVE_TOOL_DEACTIVATED:String = "ExclusiveToolDeactivated";
		
		public static const EXCLUSIVE_TOOL_JOB_FINISHED:String = "ExclusiveToolJobFinished";
		
		public static const EXCLUSIVE_TOOL_LOCKED:String = "ExclusiveToolLocked";
		
		public var tool:Tool;
		
		public function ToolEvent(type:String, tool:Tool)	{
			super(type);
			this.tool = tool;
		}
	}
}