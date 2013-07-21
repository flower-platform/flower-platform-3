package org.flowerplatform.editor.mindmap.controller {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.editor.mindmap.NotationMindMapDiagramShell;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapNodeController extends ControllerBase implements IMindMapModelController {
		
		public function MindMapNodeController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getParent(model:Object):Object {			
			if (MindMapNode(model).parentView_RH != null) {
				if (ReferenceHolder(MindMapNode(model).parentView_RH).referencedObject is Diagram) {
					return null;
				}
				return ReferenceHolder(MindMapNode(model).parentView_RH).referencedObject;
			}
			return null;
		}
		
		public function setParent(model:Object, value:Object):void {
//			var oldParent:Object = model.parent;
//			var newParent:MindMapNode = MindMapNode(value);
//			if (newParent.side != MindMapDiagramShell.NONE && newParent.side != MindMapNode(model).side) {
//				setSide(model, newParent.side);				
//			}
//			IEventDispatcher(model).dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, "parent", oldParent, newParent));
		}
		
		public function setChildren(model:Object, value:ArrayList):void {
			MindMapNode(model).persistentChildren_RH = value;
		}
				
		public function getX(model:Object):Number {
			return MindMapNode(model).x;			
		}
		
		public function setX(model:Object, value:Number):void {			
			MindMapNode(model).setX(value);
		}
		
		public function getY(model:Object):Number {
			return MindMapNode(model).y;			
		}
		
		public function setY(model:Object, value:Number):void {		
			MindMapNode(model).setY(value);
		}
		
		public function getWidth(model:Object):Number {		
			return MindMapNode(model).width;
		}
		
		public function setWidth(model:Object, value:Number):void {			
			MindMapNode(model).setWidth(value);
		}
		
		public function getHeight(model:Object):Number {
			return MindMapNode(model).height;			
		}
		
		public function setHeight(model:Object, value:Number):void {
			MindMapNode(model).setHeight(value);
		}
		
		public function getExpanded(model:Object):Boolean {
			return MindMapNode(model).expanded;
		}
		
		public function setExpanded(model:Object, value:Boolean):void {
			NotationMindMapDiagramShell(diagramShell).editorStatefulClient.service_setExpanded(MindMapNode(model).id, value);
		}
		
		public function getSide(model:Object):int {
			return MindMapNode(model).side;
		}
		
		public function setSide(model:Object, value:int):void {
			NotationMindMapDiagramShell(diagramShell).editorStatefulClient.service_setSide(MindMapNode(model).id, value);
		}
	}
}