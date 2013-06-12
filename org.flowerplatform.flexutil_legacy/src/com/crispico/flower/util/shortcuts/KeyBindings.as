package com.crispico.flower.util.shortcuts {
	
	import com.crispico.flower.flexdiagram.action.IAction;
	
	import flash.display.Stage;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.utils.Dictionary;
	
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	/**
	 * This class binds shortcuts to actions (or functions).
	 * 
	 * This class uses a global event handler as described here:
	 * http://cookbooks.adobe.com/post_Global_keyboard_event_handling-304.html
	 * 
	 * As key down event does not work well in internet explorer version 8 or older, this uses key up event.
	 * http://stackoverflow.com/questions/9507200/why-keydown-listener-doesnt-work-in-ie
	 * 
	 * @author Florin
	 * @flowerModelElementId _gTQEQIiVEeG7SeaoTFzMGQ
	 */
	public class KeyBindings {
				
		/**
		 * @flowerModelElementId _gTQrUoiVEeG7SeaoTFzMGQ
		 */
		private var bindings:Dictionary = new Dictionary();

		public function KeyBindings():void {
			if (UIComponent(FlexGlobals.topLevelApplication).stage != null) {
				registerListener(null);
			} else {
				UIComponent(FlexGlobals.topLevelApplication).addEventListener(Event.ADDED_TO_STAGE, registerListener);
			}
		}
		
		protected function registerListener(event:Event):void {
			UIComponent(FlexGlobals.topLevelApplication).removeEventListener(Event.ADDED_TO_STAGE, registerListener);
			UIComponent(FlexGlobals.topLevelApplication).stage.addEventListener(KeyboardEvent.KEY_UP, onKeyUp);
		}
				
		/**
		 * Action can be an IAction or a Function.
		 * @flowerModelElementId _gTRSYoiVEeG7SeaoTFzMGQ
		 */
		public function registerBinding(shortcut:Shortcut, action:Object): void {
			if (!(action is IAction) && !(action is Function)) {
				throw new Error("invalid argument");
			}
			
			// check if shortcut already exists; replace it
			for (var obj:Object in bindings) {
				var registeredShortcut:Shortcut = obj as Shortcut;
				if (registeredShortcut.equals(shortcut)) {
					delete bindings[registeredShortcut];
					break;
				}
			}
			bindings[shortcut] = action;
		}

		/**
		 * @flowerModelElementId _gTSghoiVEeG7SeaoTFzMGQ
		 * @author Cristi
		 * @author Daniela
		 */
		private function onKeyUp(e:KeyboardEvent): void {
			if (!e.ctrlKey) {
				// performance related: if ctrl is not enabled, we exit; otherwise, each keypress
				// would trigger an iteration through all the shortcuts
				return;
			}
			for (var obj:Object in bindings) {
				var shortcut:Shortcut = obj as Shortcut;
				if (isShortcutActivated(shortcut, e)) {					
					if (bindings[shortcut] is IAction) {
						var action:IAction = bindings[shortcut];
						if (canRun(action)) {
							action.run(null); 
						}
					} else {
						var funct:Function = bindings[shortcut];
						funct.call(null)
					}
					// Daniela: if found one shortcut, we return because only one shortcut
					// corespunds to a KeybordEvent
					return;
				}
			}
		}
		
		/** 
		 * @flowerModelElementId _gTTHkoiVEeG7SeaoTFzMGQ
		 * @author Cristi
		 * @author Daniela
		 */
		private function isShortcutActivated(shortcut:Shortcut, e:KeyboardEvent): Boolean {
			var isActivated:Boolean = true;
			// Daniela: Logic changed because, like in eclipse, a combination ctrl/shift + a char
			// should uniquelly identify a shorcut to an action
			// The old logic for a ctrl + shift + s event will identify bouth the 
			// ctrl + s and the ctrl + shift + s shortcuts to two diffrent actions
			isActivated = isActivated && (shortcut.ctrl && e.ctrlKey || !shortcut.ctrl && ! e.ctrlKey);
			isActivated = isActivated && (shortcut.shift && e.shiftKey || !shortcut.shift && ! e.shiftKey);
			isActivated = isActivated && (shortcut.lowerCaseCode == e.charCode || shortcut.upperCaseCode == e.charCode);
			return isActivated;
		}
		
		protected function canRun(action:IAction):Boolean {
			return true;
		}

	}
}