package com.crispico.flower.texteditor {
	
	import mx.core.mx_internal;
	use namespace mx_internal;
	import flashx.textLayout.tlf_internal;
	use namespace tlf_internal;
	import spark.components.TextArea;
	import spark.events.TextOperationEvent;
	import flashx.textLayout.compose.TextFlowLine;
	import mx.events.PropertyChangeEvent;
	import mx.events.FlexEvent;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flashx.textLayout.operations.SplitParagraphOperation;
	import flashx.textLayout.operations.CutOperation;
	import flashx.textLayout.operations.DeleteTextOperation;
	import flashx.textLayout.edit.TextScrap;
	import flashx.textLayout.operations.PasteOperation;
	import flashx.textLayout.operations.InsertTextOperation;
	import flashx.textLayout.operations.FlowTextOperation;
	import flashx.textLayout.edit.SelectionState;
	import com.crispico.flower.texteditor.model.Range;
	import com.crispico.flower.texteditor.events.TextChangedEvent;
	import com.crispico.flower.texteditor.events.VisibleRangeChangedEvent;
	import mx.events.ResizeEvent;
	import mx.graphics.shaderClasses.ColorShader;
	import flash.display.BlendMode;
	import flashx.textLayout.edit.SelectionFormat;
	import flashx.textLayout.operations.UndoOperation;
	import flashx.textLayout.operations.RedoOperation;
	import flashx.textLayout.operations.FlowOperation;

	
	/**
	 * A TextArea used to display the colored text. Listens for user edits in the text 
	 * (insert, delete, replace) and dispatches an event to trigger partitioning. Also
	 * listens for scrolling and dispatches a scrolling event to trigger the coloring of the
	 * visible lines.
	 * 
	 * @flowerModelElementId _x_73cSyKEeGsGrJcrtxw9Q
	 */ 
	public class SyntaxTextArea extends TextArea {
		
		/**
		 * The keystroke buffer used to collect and dispatch
		 * TextChangedEvents from this editor to other paired
		 * editors.
		 */
		private var keystrokeBuffer:TextChangedEventBuffer;
		private var textChangedEvent:TextChangedEvent;
		
		private var selectionColor:uint = 0x99DDFF; // blue
		
		private var initialSelectedRange:Range;
		
		/**
		 * The <code>CustomEditManager</code> to be set every
		 * time the text is reinitialized in <code>loadTextContent</code>.
		 * This instance is cached because setting a new instance
		 * will lead to losing the undo stack and selection.
		 */ 
		private var customEditManager:CustomEditManager = new CustomEditManager();
		
		/**
		 * Creates this viewer. Adds listeners for loading and editing text.
		 */ 
		public function SyntaxTextArea():void {
			super();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			addEventListener(FocusEvent.KEY_FOCUS_CHANGE, keyFocusChangedHandler);
			addEventListener(FocusEvent.FOCUS_IN, focusChangedHandler);
			addEventListener(FocusEvent.FOCUS_OUT, focusChangedHandler);
			// the changing handler (before the display is updated) creates an event that reflects the edit
			// the change handler (after the display is updated) dispatches the event to trigger partitioning/coloring
			addEventListener(TextOperationEvent.CHANGING, changingHandler);
			addEventListener(TextOperationEvent.CHANGE, changeHandler);
			
			// the use_capture argument is true to capture the resize event on the application first
			// this ensures that the text is colored and then the editor is resized
			addEventListener(ResizeEvent.RESIZE, resizeHandler, true);
			
			// no word-wrap
			setStyle("lineBreak", "explicit"); 
		}
		
		/**
		 * Hack needed so that when focusing/unfocusing to not lose the color of selection.
		 * TextContainerManager#configuration can not be changed and there seems to be no method
		 * to contruct a custom TextContainerManager so instead we adopted this solution.
		 */
		private function focusChangedHandler(event:FocusEvent):void {
			var selectionformat : SelectionFormat =  new SelectionFormat(selectionColor, 1, BlendMode.NORMAL);
			event.currentTarget.textFlow.interactionManager.inactiveSelectionFormat = selectionformat;
			event.currentTarget.textFlow.interactionManager.focusedSelectionFormat = selectionformat;
			event.currentTarget.textFlow.interactionManager.unfocusedSelectionFormat = selectionformat;
			event.currentTarget.textFlow.interactionManager = event.currentTarget.textFlow.interactionManager;
		}
		
		/**
		 * Enables the use of a <code>TextChangedEventBuffer</code> with
		 * the given interval.
		 */ 
		 public function setKeystrokeAggregationInterval(interval:int = 0):void {
			 if (keystrokeBuffer)
				keystrokeBuffer.setInterval(interval);
			 else
		 		keystrokeBuffer = new TextChangedEventBuffer(this, interval);
		 }
		
		 /**
		 * Triggers events aggregation on the <code>TextChangedEventBuffer</code>
		 * if the user is saving his changes. This is to ensure that all the 
		 * changes have arrived on the server before the text file is saved.
		 */ 
		 public function emptyKeystrokeAggregationBuffer():void {
			 if (keystrokeBuffer)
				 keystrokeBuffer.aggregateEvents();
		 }
		 
		 public function setKeystrokeAggregationBufferDirtyState(dirty:Boolean):void {
			 if (keystrokeBuffer)
				 keystrokeBuffer.dirty = dirty;
		 }
		 
		 /**
		  * Called from <code>HyperlinkClickedClientCommand</code>. Used to 
		  * select the range in this editor when the editor is open, because 
		  * in the case of large files, the editor takes too much time to 
		  * display the text.
		  */ 
		 public function setInitialSelectedRange(range:Range):void {
			 initialSelectedRange = range;
		 }
		 
		/**
		 * Adds a listener for scrolling, to trigger recoloring when the visible area changes.
		 * 
		 * @flowerModelElementId _yAgfMCyKEeGsGrJcrtxw9Q
		 */ 
		private function creationCompleteHandler(evt:FlexEvent):void {
			textDisplay.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, scrollHandler);
			
			// prevents several behaviours such as focusing out of the editor when pressing TAB
			textFlow.interactionManager = customEditManager;
			
			if (initialSelectedRange != null) {
				textDisplay.setFocus();
				selectRange(initialSelectedRange.offset, initialSelectedRange.offset + initialSelectedRange.length);
				scrollToRange(initialSelectedRange.offset);
			}
		} 
		
		/**
		 * Notifies that the visible range changed due to scrolling, to trigger recoloring on the new visible range.
		 * 
		 * @flowerModelElementId _yAgfNCyKEeGsGrJcrtxw9Q
		 */ 
		private function scrollHandler(evt:PropertyChangeEvent):void {
			if (evt.property == "verticalScrollPosition") {
					notifyVisibleRangeChanged();
			}
		}
		  
		/**
		 * Notifies that the visible range changed due to resizing, to trigger recoloring on the new visible range.
		 */   
		private function resizeHandler(evt:ResizeEvent):void {
			notifyVisibleRangeChanged();
		}
		
		/**
		 * Dispatches a VisibleRangeChangedEvent to notify that the visible area changed due to scrolling
		 * or resizing.
		 */ 
		private function notifyVisibleRangeChanged():void {
			// the SyntaxTextEditor has a listener for this event, to trigger coloring on the new visible range
			dispatchEvent(new VisibleRangeChangedEvent(getVisibleRange()));
		}
		
		/**
		 * Loads the new text in the editor and scrolls the text to the top.
		 */ 
		protected function loadTextContent(newContent:String):void {
			text = newContent;
			
			// the custom interaction manager needs to be set every time new text is loaded
			// because the textFlow is reinitialized
			textFlow.interactionManager = customEditManager;
			
			// call later because the component first needs to load the new text  
			// we need to scroll to top because after the text is loaded, the cursor is set at the end of the text
			// and that would trigger the partitioning of the entire text
			callLater(textContentLoadedHandler);
		}
		
		/** 
		 * Scrolls to the top of the editor after the content is loaded. 
		 * Dispatches a ScrollPositionChangedEvent to trigger the coloring of the first lines.
		 */ 
		private function textContentLoadedHandler():void {
			textDisplay.verticalScrollPosition = 0;
			// the SyntaxTextEditor has a listener for this event, to trigger coloring on the visible range
			dispatchEvent(new VisibleRangeChangedEvent(getVisibleRange()));
		} 
		
		/**
		 * Computes the visible range. Used after the user scrolls.
		 */ 
		protected function getVisibleRange():Range {
			// get the first and last visible lines
			var firstLine:TextFlowLine = textFlow.flowComposer.getControllerAt(0).tlf_internal::getFirstVisibleLine();
			var lastLine:TextFlowLine = textFlow.flowComposer.getControllerAt(0).tlf_internal::getLastVisibleLine();
			
			// get the index of the first character on the first visible line
			// and the index of the last character on the last visible line
			var firstIndex:int = firstLine != null ? firstLine.absoluteStart : 0;
			var lastIndex:int = lastLine != null ? lastLine.absoluteStart + lastLine.textLength - 1 : text.length;

			return new Range(firstIndex, lastIndex - firstIndex);
		}
		
		/** 
		 * Prevents focusing out of the editor when pressing TAB; instead the CustomEditManager inserts \t.
		 */ 
		private function keyFocusChangedHandler(evt:FocusEvent):void {
		 	evt.preventDefault();
		}
		
		/**
		 * Creates a TextChangedEvent that corresponds to the editing from the user.
		 * 
		 * <p>
		 * 
		 * Note: this event must be created in the changing handler (NOT in the changed
		 * handler) because we need the original text as well.
		 * 
		 * @flowerModelElementId _yAhGRCyKEeGsGrJcrtxw9Q
		 */ 
		private function changingHandler(evt:TextOperationEvent):void {
//			var startChanging:Date = new Date();
			
			var notify:Boolean = false;
			var operation:FlowTextOperation = evt.operation as FlowTextOperation;
			
			// if this is a redo, get the operation that is being redone
			if (evt.operation is RedoOperation) {
				operation = FlowTextOperation(RedoOperation(evt.operation).operation);
			}
			
			// if this is a undo, let the changeHandler get the new text from the modified textFlow
			if (evt.operation is UndoOperation) {
				notify = true;
			}
			
			var start:int;
			var oldTextLength:int;
			var newText:String;
			
			// create the TextChangedEvent depending on the type of the edit
			if (operation != null) {
				start = operation.absoluteStart;
				oldTextLength = Math.abs(operation.absoluteStart - operation.absoluteEnd);
				newText = null;
				
				// if the edit was an insert, get the new text that was inserted
				if (operation is InsertTextOperation) {
					newText = (operation as InsertTextOperation).text;
					notify = true;
				}
				
				// if the edit was a paste, get the new text that was pasted
				if (operation is PasteOperation) {
					var scrap:TextScrap = (operation as PasteOperation).textScrap;
					newText = scrap.textFlow.getText();
					notify = true;
				}
				
				// if the edit was a cut or delete, set the new text to ""
				if (operation is CutOperation || operation is DeleteTextOperation) {
					newText = "";					
					notify = true;
				}
				if (operation is SplitParagraphOperation) {
					// this should not happen
					throw new Error("A split paragraph operation has occured!");
				}
			}
			if (notify) {
				textChangedEvent = new TextChangedEvent(start, oldTextLength, newText);
			} else {
				textChangedEvent = null;
			}
			
//			trace ("Changing handler", new Date().valueOf() - startChanging.valueOf());
		}
		
		/**
		 * Dispatches the TextChangedEvent to trigger partitioning and coloring.
		 * Note: this needs to be done in the change handler (NOT in the 
		 * changing handler) because the text needs to be updated before
		 * partitioning and coloring.
		 */ 		
		private function changeHandler(evt:TextOperationEvent):void {
//			var startChange:Date = new Date();
			if (textChangedEvent) {
				// the operation is an undo, get the new text
				if (evt.operation is UndoOperation) {
					textChangedEvent = getTextChangedEventAfterUndo(UndoOperation(evt.operation).operation as FlowTextOperation);
				}
				dispatchEvent(textChangedEvent);
				if (keystrokeBuffer) {
					keystrokeBuffer.add(textChangedEvent);
				}
				textChangedEvent = null;
			}
//			trace ("Change handler", new Date().valueOf() - startChange.valueOf());
		}
		
		/**
		 * Returns the <code>TextChangedEvent</code> after an undo operation, depending on the 
		 * operation that was undone.
		 */ 
		private function getTextChangedEventAfterUndo(op:FlowTextOperation):TextChangedEvent {
			var offset:int = op.absoluteStart;
			var newText:String;
			var oldTextLength:int;
			
			// if we undo an insert operation, get the new text from the textFlow and the oldTextLength as the length of the text that was inserted before
			if (op is InsertTextOperation) {
				var insertOp:InsertTextOperation = op as InsertTextOperation;
				if (insertOp.deleteSelectionState)
					newText = textFlow.getText(insertOp.deleteSelectionState.absoluteStart, insertOp.deleteSelectionState.absoluteEnd);
				else
					newText = "";
				oldTextLength = insertOp.text.length;
			}
			
			// if we undo a paste operation, get the new text from the textFlow and the oldTextLength as the length of the pasted text
			if (op is PasteOperation) {
				var pasteOp:PasteOperation = op as PasteOperation;
				newText = textFlow.getText(pasteOp.originalSelectionState.absoluteStart, pasteOp.originalSelectionState.absoluteEnd);
				oldTextLength = pasteOp.textScrap.textFlow.getText().length;
			}
			
			// if we undo a delete operation, get the new text from the textFlow and set oldTextLength to 0
			if (op is DeleteTextOperation) {
				var deleteOp:DeleteTextOperation = op as DeleteTextOperation;
				newText = textFlow.getText(deleteOp.deleteSelectionState.absoluteStart, deleteOp.deleteSelectionState.absoluteEnd);
				oldTextLength = 0;
			}
			
			// if we undo a cut operation, get the new text from the textFlow and set oldTextLength to 0
			if (op is CutOperation) {
				var cutOp:CutOperation = op as CutOperation;
				newText = textFlow.getText(cutOp.originalSelectionState.absoluteStart, cutOp.originalSelectionState.absoluteEnd);
				oldTextLength = 0;
			}
			
			return new TextChangedEvent(offset, oldTextLength, newText);
		}
	}
}	

