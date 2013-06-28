package org.flowerplatform.web.explorer;

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.explorer.AbstractVirtualItemInOrganizationNodeDataProvider;

public class FileSystemNodeDataProvider extends AbstractVirtualItemInOrganizationNodeDataProvider {

	public FileSystemNodeDataProvider() {
		super();
		nodeLabel = WebPlugin.getInstance().getMessage("explorer.fileSystem");
		nodeIcon = WebPlugin.getInstance().getResourceUrl("images/folder.gif");
	}

}
