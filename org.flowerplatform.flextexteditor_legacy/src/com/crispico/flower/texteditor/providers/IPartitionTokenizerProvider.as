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
	 * 
	 */
	public interface IPartitionTokenizerProvider {
		
		/**
		 * Returns the partition tokenizer specific to the content type.
		 * 
		 * 
		 */
		 function getPartitionTokenizer(partitionContentType:String):RuleBasedScanner;
	}
}