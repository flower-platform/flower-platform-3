package com.crispico.flower.flexdiagram {
	import com.crispico.flower.flexdiagram.ui.IDraggable;
	
	import mx.core.IVisualElement;

	/**
	 * Interface that should be implemented by graphical components that act as figures.
	 * Classes that implement <code>IFigure</code> must be subclasses of Flex's 
	 * <code>DisplayObject</code>. 
	 * 
	 * <p>
	 * Implementing classes should:
	 * <ul>
	 * 	<li>have a field to store the associated EditPart,
	 * 	<li>implement accessors for it (according to the methods defined
	 * 		in the interface).
	 * </ul>
	 * 
	 * @author cristi
	 * @flowerModelElementId _31vw-chaEd6f3oj4VmBqug
	 */
	public interface IFigure extends IDraggable, IVisualElement {
		
		/**
		  * The figure becomes active. This method is called when:  
		  * <ul>
		  * 	<li>a newly created (or recycled) figure becomes active (ep != null, old EditPart == null); or
		  * 	<li>an existent figure is being reused; i.e. "given" to another EditPart, because the old
		  * 		EditPart is no longer visible (ep != null, old EditPart != null)
		  * 	<li>the figure is being recycled; i.e. it is detached from its EditPart and visual container
		  * 		and put in the recycled figures list (ep == null)
		  * </ul>
		  * 
		  * NOTE: as one can see from the above list, when an EditPart is associated to a figure, the
		  * figure may not be a fresh one. It may be reused, so no assumptions on its graphical
		  * state should be made. Every graphical property (e.g. color, font, etc) needs to be
		  * reinitialized according to the new EditPart/model.
		  * 
		  * <p>
		  * The implementation should store the EditPart in an internal field. It may also want to add/remove
		  * listeners to the EditPart and/or the EditPart's model.
		  * 
		  * <p>
		  * This method is called only by <code>EditPart.setFigure()</code> and shouldn't be called
		  * explicitly.
		  */ 
		  function setEditPart(ep:EditPart):void;
		
	}
}