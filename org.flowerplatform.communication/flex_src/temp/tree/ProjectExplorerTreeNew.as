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