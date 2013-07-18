package org.flowerplatform.codesync {
	
	import mx.collections.IList;
	
	import org.flowerplatform.codesync.remote.CodeSyncAction;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	
	public class CodeSyncTreeActionProvider implements IActionProvider {
		
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			result.push(new CodeSyncAction("Code Sync", "java"));
			result.push(new CodeSyncAction("Wiki Sync", "github"));
			return result;
		}
	}
}