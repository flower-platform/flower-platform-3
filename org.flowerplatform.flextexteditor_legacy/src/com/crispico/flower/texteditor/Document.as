package com.crispico.flower.texteditor {
	
	/**
	 * Contains the text.
	 */ 
	public class Document {
		
		/**
		 * The text in this document.
		 */ 
		public var text:String;
		
		/**
		 * The index of the last character that was loaded in the editor from this document.
		 */ 
		public var endLimit:int = 0;
		
		/**
		 * Adds the text to this document and initializes the PartitionUpdater.
		 */ 
		public function Document(text:String=null) {
			this.text = text == null ? new String() : new String(text);
		}
		
		public function get length():int {
			return text.length;
		}
		
		/**
		 * Returns the character at the specified offset.
		 */ 
		public function getChar(offset:int):int {
			return text.charCodeAt(offset);
		}
	}
}