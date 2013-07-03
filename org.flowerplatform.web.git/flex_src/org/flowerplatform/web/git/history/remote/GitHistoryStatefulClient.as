package org.flowerplatform.web.git.history.remote {
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.web.git.history.GitHistoryView;
	import org.flowerplatform.web.git.history.remote.dto.HistoryEntryDto;
	import org.flowerplatform.web.git.history.remote.dto.HistoryViewInfoDto;
	
	/**
	 *	@author Cristina Constantinescu
	 */   
	public class GitHistoryStatefulClient extends StatefulClient {
		
		private var historyView:GitHistoryView;
		
		public function GitHistoryStatefulClient(view:GitHistoryView) {
			historyView = view;
		}
		
		///////////////////////////////////////////////////////////////
		// Normal methods
		///////////////////////////////////////////////////////////////
		
		public override function getStatefulClientId():String {
			return "Git History";
		}
		
		public override function getCurrentStatefulClientLocalState(dataFromRegistrator:Object = null):IStatefulClientLocalState {			
			var localState:GitHistoryStatefulClientLocalState = new GitHistoryStatefulClientLocalState();
			localState.info = HistoryViewInfoDto(historyView.info);
			
			return localState;
		}
		
		public override function getStatefulServiceId():String {				
			return "GitHistoryStatefulService";
		}
		
		protected override function removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully():void {
			//WebPlugin.getInstance().workbench.closeView(historyView);
		}
		
		public function getObjectInfo(info:HistoryViewInfoDto, 
									  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"getObjectInfo", 
				[info], 
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}
		
		public function getLogEntries(info:HistoryViewInfoDto, 
									  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"getLogEntries", 
				[info], 
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}	
		
		public function getCommitFileDiffs(entry:HistoryEntryDto, info:HistoryViewInfoDto,  
										   resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod( 
				"getCommitFileDiffs", 
				[entry, info], 
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}
		
		public function getCommitMessage(entry:HistoryEntryDto, repositoryLocation:String,
										 resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"getCommitMessage", 
				[entry, repositoryLocation], 
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}
		
//		public function checkout(entry:HistoryEntryDto, repositoryLocation:String,
//								 resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
//			invokeServiceMethod(
//				"checkout", 
//				[entry, repositoryLocation], 
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function cherryPick(entry:HistoryEntryDto, repositoryLocation:String,
//								   resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
//			invokeServiceMethod(
//				"cherryPick", 
//				[entry, repositoryLocation], 
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function revert(entry:HistoryEntryDto, repositoryLocation:String,
//							   resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
//			invokeServiceMethod(
//				"revert", 
//				[entry, repositoryLocation], 
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function populate_createBranchPage(entry:HistoryEntryDto, repositoryLocation:String, 
//												  resultCallbackObject:Object, resultCallbackFunction:Function):void {
//			invokeServiceMethod(
//				"populate_createBranchPage",
//				[entry, repositoryLocation],
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function createBranch(entry:HistoryEntryDto, repositoryLocation:String, name:String, basedOn:GitRef, checkout:Boolean, 
//									 resultCallbackObject:Object, resultCallbackFunction:Function):void {
//			invokeServiceMethod(
//				"createBranch",
//				[entry, repositoryLocation, name, basedOn, checkout],
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function populate_createTagPage(entry:HistoryEntryDto, repositoryLocation:String,
//											   resultCallbackObject:Object, resultCallbackFunction:Function):void {
//			invokeServiceMethod(
//				"populate_createTagPage",
//				[entry, repositoryLocation],
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function createTag(entry:HistoryEntryDto, repositoryLocation:String, name:String, message:String, commit:CommitDto, 
//								  resultCallbackObject:Object, resultCallbackFunction:Function):void {
//			invokeServiceMethod(
//				"createTag",
//				[entry, repositoryLocation, name, message, commit],
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function reset(entry:HistoryEntryDto, repositoryLocation:String, type:int,
//							  resultCallbackObject:Object = null, resultCallbackFunction:Function = null):void {
//			invokeServiceMethod(
//				"reset",
//				[entry, repositoryLocation, type],
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function populate_pushCommitPage(repositoryLocation:String,
//												resultCallbackObject:Object, resultCallbackFunction:Function):void {
//			invokeServiceMethod(
//				"populate_pushCommitPage",
//				[repositoryLocation],
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function pushCommit(entry:HistoryEntryDto, repositoryLocation:String, remoteConfig:RemoteConfig, target:String, isForceUpdate:Boolean, 
//								   resultCallbackObject:Object, resultCallbackFunction:Function):void {
//			invokeServiceMethod( 
//				"pushCommit",
//				[entry, repositoryLocation, remoteConfig, target, isForceUpdate],
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function merge(entry:HistoryEntryDto, repositoryLocation:String,
//							   resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
//			invokeServiceMethod(
//				"merge", 
//				[entry, repositoryLocation], 
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
//		public function rebase(entry:HistoryEntryDto, repositoryLocation:String,
//							   resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
//			invokeServiceMethod(
//				"rebase", 
//				[entry, repositoryLocation], 
//				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
//		}
//		
		///////////////////////////////////////////////////////////////
		//@RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		
		[RemoteInvocation]
		public function refresh(info:HistoryViewInfoDto):void {
			historyView.info = info;
			historyView.refresh();
		}
		
	}
}