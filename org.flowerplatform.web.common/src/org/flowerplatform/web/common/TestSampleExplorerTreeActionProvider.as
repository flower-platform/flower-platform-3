package org.flowerplatform.web.common {
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.ComposedAction;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	
	public class TestSampleExplorerTreeActionProvider implements IActionProvider {
		
		public function getActions(selection:IList):Vector.<IAction> {
			var actions:Vector.<IAction> = new Vector.<IAction>();
			var action:ActionBase;
			
			action = new ActionBase();
			action.label = "Disabled";
			action.enabled = false;
			actions.push(action);
			
			action = new ActionBase();
			action.label = "Disabled/top";
//			action.preferShowOnActionBar = true;
			action.enabled = false;
			actions.push(action);
			
			action = new ActionBase();
			action.label = "Action 1";
			actions.push(action);
			
			action = new ComposedAction();
			action.id = "p1";
			action.label = "Action 2 (with children)";
			actions.push(action);
			
			
			action = new ActionBase();
			action.label = "Action 3";
			actions.push(action);
			
			action = new ActionBase();
			action.parentId = "p1";
			action.label = "Sub Action 1";
			actions.push(action);
			
			action = new ActionBase();
			action.parentId = "p1";
			action.label = "Sub Action 2";
			actions.push(action);
			
			action = new ActionBase();
			action.parentId = "p1";
			action.label = "Sub Action 3";
			actions.push(action);
			
			action = new ComposedAction();
			action.id = "p2";
			action.label = "Action 4 (with children)";
			actions.push(action);
			
			action = new ActionBase();
			action.parentId = "p2";
			action.label = "Sub Action 1";
			actions.push(action);
			
//					action = new ActionBase();
//					action.parentId = "p2";
//					action.label = "Sub Action 2";
//					actions.push(action);
//					
//					action = new ActionBase();
//					action.parentId = "p2";
//					action.label = "Sub Action 3";
//					actions.push(action);
			return actions;
		}
	}
}