package org.flowerplatform.flexdiagram.tool.toolbar {
	import org.flowerplatform.flexdiagram.tool.Tool;
	
	/**
	 * @author Cristina Constantinescu
	 */ 	
	[Bindable]
	public class ToolbarItem {
		
		public var label:String;
		public var icon:Object;
		public var toolTip:String;
		
		public var tool:Tool;
		
		public var selected:Boolean;
		
	}
}