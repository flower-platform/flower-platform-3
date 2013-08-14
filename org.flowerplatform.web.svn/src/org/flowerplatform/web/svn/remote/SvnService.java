package org.flowerplatform.web.svn.remote;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.svn.SvnPlugin;
import org.tigris.subversion.subclipse.core.ISVNRemoteFolder;
import org.tigris.subversion.subclipse.core.SVNException;

/**
 * @flowerModelElementId _bcYyQAMcEeOrJqcAep-lCg
 */
public class SvnService {
	/**
	 * @flowerModelElementId _yaKVkAMcEeOrJqcAep-lCg
	 */

	public boolean createRemoteFolder(ServiceInvocationContext context,
			List<PathFragment> parentPath, String folderName, String comment) {

		Object selectedParent = GenericTreeStatefulService.getNodeByPathFor(
				parentPath, null);

		ISVNRemoteFolder parentFolder = null;
		if (selectedParent instanceof ISVNRemoteFolder) {
			parentFolder = (ISVNRemoteFolder) selectedParent;
		} else if (selectedParent instanceof IAdaptable) {
			// ISVNRepositoryLocation is adaptable to ISVNRemoteFolder
			IAdaptable a = (IAdaptable) selectedParent;
			Object adapter = a.getAdapter(ISVNRemoteFolder.class);
			parentFolder = (ISVNRemoteFolder) adapter;
		}

		try {

			// create remote folder

			parentFolder.createRemoteFolder(folderName, comment,
					new NullProgressMonitor());

		} catch (SVNException e) { // something wrong happened

			CommunicationChannel channel = (CommunicationChannel) context
					.getCommunicationChannel();

			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}

		return true;

	}
}