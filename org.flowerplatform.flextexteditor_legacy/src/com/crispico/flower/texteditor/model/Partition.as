/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package com.crispico.flower.texteditor.model {
	
	import com.crispico.flower.texteditor.partitioning.scanners.PartitionScanner;
	
	/**
	 * A range in the document with a specific content type. It is detected by a specific partition scanner, and it may be
	 * delimited by start and end delimiters.
	 * 
	 * 
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
		 * 
		 */
		public function Partition(offset:int, length:int, contentType:String, scanner:PartitionScanner=null)
		{
			super(offset, length);
			this.contentType = contentType;
			this.dirty = true;
			this.dirtyRange = new Range(offset, length);
		}
		
		/**
		 * 
		 */
		public function equals(partition:Partition):Boolean {
			return contentType == partition.contentType &&
					offset == partition.offset &&
					length == partition.length;
		} 
	}
}