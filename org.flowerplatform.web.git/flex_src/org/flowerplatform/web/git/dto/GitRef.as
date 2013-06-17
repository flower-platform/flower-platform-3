package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	import mx.controls.List;
	
	/**
	 *	@author Cristina Constantinescu
	 */   
	[RemoteClass]
	[Bindable]
	public class GitRef {
		
		public var name:String;
		
		public var shortName:String;
		
		public var label:String;
		
		public var upstream:String;
		
		public var image:String;
			
		private var _selected:Boolean = false;
		
		public function getSelected():Boolean {
			return _selected;
		}
		
		public function setSelected(value:Boolean):void {
			this._selected = value;
		}
	}
}