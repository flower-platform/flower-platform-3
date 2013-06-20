package org.flowerplatform.web.common.security.dto {
	
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Mariana
	 */
	[RemoteClass(alias="org.flowerplatform.web.security.dto.InitializeCurrentUserLoggedInClientCommand")]
	public class InitializeCurrentUserLoggedInClientCommand extends AbstractClientCommand {
		
		public var user:User_CurrentUserLoggedInDto;
		
		override public function execute():void {
			WebCommonPlugin.getInstance().authenticationManager.currentUserLoggedIn = user;
		}
		
	}
}