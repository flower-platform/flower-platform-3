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
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.samples.renderer.MultipleIconItemRendererSample;
	
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
			
			// first level (menu bar)
			var action:ActionBase = new SampleActionGlobalMenuC1();
			action.label = "First Action";
			action.id = "firstaction";
			action.icon = MultipleIconItemRendererSample.infoImage;
			actions.push(action);
			
			action = new SampleActionGlobalMenuC1();
			action.id = "secondaction";
			SampleActionGlobalMenuC1(action).disableFor = "Flash";
			actions.push(action);
			
			// second level
			action = new SampleActionGlobalMenu1();
			action.label = "First SubAction";
			action.id = "firstsubaction";
			action.parentId = "firstaction";
			actions.push(action);
			
			action = new SampleActionGlobalMenuC1();
			action.label = "Second SubAction";
			action.id = "secondsubaction";
			action.parentId = "secondaction";
			action.icon = MultipleIconItemRendererSample.defaultImage;
			actions.push(action);

			action = new SampleActionGlobalMenu1();
			action.label = "Third SubAction";
			action.id = "thirdsubaction";
			action.parentId = "secondaction";
			SampleActionGlobalMenu1(action).hideOn = "ColdFusion";
			actions.push(action);

			// Third level (only for Second SubAction)
			action = new SampleActionGlobalMenu1();
			action.label = "Second first SubAction";
			action.id = "secondfirstsubaction";
			action.parentId = "secondsubaction";
			actions.push(action);

			action = new SampleActionGlobalMenu1();
			action.id = "secondsecondsubaction";
			action.parentId = "secondsubaction";
			action.icon = MultipleIconItemRendererSample.defaultImage;
			actions.push(action);
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			return actions;
		}
	}
}