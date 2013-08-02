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
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	import com.crispico.flower.texteditor.model.DocumentChange;
	import com.crispico.flower.texteditor.model.Partition;
	import com.crispico.flower.texteditor.model.Token;
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Base implementation of an IPartitionScanner. After it is connected to a document, it uses rules to detect
	 * partitions in a certain range in the text; the range is detected based on the change the user made in the document.
	 * 
	 * In order to use a PartitionScanner for a specific programming language, the user must initialize it with a 
	 * default content type, and then for each content type the language recognizes, the user must add a rule that
	 * matches that content type. A PartitionScanner may have a ComposedPartitonScanner as its parent scanner, 
	 * if the language supports multiple types of partitioning (eg. a CSS PartitionScanner will have the MXMLPartitionScanner
	 * as its parent scanner if the language is MXML). 
	 * 
	 * Important to note: 
	 * 	- a content type is only matched by one rule, otherwise an error will be thrown.
	 *  - the order is very important when adding the rules. It is very important to add a rule where the 
	 * 	  start delimiter is a substring of the start delimiter of another rule AFTER that rule was added.
	 * 	  Eg. for Java, the rule that matches a multiline comment (start delimiter is /*) must be added
	 *    after the rule that matches documentation (start delimiter is /**)
	 * 
	 * The PartitionScanner is then set on the SyntaxTextEditor when the editor is initialized.
	 * 
	 * @see SyntaxTextEditor#init
	 * 
	 * 
	 */
	public class PartitionScanner extends RuleBasedScanner implements IPartitionScanner {
				
		/** 
		 * The content types this scanner recognizes.
		 */ 
		private var recognizedContentTypes:ArrayCollection;
		
		/**
		 * Maps a content type to a rule. A content type can only be recognized by one rule.
		 */ 
		private var contentTypeToRule:Dictionary;
		
		/** 
		 * The parent scanner, if this scanner is part of a composed partition scanner.
		 */ 
		private var parentScanner:IPartitionScanner;
		
		/**
		 * A specific rule used to scan inside a specific partition; it skips the start delimiter.
		 */ 
		private var specificRule:IRule;
		
		/**
		 * The length of the longest delimiter used in this scanner's rules.
		 */ 
		private var maxDelimiterLength:int;
		
		/**
		 * The current partition where the scanning starts.
		 */ 
		private var currentPartition:Partition = null;
		
		/**
		 * The content tye of the current partition.
		 */ 
		private var currentContentType:String = null;
		
		/**
		 * The offset of the last detected partition.
		 */ 
		private var partitionOffset:int = -1;
		
		/**
		 * The position where the scanning starts.
		 */ 
		private var startPosition:int = -1;
		
		/** 
		 * The position where the scanning ends.
		 */ 
		private var endPosition:int = -1;
		
		/**
		 * A flag that is true if scanning has just started on the new range, and false otherwise.
		 * 
		 * Used when detecting the next partition.
		 * 
		 * If true, first check if the existing partition was not damaged (no delimiters were inserted)
		 * and if so, return the existing partition. Also, if the new partition has the same content 
		 * type as the existing partition, set the new partition's offset and length and continue scanning.
		 * 
		 * @see PartitionScanner#nextPartition
		 */ 
		private var firstTimeAfterComputeRange:Boolean = false;
		
		/**
		 * Sets the default content type and parent scanner(if any) and initializes the content types list.
		 */ 
		public function PartitionScanner(defaultContentType:String, parentScanner:IPartitionScanner = null) {
			super(defaultContentType);
			recognizedContentTypes = new ArrayCollection([defaultContentType]);
			contentTypeToRule =  new Dictionary();
			this.parentScanner = parentScanner;
		}
		
		/**
		 * Sets the document that will be scanned with this scanner and gets its partition updater.
		 */ 
		public function connect(document:Document):void {
			this.document = document;
		}
		
		public function getParentScanner():IPartitionScanner {
			return parentScanner;
		}
		
		public function setParentScanner(value:IPartitionScanner):void {
			parentScanner = value;
		}
		
		/**
		 * Computes the range to be scanned and the rules to be used depending on the content type of the damaged partitions.
		 * 
		 * Because it is not enough to scan only the text that was inserted (eg. the inserted text is the last character of a start delimiter),
		 * scanning should start with a specific number of characters before and end with a specific number of characters after the inserted
		 * text. This specific number of characters is the length of the start delimiter, and end delimiter respectively, of the current partition,
		 * or the length of the longest delimiter, if this partition does not have delimiters. This way it is ensured that the delimiters are 
		 * detected correctly.
		 * 
		 * In addition, if the range to be scanned overlaps with the start delimiter of a partition, then scanning should start at
		 * the beginning of the partition (eg. adding "* " after a /* in a comment partition should produce a javadoc partition; however,
		 * scanning will start after the slash in the delimiter (two characters before the inserted text), and it will not detect the new 
		 * partition; in this case scanning should start at the beginning of the partition). The same behavious is implemented for the
		 * end delimiter as well.
		 * 
		 * When scanning from the start of a partition, or inside a default partition, the scanner will use all its rules, otherwise
		 * it will only use the rule specific to the content type of the current partition, setting several flags on it as well (eg. the
		 * rule should consider that the start delimiter for this partition was already found).
		 */
		public function computeRange(documentChange:DocumentChange, currentPartition:Partition, hasDeletedPartitions:Boolean):void {
			// if there are no partitions, then this is the first partitioning => fully scan the document 
			this.currentPartition = currentPartition;
			if (currentPartition == null) {
				setRange(document, 0, document.endLimit);
			} else {
				// find the minimum range to scan, by going before and after the damaged region with a specific offset
				
				// for the offset to go before the damaged region, get this partition's start delimiter length (or the length of the longest delimiter
				// if this partition does not have a specific start delimiter) 
				var startDelimiterLength:int = currentPartition.startDelimiterLength;
				if (startDelimiterLength == 0) {
					startDelimiterLength = maxDelimiterLength;
				}
				
				// for the offset to go after the damaged region, get this partition's end delimiter length (or the length of the longest delimiter
				// if this partition does not have a specific end delimiter) 
				var endDelimiterLength:int = currentPartition.endDelimiterLength;
				if (endDelimiterLength == 0) {
					endDelimiterLength = maxDelimiterLength;
				}
				
				// the position where we start scanning will be changed to the partition's start position, because there is no need to scan inside the previous partition
				startPosition = Math.max(currentPartition.offset, documentChange.offset - startDelimiterLength);
				
				// the position where we stop scanning will be changed to the partition's end position, because there is no need to scan inside the next partition
				endPosition = Math.min(currentPartition.offset + currentPartition.length, documentChange.offset + documentChange.textLength + endDelimiterLength);
				
				// if the start position is inside the partition's start delimiter, change the start position to the start of the partition
				if (startPosition < currentPartition.offset + currentPartition.startDelimiterLength) {
					startPosition = currentPartition.offset;
				}
					
				// if the end position is inside the partition's end delimiter, change the end position to the end of the partition
				if (endPosition > currentPartition.offset + currentPartition.length - currentPartition.endDelimiterLength) {
					endPosition = document.endLimit;
				}
				
				// if there were any deleted partitions during the last update, scan until the end of the document
				if (hasDeletedPartitions) {
					endPosition = document.endLimit;
				}
				
				currentContentType = currentPartition.contentType;
				partitionOffset = currentPartition.offset;
				
				// if scanning from the start of the partition, scan in "default" mode (with all the rules, until the end of the document)
				if (startPosition == currentPartition.offset) {
					currentContentType = defaultToken.type;
					endPosition = document.endLimit;
				}
				
				firstTimeAfterComputeRange = true;
				setRange(document, startPosition, endPosition - startPosition);
				
				// if scanning in a non-default content type, use the rule specific to the content type and skip the start delimiter
				if (currentContentType != defaultToken.type) {
					specificRule = getSpecificRule(currentContentType);
					specificRule.setOptimizedMode();
				}
			}
		}
		
		/**
		 * Returns the next partition based on the detected token. 
		 */ 
		public function nextPartition():Partition {
			var token:Token = nextToken();
			if (token.isEof()) {
				return null;
			}
			var partition:Partition = new Partition(token.offset, token.length, token.type);
			if (firstTimeAfterComputeRange) {
				// if the old partition was not damaged, return the old partition
				if (token.type == defaultToken.type && token.offset == startPosition 
					&& token.offset + token.length == endPosition  && token.offset + token.length != document.endLimit ) { 
						partition = currentPartition;
				} else {
					// if the new partition has the same content type as the old partition, change the offset and length
					// and continue scanning until the end of the document
					if (partition.contentType == currentContentType) {
						partition.length = (partition.offset + partition.length) - partitionOffset;
						partition.offset = partitionOffset;
						setEnd(document.endLimit);
						if (partition.contentType == defaultToken.type) {
							setRange(document, partition.offset + partition.length, document.endLimit - partition.offset - partition.length);
						}
					}
				}
				unsetFirstTimeAfterComputeRange();
			}
			
			// update the partition's delimiters; if it is a default partition, it does not have delimiters
			if (partition.contentType != defaultToken.type) {
				computeDelimiterLengths(partition);
			}
			// update the length of the longest delimiter
			var max:int = Math.max(partition.startDelimiterLength, partition.endDelimiterLength);
			if (maxDelimiterLength < max) {
				maxDelimiterLength = max;
			}
				
			return partition;
		}
		
		public function getRecognizedContentTypes():ArrayCollection {
			return recognizedContentTypes;
		}
		
		public function getDefaultContentType():String {
			return defaultToken.type;
		}
		
		/**
		 * Adds a new rule to the list of rules. Checks if the new rule matches an already existent content type this
		 * scanner recognizes.
		 */ 
		override public function addRule(rule:IRule):void {
			for each (var ruleContentType:String in rule.getTypes()) {
				if (contentTypeToRule[ruleContentType] != null) {
					throw new Error("Duplicate content type: " + ruleContentType + ".");
				}
				contentTypeToRule[ruleContentType] = rule;
			}
			super.addRule(rule);
			recognizedContentTypes.addAll(rule.getTypes());
			
			// update the length of the longest delimiter
			var max:int = Math.max(rule.getStartDelimiterLength(), rule.getEndDelimiterLength());
			if (max > maxDelimiterLength)
				maxDelimiterLength = max;
		}
		
		/**
		 * Sets the length of this partition's start and end delimiters. If the rule used to detect this partition
		 * does not have start or end delimiters, set their lengths to the length of the partition.
		 */ 
		private function computeDelimiterLengths(partition:Partition):void {
			var rule:IRule = getSpecificRule(partition.contentType);
			var startDelimiterLength:int = rule == null ? partition.length : rule.getStartDelimiterLength();
			var endDelimiterLength:int = rule == null ? partition.length : rule.getEndDelimiterLength();
			
			partition.startDelimiterLength = startDelimiterLength > -1 ? startDelimiterLength : partition.length;
			partition.endDelimiterLength = endDelimiterLength > -1 ? endDelimiterLength : partition.length;
		}
		
		/**
		 * Resets various flags.
		 */ 
		private function unsetFirstTimeAfterComputeRange():void {
			firstTimeAfterComputeRange = false;
			currentPartition = null;
			currentContentType = null;
			partitionOffset = -1;
			startPosition = -1;
			endPosition = -1;
			resetSpecificRule();	
		}
		
		/** 
		 * Get the rules that matches this content type.
		 */ 
		private function getSpecificRule(contentType:String):IRule {
			return contentTypeToRule[contentType];		
		}
		
		/**
		 * Reset the flags on the specific rule used to scan.
		 */ 
		private function resetSpecificRule():void {
			if (specificRule == null) 
				return;
			specificRule.resetOptimizedMode();
			specificRule = null;
		}
		
		/**
		 * Returns a set of rules to be used by this scanner to detect the next partition. It will be either a list with all the rules
		 * this partition scanner has, or a singleton with the rule used to detect the current partition, when the scanning is done inside
		 * this partition if the start delimiter was not damaged.
		 */ 
		override protected function getProposedRules():ArrayCollection {
			if (specificRule != null) {
				var result:ArrayCollection = new ArrayCollection();
				result.addItem(specificRule);
				return result;
			}
			return super.getProposedRules();
		}
	}
}