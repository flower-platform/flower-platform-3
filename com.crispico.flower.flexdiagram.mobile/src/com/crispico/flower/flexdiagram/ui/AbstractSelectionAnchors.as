package com.crispico.flower.flexdiagram.ui {
	import com.crispico.flower.flexdiagram.AbsolutePositionEditPartUtils;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	
	import mx.core.Container;
	import mx.core.UIComponent;
	
	import spark.components.Group;
	
	/**
	 * The class is the superclass for <code>AbsolutePositionSelectionAnchors</code>
	 * and <code>ConnectionSelectionAnchors</code>.
	 * 
	 * Children can override the following methods:
	 * <ul>
	 * 		<li><code>setMainSelection</code></li>
	 * 		<li><code>activate()</code> - used to create the anchors, set its 
	 * 			associated editPart and register listeners to update anchors </li>
	 * 		<li><code>deactivate()</code> - removes the anchors and unregister
	 * 			the listeners</li>
	 * </ul>
	 * 
	 * @author Georgi
	 * @flowerModelElementId _b1WyUb8REd6XgrpwHbbsYQ
	 */
	public class AbstractSelectionAnchors extends UIComponent {
		/**
		 * The figure where the anchors will be shown.
		 * @flowerModelElementId _b1qUVL8REd6XgrpwHbbsYQ
		 */
		protected var target:IFigure;
		
		/**
		 * Main selection influences the aspect of the anchors
		 * @flowerModelElementId _b1qUWL8REd6XgrpwHbbsYQ
		 */
		protected var isMainSelection:Boolean;
		
		/**
		 * Children can register listeners.
		 * @flowerModelElementId _b1qUXL8REd6XgrpwHbbsYQ
		 */
		public function activate(target:IFigure):void {
			this.target = target;
			
			// add itself to the rootFigure (Diagram) 
			var rootFigure:Group = Group(EditPart(target.getEditPart()).getViewer().getRootEditPart().getFigure());
			AbsolutePositionEditPartUtils.addChildFigure(rootFigure, this);
		}
		
		public function deactivate():void {
			// remove itself from diagram
			AbsolutePositionEditPartUtils.removeChildFigure(Group(EditPart(target.getEditPart()).getViewer().getRootEditPart().getFigure()), this);
		}
		
		/**
		 * Returns the EditPart of the figure received at
		 * activate.
		 * 
		 * If activate was not called prior to 
		 * calling this function then return null
		 * @flowerModelElementId _b1qUZr8REd6XgrpwHbbsYQ
		 */
		public function getEditPart():EditPart {
			if (target != null) 
				return target.getEditPart();
			else 
				return null;
		}
		
		/**
		 * Setter for main selection.
		 * @flowerModelElementId _b1qUa78REd6XgrpwHbbsYQ
		 */
		public function setMainSelection(value:Boolean):void {
			isMainSelection = value;
		}
		
		/**
		 * Getter for main selection.
		 * @flowerModelElementId _b1qUcb8REd6XgrpwHbbsYQ
		 */
		public function getMainSelection():Boolean {
			return isMainSelection;
		}
		
		
		public function getConnectionFigure():ConnectionFigure {
			return ConnectionFigure(target);
		}
	}
}