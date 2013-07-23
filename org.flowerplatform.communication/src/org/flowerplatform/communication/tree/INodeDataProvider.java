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
package org.flowerplatform.communication.tree;

import java.util.List;

import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;

public interface INodeDataProvider extends INodePopulator {
	
	PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context);
	
	String getLabelForLog(Object node, String nodeType);
	
	/**
	 * This method can safely use the {@link GenericTreeStatefulService#getVisibleNodes()}. 
	 */
	String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath);
	
	/**
	 * This method can safely use the {@link GenericTreeStatefulService#getVisibleNodes()}. 
	 */
	boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text);
}