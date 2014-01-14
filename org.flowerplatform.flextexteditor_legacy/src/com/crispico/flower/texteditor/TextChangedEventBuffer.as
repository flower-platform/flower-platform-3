package com.crispico.flower.texteditor {
	
	import com.crispico.flower.texteditor.SyntaxTextArea;
	import com.crispico.flower.texteditor.events.BufferedTextChangesEvent;
	import com.crispico.flower.texteditor.events.TextChangedEvent;
	
	import flash.events.IEventDispatcher;
	import flash.events.TimerEvent;
	import flash.text.TextDisplayMode;
	import flash.utils.Timer;
	
	import flashx.textLayout.elements.ParagraphElement;
	
	import mx.collections.ArrayCollection;
	
	import org.osmf.events.TimeEvent;
	
	import spark.core.IDisplayText;
	
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
	public class TextChangedEventBuffer {
		
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
		private var eventDispatcher:IEventDispatcher;
		
		/**
		 * If the dirty flag is false, then updates are dispatched 
		 * immediately, to avoid the case when the user closes the 
		 * editor before modification are sent.
		 */ 
		public var dirty:Boolean = false;
		
		public function TextChangedEventBuffer(eventDispatcher:IEventDispatcher, interval:int = 0):void {
			this.eventDispatcher = eventDispatcher;
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
				eventDispatcher.dispatchEvent(new BufferedTextChangesEvent(events));
				// clear the buffer
				buffer = new ArrayCollection();
				dirty = true;
			}
		}
	}
}