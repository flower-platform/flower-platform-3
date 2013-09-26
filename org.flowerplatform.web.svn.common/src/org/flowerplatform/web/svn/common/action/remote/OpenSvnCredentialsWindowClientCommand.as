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
package org.flowerplatform.web.svn.common.action.remote {
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.web.common.ui.LoginView;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	
	/**
	 * @author Cristina Necula
	 */ 
	[RemoteClass]
	public class OpenSvnCredentialsWindowClientCommand extends AbstractClientCommand implements IDialogResultHandler{
		
		private static const LOGIN_STATE:int = 0;
		
		private static const CREDENTIALS_STATE:int = 1;
		
		public var logging:Boolean;
		
		public var repositoryURI:String;
		
		// FOR LOGIN STATE
		public var command:InvokeServiceMethodServerCommand;
		
		// FOR CREDENTIALS STATE
		public var user:String;
		
		//USE ONE LOGIN WINDOW FOR A REPOSITORY
		private static var openLoginViewsForRepository:Dictionary = new Dictionary();
		
		public override function execute():void {
			
			var loginView:LoginView = new LoginView();
			loginView.setResultHandler(this);
			loginView.message = SvnCommonPlugin.getInstance().getMessage("svn.login.message");
			loginView.repositoryURI = repositoryURI;
			loginView.user = user;
			loginView.command = command;
			if (openLoginViewsForRepository[repositoryURI] == null) {
				openLoginViewsForRepository[repositoryURI] = loginView;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setPopupContent(loginView)
					.show();
			}
		}	
		
		public function handleDialogResult(result:Object):void {
			
			var resultInfo:ArrayCollection = new ArrayCollection();
			resultInfo.addAll(result as ArrayCollection);
			var logging:Boolean = true;
			var repoURI:String = resultInfo.getItemAt(1) as String;
			var userName:String = resultInfo.getItemAt(2) as String;
			var password:String = resultInfo.getItemAt(3) as String;
			
			if(logging){
				var	command:InvokeServiceMethodServerCommand = resultInfo.getItemAt(4) as InvokeServiceMethodServerCommand;
				delete openLoginViewsForRepository[repositoryURI];
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand("svnService", 
						"login", [repoURI, userName, password, command], this, null));
			}
			else{
				if (openLoginViewsForRepository[repositoryURI] != null)
					delete openLoginViewsForRepository[repositoryURI];
				CommunicationPlugin.getInstance().bridge.sendObject(	
					new InvokeServiceMethodServerCommand("svnService", 
						"changeCredentials", [repoURI, userName, password], this, null));
			}
				
		}
		
		public function finishHandler(result:Object):void{
//			delete openLoginViewsForRepository[repositoryURI];
		}
	}
}