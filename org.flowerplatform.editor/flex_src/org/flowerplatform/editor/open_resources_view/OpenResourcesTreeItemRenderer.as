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
	
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.core.IFlexDisplayObject;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.communication.tree.GenericTreeItemRenderer;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.ResourceStatusBar;
	import org.flowerplatform.editor.remote.EditableResource;
	import org.flowerplatform.editor.remote.EditableResourceClient;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.tree.TreeList;
	
	/**
	 * Customized to handle <code>EditableResource</code>s and
	 * <code>EditableResourceClient</code>s.
	 * 
	 * @author Cristi
	 * @author Sebastian Solomon
	 * 
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
		

		override protected function getIconFunction(data:HierarchicalModelWrapper):Object {
			if (data.treeNode is EditableResource) {
				var iconUrl:String = EditableResource(data.treeNode).iconUrl;
				return iconUrl;
			}else if (data.treeNode is EditableResourceClient) {
				return EditorPlugin.getInstance().getResourceUrl('images/user.png');
			}
			return null;
		}
		
		override protected function getLabelFunction(data:HierarchicalModelWrapper):String
		{
			if ( data.treeNode is EditableResource ) {
				return EditorPlugin.getInstance().globalEditorOperationsManager.getEditableResourceLabel( EditableResource(data.treeNode) , true);
				//return EditableResource(data.treeNode).label;
			} else if (data.treeNode is EditableResourceClient) {
				var label:String = EditableResourceClient(data.treeNode).name + " (" + 
					EditableResourceClient(data.treeNode).login + ")";
				return label;
			}
		return null;
		}
		
		
		override public function set data(value:Object):void {
			var initialData:Object = data;
			if (data != null && value != initialData) {
				HierarchicalModelWrapper(data).treeNode.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertyChangeHandler, false);
			}
			super.data = value;
			if (data != null && value != initialData) {
				HierarchicalModelWrapper(data).treeNode.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertyChangeHandler);
			}
		}
		
		protected function propertyChangeHandler(event:PropertyChangeEvent):void {			
			data = data;
			dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
		}
		
	}
}
