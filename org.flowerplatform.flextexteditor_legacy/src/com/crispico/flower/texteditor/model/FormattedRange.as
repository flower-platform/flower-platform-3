package com.crispico.flower.texteditor.model {
	
	import flashx.textLayout.formats.TextLayoutFormat;
	
	/**
	 * A range that has a format applied.
	 * 
	 * @flowerModelElementId _0rR4kO2uEeCF5Ozw-0NJ0A
	 */
	public class FormattedRange extends Range {
		
		/** 
		 * The format (text color, text weight etc.) to be applied to this range.
		 */ 
		public var format:TextLayoutFormat;
		
		public function FormattedRange(offset:int, length:int, format:TextLayoutFormat) {
			super(offset, length);
			this.format = format;
		}
	}
}