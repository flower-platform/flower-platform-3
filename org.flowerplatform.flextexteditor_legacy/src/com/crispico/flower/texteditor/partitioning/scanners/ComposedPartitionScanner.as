package com.crispico.flower.texteditor.partitioning.scanners {
	
	import com.crispico.flower.texteditor.Document;
	import com.crispico.flower.texteditor.rules.IRule;
	import com.crispico.flower.texteditor.model.DocumentChange;
	import com.crispico.flower.texteditor.model.Partition;
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * A partition scanner used to scan documents that support multiple types of partitioning (eg. MXML).
	 * 
	 * It has a list of internal partition scanners, mapped to specific content types. Whenever a partition that 
	 * has a specific content type is detected, it switches its current scanner to the one mapped to the content type 
	 * (eg. the AS3 partition scanner is used after the CDATA tag is detected).
	 * 
	 * In order to use a ComposedPartitionScanner for a specific programming language, the user must add
	 * each internal partition scanner mapped to the content type that determines its use, and after the scanners 
	 * are added, one must be set as the currentScanner that will be used the first time this ComposedPartitioScanner
	 * is used.
	 * 
	 * The ComposedPartitionScanner is then set on the SyntaxTextEditor when the editor is initialized.
	 * 
	 * @see SyntaxTextEditor#init
	 * 
	 * @flowerModelElementId _MtnecN3QEeCGOND4c9bKyA
	 */
	public class ComposedPartitionScanner implements IPartitionScanner {
		
		/**
		 * The scanner that is currently used to scan this document.
		 */ 
		protected var currentScanner:IPartitionScanner;
		
		/**
		 * A map from specific content types to the internal partition scanners.
		 * 
		 * After each partition is detected, the current scanner is switched to the 
		 * scanner mapped to the partition's content type (eg. the MXML default 
		 * partition scanner is switched to the CSS partition scanner after a <mx:Style>
		 * tag is found).
		 * 
		 * @flowerModelElementId _MttlEN3QEeCGOND4c9bKyA
		 */
		private var scanners:Dictionary;
		
		/**
		 * A map from specific content types to the internal partition scanners.
		 * 
		 * Used to find the partition scanner that detected a partition. Each scanner
		 * is added to the map after it finds a partition.
		 * 
		 * @see PartitionScanner#nextPartition
		 */ 
		private var internalScanners:Dictionary;
		
		/**
		 * The parent scanner of this scanner, if it is part of a composed partition scanner.
		 */ 
		private var parentScanner:IPartitionScanner;
		
		/**
		 * The list of content types this scanner recognizes.
		 */ 
		private var recognizedContentTypes:ArrayCollection;
		
		/**
		 * Initalizes the internal scanners map and content types list. Sets the parent scanner if any.
		 */ 
		public function ComposedPartitionScanner(parentScanner:IPartitionScanner = null) {
			scanners = new Dictionary();
			internalScanners = new Dictionary();
			recognizedContentTypes = new ArrayCollection();
			this.parentScanner = parentScanner;
		}
		
		/**
		 * Get the content types this scanner recognizes.
		 */ 
		public function getRecognizedContentTypes():ArrayCollection {
			return recognizedContentTypes;
		}
		
		public function getDefaultContentType():String {
			return currentScanner.getDefaultContentType();
		}
		
		/**
		 * Connects the PartitionUpdater and the internal scanners to the document.
		 */ 
		public function connect(document:Document):void {
			for each (var scanner:IPartitionScanner in scanners)
				scanner.connect(document);
		}
		
		public function getParentScanner():IPartitionScanner {
			return parentScanner;
		}
		
		public function setParentScanner(value:IPartitionScanner):void {
			parentScanner = value;
		}
		
		/**
		 * Gets the scanner that was used to determine the damaged partition, and uses it (or its parent scanner)
		 * to compute the range to be scanned.
		 */ 
		public function computeRange(documentChange:DocumentChange, currentPartition:Partition, hasDeletedPartitions:Boolean):void {
			if (currentPartition != null) {
				currentScanner = internalScanners[currentPartition.contentType];
				// we can't switch from an internal scanner to another internal scanner that has a different parent
				// so we switch to the parent scanner instead
				while (currentScanner.getParentScanner() != this && currentScanner.getParentScanner() != null)
					currentScanner = currentScanner.getParentScanner();
			}
			currentScanner.computeRange(documentChange, currentPartition, hasDeletedPartitions);
		}
		
		/**
		 * Finds the scanner to be used to scan this document depending on the partition where the change was done
		 * and uses it to detect the next partition.
		 */ 
		public function nextPartition():Partition {
			var partition:Partition = currentScanner.nextPartition();
			if (partition == null)
				return null;
			
			// if there is a scanner mapped to the new partition's content type, switch to it
			var scanner:IPartitionScanner = scanners[partition.contentType];
			if (scanner != null) {
				scanner.setRange(currentScanner.getDocument(), currentScanner.getOffset(), currentScanner.getEnd()-currentScanner.getOffset());
				currentScanner = scanner;
			}
				
			return partition;
		}
		
		/**
		 * Adds the scanner to the internal scanners, mapped by the content type that determines the use of this scaner.
		 */ 
		public function addScanner(scanner:IPartitionScanner, contentType:String):void {
			scanners[contentType] = scanner;
			recognizedContentTypes.addAll(scanner.getRecognizedContentTypes());
			
			addToInternalScanners(scanner);
		}
		
		/**
		 * Adds a rule to the current scanner. Then adds the current scanner 
		 * to its recognized content types in the internal scanners dictionary.
		 */ 
		public function addRule(rule:IRule):void {
			currentScanner.addRule(rule);
			recognizedContentTypes.addAll(rule.getTypes());
			
			addToInternalScanners(currentScanner);
		}
		
		/**
		 * Maps the given scanner to each of its recognized content types in the internal scanners dictionary.
		 */ 
		private function addToInternalScanners(scanner:IPartitionScanner):void {
			for each (var recognizedContentType:String in scanner.getRecognizedContentTypes()) {
				internalScanners[recognizedContentType] = scanner;
			}	
		}
		
		public function getDocument():Document {
			return currentScanner.getDocument();
		}
		
		public function getOffset():int {
			return currentScanner.getOffset();
		}
		
		public function getEnd():int {
			return currentScanner.getEnd();
		}
		
		public function setRange(document:Document, startOffset:int, length:int):void {
			currentScanner.setRange(document, startOffset, length);
		} 
	}
}