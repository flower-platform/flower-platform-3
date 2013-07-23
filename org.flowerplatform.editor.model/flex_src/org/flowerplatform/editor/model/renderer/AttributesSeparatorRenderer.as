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
package org.flowerplatform.editor.model.renderer {
	import org.flowerplatform.editor.model.EditorModelPlugin;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AttributesSeparatorRenderer extends SeparatorRenderer {
		
		public function AttributesSeparatorRenderer() {
			title = "attributes";
			serviceMethod = "addNew_attribute";
			newChildIcon = EditorModelPlugin.getInstance().getResourceUrl("images/obj16/SyncProperty.gif");
			newChildLabel = "+attr1:int";
		}
	}
}