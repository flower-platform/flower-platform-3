package org.flowerplatform.web.common.explorer {
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	public class ExplorerViewProvider implements IViewProvider {

		public static const ID:String = "explorer"; 
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			var treeList:GenericTreeList = new ExplorerTreeList();
			var statefulClient:GenericTreeStatefulClient = new GenericTreeStatefulClient();
			
			treeList.dispatchEnabled = true;
			treeList.statefulClient = statefulClient;
			
			statefulClient.statefulServiceId = "explorerTreeStatefulService";
			statefulClient.clientIdPrefix = "Explorer";
			statefulClient.treeList = treeList;
			
			CommunicationPlugin.getInstance().statefulClientRegistry.register(statefulClient, null);
			return treeList;
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return "Explorer";
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return null;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}