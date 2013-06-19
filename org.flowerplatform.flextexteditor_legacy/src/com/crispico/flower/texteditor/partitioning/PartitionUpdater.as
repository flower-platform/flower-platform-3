package com.crispico.flower.texteditor.partitioning {
	
	import com.crispico.flower.texteditor.model.DocumentChange;
	import com.crispico.flower.texteditor.model.Partition;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Manages a list of partitions. Has functions to get, insert and delete partitions, 
	 * and update the list after a change in the document.
	 * 
	 * It is necessary to call the update function after each change in the document, 
	 * to make sure that the partitions are always updated.
	 * 
	 * @flowerModelElementId _iXP5UMpIEeCTIbyT6X5eRg
	 */
	public class PartitionUpdater {
		
		/**
		 * List of partitions.
		 * @flowerModelElementId _x-iJQSyKEeGsGrJcrtxw9Q
		 */ 
		public var partitions:ArrayCollection;
		
		/**
		 * A flag that is true if any partitions were deleted during the last update.
		 * 
		 * The PartitionScanner uses this flag to compute the range to scan; if there were any partitions deleted
		 * then the scanner will scan until the end of the document.
		 * 
		 * @see PartitionScanner#computeRange
		 */ 
		public var hasDeletedPartitions:Boolean;
		
		public function PartitionUpdater() {
			partitions = new ArrayCollection();
		}
		
		/**
		 * Updates the list of partitions depending on the change that occured in the document (insert, delete, replace).
		 * Each specific type of change is handled diferently, by updating the damaged partition(s) and shifting
		 * all the partitions after the damaged region.
		 * 
		 * Used by the Partitioner to keep the partitions updated before starting the partitioning.
		 * 
		 * @see Partition#computePartitioning
		 * 
		 * @flowerModelElementId _iXSVlspIEeCTIbyT6X5eRg
		 */
		public function update(documentChange:DocumentChange):void {
			hasDeletedPartitions = false;
			var start:Date = new Date();
			// delete operation - no text was inserted
			if (documentChange.textLength == 0) 
				deleteEventUpdate(documentChange.offset, documentChange.offset + documentChange.length);
			// insert operation - no text was deleted
			if (documentChange.length == 0)
				insertEventUpdate(documentChange.offset, documentChange.textLength);
			// replace operation - a delete operation followed by an insert operation
			if (documentChange.textLength > 0 && documentChange.length > 0) {
				deleteEventUpdate(documentChange.offset, documentChange.offset + documentChange.length);
				insertEventUpdate(documentChange.offset, documentChange.textLength);
			}
		}
		
		/**
		 * Finds the partition where text was inserted and changes its length.
		 * Then shifts the partitions after the damaged range.
		 */ 
		private function insertEventUpdate(position:int, newLength:int):void {
			var partition:Partition = getPartitionAtPosition(position);
			if (partition != null) {
				partition.length += newLength;
				shiftPartitions(partitions.getItemIndex(partition), newLength);
			}
		}
		
		/**
		 * Deletes the partitions that overlap with the deleted range. Merges the first and last partition.
		 * Then shifts the partitions after the damaged range.
		 */ 
		private function deleteEventUpdate(start:int, end:int):void {
			var startPartition:Partition = getPartitionAtPosition(start);
			var endPartition:Partition = getPartitionAtPosition(end);
			
			if (startPartition != null && endPartition != null) {
			
				hasDeletedPartitions = (startPartition!=endPartition) ? true : false;
				
				// if the deleted range overlaps with more than one partition, merge the first and last partition
				if (startPartition != endPartition) {
					startPartition.length = start - startPartition.offset;
					startPartition.length += endPartition.offset + endPartition.length - end;
				} else {
					startPartition.length -= (end-start);
				}
					
				// remove all the partitions between the first and last partition
				var startIndex:int = partitions.getItemIndex(startPartition);
				if (startPartition.length>0) startIndex++;
				var endIndex:int = partitions.getItemIndex(endPartition);				
				endIndex++;
				for (var i:int = startIndex; i<endIndex; i++)
					partitions.removeItemAt(startIndex);
					
				// shift the partitions that follow the damaged region
				shiftPartitions(startIndex-1, start-end);
			}
		}
		
		/**
		 * Shifts the partitions after the specified position, with the specified offset.
		 */ 
		private function shiftPartitions(position:int, offsetDiff:int):void {
			position++;
			for (var i:int=position; i<partitions.length; i++) {
				var partition:Partition = Partition(partitions.getItemAt(i));
				partition.offset += offsetDiff;
				if (partition.dirtyRange != null)
					partition.dirtyRange.offset += offsetDiff;
			}
		}
		
		/**
		 * Gets the next partition after the specified partition. If the specified partition is the last one, returns null.
		 */ 
		public function nextPartition(partition:Partition):Partition {
			var index:int = partitions.getItemIndex(partition);
			index++;
			if (index < partitions.length)
				return partitions.getItemAt(index) as Partition;
			return null;
		}
		
		/**
		 * Gets the previous partition before the specified partition. If the specified partition is the first one, return null.
		 */ 
		public function previousPartition(partition:Partition):Partition {
			var index:int = partitions.getItemIndex(partition);
			index--;
			if (index < 0) 
				return null;
			return partitions.getItemAt(index) as Partition;
		}
		
		/**
		 * Deletes the specified partition.
		 * 
		 * Used by the Partitioner to delete a partition from the list if it doesn't exist anymore.
		 * 
		 * @see Partitioner#computePartitioning
		 */ 
		public function deletePartition(partition:Partition):void {
			var index:int = partitions.getItemIndex(partition);
			if (index >= 0)
				partitions.removeItemAt(index);
		}
		
		/**
		 * Inserts a new partition before the specified partition. If the specified partition is null, then adds the new
		 * partition at the end of the list.
		 * 
		 * Used by the Partitioner to insert a new partition at a specific region in the list.
		 * 
		 * It is a better approach to pass the second parameter to specify the position where to insert the new partition, 
		 * rather than searching for the partition in the list, since the partitioner already knows the current partition.
		 * 
		 * @see Partitioner#computePartitiong
		 */ 
		public function insertBeforePartition(partition:Partition, partitionToInsert:Partition):void {
			if (partition == null) {
				partitions.addItem(partitionToInsert);
			} else {
				var index:int = partitions.getItemIndex(partition);
				partitions.addItemAt(partitionToInsert, index);
			}
		}

		/**
		 * Gets the partition at the specified position in the text. Since the list of partitions is ordered as they appear in 
		 * the document, uses binary search to find the partition that contains the specified position.
		 */ 
		public function getPartitionAtPosition(position:int):Partition {
			var left:int = 0;
			var right:int = partitions.length;
			right--;
			while (left<=right) {
				var m:int = (left+right)/2;
				var partition:Partition = partitions.getItemAt(m) as Partition;
				if (partition.offset <= position) {
					if (partition.offset + partition.length > position) {
						return partition;
					} else {
						left = m+1;
					}
				} else {
					right = m;
				}
			}
			if (partitions.length > 0)
				return partitions.getItemAt(partitions.length-1) as Partition;
				
			return null;
		}
	}
}