import com.crispico.flower.texteditor.utils.Util;

import flash.display.BlendMode;
import flash.display.InteractiveObject;
import flash.errors.IllegalOperationError;
import flash.events.KeyboardEvent;
import flash.utils.getQualifiedClassName;
import flash.utils.setTimeout;

import flashx.textLayout.edit.EditManager;
import flashx.textLayout.edit.ISelectionManager;
import flashx.textLayout.edit.SelectionFormat;
import flashx.textLayout.edit.SelectionState;
import flashx.textLayout.edit.TextScrap;
import flashx.textLayout.elements.GlobalSettings;
import flashx.textLayout.elements.TextFlow;
import flashx.textLayout.events.FlowOperationEvent;
import flashx.textLayout.operations.ApplyLinkOperation;
import flashx.textLayout.operations.CutOperation;
import flashx.textLayout.operations.DeleteTextOperation;
import flashx.textLayout.operations.FlowOperation;
import flashx.textLayout.operations.FlowTextOperation;
import flashx.textLayout.operations.InsertTextOperation;
import flashx.textLayout.operations.RedoOperation;
import flashx.textLayout.operations.UndoOperation;
import flashx.textLayout.tlf_internal;
import flashx.textLayout.utils.NavigationUtil;
import flashx.undo.IOperation;
import flashx.undo.UndoManager;

