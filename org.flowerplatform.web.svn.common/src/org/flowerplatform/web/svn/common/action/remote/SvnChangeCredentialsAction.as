package org.flowerplatform.web.svn.common.action.remote {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.common.ui.LoginView;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.action.OpenSvnCredentialsWindowClientCommand;

	public class SvnChangeCredentialsAction extends ActionBase implements IDialogResultHandler{
		
		public var repo:String; 
		
		private static const LOGIN_STATE:int = 0;
		
		private static const CREDENTIALS_STATE:int = 1;
		
		public var logging:Boolean;
		
		public var repositoryURI:String;
		
		// FOR LOGIN STATE
		public var command:InvokeServiceMethodServerCommand;
		
		// FOR CREDENTIALS STATE
		public var user:String;
		
		public var selectedNode:TreeNode = new TreeNode();
		
		public function SvnChangeCredentialsAction() {
			label = WebCommonPlugin.getInstance().getMessage("git.action.changeCredentials.label");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/permission.png");
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type 
					== SvnCommonPlugin.NODE_TYPE_REPOSITORY;
			}
			return false;
		}
		
		override public function run():void {
			selectedNode = selection.getItemAt(0) as TreeNode;
			repo = selectedNode.pathFragment.name;
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("svnService", 
					"getCredentials", [selectedNode.getPathForNode(true)], this, resultClallbackFunction));	
			
		}
		
		public function resultClallbackFunction(result:ArrayCollection):void{
			
			var loginView:LoginView = new LoginView();		
			loginView.setResultHandler(this);
			loginView.command = command;
			if(result == null){
				loginView.repositoryURI = repo;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setPopupContent(loginView)
					.show();
			}
			else {
				
				loginView.repositoryURI = result.getItemAt(0) as String;
				loginView.user = result.getItemAt(1) as String;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setPopupContent(loginView)
					.show();
			}
		}
		
		public function handleDialogResult(result:Object):void {
			
			var resultInfo:ArrayCollection = new ArrayCollection();
			resultInfo.addAll(result as ArrayCollection);
			var logging:Boolean = resultInfo.getItemAt(0) as Boolean;
			var repoURI:String = resultInfo.getItemAt(1) as String;
			var userName:String = resultInfo.getItemAt(2) as String;
			var password:String = resultInfo.getItemAt(3) as String;
			
			if(logging){
//				var command:InvokeServiceMethodServerCommand = new InvokeServiceMethodServerCommand();
//				command.serviceId = this.id;// = resultInfo.getItemAt(4) as InvokeServiceMethodServerCommand;
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand("svnService", 
						"login", [repoURI, userName, password], this, null));
			}
			else
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand("svnService", 
						"changeCredentials", [repoURI, userName, password], this, null));
		}
		
	}
}