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
package org.flowerplatform.editor.open_resources_view {
	
	import com.crispico.flower.flexdiagram.util.common.BitmapContainer;
	import com.crispico.flower.util.tree.CustomIconTreeItemRenderer;
	
	import mx.collections.ArrayCollection;
	import mx.core.IFlexDisplayObject;
	
	import org.flowerplatform.communication.tree.GenericTreeItemRenderer;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.ResourceStatusBar;
	import org.flowerplatform.editor.remote.EditableResource;
	import org.flowerplatform.editor.remote.EditableResourceClient;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	
	/**
	 * Customized to handle <code>EditableResource</code>s and
	 * <code>EditableResourceClient</code>s.
	 * 
	 * @author Cristi
	 */
	public class OpenResourcesTreeItemRenderer extends GenericTreeItemRenderer {
		
//		/**
//		 * If the current resource is locked, as small lock is overlayed.
//		 * 
//		 * @flowerModelElementId _iNpZQbq6EeGcctunuFRi4Q
//		 */
//		override protected function createIcon():IFlexDisplayObject	{
//			if (data is EditableResource) {
//				var editableResource:EditableResource = EditableResource(data);
//				if (editableResource.locked) {
//					return new BitmapContainer(new ArrayCollection([editableResource.iconUrl, ResourceStatusBar.LOCK_ICON_SMALL]));
//				} else {
//					return new BitmapContainer(editableResource.iconUrl);
//				}
//			} else if (data is EditableResourceClient) {
//				return new BitmapContainer("icons/Web/icons/usr_admin/user.png");
//			} else {
//				return null;
//			}
//		}
		
		/**
		 * @author Sebastian
		 */

		override protected function getIconFunction(data:HierarchicalModelWrapper):Object
		{
			if (data.treeNode is EditableResource){
				var icon_URL:String = EditableResource(data.treeNode).iconUrl;
				return icon_URL;
			}else if (data.treeNode is EditableResourceClient)
				return null;	
			return null;
		}
		
		/**
		 * @author Sebastian
		 */
		override protected function getLabelFunction(data:HierarchicalModelWrapper):String
		{
			if (data.treeNode is EditableResource){
				return EditableResource(data.treeNode).label;
			}else if (data.treeNode is EditableResourceClient)
				return EditableResourceClient(data.treeNode).name;				
			return null;
			
			
		}
		
	}
}