package org.flowerplatform.web.svn.explorer;

import java.io.File;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.svn.SvnPlugin;
import org.flowerplatform.web.svn.remote.SvnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.SVNException;


/**
 * 
 * @author Victor Badila 
 */
public class FSFile_SvnNodePopulator implements INodePopulator{
	
	private static Logger logger = LoggerFactory.getLogger(SvnService.class);
	
	private final String TREE_NODE_SVN_FILE_TYPE = "svnFileType";

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		if (!(source instanceof Pair<?, ?> && ((Pair<?, ?>) source).a instanceof File)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		File file = ((Pair<File, String>) source).a;
		boolean isFileFromSvnRepository;
		try {
			isFileFromSvnRepository = SvnPlugin.getInstance().getUtils().isRepository(file);
			if(isFileFromSvnRepository) {
				destination.getOrCreateCustomData().put(TREE_NODE_SVN_FILE_TYPE, true);
			}
		} catch (SVNException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
		}
		
		return false;		
	}

}
