package com.crispico.flower.texteditor.coloring {
	
	import com.crispico.flower.texteditor.SyntaxTextArea;
	import com.crispico.flower.texteditor.model.FormattedRange;
	import com.crispico.flower.texteditor.model.Range;
	
	import flashx.textLayout.elements.SpanElement;
	import flashx.textLayout.formats.TextLayoutFormat;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Keeps a list of formatted ranges to be applied on the text displayed in the web viewer.
	 * Applies the formats by coloring each span in the viewer.
	 * 
	 * @flowerModelElementId _0qq0kO2uEeCF5Ozw-0NJ0A
	 */
	public class PresentationUpdater {
		
		/**
		 * The list of formatted ranges.
		 * 
		 * @flowerModelElementId _YsYUgMpeEeCTIbyT6X5eRg
		 */
		private var formattedRanges:ArrayCollection;
		
		/**
		 * The viewer that displays the colored text.
		 */ 
		private var textArea:SyntaxTextArea;
		
		/**
		 * A flag that is true if there were new formats to apply, and false otherwise.
		 * Used to check if there were any changes in the text, and if there were, 
		 * force the text area to update its display.
		 */
		private var updated:Boolean = false;
		
		public function PresentationUpdater(textArea:SyntaxTextArea) {
			formattedRanges = new ArrayCollection();  
			this.textArea = textArea;
			updated = false;
		}
		
		/**
		 * Adds the formatted range to the list.
		 */ 
		public function addFormattedRange(range:FormattedRange):void {
			formattedRanges.addItem(range);
		}
		
		/**
		 * Colors each range of text in the list.
		 * 
		 * @flowerModelElementId _9l2_hMyUEeCg_4m4jRGp8Q
		 */
		public function apply():void {  
//			trace ("Apply", formattedRanges.length, "formats");
			
		 	for (var i:int=0; i<formattedRanges.length; i++) {
		 		// check if the range does not exceed the text length
				if ((formattedRanges.getItemAt(i) as Range).offset + (formattedRanges.getItemAt(i) as Range).length <= textArea.textFlow.textLength) {
					formatRange(formattedRanges.getItemAt(i) as FormattedRange);
				}
			}
			formattedRanges = new ArrayCollection();
			if (updated) {
				textArea.textFlow.flowComposer.updateAllControllers();
			}
			updated = false;
		}
		
		/**
		 * Applies the format to the span element.
		 */ 
		private function formatElement(span:SpanElement, format:TextLayoutFormat):void {
			if (span.format != format) {
				updated = true;
				span.format = format;	
			}
		}
		
		/**
		 * Colors a range of text in the viewer, by coloring all the spans that overlap with the text range.
		 * 
		 * This is an alternative to the TextArea.setFormatOfRange() function, which is too slow because it updates the display on every call;
		 * we don't want this, because that would mean updating the display after coloring each range.
		 */ 
		private function formatRange(range:FormattedRange):void {
			// find the first span that overlaps with the range to be colored
			// if the span doesn't start at the first position in the range, split the span and keep the last half
			var firstSpan:SpanElement = textArea.textFlow.findLeaf(range.offset) as SpanElement;
			if (firstSpan.getAbsoluteStart() != range.offset) {
				firstSpan = firstSpan.splitAtPosition(range.offset - firstSpan.getAbsoluteStart()) as SpanElement;
			}
			
			// find the last span that overlaps with the range to be colored
			// if the span doesn't end at the last position in the range, split the span and keep the first half
			var lastSpan:SpanElement = textArea.textFlow.findLeaf(range.offset + range.length -1) as SpanElement;
			if (lastSpan.getAbsoluteStart() + lastSpan.textLength != range.offset + range.length) {
				lastSpan.splitAtPosition(range.offset + range.length - lastSpan.getAbsoluteStart());
			}
						
			// color the spans between the first and last span, including the first and last (or parts of them)
			var span:SpanElement = firstSpan;
			while (span != lastSpan) {
				formatElement(span, range.format);
				span = span.getNextLeaf() as SpanElement;
			}
			formatElement(lastSpan, range.format);
		}
	}
}