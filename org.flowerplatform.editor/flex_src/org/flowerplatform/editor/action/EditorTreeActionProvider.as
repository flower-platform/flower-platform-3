package org.flowerplatform.editor.action {
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;

	public class EditorTreeActionProvider implements IActionProvider{
		
		protected var openAction:OpenAction = new OpenAction();
		
		public function EditorTreeActionProvider() {
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			result.push(openAction);
			return result;
		}
		
	}
}