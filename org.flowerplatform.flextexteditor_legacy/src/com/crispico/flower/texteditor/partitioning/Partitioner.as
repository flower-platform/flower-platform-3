package com.crispico.flower.texteditor.partitioning {
	
	import com.crispico.flower.texteditor.Document;
	import com.crispico.flower.texteditor.model.DocumentChange;
	import com.crispico.flower.texteditor.model.Partition;
	import com.crispico.flower.texteditor.model.Range;
	import com.crispico.flower.texteditor.partitioning.scanners.IPartitionScanner;
	
	/**
	 * Uses an IPartitionScanner to scan the Document after a change occurs in the Document (insert, delete, replace).
	 * 
	 * The list of partitions in this document are updated by the PartitionUpdater, and the partition scanner is used
	 * to detect new partitions. It compares the  new partitions to the existing ones, deleting those that overlap 
	 * with the new partitions, and stops when a new partition matches an existing partition (i.e. it has the same 
	 * offset, length and content type). New partitions are marked for coloring.
	 * 
	 * @flowerModelElementId _iXV_8MpIEeCTIbyT6X5eRg
	 */
	public class Partitioner {
		
		/**
		 * The scanner used to create partitions.
		 * 
		 * @flowerModelElementId _0q-Wku2uEeCF5Ozw-0NJ0A
		 */
		private var _partitionScanner:IPartitionScanner;
		
		/**
		 * Document to be partitioned.
		 */ 
		private var _document:Document;
		
		/**
		 * The PartitionUpdater used to manage the partitions.
		 * 
		 * @flowerModelElementId _0q-WlO2uEeCF5Ozw-0NJ0A
		 */
		private var _partitionUpdater:PartitionUpdater;
		
		
		public function Partitioner():void {
			_partitionUpdater = new PartitionUpdater();
		}
		
		/**
		 * Sets the IPartitionScanner this Partitioner will use.
		 */ 
		public function set partitionScanner(partitionScanner:IPartitionScanner):void {
			_partitionScanner = partitionScanner;
			if (_document != null)
				partitionScanner.connect(_document);
		}
		
		/** 
		 * Sets the Document to partition.
		 */ 
		public function set document(document:Document):void {
			_document = document;
			_partitionUpdater = new PartitionUpdater();
			if (_partitionScanner != null)
				_partitionScanner.connect(document);
		}
		
		public function get partitionUpdater():PartitionUpdater {
			return _partitionUpdater;
		}

		/**
		 * Computes the new partitioning after a change in the document. Uses the partition scanner to detect new partitions. It compares the 
		 * new partitions to the existing ones, deleting those that overlap with the new partitions, and stops when a new partition matches 
		 * an existing partition (i.e. it has the same offset, length and content type). New partitions are marked for coloring.
		 * 
		 * @flowerModelElementId _0q-9o-2uEeCF5Ozw-0NJ0A
		 */
		 public function computePartitioning(documentChange:DocumentChange):void {
		 	if (_partitionScanner != null) {
//				var startedPartitioning:Date = new Date();
				
				// get the partition at the damaged position in the text before updating the partitions list
				var currentPartition:Partition = _partitionUpdater.getPartitionAtPosition(documentChange.offset);
				
				// update the partitions list
				_partitionUpdater.update(documentChange);
				
				_partitionScanner.computeRange(documentChange, _partitionUpdater.getPartitionAtPosition(documentChange.offset), _partitionUpdater.hasDeletedPartitions);
				var newPartition:Partition = _partitionScanner.nextPartition();
				
				// check if the new partition must be merged with the existing previous partition (they are both default)
				var previousPartition:Partition = _partitionUpdater.previousPartition(currentPartition);
				if (newPartition != null && previousPartition != null && newPartition.contentType == previousPartition.contentType && newPartition.contentType == _partitionScanner.getDefaultContentType()) {
					newPartition.offset = previousPartition.offset;
					newPartition.length += previousPartition.length;
					currentPartition = previousPartition;		
				}
				
				var existingContentType:String = currentPartition != null ? currentPartition.contentType : null;
				var lastScannedPosition:int;
				// while there is a new partition, compare it to existing partitions
				// delete any existing partitions that overlap with the new partition, unless the new partition already exists 
				// (i.e. same offset, length, content type)
				while (newPartition != null) {
					if (newPartition.length > 0) {
						lastScannedPosition = newPartition.offset + newPartition.length;
						
						computeDirtyRange(newPartition, existingContentType, documentChange);
						existingContentType = null;
						
						while (currentPartition != null && !currentPartition.equals(newPartition) && lastScannedPosition > currentPartition.offset) {
							// delete the existing partitions that overlap with the new partition
							// until the new partition is found
							var toDelete:Partition = currentPartition;
							currentPartition = _partitionUpdater.nextPartition(currentPartition);
							_partitionUpdater.deletePartition(toDelete);
						}
							
						if (currentPartition != null && currentPartition.equals(newPartition) && lastScannedPosition > documentChange.offset) {
							// the new partition was found and the modified range was scanned => stop partitioning
							currentPartition.dirty = newPartition.dirty;
							currentPartition.dirtyRange = newPartition.dirtyRange;
							break;
						} else {
							if (currentPartition != null && currentPartition.equals(newPartition)) {
								currentPartition.dirty = newPartition.dirty;
								currentPartition.dirtyRange = newPartition.dirtyRange;
								currentPartition = _partitionUpdater.nextPartition(currentPartition);
							} else { 	
								_partitionUpdater.insertBeforePartition(currentPartition, newPartition);
							}
						}
					}
					
					newPartition = _partitionScanner.nextPartition();
				}
			
//				trace ("Partitioning", new Date().valueOf() - startedPartitioning.valueOf());
		 	}
		}

		/**
		 * Checks if the new partition has the same content type as before; if it does, then we will only need to color the
		 * inserted text. Otherwise mark the entire partition as dirty.
		 * 
		 * @flowerModelElementId _x-r6QSyKEeGsGrJcrtxw9Q
		 */ 
		 private function computeDirtyRange(newPartition:Partition, existingContentType:String, documentChange:DocumentChange):void {
		  	// check if the new partition has a different content type 
		 	// a special case is if the new partition alreay has a dirty range; in this case mark the entire partition as dirty
		 	if (newPartition.contentType != existingContentType || newPartition.dirty || _partitionUpdater.hasDeletedPartitions) {
		 		newPartition.dirty = true;
		 		newPartition.dirtyRange = new Range(newPartition.offset, newPartition.length);
		 	} else {
		 		newPartition.dirty = true;
		 		var start:int = Math.max(newPartition.offset, documentChange.offset);
		 		var end:int = Math.min(newPartition.offset + newPartition.length, documentChange.offset + documentChange.textLength);
		 		newPartition.dirtyRange = new Range(start, end - start);
		 	} 
		 }
	}
}