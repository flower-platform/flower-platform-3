package com.crispico.flower.texteditor.providers {
	
	import mx.collections.ArrayCollection;
	
	/**
	 * An IPartitionTokenFormatProvider intended to be used for programming languages
	 * that support multiple types of partitioning (eg. MXML), and has a list of 
	 * internal providers.
	 * 
	 * @see MXMLPartitionTokenFormatProvider
	 */ 
	public interface IComposedPartitionTokenFormatProvider extends IPartitionTokenFormatProvider {
		
		/**
		 * Returns a list of internal providers.
		 * 
		 * @see MXMLPartitionTokenFormatProvider#getProviders
		 */ 
		function getProviders():ArrayCollection;
	}
}