package org.flowerplatform.web.git.history {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.web.git.history.dto.HistoryEntryDto;
	import org.flowerplatform.web.git.history.dto.HistoryViewInfoDto;
	
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