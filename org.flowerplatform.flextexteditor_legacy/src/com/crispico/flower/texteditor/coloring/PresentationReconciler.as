package com.crispico.flower.texteditor.coloring {
	
	import com.crispico.flower.texteditor.Document;
	import com.crispico.flower.texteditor.SyntaxTextArea;
	import com.crispico.flower.texteditor.model.FormattedRange;
	import com.crispico.flower.texteditor.model.Partition;
	import com.crispico.flower.texteditor.model.Range;
	import com.crispico.flower.texteditor.model.Token;
	import com.crispico.flower.texteditor.providers.IComposedPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.providers.IComposedPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.providers.IPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.providers.IPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.scanners.CharacterScanner;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	
	import flashx.textLayout.formats.TextLayoutFormat;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Computes the coloring on a list of partitions. For each partition, finds the minimum range to recolor.
	 * 
	 * It needs a SyntaxTextArea to color and also providers for coloring scanners specific to each content type
	 * and formats for each token in a partition.
	 * 
	 * @flowerModelElementId _qsR0IMpUEeCTIbyT6X5eRg
	 */
	public class PresentationReconciler {
		
		/**
		 * The text editor that displays the colored text.
		 */ 
		private var _syntaxTextArea:SyntaxTextArea;
		
		/**
		 * Used to update the coloring on text editor after each change in the text or after the user scrolls.
		 */ 
		private var presentationUpdater:PresentationUpdater;
		
		/**
		 * The document that contains the text. 
		 */ 
		private var _document:Document;
		
		/**
		 * The provider used to get the coloring scanner to use on specific partitions.
		 * 
		 * @flowerModelElementId _0qvtEu2uEeCF5Ozw-0NJ0A
		 */
		private var _partitionTokenizerProvider:IPartitionTokenizerProvider;
		
		/**
		 * The provider used to get the formats for each token in a partition.
		 */ 
		private var _formatProvider:IPartitionTokenFormatProvider;
		
		/** 
		 * Sets the text area that presents the colored text
		 * and initialize the presentation updater.
		 */
		public function set syntaxTextArea(syntaxTextArea:SyntaxTextArea):void {
			_syntaxTextArea = syntaxTextArea;
			presentationUpdater = new PresentationUpdater(syntaxTextArea);
		}
		
		/**
		 * Sets the document to be scanned.
		 */ 
		public function set document(document:Document):void {
			_document = document;
		}
		
		public function set partitionTokenizerProvider(partitionTokenizerProvider:IPartitionTokenizerProvider):void {
			_partitionTokenizerProvider = partitionTokenizerProvider;
		}
		
		public function set formatProvider(formatProvider:IPartitionTokenFormatProvider):void {
			_formatProvider = formatProvider;
		}
		
		/**
		 * Computes the coloring for the partitions. For each partition, check if it is marked for coloring, and
		 * if that is the case, compute the coloring on the dirty range of that partition.
		 * 
		 * @flowerModelElementId _kV2rUMpZEeCTIbyT6X5eRg
		 */
		public function createAndApplyPresentation(partitions:ArrayCollection):void {
//			var startedComputing:Date = new Date();
			
			// compute the coloring on each partition with a coloring scanner specific to its content type
			for each (var partition:Partition in partitions) {
				if (partition.dirty) {
					// get the tokenizer specific to the content type of the partition and if there is no tokenizer throw an error
					var partitionTokenizer:RuleBasedScanner = _partitionTokenizerProvider.getPartitionTokenizer(partition.contentType);
					if (partitionTokenizer == null) {
						if (_partitionTokenizerProvider is IComposedPartitionTokenizerProvider) {
							// if the provider is composed, check if the partition type is specific to one of its internal providers
							for each (var internalProvider:IPartitionTokenizerProvider in (_partitionTokenizerProvider as IComposedPartitionTokenizerProvider).getProviders()) {
								partitionTokenizer = internalProvider.getPartitionTokenizer(partition.contentType);
								if (partitionTokenizer != null) {
									break;
								}
							}
						} else {
							throw new Error("There is no partition tokenizer for the " + partition.contentType + " partition content type.");
						}
					}
					computeFormats(minimumRange(partition), partitionTokenizer);
				
					// mark the partition as colored
					partition.dirty = false;
					partition.dirtyRange = null;
				}
			}
			
//			trace ("Compute formats", new Date().valueOf() - startedComputing.valueOf());
//			var startedApplying:Date = new Date();
			
			// applies the presentation
			presentationUpdater.apply();
			
//			trace ("Apply formats", new Date().valueOf() - startedApplying.valueOf());
		}
		
		/**
		 * Uses the provided coloring scanner to compute the text formats to be applied to the region. Adds the formatted ranges to the presentation.
		 * 
		 * @flowerModelElementId _Mu87Nt3QEeCGOND4c9bKyA
		 */
		private function computeFormats(region:Range, partitionTokenizer:RuleBasedScanner):void {
			// use the specific coloring scanner to find tokens
			partitionTokenizer.setRange(_document, region.offset, region.length);
			var token:Token = partitionTokenizer.nextToken();
			while (!token.isEof()) {
				// create a formatted range for each token found
				var format:TextLayoutFormat = _formatProvider.getFormat(token.type);
				if (format == null) {
					// if the provider is composed, check if the token type is specific to one of its internal providers
					if (_formatProvider is IComposedPartitionTokenFormatProvider) {
						for each (var provider:IPartitionTokenFormatProvider in (_formatProvider as IComposedPartitionTokenFormatProvider).getProviders()) {
							format = provider.getFormat(token.type);
							if (format != null) {
								break;
							}
						}
					} else {
						throw new Error("There is no format for the " + token.type + " token.");
					}
				}
				presentationUpdater.addFormattedRange(new FormattedRange(token.offset, token.length, format));
				token = partitionTokenizer.nextToken();
			}
		}
		
		/**
		 * Returns the minimum range to be scanned in order to color the partition, because the token to color might start
		 * before or end after the dirty range of the partition. Eg. the dirty rage starts in the middle of a keyword. Scanning from 
		 * the start of the dirty range will not find the keyword. 
		 * 
		 * The minimum range is considered to be the lines that contain the dirty region, since tokens don't cover multiple
		 * lines (i.e. no token starts on one line and ends on the next one).
		 */ 
		private function minimumRange(partition:Partition):Range {
			var chScanner:CharacterScanner = new CharacterScanner();
			chScanner.setDocument(_document);
			var dirtyRange:Range = partition.dirtyRange;
			var startIndex:int = chScanner.getLineStart(dirtyRange.offset);
			var endIndex:int = chScanner.getLineEnd(dirtyRange.offset + dirtyRange.length);
			startIndex = Math.max(startIndex, partition.offset);
			endIndex = Math.min(endIndex, partition.offset + partition.length);
			return new Range(startIndex, endIndex - startIndex);
		}
	}
}