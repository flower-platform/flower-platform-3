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
package org.flowerplatform.communication.tree.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Cristi
 * @author Cristina
 * @author Sorin
 * 
 * @see Corresponding AS class.
 * 
 * 
 */
public class TreeNode {
		
	/**
	 * 
	 */
	private String label;
	
	/**
	 * 
	 */
	private String icon;
	
	/**
	 * 
	 */
	private boolean hasChildren;
	
	/**
	 * 
	 */
	private List<TreeNode> children;
	
	/**
	 * 
	 */
	private TreeNode parent;
	
	/**
	 * 
	 */
	private PathFragment pathFragment;
	
	private Map<String, Object> customData;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String iconUrl) {
		this.icon = iconUrl;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public PathFragment getPathFragment() {
		return pathFragment;
	}

	public void setPathFragment(PathFragment pathFragment) {
		this.pathFragment = pathFragment;
	}

	/**
	 * This getter is intended only for serialization.
	 * For working with the map see #getOrCreateCustomData(). 
	 */
	public Map<String, Object> getCustomData() {
		return customData;
	}

	public void setCustomData(Map<String, Object> customData) {
		this.customData = customData;
	}
	
	/**
	 * Method should be used only for behaviors that wish to populate this field.
	 * This field may be useful for transporting specific information to the client side
	 * about the corresponding object on the server side.
	 *  
	 * Note: the logic that computes information must be fast.
	 */
	public Map<String, Object> getOrCreateCustomData() {
		if (customData == null)
			customData = new HashMap<String, Object>(); 
		return customData;
	}
	
	@Override
	public String toString() {
		return "TreeNode [label=" + label + ", iconUrl=" + icon + ", pathFragment=" + pathFragment + "]";
	}
}