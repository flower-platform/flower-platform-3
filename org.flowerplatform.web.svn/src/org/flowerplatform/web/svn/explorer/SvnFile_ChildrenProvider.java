package org.flowerplatform.web.svn.explorer;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.team.core.TeamException;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.svn.SvnNodeType;
import org.flowerplatform.web.svn.SvnPlugin;
import org.flowerplatform.web.svn.remote.SvnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.ISVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.resources.RemoteFile;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;
import org.tigris.subversion.svnclientadapter.SVNClientException;



/**
 * Parent node = Svn Repository virtual node (i.e. Pair<File, String>).<br/>
 * Child node = svn file or svn folder
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _UcYSoP3LEeKrJqcAep-lCg
 */
public class SvnFile_ChildrenProvider implements IChildrenProvider {
	
	private static Logger logger = LoggerFactory.getLogger(SvnFile_ChildrenProvider.class);

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node,	TreeNode treeNode, GenericTreeContext context) {		
		InvokeServiceMethodServerCommand command = (InvokeServiceMethodServerCommand)context.get("clientCommandKey");
		command.getParameters().remove(0);
		SvnService.tlCommand.set(command);
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		if (node instanceof ISVNRepositoryLocation || node instanceof RemoteFolder){
			try {		
				ISVNRemoteResource[] children;
				if (node instanceof ISVNRepositoryLocation) {
					children = ((SVNRepositoryLocation) node).members(null);
				} else {
					children = ((RemoteFolder) node).members(null);
				}
				for (ISVNRemoteResource child : children) {
					String nodeType = SvnNodeType.NODE_TYPE_FILE;
					result.add(new Pair<Object, String>(child, nodeType));
				}				
			} catch (TeamException e) {	
				CommunicationPlugin.getInstance().tlCurrentChannel.get().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(CommonPlugin.getInstance().getMessage("error"), 
								e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
//				if ( (e.getStatus().getMessage() != null && (e.getStatus().getMessage().indexOf(SVNClientException.OPERATION_INTERRUPTED) != -1)
//						&& e.getStatus().getMessage().indexOf(SVNException.NOT_AUTHORIZED) != -1))
//					return result;
//				if (e.getCause() == null)
//					return result;
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);				
			}
		}
		return result;
	}

	/**
	 * @flowerModelElementId _z6dDEP3LEeKrJqcAep-lCg
	 */

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode,
			GenericTreeContext context) {		
		if(node instanceof RemoteFile) {			
			treeNode.getOrCreateCustomData().put(SvnPlugin.TREE_NODE_KEY_IS_FOLDER, false);
			return false;
		}
		if(node instanceof RemoteFolder)
			treeNode.getOrCreateCustomData().put(SvnPlugin.TREE_NODE_KEY_IS_FOLDER, true);
		return true;				
	}

}