import spark.events.TextOperationEvent;

/**
 * Used to prevent several default behaviours (eg. focusing out of the editor when the TAB key is pressed),
 * and handles undo/redo.
 */ 
class CustomEditManager extends EditManager implements ISelectionManager {
	
	public function CustomEditManager():void {
		super(new CustomUndoManager());
	}
			
	/**
	 * Prevents creating a new paragraph after pressing ENTER.
	 * By default our scanner considers that the content will only have \n separator.
	 * 
	 * @see SyntaxTextEditor#setEndOfLineSeparator
	 */ 
	override public function splitParagraph(operationState:SelectionState = null):ParagraphElement {
		insertText('\n');
		return null;
	}
	
	/**
	 * Catches an error that is sometimes thrown after pressing the DELETE or BACKSPACE buttons.
	 */  
	override public function deletePreviousCharacter(operationState:SelectionState=null):void {
		try {
			super.deletePreviousCharacter(operationState);
		}
		catch (err:IllegalOperationError) {	}
	}
	
	override public function deleteNextCharacter(operationState:SelectionState=null):void {
		try {
			super.deleteNextCharacter(operationState);
		}
		catch (err:IllegalOperationError) {	}
	}
	
	override public function deleteNextWord(operationState:SelectionState=null):void {
		try {
			super.deleteNextWord(operationState);
		}
		catch (err:IllegalOperationError) { }
	}
	
