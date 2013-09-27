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
package org.flowerplatform.web.git.common.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.web.common.ui.LoginView;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	

	/**
	 * @author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class OpenGitCredentialsWindowClientCommand extends AbstractClientCommand implements IDialogResultHandler{

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
			loginView.setResultHandler(this);
			loginView.message = GitCommonPlugin.getInstance().getMessage("git.login.message");
			loginView.repositoryURI = repositoryURI;
			loginView.user = user;
			loginView.command = command;
			loginView.logging = logging;
						
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(loginView)
				.show();
			
		}	
		
		public function handleDialogResult(result:Object):void {
			var resultInfo:ArrayCollection = new ArrayCollection();
			resultInfo.addAll(result as ArrayCollection);
			if((resultInfo.getItemAt(0) as String) != "cancel"){
				var logging:Boolean = resultInfo.getItemAt(0) as Boolean;
				
				if (logging)
					GitCommonPlugin.getInstance().service.login(
						//repositoryURI
						resultInfo.getItemAt(1) as String,
						//username
						resultInfo.getItemAt(2) as String, 
						//password
						resultInfo.getItemAt(3) as String, 
						//command
						resultInfo.getItemAt(4) as InvokeServiceMethodServerCommand);
				else
					GitCommonPlugin.getInstance().service.changeCredentials(
						//repositoryURI
						resultInfo.getItemAt(1) as String,
						//username
						resultInfo.getItemAt(2) as String, 
						//password
						resultInfo.getItemAt(3) as String);
			}
		}
	}
	
}