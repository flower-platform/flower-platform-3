package com.crispico.flower.flexdiagram.action
{
	import com.crispico.flower.flexdiagram.EditPart;

	/**
	 * Data structure that has parameters related to the drag-to-create operation.
	 * This kind of object is available only for "create" actions.
	 * 
	 * @see com.crispico.flower.flexdiagram.action.IAction#context
	 * @see com.crispico.flower.flexdiagram.action.ActionContext
	 * @see com.crispico.flower.flexdiagram.gantt.contextmenu.GanttCreateActionContext
	 * @author Sorin
	 * @flowerModelElementId _ewLQgIhTEeC3D4t2GHNvxQ
	 */ 	
	public class CreateElementActionContext extends ActionContext {
		
		/**
		 * The X coordinate of the selection rectangle, in pixels,
		 * from the left of the diagram.
		 */ 
		public var x:Number;
		
		/**
		 * The Y coordinate of the selection rectangle, in pixels,
		 * from the top of the diagram.
		 */ 
		public var y:Number;
		
		/**
		 * The width of the selection rectangle, in pixels.
		 */ 
		public var width:Number;
		
		/**
		 * The height of the selection rectangle, in pixels.
		 */ 
		public var height:Number;
		
		/**
		 * The <code>EditPart</code> where the
		 * select operation has been initiated from.
		 * 
		 * <p> 
		 * If a "child" <code>EditPart</code> was not the source of the operation,
		 * the <code>EditPart</code> of the diagram will be returned.
		 */ 
		public var startDragEditPart:EditPart;
		
		/**
		 * Same as startDragEditPart only that it keeps the associated model.
		 * 
		 * <p>
		 * It is equivalent to startDragEditPart.getModel().
		 */ 
		public var startDragModel:Object;
	}
}