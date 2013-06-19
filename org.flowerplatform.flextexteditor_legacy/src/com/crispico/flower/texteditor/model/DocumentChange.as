package com.crispico.flower.texteditor.model {
	
	import com.crispico.flower.texteditor.model.Range;
	
	/**
	 * A change in the document starting at a specific offset. The length of the deleted text is length,
	 * and the length of the inserted text is textLength.
	 * 
	 * @flowerModelElementId _i6d90MHGEeC11PaV83glSQ
	 */
	public class DocumentChange extends Range {
		
		/**
		 * The length of the inserted text.
		 */ 
		public var textLength:int;
		
		public function DocumentChange(offset:int, length:int, textLength:int)	{
			super(offset, length);
			this.textLength = textLength;
		}
	}
}