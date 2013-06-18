package com.crispico.flower.texteditor.model {
	
	import com.crispico.flower.texteditor.partitioning.scanners.PartitionScanner;
	
	/**
	 * A range in the document with a specific content type. It is detected by a specific partition scanner, and it may be
	 * delimited by start and end delimiters.
	 * 
	 * @flowerModelElementId _0rKj0O2uEeCF5Ozw-0NJ0A
	 */
	public class Partition extends Range {
		
		/**
		 * The content type of this partition.
		 */ 
		public var contentType:String;
		
		/** 
		 * The length of the start delimiter.
		 * 
		 * Used by the partition scanner to check if the start delimiter of this partition was damaged by the 
		 * changes in the document.
		 * 
		 * @see PartitionScanner#computeRange
		 */ 
		public var startDelimiterLength:int;
		
		/**
		 * The length of the end delimiter.
		 * 
		 * Used by the partition scanner to check if the end delimiter of this partition was damaged by the 
		 * changes in the document.
		 * 
		 * @see PartitionScanner#computeRange
		 */ 
		public var endDelimiterLength:int;

		/**
		 * A flag that is true if the partition was changed after the last time it was colored.
		 * The PresentationReconciler checks this flag to see if a partition needs to be recolored.
		 */ 
		public var dirty:Boolean;
		
		/**
		 * The range that was changed. It is used by the PresentationReconciler to compute the range 
		 * to be colored as the lines that contain the dirty region.
		 * 
		 * @see PresentationReconciler#minimumRange
		 */ 
		public var dirtyRange:Range;
		
		/**
		 * @flowerModelElementId _x--OIyyKEeGsGrJcrtxw9Q
		 */
		public function Partition(offset:int, length:int, contentType:String, scanner:PartitionScanner=null)
		{
			super(offset, length);
			this.contentType = contentType;
			this.dirty = true;
			this.dirtyRange = new Range(offset, length);
		}
		
		/**
		 * @flowerModelElementId _x_AqZSyKEeGsGrJcrtxw9Q
		 */
		public function equals(partition:Partition):Boolean {
			return contentType == partition.contentType &&
					offset == partition.offset &&
					length == partition.length;
		} 
	}
}