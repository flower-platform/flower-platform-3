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
package com.crispico.flower.texteditor.partitioning.scanners {
	
	import com.crispico.flower.texteditor.Document;
	import com.crispico.flower.texteditor.rules.IRule;
	import com.crispico.flower.texteditor.model.DocumentChange;
	import com.crispico.flower.texteditor.model.Partition;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * A scanner that uses rules to the detect specific partitions.
	 * 
	 * 
	 */
	public interface IPartitionScanner {
		/**
		 * Connects this scanner to the given document.
		 */ 
		function connect(document:Document):void;
		
		/** 
		 * Gets the current scanned position in the document.
		 */ 
		function getOffset():int;
		
		/**
		 * Gets the end of the scanned region.
		 */ 
		function getEnd():int;
		
		/**
		 * Gets the document being scanned.
		 */ 
		function getDocument():Document;
		
		/**
		 * Sets the range to be scanned in the document.
		 */ 
 		function setRange(document:Document, offset:int, end:int):void; 
		
		/**
		 * Add a rule to be used when scanning.
		 */ 
		function addRule(rule:IRule):void;
		
		/** 
		 * Computes the range to be scanned depending on the change in the document.
		 */ 
		function computeRange(documentChange:DocumentChange, currentPartition:Partition, hasDeletedPartitions:Boolean):void;
		
		/**
		 * Uses the rules to compute and return the next scanned partition.
		 * 
		 * 
		 */
		function nextPartition():Partition; 
		
		/**
		 * Get the content types specific to the partitions this scanner can detect.
		 */
		function getRecognizedContentTypes():ArrayCollection;
		
		/**
		 * Returns the default content type this partition scanner recognizes.
		 */ 
		function getDefaultContentType():String;
		
		/**
		 * Gets the parent scanner, if any.
		 */ 
		function getParentScanner():IPartitionScanner;
		
		/**
		 * Sets the parent scanner.
		 */ 
		function setParentScanner(value:IPartitionScanner):void;
	}
}