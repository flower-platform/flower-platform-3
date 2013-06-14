package org.flowerplatform.flexutil.popup {
	import mx.collections.IList;

	public interface IActionProvider {
		function getActions(selection:IList):Vector.<IAction>;
	}
}