package com.crispico.flower.texteditor.model {
	
	/** 
	 * A region in the document determined by its offset and length.
	 */ 
	public class Range {
		
		/**
		 * The position in the document where this range starts.
		 */ 
		public var offset:int;
		
		/**
		 * The length of this range.
		 */ 
		public var length:int;
		
		public function Range(offset:int=-1, length:int=-1) {
			this.offset = offset;
			this.length = length;
		}
	}
}