package org.flowerplatform.web.git.staging.communication {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.git.staging.GitStagingView;
	import org.flowerplatform.web.git.staging.dto.StagingViewInfoDto;

	/**
	 *	@author Cristina Constantinescu
	 */
	public class GitStagingStatefulClient extends StatefulClient {
		
		private var stagingView:GitStagingView;
		
		public function GitStagingStatefulClient(view:GitStagingView) {
			stagingView = view;
		}
		
		///////////////////////////////////////////////////////////////
		// Normal methods
		///////////////////////////////////////////////////////////////
		
		public override function getStatefulClientId():String {
			return "Git Staging";
		}
		
		public override function getCurrentStatefulClientLocalState(dataFromRegistrator:Object = null):IStatefulClientLocalState {			
			var localState:GitStagingStatefulClientLocalState = new GitStagingStatefulClientLocalState();
			localState.repositoryLocation = stagingView.info.repositoryLocation;
			return localState;
		}
		
		public override function getStatefulServiceId():String {				
			return "GitStagingStatefulService";
		}
		
		protected override function removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully():void {
			FlexUtilGlobals.getInstance().workbench.closeView(stagingView);
		}
		
		public function getViewInfo(info:StagingViewInfoDto,
									resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"getViewInfo", 
				[info], 
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}
				
		public function addToIndex(files:ArrayCollection, 
								   resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"addToIndex",
				[files],
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}
		
		public function removeFromIndex(files:ArrayCollection,
								   resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod( 
				"removeFromIndex",
				[files],
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}
		
	}
}