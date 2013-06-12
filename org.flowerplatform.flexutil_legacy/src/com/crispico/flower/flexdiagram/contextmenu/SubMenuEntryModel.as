package com.crispico.flower.flexdiagram.contextmenu
{
	public class SubMenuEntryModel implements IMenuEntryModel {
		
		private var _label:String;
		
		private var _image:Object;
		
		private var _sortIndex:int = FlowerContextMenu.DEFAULT_SORT_INDEX;
		
		public function SubMenuEntryModel(image:Object, label:String, sortIndex:int = int.MAX_VALUE){
			_label = label;
			_image = image;
			_sortIndex = sortIndex;
		}
		
		public function get label():String {
			return _label;
		}
		
		public function set label(value:String):void {
			_label = value;
		}
		
		public function get image():Object {
			return _image;
		}
		
		public function set image(value:Object):void {
			_image = value;
		}
		
		public function get sortIndex():int {
			return _sortIndex;
		}
		
		public function set sortIndex(value:int):void {
			this._sortIndex = value;
		}
		
	}
}