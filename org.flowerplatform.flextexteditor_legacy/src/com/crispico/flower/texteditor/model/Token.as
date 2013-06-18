package com.crispico.flower.texteditor.model {
	
	/**
	 * A range of text that has a specific type.
	 * 
	 * @flowerModelElementId _0rUU0O2uEeCF5Ozw-0NJ0A
	 */
	public class Token extends Range {
		
		/**
		 * The type of this token.
		 */ 
		public var type:String;
		
		private static const TYPE_EOF:String = "EOF";
		private static const TYPE_UNDEFINED:String = "UNDEFINED";
		
		/**
		 * Returned when the end of file (end of range to scan) is reached.
		 */ 
		public static const EOF:Token = new Token(TYPE_EOF);
		
		/**
		 * Returned when the rule does not match.
		 */ 
		public static const UNDEFINED:Token = new Token(TYPE_UNDEFINED);
		
		public function Token(value:String):void {
			super();
			this.type = value;
		}
		
		/**
		 * Returns true if this is an undefined token, false otherwise.
		 */ 
		public function isUndefined():Boolean {
			return type == TYPE_UNDEFINED;
		}
		
		/**
		 * Returns true if this is an EOF token, false otherwise.
		 */ 
		public function isEof():Boolean {
			return type == TYPE_EOF;
		}
	}
}