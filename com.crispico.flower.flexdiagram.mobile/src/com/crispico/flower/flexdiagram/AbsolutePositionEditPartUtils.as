package com.crispico.flower.flexdiagram {
	import com.crispico.flower.flexdiagram.ui.AbsolutePositionSelectionAnchors;
	
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.MoveEvent;
	import mx.events.ResizeEvent;
	
	/**
	 * Behaviour for classes that implement <code>IAbsoluteLayoutEditPart</code>. 
	 * EditParts that implement the latter interface, for each method defined in 
	 * this class, should override the methods with same name from EditPart and 
	 * delegate to this implementation.
	 * 
	 * <p>
	 * We use this mechanism to "simulate" multiple inheritance (e.g. from
	 * <code>SequentialLayoutEditPart</code> and from 
	 * <code>AbsoluteLayoutEditPart</code>.  
	 * 
	 * @author Cristi
	 * @flowerModelElementId _b4C6D78REd6XgrpwHbbsYQ
	 */ 
	public class AbsolutePositionEditPartUtils {
	
		/**
		 * Returns <code>getBoundsRect()</code> if
		 * a figure doesn't exist; otherwise it returns the figure's 
		 * coordinates.
		 */ 
		public static function getConnectionAnchorRect(editPart:EditPart):Array {
			if (editPart.getFigure() == null)
				return (IAbsolutePositionEditPart(editPart).getBoundsRect());
			else
				return [DisplayObject(editPart.getFigure()).x, DisplayObject(editPart.getFigure()).y, 
					DisplayObject(editPart.getFigure()).width, DisplayObject(editPart.getFigure()).height];
		}

		/**
		 * Listeners are registered for move and resize events.
		 * @flowerModelElementId _b4C6Gb8REd6XgrpwHbbsYQ
		 */
		public static function setFigure(editPart:EditPart, newFigure:IFigure):void {
			DisplayObject(editPart.getFigure()).addEventListener(MoveEvent.MOVE, editPart.dispatchUpdateConnectionEndsEvent);
			DisplayObject(editPart.getFigure()).addEventListener(ResizeEvent.RESIZE, editPart.dispatchUpdateConnectionEndsEvent);
			
			updateSelectedState(IAbsolutePositionEditPart(editPart), editPart.getSelected(), editPart.getMainSelection());
		}

		/**
		 * @flowerModelElementId _b4C6IL8REd6XgrpwHbbsYQ
		 */
		public static function unsetFigure(editPart:EditPart):void {
			if (editPart.getFigure() == null) {
				// may be null if the element is hidden (because of view reuse)
				// I saw that it happens e.g. viewer.setRootModel(null) for a big diagram
				return;
			}
			
			DisplayObject(editPart.getFigure()).removeEventListener(MoveEvent.MOVE, editPart.dispatchUpdateConnectionEndsEvent);
			DisplayObject(editPart.getFigure()).removeEventListener(ResizeEvent.RESIZE, editPart.dispatchUpdateConnectionEndsEvent);
			
			// destroy selection components
			// this works because isSelected and isMainSelected is 
			// retained in EditPart so it can be corectly resotred at setFigure
			updateSelectedState(IAbsolutePositionEditPart(editPart), false, false);
		}
	
		/**
		 * Every AbsolutePositionEditPart is selectable.
		 * The exceptions will have other comportment (and will not pass through this functions). 
		 * @flowerModelElementId _b4MD4r8REd6XgrpwHbbsYQ
		 */
		public static function isSelectable(editPart:EditPart):Boolean {
			return true;
		}
		/**
		 * 
		 * Uses a <code>AbsolutePositionSelectionAnchors</code> component to
		 * display the selected state
		 * @flowerModelElementId _b4MD6L8REd6XgrpwHbbsYQ
		 */
		public static function updateSelectedState(editPart:IAbsolutePositionEditPart, isSelected:Boolean, isMainSelection:Boolean):void {
			
			var anchors:AbsolutePositionSelectionAnchors;
			
			// If the selected EditPart does not have a figure
			// it means it takes part into the reusing/recycling 
			// figure process.
			// It is selectable.			
			if (EditPart(editPart).getFigure() == null) {
				return;
			}
			
			// get selection component
			anchors = editPart.getSelectionComponent();
			if (isSelected) {
				if (anchors == null) {
					// create the selection component
					anchors = editPart.createNewSelectionComponent();
					// attached it to the editPart
					editPart.setSelectionComponent(anchors);
					// set its main selection
					anchors.setMainSelection(isMainSelection);
					// activate it
					anchors.activate(EditPart(editPart).getFigure());
				} else {
					// if selection not lost check if main selection changed
					if (isMainSelection != anchors.getMainSelection()) {
						// and notify accordingly
						anchors.setMainSelection(isMainSelection);
					}
				}
			} else {
				if (anchors != null) {
					// if selection lost
					// deactivate anchors
					anchors.deactivate();
					// destroy the selection component
					editPart.setSelectionComponent(null);
				}
			}
		}
		
		/**
		 * @flowerModelElementId _QtSAQElXEeCVS-1PDSuDNg
		 */
		public static function addChildFigureAtIndex(parentFigure:IVisualElementContainer, childFigure:IVisualElement, index:int):void {
			var container:IVisualElementContainer = (parentFigure is IComposedFigure) ? 
														IComposedFigure(parentFigure).getVisualChildrenContainer() : parentFigure;
			
			if (index == container.numElements) {
				container.addElement(childFigure);
			} else {
				container.addElementAt(childFigure, index);
			}
		}
		
		/**
		 * Adds <code>childFigure</code> to the <code>parentFigure</code>.
		 * 
		 * <p>If <code>parentFigure</code> is IComposedFigure then adds the child figure to the container holding the
		 * children figures.
		 * 
		 * @author Luiza
		 * @flowerModelElementId _weU9oNqWEd-ShIirE0EYeQ
		 */ 
		public static function addChildFigure(parentFigure:IVisualElementContainer, childFigure:IVisualElement):void {
			if (parentFigure is IComposedFigure) {
				IComposedFigure(parentFigure).getVisualChildrenContainer().addElement(childFigure);
			} else {
				parentFigure.addElement(childFigure);
			}
		}
		/**
		 * Adds <code>childFigure</code> from the <code>parentFigure</code>.
		 * 
		 * @see addChildFigure
		 * 
		 * @author Luiza
		 */ 
		public static function removeChildFigure(parentFigure:IVisualElementContainer, childFigure:IVisualElement):void {
			if (parentFigure is IComposedFigure) {
				IComposedFigure(parentFigure).getVisualChildrenContainer().removeElement(childFigure);
			} else {
				parentFigure.removeElement(childFigure);
			}
		}

		
		/**
		 * @see removeChildFigure
		 * 
		 * @author Luiza
		 */ 
		public static function removeChildFigureAtIndex(parentFigure:IVisualElementContainer, index:int):void {
			if (parentFigure is IComposedFigure) {
				IComposedFigure(parentFigure).getVisualChildrenContainer().removeElementAt(index);
			} else {
				parentFigure.removeElementAt(index);
			}
		}
		
		/**
		 * Retrieves the index of <code>childFigure</code> inside <code>parentFigure</code>.
		 * 
		 * <p>If <code>parentFigure</code> is IComposedFigure retrieves the index from the 
		 * visual children container(the component holding children figures).
		 * 
		 * @author Luiza
		 */ 
		public static function getChildFigureIndex(parentFigure:IVisualElementContainer, childFigure:IVisualElement):int {
			if (parentFigure is IComposedFigure) {
				return IComposedFigure(parentFigure).getVisualChildrenContainer().getElementIndex(childFigure);
			} else {
				return parentFigure.getElementIndex(childFigure);
			}
		}
		
		/**
		 * Resets the index of the <code>childFigure</code> inside <code>parentFigure</code>.
		 * 
		 * @see getChildFigureIndex
		 * 
		 * @author Luiza
		 */ 
		public static function setChildFigureIndex(parentFigure:IVisualElementContainer, childFigure:IVisualElement, index:int):void {
			var container:IVisualElementContainer = (parentFigure is IComposedFigure) ? 
													IComposedFigure(parentFigure).getVisualChildrenContainer(): parentFigure;
			
			if (index == container.numElements) { // if i want to place this child on the last position
				container.setElementIndex(childFigure, index - 1);
			} else {
				container.setElementIndex(childFigure, index);
			}			
		}
		
		public static function containsFigure(parentFigure:IVisualElementContainer, childFigure:IVisualElement):Boolean {
			try {
				if (parentFigure is IComposedFigure) {
					IComposedFigure(parentFigure).getVisualChildrenContainer().getElementIndex(childFigure);
				} else {
					parentFigure.getElementIndex(childFigure);
				}				
			} catch (error:ArgumentError) {
				return false;
			}	
			return true;
		}
		
		public static function getActualFigure(parentFigure:IVisualElementContainer):IVisualElementContainer {
			if (parentFigure is IComposedFigure) {
				return IComposedFigure(parentFigure).getVisualChildrenContainer();
			} else {
				return parentFigure;
			}
		}
	}
}