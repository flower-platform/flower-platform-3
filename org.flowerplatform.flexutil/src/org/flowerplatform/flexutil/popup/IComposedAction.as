package org.flowerplatform.flexutil.popup {
	public interface IComposedAction extends IAction {
		function get childActions():Vector.<IAction>;
		function set childActions(value:Vector.<IAction>):void;
	}
}