package org.flowerplatform.web.git.common.remote.dto {
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	[Bindable]
	public class ProjectDto	{
	
		public var name:String;
		
		public var image:String;
		
		public var location:String;
		
		private var _selected:Boolean;
		
		public function getSelected():Boolean {
			return _selected;
		}
		
		public function setSelected(value:Boolean):void {
			this._selected = value;
		}
		
	}
}