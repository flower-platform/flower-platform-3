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
package org.flowerplatform.flexdiagram.samples.mindmap {
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.samples.IModelHolder;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	public class MindMapPopulator {
		
		public static function populateRootModel(modelHolder:IModelHolder):void {
			if (modelHolder.rootModel == null) {
				modelHolder.rootModel = new ParentAwareArrayList(null);
				modelHolder.rootModel.parent = modelHolder.rootModel;
			}
			
			var model:MindMapModel = getMindMapModel(null);				
			model.children.addItem(getMindMapModel(model));
			model.children.addItem(getMindMapModel(model));
			model.children.addItem(getMindMapModel(model));
			modelHolder.rootModel.addItem(model);
			
			var child2:MindMapModel = getMindMapModel(model);	
			child2.side = MindMapDiagramShell.LEFT;
			child2.children.addItem(getMindMapModel(child2));
			child2.children.addItem(getMindMapModel(child2));	
			
			model.children.addItem(child2);
			
			var child:MindMapModel = getMindMapModel(model);
			child.side = MindMapDiagramShell.RIGHT;
			child.children.addItem(getMindMapModel(child));
			child.children.addItem(getMindMapModel(child));
			child.children.addItem(getMindMapModel(child));
			child.children.addItem(getMindMapModel(child));
			child.parent = model;
			
			model.children.addItem(child);
			
			var child1:MindMapModel = getMindMapModel(model);	
			child1.side = MindMapDiagramShell.RIGHT;
			child1.children.addItem(getMindMapModel(child1));
			child1.parent = model;				
			model.children.addItem(child1);	
			
			var child11:MindMapModel = getMindMapModel(child1);				
			child11.children.addItem(getMindMapModel(child11));
			child11.children.addItem(getMindMapModel(child11));
			child1.children.addItem(child11);	
			var child111:MindMapModel = getMindMapModel(child11);				
			child111.children.addItem(getMindMapModel(child111));
			child111.children.addItem(getMindMapModel(child111));
			child11.children.addItem(child111);		
		}
		
		private static function getMindMapModel(parent:MindMapModel):MindMapModel {
			var model:MindMapModel;
			
			model = new MindMapModel();
			model.text = "MindMap" + (new Date()).time;
			model.width = 151;
			model.height = 22;
			if (parent != null && parent.side != 0) {
				model.side = parent.side;
			} else if (parent != null) {
				model.side = MindMapDiagramShell.LEFT;
			}
			model.parent = parent;
			
			return model;
		}
	}
}