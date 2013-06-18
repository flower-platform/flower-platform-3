package com.crispico.flower.texteditor.providers {
	
	import mx.collections.ArrayCollection;
	
	/**
	 * An IPartitionTokenizerProvider intended to be used for programming languages
	 * that support multiple types of partitioning (eg. MXML), and has a list of 
	 * internal providers.
	 * 
	 * @see MXMLPartitionTokenizerProvider
	 */ 
	public interface IComposedPartitionTokenizerProvider extends IPartitionTokenizerProvider {
		
		/**
		 * Returns the list of internal providers.
		 * 
		 * @see MXMLPartitionTokenizerProvider#getProviders
		 */ 
		function getProviders():ArrayCollection;
	}
}