package com.crispico.flower.texteditor.providers {
	
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	
	/**
	 * Provides the partition tokenizer specific to each content type.
	 * 
	 * A partition tokenizer is a RuleBasedScanner used to detect tokens
	 * that will be colored in a partition (eg. for Java, a RuleBasedScanner 
	 * that detects keywords).
	 * 
	 * Since the partition tokenizer may be requested often, it would be
	 * better to create the partition tokenizers when the provider is 
	 * initialized instead of creating a new partition tokenizer each time 
	 * it is requested.
	 * 
	 * @flowerModelElementId _0og4ce2uEeCF5Ozw-0NJ0A
	 */
	public interface IPartitionTokenizerProvider {
		
		/**
		 * Returns the partition tokenizer specific to the content type.
		 * 
		 * @flowerModelElementId _0p4xcO2uEeCF5Ozw-0NJ0A
		 */
		 function getPartitionTokenizer(partitionContentType:String):RuleBasedScanner;
	}
}