package org.flowerplatform.web.git {
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	import org.flowerplatform.web.git.action.ChangeCredentialsAction;
	import org.flowerplatform.web.git.action.CheckoutAction;
	import org.flowerplatform.web.git.action.ClearCredentialsAction;
	import org.flowerplatform.web.git.action.CloneAction;
	import org.flowerplatform.web.git.action.CommitAction;
	import org.flowerplatform.web.git.action.ConfigRemoteAction;
	import org.flowerplatform.web.git.action.CreateRemoteAction;
	import org.flowerplatform.web.git.action.DeleteRemoteAction;
	import org.flowerplatform.web.git.action.DeleteRepositoryAction;
	import org.flowerplatform.web.git.action.FetchAction;
	import org.flowerplatform.web.git.action.ImportProjectsAction;
	import org.flowerplatform.web.git.action.MergeAction;
	import org.flowerplatform.web.git.action.PushAction;
	import org.flowerplatform.web.git.action.RebaseAction;
	import org.flowerplatform.web.git.action.ResetAction;
	
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
			
			actions.push(new CreateRemoteAction());
			actions.push(new ConfigRemoteAction());
			actions.push(new DeleteRemoteAction());
			
			actions.push(new ChangeCredentialsAction());
			actions.push(new ClearCredentialsAction());
			
			actions.push(new FetchAction());
			actions.push(new FetchAction(true));
			
			actions.push(new PushAction());
			actions.push(new PushAction(true));
			
			actions.push(new CheckoutAction());
			actions.push(new ImportProjectsAction());
			actions.push(new CommitAction());
			return actions;
		}
	}
	
}