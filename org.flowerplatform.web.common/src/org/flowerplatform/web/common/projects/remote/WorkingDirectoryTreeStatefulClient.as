package org.flowerplatform.web.common.projects.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class WorkingDirectoryTreeStatefulClient extends GenericTreeStatefulClient {
		
		public static const WORKING_DIRECTORY_KEY:String = "workingDirectory";
		public static const ORGANIZATION_KEY:String = "organization";
		
		public function WorkingDirectoryTreeStatefulClient() {
			super();
			
			clientIdPrefix = "Working Directory";
			requestDataOnSubscribe = false;			
			statefulServiceId = "workingDirectoryTreeStatefulService";
		}		
						
	}
}