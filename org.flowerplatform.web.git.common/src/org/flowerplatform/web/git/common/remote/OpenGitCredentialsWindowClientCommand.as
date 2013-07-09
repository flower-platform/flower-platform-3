package org.flowerplatform.web.git.common.remote {
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.LoginView;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class OpenGitCredentialsWindowClientCommand extends AbstractClientCommand {

		private static const LOGIN_STATE:int = 0;
		
		private static const CREDENTIALS_STATE:int = 1;
				
		public var logging:Boolean;
		
		public var repositoryURI:String;
		
		// FOR LOGIN STATE
		public var command:InvokeServiceMethodServerCommand;
		
		// FOR CREDENTIALS STATE
		public var user:String;
		
		public override function execute():void {
			var loginView:LoginView = new LoginView();			
			loginView.repositoryURI = repositoryURI;
			loginView.user = user;
			loginView.command = command;
			loginView.logging = logging;
						
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(loginView)
				.show();
		}		
	}
	
}