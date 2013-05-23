package com.crispico.flower.flexdiagram {

	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	/**
	 * An EditPart for figures that are based on a sequential layout (i.e. the order
	 * in the DisplayObject children of the container is the order of appearance on the
	 * screen). A mechanism for figure reuse is present.
	 * 
	 * <p>
	 * EditParts that don't have children but want to inherit from this class 
	 * (e.g. if one wants to have a base class (to avoid code duplication) for all 
	 * direct children of the diagram but not all of them support children) can do this.
	 * The figure reuse/recycle mechanism is disabled if the figure is not a Flex
	 * Container. 
	 * 
	 * <p>
	 * If the figure of the EditPart has already added other UIComponents, the constructor
	 * receives an offset representing the number of visual children already added.
	 * 
	 * @author Cristi
	 */
	public class SequentialLayoutEditPart extends EditPart {
		
		protected var _offset:int;
		
		public function SequentialLayoutEditPart(model:Object, viewer:DiagramViewer, offset:int = 0) {
			super(model, viewer);
			this.offset =  offset;
		}
		
		public function get offset():int {
			return _offset
		}
		
		public function set offset(value:int):void {
			_offset = value;
		}
		
		/**
		 * It iterates the DisplayObject children. If the figure found at Figure.children[i] is
		 * compatible with the EditPart child (EditPart.children[i]) it is reused; otherwise
		 * we use recycled or new figures.
		 */ 
		override public function refreshVisualChildren():void {
			if (!(figure is DisplayObjectContainer) || !figure is IComposedFigure) {
				return;
			}
			// TODO : Luiza - de testat pe XOPS de ce a fost modificata conditia (xops containere). (rev 3563)
			// In versiunea asta apar probleme cand adaugi o clasa, se pun si atributele unei clase reciclate.
			//if (!(figure is DisplayObjectContainer) || children.length == 0) {
			//	return;
			//}
			
			// the figure cast as a container
			var container:IVisualElementContainer = IVisualElementContainer(getFigure()); 
			if (figure is IComposedFigure) {
				container = IComposedFigure(figure).getVisualChildrenContainer();
			}
			for (var i:int = 0; i < children.length; i++) {
				var ep:EditPart = EditPart(children[i]);
				if (container.numElements > i + offset) {
					// we have a figure candidate to be reused
					var currentFigure:IFigure = IFigure(container.getElementAt(i + offset));
					if (Object(currentFigure).constructor == ep.getFigureClass()) {
						// compatible figure found (same type); it will be reused
						// if the figure is associated to the same editPart, we don't unset the figure
						// otherwise we unset it 
						if (currentFigure.getEditPart() != null) 
							currentFigure.getEditPart().unsetFigure(); 
					} else {
						// the figure is not compatible, recycle all figures starting
						// with current
						recycleFigures(container, i + offset); 
						currentFigure = ep.createOrGetRecycledFigure();
						AbsolutePositionEditPartUtils.addChildFigure(IVisualElementContainer(figure), IVisualElement(currentFigure));
					}
				} else {
					currentFigure = ep.createOrGetRecycledFigure();
					AbsolutePositionEditPartUtils.addChildFigure(IVisualElementContainer(figure), IVisualElement(currentFigure));
				}
				if (ep.getFigure() != null)
					ep.unsetFigure();
				ep.setFigure(currentFigure);
			}
			// the case when there are still figures left 
			recycleFigures(container, children.length + offset);		
		}
		
		/**
		 * Recycles all figures starting with the visual position: 'index'.
		 * The offset correction has already been aplied for index.
		 */
		private function recycleFigures(container:IVisualElementContainer, index:int):void {
			while (index < container.numElements) {
				var currentFigure:IFigure = IFigure(container.getElementAt(index));
				if (currentFigure.getEditPart() != null)
					currentFigure.getEditPart().unsetFigure();
				AbsolutePositionEditPartUtils.removeChildFigureAtIndex(container, index);
				recycleFigure(currentFigure);
			}
		}
		
		/**
		 * Without this override, the algorithm is incorrect for the 
		 * <code>SequentialLayoutEditPart</code> because it removes and adds
		 * all children when a child is added/deleted. The method 
		 * <code>refreshVisualChildren()</code> already removes all the 
		 * unassociated children.
		 */
		override public function childRemoved(child:EditPart, recycleFigureNeeded:Boolean=true):void {
			super.childRemoved(child, false);
		}
		
	}
}