	override public function deletePreviousWord(operationState:SelectionState=null):void {
		try {
			super.deletePreviousWord(operationState);
		}
		catch (err:IllegalOperationError) { }
	}
	
	override public function deleteText(operationState:SelectionState=null):void {
		try {
			super.deleteText(operationState);
		}
		catch (err:IllegalOperationError) { }
	}
	
	/**
	 * Prevents some unwanted behaviours on the editor, like focusing out when the TAB key is pressed
	 * (instead insert \t in the text) and only updating the text after the user releases the 
	 * BACKSPACE or DELETE key, instead of updating the display while the key is held down.
	 */ 
	 override public function keyDownHandler(evt:KeyboardEvent):void {
		try {			
			// tab handler; prevent focusing out of the editor when the TAB key is pressed; instead insert \t
			if (evt.charCode == Util.TAB) {
				insertText("\t");
			} else
				// backspace or delete handler; set a very short timeout to force redrawing after each backspace or delete
				// otherwise, if the user holds down the backspace or delete key, only the first character is deleted
				// then the display isn't updated until the user releases the key
				if (evt.charCode == Util.BACKSPACE || evt.charCode == Util.DELETE) {
					setTimeout(super.keyDownHandler, 1, evt);
				} else {
					super.keyDownHandler(evt);
				}
		}
		catch (err:Error) { }
	}
	
