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
package org.flowerplatform.web.git.common {
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.web.git.common.action.ChangeCredentialsAction;
	import org.flowerplatform.web.git.common.action.CheckoutAction;
	import org.flowerplatform.web.git.common.action.ClearCredentialsAction;
	import org.flowerplatform.web.git.common.action.CloneAction;
	import org.flowerplatform.web.git.common.action.CommitAction;
	import org.flowerplatform.web.git.common.action.ConfigBranchAction;
	import org.flowerplatform.web.git.common.action.ConfigRemoteAction;
	import org.flowerplatform.web.git.common.action.CreateRemoteAction;
	import org.flowerplatform.web.git.common.action.DeleteBranchAction;
	import org.flowerplatform.web.git.common.action.DeleteRemoteAction;
	import org.flowerplatform.web.git.common.action.DeleteRepositoryAction;
	import org.flowerplatform.web.git.common.action.FetchAction;
	import org.flowerplatform.web.git.common.action.ImportProjectsAction;
	import org.flowerplatform.web.git.common.action.MergeAction;
	import org.flowerplatform.web.git.common.action.PullAction;
	import org.flowerplatform.web.git.common.action.PushAction;
	import org.flowerplatform.web.git.common.action.RebaseAction;
	import org.flowerplatform.web.git.common.action.ResetAction;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GitExplorerTreeActionProvider implements IActionProvider {
		
		public function GitExplorerTreeActionProvider() {
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			var actions:Vector.<IAction> = new Vector.<IAction>();
			actions.push(new CloneAction());
			actions.push(new DeleteRepositoryAction());
			
			actions.push(new MergeAction());
			actions.push(new RebaseAction());
			actions.push(new ResetAction());
			actions.push(new PullAction());
			
			actions.push(new CreateRemoteAction());
			actions.push(new ConfigRemoteAction());
			actions.push(new DeleteRemoteAction());
			
			actions.push(new ChangeCredentialsAction());
			actions.push(new ClearCredentialsAction());
			
//			actions.push(new FetchAction());
			actions.push(new FetchAction(true));
			
//			actions.push(new PushAction());
			actions.push(new PushAction(true));
			
			actions.push(new CheckoutAction());
			actions.push(new ConfigBranchAction());
			actions.push(new DeleteBranchAction());
			
			actions.push(new ImportProjectsAction());
			actions.push(new CommitAction());

			return actions;
		}
	}
	
}