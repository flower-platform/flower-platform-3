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
package org.flowerplatform.web.git.explorer;

import org.flowerplatform.web.explorer.AbstractVirtualItemNodeDataProvider;
import org.flowerplatform.web.git.GitNodeType;
import org.flowerplatform.web.git.GitPlugin;

/**
 * @author Cristina Constantienscu
 */
public class VirtualItemDataProvider extends AbstractVirtualItemNodeDataProvider {
	
	public VirtualItemDataProvider() {
		super();
		nodeInfo.put(GitNodeType.NODE_TYPE_LOCAL_BRANCHES, 
				new String[] {
				GitPlugin.getInstance().getMessage("git.localBranches"), 
				GitPlugin.getInstance().getResourceUrl("images/full/obj16/branches_obj.gif")});
		nodeInfo.put(GitNodeType.NODE_TYPE_REMOTE_BRANCHES, 
				new String[] {
				GitPlugin.getInstance().getMessage("git.remoteBranches"), 
				GitPlugin.getInstance().getResourceUrl("images/full/obj16/branches_obj.gif")});
		nodeInfo.put(GitNodeType.NODE_TYPE_TAGS, 
				new String[] {
				GitPlugin.getInstance().getMessage("git.tags"), 
				GitPlugin.getInstance().getResourceUrl("images/full/obj16/tags.gif")});
		nodeInfo.put(GitNodeType.NODE_TYPE_REMOTES,
				new String[] {
				GitPlugin.getInstance().getMessage("git.remotes"), 
				GitPlugin.getInstance().getResourceUrl("images/full/obj16/remote_entry_tbl.gif")});
		nodeInfo.put(GitNodeType.NODE_TYPE_WDIRS, 
				new String[] {
				GitPlugin.getInstance().getMessage("git.workingDirectories"), 
				GitPlugin.getInstance().getResourceUrl("images/full/obj16/submodules.gif")});
	}
	
}