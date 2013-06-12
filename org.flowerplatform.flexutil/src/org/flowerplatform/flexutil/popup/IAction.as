package org.flowerplatform.flexutil.popup {
	public interface IAction {
		function get orderIndex():int;
		function get preferShowOnActionBar():Boolean;
		function get visible():Boolean;
		function get enabled():Boolean;
		function get label():String;
		function run():void;
	}
}