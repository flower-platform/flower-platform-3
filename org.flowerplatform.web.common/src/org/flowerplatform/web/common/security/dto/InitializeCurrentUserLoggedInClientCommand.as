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
package org.flowerplatform.web.common.security.dto {
	
	import org.flowerplatform.common.CommonPlugin;
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
			CommonPlugin.getInstance().handleLink(); // handle browser url
		}
		
	}
}