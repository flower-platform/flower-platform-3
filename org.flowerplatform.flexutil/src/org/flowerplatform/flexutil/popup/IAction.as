package org.flowerplatform.flexutil.popup {
	import mx.collections.IList;

	public interface IAction {
		function get id():String;
		function get parentId():String;
		function get orderIndex():int;
		function get preferShowOnActionBar():Boolean;
		function get icon():Object;
		function get label():String;
		
		/**
		 * The system sets this before invoking: visible, enabled, run(). After
		 * these invocations, the selection is removed in a try/finally block
		 */
		function set selection(value:IList):void;

		/**
		 * Before this method is invoked, the <code>selection</code> property is populated.
		 * After this method call, the selection is removed.
		 */
		function get visible():Boolean;

		/**
		 * Before this method is invoked, the <code>selection</code> property is populated.
		 * After this method call, the selection is removed.
		 */
		function get enabled():Boolean;
		
		/**
		 * Before this method is invoked, the <code>selection</code> property is populated.
		 * After this method call, the selection is removed.
		 */
		function run():void;
	}
}