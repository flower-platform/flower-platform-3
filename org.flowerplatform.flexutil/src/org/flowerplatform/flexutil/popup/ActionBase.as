package org.flowerplatform.flexutil.popup {
	import mx.messaging.AbstractConsumer;

	public class ActionBase implements IAction {

		private var _orderIndex:int;
		private var _preferShowOnActionBar:Boolean;
		private var _visible:Boolean = true;
		private var _enabled:Boolean = true;
		private var _label:String;
		
		public function get orderIndex():int
		{
			return _orderIndex;
		}

		public function set orderIndex(value:int):void
		{
			_orderIndex = value;
		}

		public function get preferShowOnActionBar():Boolean
		{
			return _preferShowOnActionBar;
		}

		public function set preferShowOnActionBar(value:Boolean):void
		{
			_preferShowOnActionBar = value;
		}

		public function get visible():Boolean
		{
			return _visible;
		}

		public function set visible(value:Boolean):void
		{
			_visible = value;
		}

		public function get enabled():Boolean
		{
			return _enabled;
		}

		public function set enabled(value:Boolean):void
		{
			_enabled = value;
		}

		public function get label():String
		{
			return _label;
		}

		public function set label(value:String):void
		{
			_label = value;
		}
		
		public function run():void {
		}
		
	}
}