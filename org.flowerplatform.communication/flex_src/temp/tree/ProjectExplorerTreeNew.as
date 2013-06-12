package temp.tree {
	import org.flowerplatform.communication.CommunicationPlugin;
	
	import temp.tree.remote.GenericTreeStatefulClientOld;

	public class ProjectExplorerTreeNew extends GenericTree {
		
		public function ProjectExplorerTreeNew() {
			serviceId = "ProjectExplorerTreeStatefulService";
			clientIdPrefix = "Project Explorer";
			
			requestInitialDataAutomatically = true;
			dispatchEnabled = true;
			statefulClient = new GenericTreeStatefulClientOld();
			statefulClient.genericTree = this;
			CommunicationPlugin.getInstance().statefulClientRegistry.register(statefulClient, null);
//			dragAndDropEnabled = true;
//			
//			labelField = "labelWithDirtyMarker";
//			
//			//TODO : Temporary code (see #6777)
//			var treeContext:Object = new Object();
//			treeContext[ResourceTree.ORGANIZATION_FILTER_KEY] = FilterByOrganizationAction.filter;
//			context = treeContext;
//			
//			this.addEventListener("rightClick", rightClickHandlerForItemSelection);
//			
//			actions.addItem(new ProjectExplorer_UnsubscribeFromEditableResourceAction());
//			actions.addItem(new ProjectExplorer_GoToOpenResourcesViewAction());
		}
	}
}