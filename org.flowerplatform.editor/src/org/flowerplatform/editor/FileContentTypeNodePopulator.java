package org.flowerplatform.editor;

import java.io.File;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class FileContentTypeNodePopulator implements INodePopulator {

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		if (!(source instanceof Pair<?, ?> && ((Pair<?, ?>) source).a instanceof File)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		File file = ((Pair<File, String>) source).a;
		if (file.isDirectory()) {
			return false;
		}
		
		String contentType = null;
		int lastDotIndex = file.getName().lastIndexOf('.');
		if (lastDotIndex >= 0) {
			// has an extension
			String extension = file.getName().substring(lastDotIndex + 1);
			contentType = EditorPlugin.getInstance().getFileExtensionToContentTypeMap().get(extension);
		}
		if (contentType == null) {
			contentType = EditorPlugin.getInstance().getFileExtensionToContentTypeMap().get("*");
		}
		if (contentType != null) {
			
			destination.getOrCreateCustomData().put(EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE, EditorPlugin.getInstance().getContentTypeDescriptorsMap().get(contentType).getIndex());
		}
		return true;
	}

}
