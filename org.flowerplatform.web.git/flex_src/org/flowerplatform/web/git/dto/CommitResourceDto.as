package  org.flowerplatform.web.git.dto {
		
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	[Bindable]
	public class CommitResourceDto {
		
		public static const ADDED:int = 0;
		public static const CHANGED:int = 1;
		public static const REMOVED:int = 2;
		public static const CONFLICTING:int = 3;
		public static const MODIFIED:int = 4;
		public static const UNTRACKED:int = 5;
		public static const MISSING:int = 6;
		
		public var path:String;
		
		public var label:String;
		
		public var image:String;
		
		public var state:int;
		
		private var _selected:Boolean;
		
		public function getSelected():Boolean {
			return _selected;
		}
		
		public function setSelected(value:Boolean):void {
			this._selected = value;
		}
				
	}
}