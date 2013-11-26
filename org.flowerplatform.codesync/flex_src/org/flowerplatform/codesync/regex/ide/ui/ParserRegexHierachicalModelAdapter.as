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
package org.flowerplatform.codesync.regex.ide.ui {
	import mx.collections.IList;
	
	import org.flowerplatform.editor.remote.*;
	import org.flowerplatform.emf_model.regex.ParserRegex;
	import org.flowerplatform.flexutil.tree.IHierarchicalModelAdapter;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ParserRegexHierachicalModelAdapter implements IHierarchicalModelAdapter {

		public function getChildren(treeNode:Object):IList {		
			if (treeNode is ParserRegex) {
				return ParserRegex(treeNode).matches;
			}
			return null;
		}
		
		public function hasChildren(treeNode:Object):Boolean {			
			if (treeNode is ParserRegex) {
				return ParserRegex(treeNode).matches.length != 0;
			}
			return false;
		}
		
	}
}