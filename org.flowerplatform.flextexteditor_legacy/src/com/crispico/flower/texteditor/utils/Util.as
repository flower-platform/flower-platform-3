package com.crispico.flower.texteditor.utils {
	
	import flash.text.engine.FontPosture;
	import flash.text.engine.FontWeight;
	
	import flashx.textLayout.formats.TextLayoutFormat;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Provides utility functions to work with characters and text formats.
	 */ 
	public class Util {
		
		/**
		 * Character constants.
		 */ 
		public static const EOF:int = -1;
		public static const EOLN:int = 10;
		public static const TAB:int = 9;
		public static const SPACE:int = 32;
		public static const BACKSPACE:int = 8;
		public static const DELETE:int = 127;
		
		/**
		 * Returns a format with the specified text color, style and weight. Used in IPartitionTokenFormatProviders.
		 * 
		 * @see IPartitionTokenFormatProvider
		 */ 
		public static function getFormat(color:int, italic:Boolean, bold:Boolean):TextLayoutFormat {
			var format:TextLayoutFormat = new TextLayoutFormat();
			format.color = color;
			format.fontStyle = italic ? FontPosture.ITALIC : FontPosture.NORMAL;
			format.fontWeight = bold ? FontWeight.BOLD : FontWeight.NORMAL;
			return format;
		}
		
		/**
		 * Returns a list of the character codes in the string, used to set the start and end delimiters for rules.
		 * 
		 * @see MXMLTagScanner
		 */ 
		public static function getCharCodes(string:String):ArrayCollection {
			var result:ArrayCollection = new ArrayCollection();
			for (var i:int=0; i<string.length; i++)
				result.addItem(string.charCodeAt(i));
			return result;
		}
		
		/**
		 * Checks if the character is a letter, digit or underscore.
		 * 
		 * @see WordRule#isDelimiter
		 * @see MultipleContentTypeWordRule#evaluate
		 */ 
		public static function isWordPart(char:int):Boolean {
			// A-Z
			if (char>=65 && char<=90)
				return true;
			// a-z
			if (char>=97 && char<=122)
				return true;
			// 0-9
			if (char>=48 && char<=57)
				return true;
			// _
			if (char==95)
				return true;
			return false;
		}
		
		/**
		 * Checks if the character is whitespace.
		 * 
		 * @see MultipleContentTypePatternRule#getKey
		 */ 
		public static function isWhitespace(char:int):Boolean {
			return char == EOF || char == EOLN || char == TAB || char == SPACE;
		}
	}
}