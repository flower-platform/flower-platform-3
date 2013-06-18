package com.crispico.flower.texteditor.providers {
	
	import flashx.textLayout.formats.TextLayoutFormat;
	
	/**
	 * Provides the format that should be applied to a specific token in the text.
	 * 
	 * Note:
	 * 	- because formats will be requested often, it would be better
	 * 	  to create the formats when the provider is initialized instead
	 *    of creating a new format each time it is requested
	 *  - formats may be created using the getFormat method, that allows
	 *    specifying the format's color, style (italic) and weight (bold) 
	 * 
	 * @see Util#getFormat
	 * @flowerModelElementId _0oki1e2uEeCF5Ozw-0NJ0A
	 */
	public interface IPartitionTokenFormatProvider {
		
		/**
		 * Returns the format specific to the token type.
		 */
		function getFormat(tokenType:String):TextLayoutFormat;
	}
}