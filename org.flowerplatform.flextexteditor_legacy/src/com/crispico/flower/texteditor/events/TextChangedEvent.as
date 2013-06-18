package com.crispico.flower.texteditor.events {
	
	import flash.events.Event;

	/**
	 * An event dispatched when text is edited.
	 * @flowerModelElementId _x-yn8CyKEeGsGrJcrtxw9Q
	 */ 
	public class TextChangedEvent extends Event {
		
		/**
		 * The position in the text where the editing was done.
		 */ 
		public var offset:int;
		
		/**
		 * The inserted text.
		 */ 
		public var newText:String;
	
		/**
		 * The deleted text.
		 */ 
		public var oldTextLength:int;
		
		public static const TEXT_CHANGED:String = "SYNTAX_TEXT_AREA_TEXT_CHANGED";
		
		public function TextChangedEvent(offset:int, oldTextLength:int, newText:String):void {
			super(TEXT_CHANGED);
			
			this.offset = offset;
			this.newText = newText;
			this.oldTextLength = oldTextLength;
		}
	}
}