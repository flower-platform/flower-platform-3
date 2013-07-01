package org.flowerplatform.web.explorer;

import org.flowerplatform.web.WebPlugin;

public class FileSystemNodeDataProvider extends AbstractVirtualItemInOrganizationNodeDataProvider {

	public FileSystemNodeDataProvider() {
		super();
		nodeInfo.put(FileSystem_OrganizationChildrenProvider.NODE_TYPE_FILE_SYSTEM, 
				new String[] {
					WebPlugin.getInstance().getMessage("explorer.fileSystem"), 
					WebPlugin.getInstance().getResourceUrl("images/folder.gif")});		
	}

}