	/**
	 * Performs undo on the operation. Dispatches events to keep the text area
	 * in sync with the document model and trigger partitioning and coloring.
	 */ 
	override public function performUndo(theop:IOperation):void {
		var operation:FlowOperation = theop as FlowOperation;
		if ((!operation) || (operation.textFlow != textFlow))
			return;
		
		// this event will be caught in the changingHandler of SyntaxTextArea
		var opEvent:FlowOperationEvent = new FlowOperationEvent(
			FlowOperationEvent.FLOW_OPERATION_BEGIN, false, true, new UndoOperation(operation), 0, null);
		textFlow.dispatchEvent(opEvent);
		
		var opError:Error;
		try {
			// perform undo and set the selection state
			var rslt:SelectionState = operation.undo();
			setSelectionState(rslt);
			// focus to the place where undo was performed
			updateAllControllers();
			undoManager.pushRedo(operation);
		} catch (e:Error) {
			opError = e;
		}
		
		// this event will be caught in the changeHandler of SyntaxTextArea
		opEvent = new FlowOperationEvent(
			FlowOperationEvent.FLOW_OPERATION_COMPLETE, false, true, new UndoOperation(operation), 0, opError);
		textFlow.dispatchEvent(opEvent);
	} 
	
	/**
	 * Performs redo on the operation. Dispatches events to keep the text area
	 * in sync with the document model and trigger partitioning and coloring.
	 */ 
	override public function performRedo(theop:IOperation):void {
		var operation:FlowOperation = theop as FlowOperation;
		if ((!operation) || (operation.textFlow != textFlow))
			return;
		
		// this event will be caught in the changingHandler of SyntaxTextArea
		var opEvent:FlowOperationEvent = new FlowOperationEvent(
			FlowOperationEvent.FLOW_OPERATION_BEGIN, false, true, new RedoOperation(operation), 0, null);
		textFlow.dispatchEvent(opEvent);
		
		if (opEvent.isDefaultPrevented()) {
			undoManager.pushRedo(operation);
			return;
		}
		
		var opError:Error;
		try {
			// perform redo and set the selection state
			var rslt:SelectionState = operation.redo();
			setSelectionState(rslt);
			// focus to the place where the redo was performed
			updateAllControllers();
			undoManager.pushUndo(operation);
		} catch (e:Error) {
			opError = e;
		}
		
		// this event will be caught in the changeHandler of SyntaxTextArea
		opEvent = new FlowOperationEvent(
			FlowOperationEvent.FLOW_OPERATION_COMPLETE, false, true, new RedoOperation(operation), 0, opError);
		textFlow.dispatchEvent(opEvent);
	}
}

