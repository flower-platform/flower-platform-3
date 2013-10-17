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
package org.flowerplatform.editor.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.editor.EditorPlugin;

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
		
//		String contentType = EditorPlugin.getInstance().getContentTypeFromFileName(file.getName());
//		if (contentType != null) {
//			destination.getOrCreateCustomData().put(EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE, EditorPlugin.getInstance().getContentTypeDescriptorsMap().get(contentType).getIndex());
//		}
		List<String> contentTypes = EditorPlugin.getInstance().getContentTypeFromFileName(file.getName());
		if (!contentTypes.isEmpty()) {
			List<Integer> indexes = new ArrayList<Integer>(2);
			for (String contentType : contentTypes) {
				indexes.add(EditorPlugin.getInstance().getContentTypeDescriptorsMap().get(contentType).getIndex());
			}
			destination.getOrCreateCustomData().put(EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE, indexes);
		}
		return true;
	}

}