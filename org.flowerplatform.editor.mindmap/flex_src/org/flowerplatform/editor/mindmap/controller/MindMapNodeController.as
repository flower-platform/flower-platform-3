package org.flowerplatform.editor.mindmap.controller {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapNodeController implements IMindMapModelController {
		
		public function getParent(model:Object):Object {
			return MindMapNode(model).parentView_RH;
		}
		
		public function setParent(model:Object, value:Object):void {
			var oldParent:Object = model.parent;
			var newParent:MindMapNode = MindMapNode(value);
			if (newParent.side != MindMapDiagramShell.NONE && newParent.side != MindMapNode(model).side) {
				setSide(model, newParent.side);				
			}
			MindMapNode(model).parentView_RH = ReferenceHolder(newParent);
			IEventDispatcher(model).dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, "parent", oldParent, newParent));
		}
		
		public function getChildren(model:Object):ArrayList {
			return ArrayList(MindMapNode(model).persistentChildren_RH);
		}
		
		public function getChildrenBasedOnSide(model:Object, side:int = 0):ArrayList {	
			if (side == 0) {
				side = model.side;
			}
			var list:ArrayList = new ArrayList();
			for (var i:int = 0; i < MindMapNode(model).persistentChildren_RH.length; i++) {
				var child:Object = MindMapNode(model).persistentChildren_RH.getItemAt(i);
				if (side == 0 || side == child.side) {
					list.addItem(child);
				}
			}
			return list;
		}
		
		public function setChildren(model:Object, value:ArrayList):void {
			MindMapNode(model).persistentChildren_RH = value;
		}
				
		public function getX(model:Object):Number {
			return Bounds(MindMapNode(model).layoutConstraint_RH).x;
		}
		
		public function setX(model:Object, value:Number):void {
			Bounds(MindMapNode(model).layoutConstraint_RH).x = value;
		}
		
		public function getY(model:Object):Number {
			return Bounds(MindMapNode(model).layoutConstraint_RH).y;
		}
		
		public function setY(model:Object, value:Number):void {
			Bounds(MindMapNode(model).layoutConstraint_RH).y = value;
		}
		
		public function getWidth(model:Object):Number {		
			return Bounds(MindMapNode(model).layoutConstraint_RH).width;
		}
		
		public function setWidth(model:Object, value:Number):void {			
			Bounds(MindMapNode(model).layoutConstraint_RH).width = value;
		}
		
		public function getHeight(model:Object):Number {
			return Bounds(MindMapNode(model).layoutConstraint_RH).height;
		}
		
		public function setHeight(model:Object, value:Number):void {
			Bounds(MindMapNode(model).layoutConstraint_RH).height = value;
		}
		
		public function getExpanded(model:Object):Boolean {
			return MindMapNode(model).expanded;
		}
		
		public function setExpanded(model:Object, value:Boolean):void {
			MindMapNode(model).expanded = value;
		}
		
		public function getSide(model:Object):int {
			return MindMapNode(model).side;
		}
		
		public function setSide(model:Object, value:int):void {
			MindMapNode(model).side = value;
			for (var i:int = 0; i < getChildren(model).length; i++) {
				setSide(getChildren(model).getItemAt(i), model.side);
			}
		}
	}
}