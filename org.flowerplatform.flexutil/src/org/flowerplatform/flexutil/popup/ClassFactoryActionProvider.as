package org.flowerplatform.flexutil.popup {
	import mx.collections.IList;
	
	public class ClassFactoryActionProvider implements IActionProvider {
		
		public var actionClasses:Vector.<Class> = new Vector.<Class>();
		
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			for (var i:int = 0; i < actionClasses.length; i++) {
				var action:IAction = new actionClasses[i]();
				result.push(action);
			}
			return result;
		}
	}
}