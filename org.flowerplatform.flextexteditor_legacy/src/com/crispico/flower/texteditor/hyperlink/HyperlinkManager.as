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
package com.crispico.flower.texteditor.hyperlink
{
	import com.crispico.flower.texteditor.SyntaxTextEditor;
	
	import flash.events.MouseEvent;
	
	import flashx.textLayout.edit.IEditManager;
	import flashx.textLayout.edit.SelectionManager;
	import flashx.textLayout.edit.SelectionState;
	import flashx.textLayout.tlf_internal;
	use namespace tlf_internal;
	
	import mx.collections.ArrayCollection;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	import com.crispico.flower.texteditor.model.Partition;
	import com.crispico.flower.texteditor.partitioning.Partitioner;
	import com.crispico.flower.texteditor.providers.IPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.utils.Util;
	import com.crispico.flower.texteditor.scanners.CharacterScanner;
	import com.crispico.flower.texteditor.model.Token;
	import flashx.textLayout.events.FlowElementMouseEvent;
	import mx.controls.Alert;
	import flashx.textLayout.elements.LinkElement;
	import flashx.textLayout.formats.TextLayoutFormat;
	import flash.events.KeyboardEvent;
	import flash.utils.Timer;
	import org.osmf.events.TimeEvent;
	import flash.events.TimerEvent;
	import mx.controls.ToolTip;
	import mx.charts.CategoryAxis;
	import flash.text.engine.TextLine;
	
	/**
	 * Manages hyperlinks for a specific syntax. Extending classes must
	 * set the protected fields <code>hyperlinksTypes</code> and 
	 * <cde>partitionTokenizerProvider</code>.
	 * 
	 * @see SyntaxTextEditor#addHyperlinkManager()
	 * 
	 * @author Mariana
	 * @author Sorin
	 */ 
	public class HyperlinkManager {
		
		/**
		 * The editor where the hyperlinks are highlighted. 
		 */		
		private var editor:SyntaxTextEditor;
		
		/**
		 * The partitioner used to detect the current partition. 
		 */		
		private var partitioner:Partitioner; // REFACTOR se putea obtine din editor
		
		/**
		 * List of token types that are openable. This list must be set 
		 * by extending classes for a specific syntax.
		 */		
		public var hyperlinkTypes:ArrayCollection = new ArrayCollection(); // public attribute
		
		/**
		 * An <code>IPartitionTokenizerProvider</code> set by extending
		 * classes for a specific syntax. Needed to find the tokenizer
		 * used to scan the current partition to check if the selected
		 * word may be hyperlinked.
		 */		
		public var partitionTokenizerProvider:IPartitionTokenizerProvider; // REFACTOR se putea obtine din editor

		/**
		 * The word that was previously hyperlinked. It is used to clear
		 * the hyperlink when a new one will be shown. 
		 */		
		private var previousHyperlink:Word;
		
		private var hoverTimer:Timer;
		
		private static const SHOW_HOVER_DELAY:int = 500;
		
		private var cachedHoverWord:Word;
		
		/**
		 * The style to apply to hyperlinks. 
		` */		
		private static const hyperlinkFormat:TextLayoutFormat = Util.getFormat(0x0000FF, false, false);
		
		/**
		 * The style to apply to normal text. 
		 */		
		private static const normalFormat:TextLayoutFormat = Util.getFormat(0, false, false);
		
		private static const HYPERLINK_CLICKED:String = "hyperlink_clicked";
		
		/**
		 * Sets the editor and partitioner, and adds event listeners to capture
		 * mouse movement and clicking.
		 */		
		public function initialize(editor:SyntaxTextEditor, partitioner:Partitioner):void {
			this.editor = editor;
			this.partitioner = partitioner;
			
			editor.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			// BUG There is a little bug that when releasing the CTRL key the hyperlink doesn't automatically disapears but only after moving the cursor a little. Not a big problem.
			editor.textFlow.addEventListener(HyperlinkManager.HYPERLINK_CLICKED, internalHyperlinkClickedHandler);
		}
		
		private function scheduleHoverTimer():void {
			// Stop the previous timer if existed.
			unscheduleHoverTimer();

			hoverTimer = new Timer(SHOW_HOVER_DELAY, 1);
			hoverTimer.addEventListener(TimerEvent.TIMER_COMPLETE, hoverTimer_timerCompleteHandler);
			hoverTimer.start();
		}
		
		private function unscheduleHoverTimer():void {
			if (!hoverTimer)
				return; // No timer existed.
			hoverTimer.removeEventListener(TimerEvent.TIMER_COMPLETE, hoverTimer_timerCompleteHandler);
			hoverTimer.stop();
			hoverTimer = null;
		}
		
		private function hoverTimer_timerCompleteHandler(event:TimerEvent):void {
			unscheduleHoverTimer(); 
			if (!cachedHoverWord)
				return; // Make sure a cached word still exists when the event happened. 
			
			if (cachedHoverWord.isOpenable)
				mouseHoverHandler(cachedHoverWord.startIndex);
		}
		
		public function setHoverDetails(hoverDetails:String):void {
			editor.toolTip = hoverDetails == null ? null : hoverDetails.replace(/\r\n/g, "\r"); // Flex tooltip does not support \r\n
		}

		/**
		 * Finds the word under the mouse position and adds a hyperlink if it
		 * is openable. Also clears the previous hyperlink and calls the
		 * <code>mouseHoverHandler</code> if the word is openable.
		 */		
		private function mouseMoveHandler(evt:MouseEvent):void {
			try {
				var position:int = SelectionManager.tlf_internal::computeSelectionIndex(editor.textFlow, evt.target, evt.currentTarget, evt.localX, evt.localY);
				if (position >= 0) {
					var word:Word = getWord(position);
					paintHyperlink( evt.ctrlKey ? word : null );
					// If a move happened but the new and the old word is the same dont reset the timer.
					// Makes possible moving with the mouse on the same word without requesting the hover details again, because they are already computed. 
					if (!Word.equals(word, cachedHoverWord)) {
						editor.toolTip = null; // Hide when exiting the word.
						cachedHoverWord = word; // Remeber it to be used to request the hover location on flex client.
						scheduleHoverTimer(); // After a certain amount of time passed show the hover.
					}
				}
			} catch (err:Error) {} // the tlf_internal method may sometimes throw an exception
		}
		
		/**
		 * Automatically clears the hyperlink before notifying about the hyperclick.
		 */ 
		private function internalHyperlinkClickedHandler(evt:FlowElementMouseEvent):void {
			hyperlinkClickedHandler(evt.flowElement.getAbsoluteStart()); // Must be done before removing the hyperlink.
			paintHyperlink(null); // When comming back to not see it still visible.
			editor.toolTip = null; // When opening a new editor not to see the hover details.
			unscheduleHoverTimer(); // Stop timer and not show after we are out with cursor.
			cachedHoverWord = null; // Enable hovering the same word after going out. 		
		}
		
		/**
		 * May be implemented by extending classes. It is called each time
		 * the user clicks a hyperlink.
		 */		
		public function hyperlinkClickedHandler(absoluteOffset:int):void {}
		
		/**
		 * May be implemented by extending classes. It is called each time
		 * the user hovers over an openable word.
		 */		
		public function mouseHoverHandler(absoluteOffset:int):void {}
		
		/**
		 * Given an word it clears the preavious hyperlink if needed and paints the new one if
		 * the word exists and if it is openable.
		 */ 
		private function paintHyperlink(word:Word) {
			if (Word.equals(previousHyperlink, word))
				return; // No change, needed in order not to flicker.
			
			
			// HACK : It seems that the flash textlayout framework considers that when a change is done
			// it needs to refresh all controllers inside the text component. When doing this it automaticaly
			// scrolls back relative to where was the selection last time. For this reason before operating
			// on the text component we set the selection to before 0 which is treated specially to not
			// scroll, and after operating we restore the selection.
				
			// Cleaning must be done when a previous link exists and it was openable.
			if (previousHyperlink != null  ) {
				if (previousHyperlink.isOpenable) {
					
					// @see HACK above. Keeping the old selection possition and reset to before beggining
					var preservedAnchorPosition:int = editor.selectionAnchorPosition;
					var preservedActivePosition:int = editor.selectionActivePosition;
					editor.selectRange(-1, -1);
					
					// Destroy the old link.
					IEditManager(editor.textFlow.interactionManager).applyLink(null, null, false, 
						new SelectionState(editor.textFlow, previousHyperlink.startIndex, previousHyperlink.endIndex));
					editor.setFormatOfRange(normalFormat, previousHyperlink.startIndex, previousHyperlink.endIndex);
					
					// @see HACK above. Restore preserved selection range.
					editor.selectRange(preservedAnchorPosition, preservedActivePosition);
				} 
				previousHyperlink = null;
			}
			// If the new word exists and it is openable.
			if (word != null) {
				previousHyperlink = word;
				if (word.isOpenable) {
					// @see HACK above. Keeping the old selection possition and reset to before beggining
					var preservedAnchorPosition:int = editor.selectionAnchorPosition;
					var preservedActivePosition:int = editor.selectionActivePosition;
					editor.selectRange(-1, -1);

					// Create the new link.
					var hyperlink:LinkElement = IEditManager(editor.textFlow.interactionManager).applyLink("event:" + HyperlinkManager.HYPERLINK_CLICKED, null, false, 
						new SelectionState(editor.textFlow, word.startIndex, word.endIndex));
					hyperlink.getFirstLeaf().format = hyperlinkFormat;

					// @see HACK above. Restore preserved selection range.
					editor.selectRange(preservedAnchorPosition, preservedActivePosition);
				}
			} 
		}
		
		/**
		 * Finds the word to hyperlink. First uses a simple <code>CharactedScanner</code>
		 * to detect the word at the given position, then finds the current partition and
		 * using its partition tokenizer, detect the token corresponding to the word.
		 * If the token type is openable (it is found in the <code>hyperlinkTypes</code>
		 * list) return the word; otherwise return null.
		 */			
		private function getWord(position:int):Word {
			var scanner:CharacterScanner = new CharacterScanner();
			scanner.setRange(editor.doc, 0, editor.doc.length);
			
			scanner.setOffset(position);
			do {
				var ch:int = scanner.unread();
			} while (Util.isWordPart(ch));
			if (ch != -1)
				var startIndex:int = scanner.getOffset() + 1;
			
			scanner.setOffset(position);
			do {
				var ch:int = scanner.read();
			} while (Util.isWordPart(ch));
			if (ch != -1)
				var endIndex:int = scanner.getOffset() - 1;
			
			var partition:Partition = partitioner.partitionUpdater.getPartitionAtPosition(position);
			var partitionTokenizer:RuleBasedScanner = RuleBasedScanner(partitionTokenizerProvider.getPartitionTokenizer(partition.contentType));
			partitionTokenizer.setRange(editor.doc, startIndex, endIndex - startIndex);
			var token:Token = partitionTokenizer.nextToken(); 
			if (hyperlinkTypes.contains(token.type)) 
				return new Word(startIndex, endIndex, true);
			return new Word(startIndex, endIndex, false);
		}
	}
}

/**
 * The range in the text that corresponds to a word. If the
 * <code>isOpenable</code> flag is true, then this word will
 * be hyperlinked at Ctrl + mouse over.
 */
class Word {
	public var startIndex:int;
	public var endIndex:int;
	public var isOpenable:Boolean;
	
	public function Word(startIndex:int, endIndex:int, isOpenable:Boolean):void {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.isOpenable = isOpenable;
	}
	
	public static function equals(first:Word, second:Word):Boolean {
		if (first == null && second != null) 
			return false;
		if (first != null && second == null)
			return false;
		if (first != null && second != null && 
			(first.startIndex != second.startIndex || first.endIndex != second.endIndex || first.isOpenable != second.isOpenable))
			return false;
		return true; 	
	}	
	
}