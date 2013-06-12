package com.crispico.flower.util.shortcuts {
	
	import mx.controls.Alert;

	/**
	 * @author Florin
	 * @author Cristi
	 */
	public class Shortcut {
	
		public var ctrl:Boolean; // 17 
		
		public var shift:Boolean; // 16
		
		public var lowerCaseCode:int;
		
		public var upperCaseCode:int;
		
		public function Shortcut(ctrl: Boolean, shift: Boolean, key:String) {
			this.ctrl = ctrl;
			this.shift = shift;
			this.lowerCaseCode = key.charCodeAt(0);
			this.upperCaseCode = key.toUpperCase().charCodeAt(0);
		}   

		public function equals(other:Shortcut):Boolean {
			return this.ctrl == other.ctrl && this.shift == other.shift && this.lowerCaseCode == other.lowerCaseCode;
		}
		
	}
}