package org.flowerplatform.web.git.common {
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;
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