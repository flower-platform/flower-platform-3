package com.crispico.flower.texteditor { 
	
	import com.crispico.flower.texteditor.coloring.PresentationReconciler;
	import com.crispico.flower.texteditor.events.TextChangedEvent;
	import com.crispico.flower.texteditor.events.VisibleRangeChangedEvent;
	import com.crispico.flower.texteditor.hyperlink.HyperlinkManager;
	import com.crispico.flower.texteditor.model.DocumentChange;
	import com.crispico.flower.texteditor.model.Partition;
	import com.crispico.flower.texteditor.model.Range;
	import com.crispico.flower.texteditor.partitioning.Partitioner;
	import com.crispico.flower.texteditor.partitioning.scanners.IPartitionScanner;
	import com.crispico.flower.texteditor.providers.IComposedPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.providers.IPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.providers.IPartitionTokenizerProvider;
	
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import flashx.textLayout.conversion.ConversionType;
	import flashx.textLayout.conversion.PlainTextExporter;
	
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;

	use namespace mx_internal;
	
	/**
	 * The main component that displays the text with syntax coloring. 
	 * 
	 * To use the editor for a specific programming language, provide an IPartitionScanner (detects the partitions
	 * for the language), an IPartitionTokenizerProvider (provides a partition tokenizer for each partition, that 
	 * is responsible with breaking each partition into tokens such as keywords) and an IPartitionTokenFormatProvider
	 * (provides a text format to be applied on each token in the text).
	 * 
	 * The editor manages the partitioning (using the specific partition scanner) and coloring (using the specific
	 * providers). Partitioning is done one page at a time, until the text is fully partitioned, thus allowing the user
	 * to interact with the editor even when the text is too long and would take more time to partition. Coloring is 
	 * done when the user edits text or scrolls, so the visible lines are always colored.
	 */ 
	public class SyntaxTextEditor extends SyntaxTextArea {
		
		/**
		 * The document that contains the text.
		 */ 
		public var doc:Document;
		
		/**
		 * The partitioner used to compute the partitioning.
		 */ 
		private var partitioner:Partitioner;
		
		/**
		 * The PresentationReconciler used to color the text.
		 */ 
		private var presentationReconciler:PresentationReconciler;
		
		/**
		 * The end of line separator used in the initial text loaded
		 * in the editor; it is used to recreate the text with its
		 * original end of line separator, and not the default separator
		 * TextArea uses to display the text (\n).
		 */ 
		private var endOfLineSeparator:String;
		
		/**
		 * Length of the page that is partitioned.
		 */ 
		private const paginationPageSize:int = 5000;
		
		/** 
		 * The timer used to monitor the partitioning; a page is partitioned at every tick.
		 */
		private var noUserInteractionTimer:Timer;
		
		/** 
		 * The time to wait before restarting the partition in case the user interacts with the editor
		 * (text editing or scrolling).
		 */
		private const noUserInteractionTime:int = 50; 
		
		/**
		 * The minimum time to partition before waiting for user interaction. If partitioning a page 
		 * takes less than this interval, then start partitioning another page.
		 */ 
		private const paginationAllowTime:int = 20;
		
		/**
		 * A flag that is true if the partitioning is not complete, false otherwise.
		 */ 
		private var paginationNeeded:Boolean = false;
		
		/**
		 * Manager hyperlinks, if added.
		 */ 
		private var hyperlinkManager:HyperlinkManager;
		
		[Bindable]
		public var isDirty:Boolean = false;
		
		/**
		 * Creates the Partitioner and PresentationReconciler used to partition and color the text, and the timer used to manage
		 * partitioning. Adds event listeners for changes in the text (insert, delete, replace) and scrolling.
		 */ 
		public function SyntaxTextEditor() {
			super();
			
			// initialize the document
			doc = new Document();
			
			// initialize the partitioner and set the document it will partition
			partitioner = new Partitioner();
			partitioner.document = doc;
			
			// initialize the PresentationReconciler and set the document and text area it will color
			presentationReconciler = new PresentationReconciler();
			presentationReconciler.document = doc;
			presentationReconciler.syntaxTextArea = this;	
			
			// a new page will be partitioned on each tick of the timer
			noUserInteractionTimer = new Timer(noUserInteractionTime);
			noUserInteractionTimer.addEventListener(TimerEvent.TIMER, noUserInteractionHappened);
			
			// add listeners for editing and scrolling, to trigger coloring
			addEventListener(TextChangedEvent.TEXT_CHANGED, textChangedHandler);
			addEventListener(VisibleRangeChangedEvent.VISIBLE_RANGE_CHANGED, visibleRangeChangedHandler);
		}
		
		/**
		 * Sets specific scanners and providers the partitioner and presentation reconciler will work with.
		 * Also checks if the tokenizer provider is valid for each partition.
		 */ 
		public function initializeSyntax(partitionScanner:IPartitionScanner, partitionTokenizerProvider:IPartitionTokenizerProvider, formatProvider:IPartitionTokenFormatProvider):void {
			// set the specific partitionScanner to the partitioner
			partitioner.partitionScanner = partitionScanner;
			
			// set the specific providers to the presentation reconciler
			presentationReconciler.partitionTokenizerProvider = partitionTokenizerProvider;
			presentationReconciler.formatProvider = formatProvider;
			
			// check if there is a tokenizer for each content type
			// otherwise there might be partitions that cannot be colored
			checkPartitionTokenizerProvider(partitionScanner.getRecognizedContentTypes(), partitionTokenizerProvider);
		}
		
		/**
		 * Configures the given <code>HyperlinkManager</code> to work
		 * with this editor.  
		 */		
		public function addHyperlinkManager(hyperlinkManager:HyperlinkManager):void {
			this.hyperlinkManager = hyperlinkManager;
			hyperlinkManager.initialize(this, partitioner);
		}
		
		/**
		 * Loads the content in the editor. Find the endOfLineSeparator from the content (can be \r, \n or \r\n), 
		 * to recreate the same string when returning the content (because we need to change the original text before
		 * we set it on the document, to keep it in sync with the text on the textArea where the default
		 * endOfLineSeparator \n is used).
		 * 
		 * Note: it is advised to pass this component ONLY text separated with \n eoln delimiter. At the moment 
		 * when an exact index/subregion with text is needed, it is not returned correctly because it does not
		 * considers the initial eoln delimiter (which may give different indexes or subregion). In order for 
		 * this component to work transparently with all types of eoln separators, the RuleBasedScanner and 
		 * the Rules would be needed to be fixed to work with other delimiters than \n and also a fix for the spark.TextFlow 
		 * because when splitting the text, for each line in paragraphs it forgets the original delimiter.
		 * The spark components seems not well writen so it is advised to wait for a new verion.
		 */ 
		public function setContent(content:String):void {
			if (content == null)
				content = "";
			// find the endOfLineSeparator specific to the text 
			// the search and replace functions were already tested and they don't take much time
			if (content.search("\r\n") > -1) {
				setEndOfLineSeparator("\r\n");
			} else {
				if (content.search("\r") > -1) {
					setEndOfLineSeparator("\r");
				} else {
					setEndOfLineSeparator("\n"); 	
				}
			}
			
			// delete all \r, otherwise they would only be in the document text, and not on the text area
			content = content.replace(/\r/g, ""); // BUG if it has \r separator is make a big continous text.
			
			// set the content in the document and start partitioning
			doc.text = content;
			partitioner.document = doc;
			loadTextContent(content);
			
			// content is loaded
			isDirty = false;
			
			// start partitioning
			notifyNewContent();
		}
		
		/**
		 * Get the content loaded in the editor. Use the paragraphSeparator found when the text was loaded.
		 */ 
		public function getContent():String {
			var exporter:PlainTextExporter = new PlainTextExporter();
			exporter.paragraphSeparator = endOfLineSeparator;
			
			// content is saved
			isDirty = false;
			
			return exporter.export(textFlow, ConversionType.STRING_TYPE) as String; 
		}
		
		/**
		 * Sets the end of line separator that will be used when returning the text
		 * from the editor.
		 * @see <code>setContent()</code>
		 */ 
		public function setEndOfLineSeparator(endOfLineSeparator:String):void {
			this.endOfLineSeparator = endOfLineSeparator;
		}
		
		public function updateText(offset:int, oldTextLength:int, newText:String):void {
			selectRange(offset, offset + oldTextLength);
			insertText(newText);
			dispatchEvent(new TextChangedEvent(offset, oldTextLength, newText));
		}
		
		/**
		 * Checks if the IPartitionTokenizerProvider provides coloring scanners for each content type, otherwise there would be 
		 * ranges of text that cannot be colored.
		 */ 
		private function checkPartitionTokenizerProvider(contentTypes:ArrayCollection, provider:IPartitionTokenizerProvider):void {
			for each (var contentType:String in contentTypes) {
				if (provider.getPartitionTokenizer(contentType) == null) {
					if (provider is IComposedPartitionTokenizerProvider) {
						// if the provider is composed, check if the partition type is specific to one of its internal providers
						var foundTokenizer:Boolean = false;
						for each (var internalProvider:IPartitionTokenizerProvider in (provider as IComposedPartitionTokenizerProvider).getProviders()) {
							if (internalProvider.getPartitionTokenizer(contentType) != null) {
								foundTokenizer = true;
							}
						}
						if (!foundTokenizer) {
							throw new Error(contentType + " does not have a partition tokenizer assigned.");
						}
					}
				}
			}	
		}
		
		/** 
		 * Partitions and colors the visible range after the user edits the text.
		 */ 
		private function textChangedHandler(evt:TextChangedEvent):void {
			
			// pause partitioning to start coloring
			notifyUserInteraction();
//			var date:Date = new Date();
			// update the text in the document and set the document limit to scan past the damaged region
			doc.text = doc.text.substring(0, evt.offset) + evt.newText + doc.text.substring(evt.offset + evt.oldTextLength);
//			trace ("Copy text to doc", new Date().valueOf() - date.valueOf());
			doc.endLimit += evt.newText.length - evt.oldTextLength;
			// compute the partitioning after the text was edited and color the visible range
			var documentChange:DocumentChange = new DocumentChange(evt.offset, evt.oldTextLength, evt.newText.length);
			partitioner.computePartitioning(documentChange);
			updatePresentation(getVisibleRange());
			
			// notify that the editor is dirty
			notifyDirty();
			
			// restart partitioning while the user doesn't interact with the editor
			notifyUserIdle();
			
//			trace ("Text changed handler (editor)", new Date().valueOf() - date.valueOf());
		}
		
		/**
		 * Set the dirty state of the editor to true.
		 */ 
		private function notifyDirty():void {
			isDirty = true;
		}

		/** 
		 * Colors the new visible range. The VisibleRangeChangedEvent is dispatched when the user scrolls,
		 * resizes the editor or edits the text.
		 */ 
		private function visibleRangeChangedHandler(evt:VisibleRangeChangedEvent):void {
			// pause partitioning to start coloring
			notifyUserInteraction();
			updatePresentation(evt.visibleRange);
			// restart partitioning while the user doesn't interact with the editor
			notifyUserIdle();
		}
		
		/**
		 * Colors the visible range in the editor. If partitioning was not computed yet on the visible range, 
		 * computes the partitioning until the last visible character. 
		 */ 
		private function updatePresentation(presentationRange:Range):void {
			var presentationFirstIndex:int = presentationRange.offset;
			var presentationLastIndex:int = presentationRange.offset + presentationRange.length;
			
			// if partitioning was not yet computed on the visible range, compute the partitioning until the last visible character
		 	if (presentationLastIndex > doc.endLimit) {
				var lastPartitionedIndex:int = doc.endLimit;
				doc.endLimit = presentationLastIndex;
				partitioner.computePartitioning(new DocumentChange(lastPartitionedIndex, 0, presentationLastIndex - lastPartitionedIndex));
				if (doc.endLimit == text.length) { // If scrolling on the last page, all content is partitioned so stop the pagination partitioner.
					notifyAllContentPartitioned(); 
				}				
			} 
			
			// find the partitions in the range, by iterating through the partitions from the partition at the first
			// visible index until the partition at the last visible index and put them in a list that will be passed
			// to the reconciler
			var firstPartition:Partition = this.partitioner.partitionUpdater.getPartitionAtPosition(presentationFirstIndex);
			var lastPartition:Partition = this.partitioner.partitionUpdater.getPartitionAtPosition(presentationLastIndex);
			var partitions:ArrayCollection = new ArrayCollection();
			var partition:Partition = firstPartition;
			while (partition != lastPartition) {
				partitions.addItem(partition);
				partition = partitioner.partitionUpdater.nextPartition(partition);
			}
			if (lastPartition != null)
				partitions.addItem(lastPartition);
			
			// pass the list of partitions to the reconciler for tokenizing and coloring
			presentationReconciler.createAndApplyPresentation(partitions);
		}
		
		/**
		 * Called after the new content is loaded in the text area.
		 * Initializes the timer and starts pagination.
		 */ 
		private function notifyNewContent():void {
			paginationNeeded = true;
			doc.endLimit = 0;
			noUserInteractionTimer.start();
		}
		
		/**
		 * Stops the timer when all the content was partitioned.
		 */ 
		private function notifyAllContentPartitioned():void {
			noUserInteractionTimer.stop();
			paginationNeeded = false;
		}
		
		/**
		 * Called after the user edits the text or scrolls.
		 * Resets the timer to allow partitioning and/or recoloring.
		 */ 
		private function notifyUserInteraction():void {
			noUserInteractionTimer.reset();
		}
		
		/**
		 * Called after recoloring the visible area. 
		 * Restarts pagination while the user is idle.
		 */ 
		private function notifyUserIdle():void {
			if (paginationNeeded) {
				noUserInteractionTimer.start();
			}
		}
		
		/**
		 * Computes the partitioning one page at a time (page size is fixed).
		 */ 
		 private function noUserInteractionHappened(evt:TimerEvent):void {
			var startedPartitioningTime:Date = new Date();
			
			while ((new Date().getTime() - startedPartitioningTime.getTime()) < paginationAllowTime) {
				var actualPageSize:int = Math.min(paginationPageSize, text.length - doc.endLimit);
				doc.endLimit += actualPageSize;
				partitioner.computePartitioning(new DocumentChange(doc.endLimit - actualPageSize, 0, actualPageSize));
			
				// stop when the content is loaded completely
				if (doc.endLimit == text.length) {
					notifyAllContentPartitioned();
					break;
				}
			}
		} 
		
		/* 
		public function testPartitioning():void {
			
			// test
			var content:String = getContent();
			
			var partitions:ArrayCollection = partitioner.partitionUpdater.partitions;
			if (partitions.length == 0)
				return;
			var prev:Partition = partitions.getItemAt(0) as Partition;
			for (var i:int=1; i<partitions.length; i++) {
				var crt:Partition = partitions.getItemAt(i) as Partition;
				if (crt.offset != prev.offset + prev.length)
					trace("gap at ", i, crt.offset - prev.offset - prev.length);
				if (crt.contentType == prev.contentType)
					trace(prev.contentType);
				prev = crt;
			}
			trace(partitions.length);
		}
		 */ 
	}
}