/** 
 * Prevents certain operations to be undoable (e.g. <code>ApplyLinkOperation</code>).
 */ 
class CustomUndoManager extends UndoManager {
	
	/**
	 * Due to the <code>HyperlinkManager</code>, <code>ApplyLinkOperation</code>s
	 * should not be undoable.
	 */ 
	override public function pushUndo(operation:IOperation):void {
		if (!(operation is ApplyLinkOperation)) {
			super.pushUndo(operation);
		}
	}
}

import flash.utils.Timer;
import flash.events.TimerEvent;
import mx.collections.ArrayCollection;
import org.osmf.events.TimeEvent;
import com.crispico.flower.texteditor.events.TextChangedEvent;
import com.crispico.flower.texteditor.events.BufferedTextChangesEvent;
import com.crispico.flower.texteditor.SyntaxTextArea;
import flash.text.TextDisplayMode;
import flashx.textLayout.elements.ParagraphElement;

/**
 * Collects TextChangedEvents from the SyntaxTextArea and dispatches
 * these events (as a list, not individually) at fixed intervals, 
 * so they can be applied to other paired editors.
 * 
 * <p>
 * 
 * If the aggregating mechanism is used, then consecutive events are 
 * merged to create less events, thus reducing the amount of data to
 * be sent to other editors.
 */ 
class TextChangedEventBuffer {
	
	/**
	 * A list that collects the TextChangedEvents
	 * dispatched by this editor.
	 */ 
	private var buffer:ArrayCollection;
	
	/**
	 * Controls dispatching events at a certain interval.
	 */ 
	private var timer:Timer;
	
	/**
	 * The time interval (in ms) when the buffered events
	 * are aggregated and then dispatched and the buffer is cleared.
	 * 
	 * <p>
	 * 
	 * If the interval is 0, then there is no buffering; each event 
	 * is dispatched immediately instead of being added to the buffer. 
	 */
	private var interval:Number;
	
