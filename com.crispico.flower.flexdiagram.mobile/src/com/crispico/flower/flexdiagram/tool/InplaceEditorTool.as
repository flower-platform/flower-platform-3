package com.crispico.flower.flexdiagram.tool {
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuManager;
	
	import flash.display.DisplayObject;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.controls.ColorPicker;
	import mx.core.Application;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	import mx.managers.SystemManager;
	
	
	/**
	 * The tool that handles in place editing. 
	 * The class should:
	 * 		<li> activate the inplace editor component </li>
	 * 		<li> listens to text change from inplace editor component.
	 * 			When a notification is received, it dispatches the 
	 * 			notification to the active editPart (which is responsible
	 * 			with the actual model update) </li>
	 * 
	 * @author mircea
	 * @flowerModelElementId _b0BVnL8REd6XgrpwHbbsYQ
	 */
	public class InplaceEditorTool extends Tool {
		
		/**
		 * Caching the current inplaceEditor.
		 */
		protected var inplaceEditor:UIComponent = null;
		
		/**
		 * The editpart that has its inplaceEditor activated.
		 */
		protected var activatedEditPart:EditPart = null;
		
		/**
		 * Cache last clicked IFigure where activated happened
		 */
		protected var lastActivatedClickedFigure:IFigure = null;
		
		/**
		 * Cache clicked IFigure.
		 * Necessary for the drag detection algorithm
		 */
		protected var clickedFigure:IFigure = null;
		
		
		/**
		 * Cache last clicked target. It is stored at each mouseDownHandler. 
		 * Necesary for deciding if an update of the InplaceEditor text will be done.
		 */
		protected var lastClickedTarget:Object = null;
		
		
		/**
		 * If the inplaceEditor has been activated after an element creation,
		 * the value is set to <code>true</code>. It will be reset when the inplaceEditor
		 * will be desactivated.
		 * It's purpose is to know if, after pressing CTRL+ENTER, a new element must be created or not.
		 * @see #startAutoInplaceEditor(EditPart)
		 * @author Cristina
		 */ 
		private var _activatedAfterCreation:Boolean = false;
		
		public function get activatedAfterCreation():Boolean {
			return _activatedAfterCreation;
		}
		
		public function set activatedAfterCreation(value:Boolean):void {
			_activatedAfterCreation = value;
		}
		
		/**
		 * Return <code>true</code> if this Tool is in edit mode (an inplace editor component is active).
		 * 
		 * @private
		 * Called from SelectMoveResizeTool to stop enter in Pan mode when editing and pressing SHIFT
		 * 
		 * @author Luiza
		 */ 
		public function isCurrentlyActive():Boolean {
			return inplaceEditor != null; // currently editing
		}
		
		/**
		 * Adds handlers for mouseDown, mouseUp, mouseMove, keyDown
		 * @flowerModelElementId _b0KfjL8REd6XgrpwHbbsYQ
		 */
		override public function activate(diagramViewer:DiagramViewer):void {
			super.activate(diagramViewer);
			var disp:DisplayObject = DisplayObject(diagramViewer.getRootEditPart().getFigure());
			disp.addEventListener(MouseEvent.MOUSE_DOWN, handleMouseDown);
			disp.addEventListener(MouseEvent.MOUSE_UP, handleMouseUp);
			disp.addEventListener(MouseEvent.MOUSE_MOVE, handleMouseMove);
			disp.addEventListener(KeyboardEvent.KEY_DOWN, handleKeyDown, true);
		}
		
		/**
		 * Remove handlers for mouseDown, mouseUp, mouseMove, keyDown
		 */
		override public function deactivate():void {
			var disp:DisplayObject = DisplayObject(diagramViewer.getRootEditPart().getFigure());
			disp.removeEventListener(MouseEvent.MOUSE_DOWN, handleMouseDown);
			disp.removeEventListener(MouseEvent.MOUSE_UP, handleMouseUp);
			disp.removeEventListener(MouseEvent.MOUSE_MOVE, handleMouseMove); 
			disp.removeEventListener(KeyboardEvent.KEY_DOWN, handleKeyDown, true);
			super.deactivate();
		} 
		
		/**
		 * Utility function that walks the hierarchy for the
		 * received EditPart searching for the first one
		 * that is selectable
		 */
		protected function getSelectableEditPart(selEditPart:EditPart):EditPart {
			while (selEditPart != null) {
				if (selEditPart.isSelectable()) {
					return selEditPart;
				}
				selEditPart = selEditPart.getParent();
			}
			return null;
		}
		
		/**
		 * Because we have to know if the user wanted to activate the editor, or 
		 * wanted a drag we deffer the activate InplaceEditor action to mouseUp.
		 * 
		 * Here we only check the preconditions needed to activate the InplaceEditor
		 * and deactivate when needed
		 */
		protected function handleMouseDown(event:MouseEvent):void {
			// prepare the terain for mouseUp
			clickedFigure = null;
			
			// if the click occured inside the InplaceEditor do nothing
			if (inplaceEditor != null && checkTargetIsInplaceEditor(event.target)) {
				return;
			}
			
			// get the IFigure where the click happened
			clickedFigure = getIFigureFromDisplay(event.target);
			
			// click happened in other part
			if (lastActivatedClickedFigure != null 
			 	&& !wasScrollBarClicked
				&& inplaceEditor != event.target
				//&& clickedFigure != lastActivatedClickedFigure
				&& activatedEditPart != null
				&& inplaceEditor != null) {
					updateInplaceEditorText(inplaceEditor);
					// tell mouseUp that it should not activate IED
					clickedFigure = null;
					lastActivatedClickedFigure = null;
					return;
			} 
			
			// if the click was not on a IFigure, do nothing
			if (clickedFigure == null) {
				return;
			}
			
			// gets the first selectable EditPart from clickedFigure's hierarchy
			var selectableEditPart:EditPart = getSelectableEditPart(clickedFigure.getEditPart());
			
			// if selection has more elements or does not contain the EditPart that 
			// was clicked, notify mouseUp not to activate the InplaceEditor
			if (diagramViewer.getSelectedElements().length != 1 
				|| !diagramViewer.getSelectedElements().contains(selectableEditPart)) {
				clickedFigure = null;
				return;
			}
		}
		
		/**
		 * Used to check if a drag is in progress
		 */
		protected function handleMouseMove(event:MouseEvent):void {
			// drag in progress => don't activate editing
			if (event.buttonDown) {
				clickedFigure = null;
			}
		}
		
		/**
		 * Moved the activation of InplaceEditor on mouseUp
		 * because we need to know if the user wanted to edit 
		 * or just to drag.
		 * @flowerModelElementId _b0Kfqb8REd6XgrpwHbbsYQ
		 */
		protected function handleMouseUp(event:MouseEvent):void {
			
			if (clickedFigure != null && inplaceEditor == null) {
				getAndActivateIEDCapableEditPart(clickedFigure.getEditPart(), clickedFigure);
				lastActivatedClickedFigure = clickedFigure;
			}

		}
		
		/**
		 * If an F2 is detected and we have a single element in selection
		 * activate the InplaceEditor for the selection.
		 * @flowerModelElementId _b0Kfr78REd6XgrpwHbbsYQ
		 */
		protected function handleKeyDown(event:KeyboardEvent):void {
			// On F2, activate IED only if no other IED is active.
			if (event.keyCode == Keyboard.F2 
				&& diagramViewer.getSelectedElements().length == 1
				&& inplaceEditor == null) {
				getAndActivateIEDCapableEditPart(EditPart(diagramViewer.getSelectedElements().getItemAt(0)));
				lastActivatedClickedFigure = EditPart(diagramViewer.getSelectedElements().getItemAt(0)).getFigure();
			}
			// TODO sorin :  de ce nu exista un loc global unde se termina cu editarea pentru a activa context menu?
			if (event.keyCode == Keyboard.ESCAPE && inplaceEditor) {
				ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, true);
			}	
		}
		
		/**
		 * Utility function that searches for the first IED capable EditPart 
		 * and activates it
		 * 
		 * <p> This method starting from an edit part, is responsable for obtaining
		 * an inplace editor of it's or it's parent, activating it and updating the enabledness
		 * of the associated context menu for the diagram viewer.
		 *  
		 * @author Sorin 
		 * @flowerModelElementId _b0Kftb8REd6XgrpwHbbsYQ
		 */
		protected function getAndActivateIEDCapableEditPart(startEditPart:EditPart, figure:IFigure=null):void {
			// walk the hierarchy
			while (startEditPart != null) {
				inplaceEditor = startEditPart.activateInplaceEditor(figure);
				if (inplaceEditor != null) {
					activatedEditPart = startEditPart;
					// add handler for model's changes
					activatedEditPart.getModel().addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateModelHandler);		
												
					// If an inplaceEditor has been found we must hide the context menu.  
					ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, false);
					return;
				}
				startEditPart = startEditPart.getParent();
			}
		}
				
		/**
		 * Handler for changes on model's properties.
		 * When the model is deleted, its edit part is also removed so the inplaceEditor must be disposed if active.
		 * @see Bug #5352
		 * 
		 * @author Cristina
		 */ 		
		private function updateModelHandler(event:PropertyChangeEvent):void {
			if (activatedEditPart != null && activatedEditPart.getModel() == null) {
				activatedEditPart.deactivateInplaceEditor(inplaceEditor);	
				activatedEditPart = null;
				inplaceEditor = null;
			}
		}
		/**
		 * Called from InplaceEditorComponent when an event
		 * has happened that requires the current component to be closed
		 * (Enter or Ctrl+Enter pressed)
		 * @flowerModelElementId _b0Kfvb8REd6XgrpwHbbsYQ
		 */
		public function deactivateInplaceEditor(editor:UIComponent):void {
			if (editor == inplaceEditor && activatedEditPart != null) {
				// before deactivate set the focus on its parent
				// usefull so that F2 works for activating every time
				if (editor.parent != null && editor.parent is UIComponent) {
					UIComponent(editor.parent).setFocus();
				}
				activatedEditPart.deactivateInplaceEditor(editor);
				activatedEditPart = null;
				inplaceEditor = null;
				// If the inplace editor is deactivated we can now show the context menu.  
				ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, true);
			}
		}
		
		/**
		 * Called from <code>InplaceEditorComponent</code> when the
		 * component needs to be closed and the text should be saved.
		 * It calls <code>EditPart.updateInplaceEditorText()</code>
		 * method for the active editPart.
		 * @flowerModelElementId _SDjK4NXXEd6axtupYmR2VA
		 */
		public function updateInplaceEditorText(editor:UIComponent):void {
			if (editor == inplaceEditor && activatedEditPart != null) {
				activatedEditPart.updateInplaceEditorText(editor);					
			}
		}
		
		/**
		 * Checks if the received object is, or is a component of, the 
		 * inplaceEditor
		 */
		protected function checkTargetIsInplaceEditor(obj:Object):Boolean {
			if ( !(obj is DisplayObject) )
				return false;
			// walk its hierarchy
			while (obj != null) {
				if ( obj == inplaceEditor )
					return true;
				obj = DisplayObject(obj).parent;
			}
			return false;
		}
		
		public function getIdForActiveEditPart():int {
			if (activatedEditPart != null)
				return activatedEditPart.getModel().id;
			return -1;
		}
		
		public function getActivedEditPart():EditPart {
			return activatedEditPart;
		}
		
		/**
		 * The method activates the InplaceEditor for a given edit part.
		 * If the auto create after editing preference is enabled, activates the IED.
		 * Otherwise the edit part is only selected.
		 * @author Cristina
		 * @flowerModelElementId _yoHQoAHSEeCz6e2GYrABpA
		 */
		public function startAfterCreationInplaceEditor(editPart:EditPart):void {
			if (inplaceEditor == null) {
				// the IED is activated after an element creation
				_activatedAfterCreation = true;
				activateInplaceEditorForEditPart(editPart);
			}
		}
		
		/**
		 * The method activates the InplaceEditor for a given edit part.
		 */ 
		public function activateInplaceEditorForEditPart(editPart:EditPart):void {
			if (inplaceEditor == null) {
				getAndActivateIEDCapableEditPart(editPart);
				lastActivatedClickedFigure = editPart.getFigure();	
			}
		}
		
		/** This tool doesn't need to handle the autoscroll
		*/
		override protected function toolHandlesAutoScroll():Boolean {
			return 	false;
		}
	}
}