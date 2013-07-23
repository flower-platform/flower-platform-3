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
package org.flowerplatform.web.git.history {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.web.git.history.remote.dto.HistoryEntryDto;
	import org.flowerplatform.web.git.history.remote.dto.HistoryViewInfoDto;
	
	/**
	 *	@author Cristina Constantinescu
	 */ 
	public class GitHistoryClient {
		
		public static const SERVICE_ID:String = "GitHistoryService";
		
		public static var INSTANCE:GitHistoryClient = new GitHistoryClient();
					
		public function getObjectInfo(info:HistoryViewInfoDto, 
									  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			CommunicationPlugin.getInstance().bridge.sendObject(new InvokeServiceMethodServerCommand(
				SERVICE_ID, 
				"getObjectInfo", 
				[info], 
				resultCallbackObject, resultCallbackFunction));
		}
		
		public function getLogEntries(info:HistoryViewInfoDto, 
									  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			CommunicationPlugin.getInstance().bridge.sendObject(new InvokeServiceMethodServerCommand(
				SERVICE_ID, 
				"getLogEntries", 
				[info], 
				resultCallbackObject, resultCallbackFunction));
		}	
		
		public function getCommitFileDiffs(entry:HistoryEntryDto, info:HistoryViewInfoDto,  
									  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			CommunicationPlugin.getInstance().bridge.sendObject(new InvokeServiceMethodServerCommand(
				SERVICE_ID, 
				"getCommitFileDiffs", 
				[entry, info], 
				resultCallbackObject, resultCallbackFunction));
		}
				
		public function getCommitMessage(entry:HistoryEntryDto, repositoryLocation:String,
									  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			CommunicationPlugin.getInstance().bridge.sendObject(new InvokeServiceMethodServerCommand(
				SERVICE_ID, 
				"getCommitMessage", 
				[entry, repositoryLocation], 
				resultCallbackObject, resultCallbackFunction));
		}
	}
	
}