	/**
	 * If buffering is used, events are aggregated and added to this, 
	 * list in order to be dispatched.
	 */ 
	private var aggregatedEvents:ArrayCollection;
	
	/**
	 * The SyntaxTextArea that provides the TextChangedEvents.
	 */ 
	private var textArea:SyntaxTextArea

	/**
	 * If the dirty flag is false, then updates are dispatched 
	 * immediately, to avoid the case when the user closes the 
	 * editor before modification are sent.
	 */ 
	public var dirty:Boolean = false;
	
	public function TextChangedEventBuffer(textArea:SyntaxTextArea, interval:int = 0):void {
		this.textArea = textArea;
		this.interval = interval;
		this.buffer = new ArrayCollection();
		this.aggregatedEvents = new ArrayCollection();
		initializeTimer();
	}
	
	public function setInterval(value:int):void {
		this.interval = value;
		initializeTimer();
	}
	
	private function initializeTimer():void {
		if (interval > 0) {
			timer = new Timer(interval);
			timer.addEventListener(TimerEvent.TIMER, aggregateEvents); 
			timer.start();
		}
	}
	
	/**
	 * Adds the event to the buffer, if the buffering mechanism
	 * is used; otherwise the event is dispatched immediately.
	 */ 
	public function add(evt:TextChangedEvent):void {
		buffer.addItem(evt);
		if (interval == 0 || !dirty) {
			dispatchEvents();
		}
	}
	
	/**
	 * Aggregates consecutive events in the buffer, thus creating 
	 * a list of fewer aggregated events.
	 * 
	 * <p>
	 * 
	 * Note: two events are consecutive if the one of them ends
	 * at the beginning of the other.
	 */ 
	public function aggregateEvents(evt:TimerEvent = null):void {
	  	if (buffer.length > 0) {
			aggregatedEvents = new ArrayCollection();	 		
		 	var offset:int = (buffer.getItemAt(0) as TextChangedEvent).offset;
			var oldTextLength:int = (buffer.getItemAt(0) as TextChangedEvent).oldTextLength;
			var newText:String = (buffer.getItemAt(0) as TextChangedEvent).newText;
			for (var i:int = 1; i < buffer.length; i++) {
				var prev:TextChangedEvent = buffer.getItemAt(i - 1) as TextChangedEvent;
				var crt:TextChangedEvent = buffer.getItemAt(i) as TextChangedEvent;
				
				// check if the events are consecutive
				if (crt.offset == prev.offset - prev.oldTextLength + prev.newText.length) {
					if (crt.offset < prev.offset) {
						// both edits are actually delete events
						// the aggregated event will now start at the start of the second one
						offset = crt.offset;
						oldTextLength = crt.oldTextLength + oldTextLength;
					}
					else {
						oldTextLength += crt.oldTextLength;
					}
					newText += crt.newText; 
				}
				else {
					// add the new event to the list of aggregated events
					aggregatedEvents.addItem(new TextChangedEvent(offset, oldTextLength, newText));
					offset = crt.offset;
					oldTextLength = crt.oldTextLength;
					newText = crt.newText;
				}
			}
			
			// the last event is not added in the for loop, so we add it now
			aggregatedEvents.addItem(new TextChangedEvent(offset, oldTextLength, newText)); 
		
			dispatchEvents();
	 	}  
	}
	
	/**
	 * Dispatches a BufferedTextChangesEvent that contains
	 * a list of events to be applied to other editors.
	 */ 
	private function dispatchEvents(evt:TimerEvent = null):void {
		// send the list of events only if there are any events
		if (buffer.length > 0) {
			// check if the aggregating mechanism was used to decide which of 
			// the lists will be dispatched
			var events:ArrayCollection = (interval == 0 || !dirty) ? buffer : aggregatedEvents;
			textArea.dispatchEvent(new BufferedTextChangesEvent(events));
			// clear the buffer
			buffer = new ArrayCollection();
			dirty = true;
		}
	}
}