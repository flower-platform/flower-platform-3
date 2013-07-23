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
	import flash.events.IEventDispatcher;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	import mx.events.PropertyChangeEvent;
	import mx.events.PropertyChangeEventKind;
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapModelController implements IMindMapModelController {
		
		public function getParent(model:Object):Object {
			return MindMapModel(model).parent;
		}
		
		public function setParent(model:Object, value:Object):void {
			var oldParent:Object = model.parent;
			var newParent:MindMapModel = MindMapModel(value);
			if (newParent.side != MindMapDiagramShell.NONE && newParent.side != MindMapModel(model).side) {
				setSide(model, newParent.side);				
			}
			MindMapModel(model).parent = newParent;
			IEventDispatcher(model).dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, "parent", oldParent, newParent));
		}
		
		public function getChildren(model:Object):ArrayList {
			return MindMapModel(model).children;
		}
		
		public function getChildrenBasedOnSide(model:Object, side:int = 0):ArrayList {	
			if (side == 0) {
				side = model.side;
			}
			var list:ArrayList = new ArrayList();
			for (var i:int = 0; i < MindMapModel(model).children.length; i++) {
				var child:Object = MindMapModel(model).children.getItemAt(i);
				if (side == 0 || side == child.side) {
					list.addItem(child);
				}
			}
			return list;
		}
		
		public function setChildren(model:Object, value:ArrayList):void {
			MindMapModel(model).children = value;
		}
				
		public function getX(model:Object):Number {
			return MindMapModel(model).x;
		}
		
		public function setX(model:Object, value:Number):void {
			MindMapModel(model).x = value;
		}
		
		public function getY(model:Object):Number {
			return MindMapModel(model).y;
		}
		
		public function setY(model:Object, value:Number):void {
			MindMapModel(model).y = value;
		}
		
		public function getWidth(model:Object):Number {		
			return MindMapModel(model).width;
		}
		
		public function setWidth(model:Object, value:Number):void {			
			MindMapModel(model).width = value;
		}
		
		public function getHeight(model:Object):Number {
			return MindMapModel(model).height;
		}
		
		public function setHeight(model:Object, value:Number):void {
			MindMapModel(model).height = value;
		}
		
		public function getExpanded(model:Object):Boolean {
			return MindMapModel(model).expanded;
		}
		
		public function setExpanded(model:Object, value:Boolean):void {
			MindMapModel(model).expanded = value;
		}
		
		public function getSide(model:Object):int {
			return MindMapModel(model).side;
		}
		
		public function setSide(model:Object, value:int):void {
			MindMapModel(model).side = value;
			for (var i:int = 0; i < getChildren(model).length; i++) {
				setSide(getChildren(model).getItemAt(i), model.side);
			}
		}
	}
}