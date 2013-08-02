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
package com.crispico.flower.mp.codesync.base.communication {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;

	/**
	 * The "Bindable" annotation is here in order to have the variables transformaed into accessors
	 * by the Flex compiler (needed by the merge/copy logic <code>UpdateTreeNodeCommand</code>).
	 * 
	 * 
	 */ 
	[Bindable]
	[RemoteClass(alias="com.crispico.flower.mp.codesync.base.communication.DiffTreeNode")]
	public class DiffTreeNode extends TreeNode {
		public var topLeftColor:uint = 0xFFFFFF;
		public var topRightColor:uint = 0xFFFFFF;
		public var bottomLeftColor:uint = 0xFFFFFF;
		public var bottomRightColor:uint = 0xFFFFFF;
		public var crossColor:uint = 0xFFFFFF;
		public var toolTip:String;
		public var contextMenuEntries:ArrayCollection;
	}
}