package org.flowerplatform.web.git.command.client {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.ui.GitLoginWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class OpenGitCredentialsWindowClientCommand extends AbstractClientCommand {

		private static const LOGIN_STATE:int = 0;
		
		private static const CREDENTIALS_STATE:int = 1;
				
		public var state:int;
		
		public var repositoryURI:String;
		
		// FOR LOGIN STATE
		public var command:InvokeServiceMethodServerCommand;
		
		// FOR CREDENTIALS STATE
		public var user:String;
		
		public override function execute():void {
			var popup:GitLoginWindow = new GitLoginWindow();
			popup.repositoryURI = repositoryURI;
			popup.user = user;
			
			if (state == LOGIN_STATE) {				
				popup.title = GitPlugin.getInstance().getMessage('git.login.title');
				popup.pageHandler.executeHandler = execute_login;			
			} else {
				popup.title = GitPlugin.getInstance().getMessage('git.changeCredentials.title');
				popup.pageHandler.executeHandler = execute_changeCredentials;			
			}
			popup.showPopup();	
		}
		
		private function execute_login(user:String, password:String):void {
			GitPlugin.getInstance().service.login(repositoryURI, user, password, command);
		}		
		
		private function execute_changeCredentials(user:String, password:String):void {
			GitPlugin.getInstance().service.changeCredentials(repositoryURI, user, password);
		}		
	}
	
}