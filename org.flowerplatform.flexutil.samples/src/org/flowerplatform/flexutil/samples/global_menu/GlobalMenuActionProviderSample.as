/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation version 3.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
* 
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flexutil.samples.global_menu {
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.samples.context_menu.SampleAction1;
	import org.flowerplatform.flexutil.samples.context_menu.SampleAction2;
	import org.flowerplatform.flexutil.samples.context_menu.SampleAction3;
	
	/**
	 * Sample action provider for the GlobalMenu
	 * 
	 * @author Mircea Negreanu
	 */
	public class GlobalMenuActionProviderSample implements IActionProvider {
		protected var actions:Vector.<IAction>;
		
		public function GlobalMenuActionProviderSample() {
			// create the actions for the menu
			actions = new Vector.<IAction>();
			
			var action:ActionBase = new ComposedAction();
			action.id =  "sampleaction1";
			action.label = "Sample Action 1";
			action.parentId = "sampleaction4";
			actions.push(action);
			
			action = new SampleAction3();
			action.id = "sampleaction2";
			action.label = "Sample Action 2";
			action.parentId = "sampleaction4";
			actions.push(action);
			
			action = new ComposedAction();
			action.id = "sampleaction3";
			action.label = "Sample Action 3";
			actions.push(action);
			
			action = new ActionBase();
			action.id = "sampleaction4";
			action.label = "Sample Action 4";
			actions.push(action);
			
			action = new SampleAction2();
			action.id = "sampleaction21";
			action.label = "Sample Action 21";
			action.parentId = "sampleaction3";
			actions.push(action);
			
			action = new ComposedAction();
			action.id = "sampleaction22";
			action.label = "Sample Action 22";
			action.parentId = "sampleaction1";
			actions.push(action);
			
			action = new ActionBase();
			action.id = "sampleaction23";
			action.label = "Sample Action 23";
			action.parentId = "sampleaction1";
			actions.push(action);
			
			action = new SampleAction2();
			action.id = "sampleaction24";
			action.label = "Sample Action 24";
			action.parentId = "sampleaction22";
			actions.push(action);
			
			action = new SampleAction3();
			action.id = "sampleaction25";
			action.label = "Sample action 25";
			actions.push(action);
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			return actions;
		}
	}
}