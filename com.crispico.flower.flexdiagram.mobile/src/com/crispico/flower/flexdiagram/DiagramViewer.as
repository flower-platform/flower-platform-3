package com.crispico.flower.flexdiagram {
	
	import com.crispico.flower.flexdiagram.action.ActionContext;
	import com.crispico.flower.flexdiagram.action.CreateElementActionContext;
	import com.crispico.flower.flexdiagram.action.CreateRelationActionContext;
	import com.crispico.flower.flexdiagram.action.IAction;
	import com.crispico.flower.flexdiagram.action.IActionProvider2;
	import com.crispico.flower.flexdiagram.contextmenu.ClientNotifierData;
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuManager;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	import com.crispico.flower.flexdiagram.contextmenu.IContextMenuLogicProvider;
	import com.crispico.flower.flexdiagram.event.ToolEvent;
	import com.crispico.flower.flexdiagram.print.ExportPDFManager;
	import com.crispico.flower.flexdiagram.print.PrintManager;
	import com.crispico.flower.flexdiagram.tool.InplaceEditorTool;
	import com.crispico.flower.flexdiagram.tool.MultiDiagramWorkAroundTool;
	import com.crispico.flower.flexdiagram.tool.SelectMoveResizeTool;
	import com.crispico.flower.flexdiagram.tool.Tool;
	import com.crispico.flower.flexdiagram.tool.zoom.PermanentZoomTool;
	import com.crispico.flower.flexdiagram.ui.ConnectionFigure;
	import com.crispico.flower.flexdiagram.util.FlowerArrayCollection;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.Container;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;
	
	/**
	 * Dispatched when a tool is activated.
	 */ 
	[Event(name="ExclusiveToolActivated", type="com.crispico.flower.flexdiagram.event.ToolEvent")]
	
	/**
	 * Dispatched when a tool is deactivated.
	 */
	[Event(name="ExclusiveToolDeactivated", type="com.crispico.flower.flexdiagram.event.ToolEvent")]
	
	/**
	 * Dispatched when an exclusive tool finished job.
	 */ 
	[Event(name="ExclusiveToolJobFinished", type="com.crispico.flower.flexdiagram.event.ToolEvent")]
	
	/**
	 * Dispatched when an exclusive tool is locked (activated again while still active).
	 */ 
	[Event(name="ExclusiveToolLocked", type="com.crispico.flower.flexdiagram.event.ToolEvent")]
	
	/**
	 * The "shell" for a diagram. In order to have a functionnal diagramming
	 * system a user should:
	 * <ul>
	 * 		<li>Subclass <code>EditPart</code> for each graphical model element. The
	 * 			"root" model element is the "diagram"; it would probably subclass
	 * 			<code>AbsoluteLayoutEditPart</code>; "normal" model elements would
	 * 			probably subclass <code>SequentialLayoutEditPart</code>
	 * 		<li>Subclass <code>DiagramViewer</code> and provide an EditPart factory
	 * 		<li>Instantiate a <code>RootFigure</code> and put it somewhere on the 
	 * 			Flex application and instantiate the <code>DiagramViewer</code>
	 * </ul>
	 * 
	 * This class also deals with current selection on the "diagram".
	 * 
	 * Dealing with selection is done only through <code>addToSelection</code>, 
	 * <code>removeFromSelection</code> and <code>resetSelection</code> and <b>not</b>
	 * accesing selectedElements directly. 
	 * 
	 * In order to show a ContextMenu each time a new element is selected on the diagram, 
	 * the user should set as parameter to the constructor an implementation of IActionProvider 
	 * 
	 * @author cristi
	 * @flowerModelElementId _b4MD8b8REd6XgrpwHbbsYQ
	 */
	public class DiagramViewer extends EventDispatcher implements IContextMenuLogicProvider{
		
		/**
		 * Represents the minimum width/height for a small selection area.
		 * Is used to know where to show the CM depending on the mouse position for small areas.
		 * @author Cristina
		 * @flowerModelElementId _d2MSkENJEeCTeqiaPze36w
		 */		
		public static const MINIMUM_SIZE_AREA:Number = 1;

		/**
		 * @flowerModelElementId _b4MD9b8REd6XgrpwHbbsYQ
		 */
		protected var rootEditPart:EditPart;
		
		protected var rootFigure:IFigure;
		
		/**
		 * Used by ConnectionEditParts to retrieve the EditPart associated
		 * to a model element.
		 * @flowerModelElementId _b4V04L8REd6XgrpwHbbsYQ
		 */ 
		protected var modelToEditPartMap:Dictionary = new Dictionary();

		/**
		 * List of selected elements.
		 * This is the current selection of the diagram served by the 
		 * current DiagramViewer.
		 * 
		 * We are using a <code>FlowerArrayCollection so that when we add multiple elements,
		 * only one notification to be dispatched to the listeners.
		 * 
		 * @flowerModelElementId _b4V05b8REd6XgrpwHbbsYQ
		 */
		protected const selectedElements:ArrayCollection = new FlowerArrayCollection();
		
		/**
		 * Main selected element.
		 * Index in the selectedElements collection that represents the main element.
		 * @flowerModelElementId _b4V09b8REd6XgrpwHbbsYQ
		 */		
		protected var mainSelection:int = -1;
		
		/**
		 * @flowerModelElementId _b4V06r8REd6XgrpwHbbsYQ
		 */
		protected var activeTool:Tool;

		/**
		 * The last activeTool before deactivate.
		 * 
		 * On activate reactivate this Tool.
		 * @flowerModelElementId _l9Vz48liEd6hMdHnTBcXLQ
		 */
		protected var deactivatedActiveTool:Tool = null;

		/**
		 * The action provider that populates the context menu associated to this
		 * diagram viewer. If it is null the diagram viewer does not support context menu.
		 * @flowerModelElementId _b4V07b8REd6XgrpwHbbsYQ
		 */ 
		protected var actionProvider:IActionProvider2;
		
		/**
		 * When creating elements by marquing a zone from diagram, this action provider 
		 * will be used in order to populate the context menu with entries.
		 * @flowerModelElementId _-m-kAGZnEeCop6HPVgFZ4g
		 */ 
		protected var createElementActionProvider:IActionProvider2;

		/**
		 * When creating relations by dragging from a relation anchor to an element, this action provider 
		 * will be used in order to populate the context menu with entries.
		 * @flowerModelElementId _QJg2gAXxEeGWzty_NJz5PA
		 */
		protected var createRelationActionProvider:IActionProvider2;
		
		/**
		 * When creating relations by marquing a zone from diagram, this action provider 
		 * will be used in order to populate the context menu with entries.
		 * @flowerModelElementId _Fk41gA7WEeGnv-PD6aKXzA
		 */
		protected var createRelationToExistingElementActionProvider:IActionProvider2;
		
		/**
		 * @flowerModelElementId _b4e-0b8REd6XgrpwHbbsYQ
		 */
		protected var inplaceEditorTool:InplaceEditorTool = null;
		
		/**
		 * The tool is active all the time. When CTRL key is pressed
		 * the mouse wheel changes its position, the current diagram
		 * is being zoomed.
		 * @flowerModelElementId _IfdasMVeEd6x1dpkaVcaXg
		 */
		protected var permanentZoomTool:PermanentZoomTool = null;
		
		/**
		 * @flowerModelElementId _zSxakFnfEeCmRqQ0cEpkTA
		 */		
		protected var printManager:PrintManager = null;
		
		/**
		 * @flowerModelElementId _OfE9oJjHEeCdZMUSEY04IA
		 */
		protected var exportManager:ExportPDFManager = null;
	
		private var _isActive:Boolean;
		
		/**
		 * @see Getter.
		 */
		private var _activateRequested:Boolean;
		
		private var _wasActiveAtLeastOnce:Boolean;
		
		protected var _selectMoveResizeTool:SelectMoveResizeTool;
		
		/**
		 * Because we delegate the rightClickEnabled setting to the data held
		 * in the context menu manager, when this viewer is not yet registered to 
		 * the CM manager and the user sets this property value, we keep this 
		 * value in the <code>rightClickEnabledSettedWhileCMWasDeactivated</code>
		 * variable, in order to later transfer it to the CM manager, when this viewer
		 * will be registered to it.
		 */ 
		private var _rightClickEnabledSettedWhileCMWasDeactivated:Boolean = false;
		
		public function get selectMoveResizeTool():SelectMoveResizeTool {
			return _selectMoveResizeTool;
		}
		
		public function set selectMoveResizeTool(selectMoveResizeTool:SelectMoveResizeTool):void {
			_selectMoveResizeTool = selectMoveResizeTool;
			activateTool(selectMoveResizeTool);
		}
		
		public var currentToolLocked:Boolean;
		
		/**
		 * Data structures that hold the data associeted with this diagramViewer
		 * when it is registered as a client to the contextMenuManager.
		 * It is delegated to verify if the viewer has a right click CM enabled
		 * or not.
		 */ 
		protected var _dataFromContextMenuManager:ClientNotifierData;
		
		/**
		 * Delegates the getting of the rightClickEnabled to the _dataFromContextMenuManager
		 * If the CM wasn't yet activated returns null
		 */ 	
		public function get rightClickEnabled():Boolean {
			return _dataFromContextMenuManager != null ? _dataFromContextMenuManager.rightClickEnabled : false;
		}	
		
		/**
		 * Delegates the setting of the rightClickEnabled to the _dataFromContextMenuManager
		 */
		public function set rightClickEnabled(rightClickEnabled:Boolean):void {
			//if the viewer it is not yet registered to the CM Manager, 
			//keep the setted value in the _rightClickEnabledSettedWhileCMWasDeactivated variable
			// and transfer it to the dataFromContextMenuManager on activateContextMenu()
			if (_dataFromContextMenuManager == null) {
				_rightClickEnabledSettedWhileCMWasDeactivated = rightClickEnabled;
				return;
			}
			_dataFromContextMenuManager.rightClickEnabled = rightClickEnabled;
			
			//Dispatch this event in order for the other classes that use this parameter 
			//(for example <code>SelectMoveResizeTool</code> class) to be notified when 
			// the parameter has been changed 
			this.dispatchEvent(new Event("rightClickEnabledChange"));
		}
		
		public function ContextMenuWasActivated():Boolean {
			if (_dataFromContextMenuManager != null)
				return true;
			else 
				return false;
		}	
				
		/**
		 * Returnes <code>true</code> if this <code>activate()</code> executed.
		 * This flad=g becomes <code>false</code> again when <code>deactivate()</code> is called.
		 * @flowerModelElementId _Dcg08GQKEeCpBYpZYiY2AQ
		 */ 
		public function get isActive():Boolean {
			return _isActive;
		}
		
		/**
		 * @private
		 * This function is intended to be called only in subclasses.
		 * Users should not call this function.
		 * @flowerModelElementId _Dcg082QKEeCpBYpZYiY2AQ
		 */ 
		public function set isActive(active:Boolean):void {
			_isActive = active;
			if (active) {
				// mark the first activation
				_wasActiveAtLeastOnce = true; 
			}
		}
		
		/**
		 * <code>true</code> after a call to <code>activate()</code>.
		 * <code>false</code> after a call to <code>deactivate()</code>.
		 */ 
		public function get activateRequested():Boolean {
			return _activateRequested;
		}
		
		/**
		 * Setter for main selected element
		 * @flowerModelElementId _b4e-1b8REd6XgrpwHbbsYQ
		 */
		public function setMainSelection(val:int):void {
			if (val < 0 || val >= selectedElements.length) {
				throw new Error("MainSelection has to be between selectedElements");
			}
			if (mainSelection != val) {
				// unmark to old main selection
				EditPart(selectedElements.getItemAt(mainSelection)).updateSelectedState(true, false);
				// mark the new main selection 
				mainSelection = val;
				EditPart(selectedElements.getItemAt(mainSelection)).updateSelectedState(true, true);
			}
		}
		
		/**
		 * Getter for main selected element
		 * @flowerModelElementId _b4e-278REd6XgrpwHbbsYQ
		 */
		public function getMainSelection():int {
			return mainSelection;
		}
		
		/**
		 * Getter for InplaceEditorTool
		 * @flowerModelElementId _b4ov0L8REd6XgrpwHbbsYQ
		 */
		public function getInplaceEditorTool():InplaceEditorTool {
			return inplaceEditorTool;
		}
		
		/**
		 * Getter for PermanentZoomTool. Called from
		 * <code>BaseFlowerDesigner</code>.
		 * @flowerModelElementId _IfdatcVeEd6x1dpkaVcaXg
		 */
		public function getPermanentZoomTool():PermanentZoomTool {
			return permanentZoomTool;
		}
		
		/**	
		 * Lazy initialize the PrintManager
		 * 
		 * @flowerModelElementId _N4VTQFk4EeCimMujaloQ8A
		 */
		public function getPrintManager():PrintManager {
			return printManager;
		}
		
		/**
		 * @flowerModelElementId _OfLrUJjHEeCdZMUSEY04IA
		 */
		public function getExportManager():ExportPDFManager {
			return exportManager;
		}
		
		/**
		 * Creates a new DiagramViewer instance. Initialize tools.
		 * 
		 * <p>Calls <code>setRootModel(model)</code> only if the given <code>model</code> is not <code>null</code>
		 * and <code>rootFigure</code> is on the stage.
		 * If these conditions are not fulfilled the user must call <code>setRootModel</code> at the right time.
		 * 
		 * @flowerModelElementId _b4ov1b8REd6XgrpwHbbsYQ
		 */
		public function DiagramViewer(rootFigure:IFigure, actionProvider:IActionProvider2, model:Object = null) {
			this.rootFigure = rootFigure;
			this.actionProvider = actionProvider;
			
			// creates inplaceEditorTool
			inplaceEditorTool = new InplaceEditorTool();
			// creates permanentZoomTool
			permanentZoomTool = new PermanentZoomTool();
			
			if (model && DisplayObject(rootFigure).stage) {
				setRootModel(model);
			}
		} 
		
		/**
		 * If <code>rootEditPart</code> is already created calls <code>resetSelection</code>, deactivates the <code>rootEditPart</code>.
		 * 
		 * If <code>model</code> is not <code>null</code>, creates and activates a new <code>rootEditPart</code>, 
		 * bounds it with the <code>rootFigure</code> and (re)activates this DiagramViewer instance.
		 * 
		 * <p>
		 * If the call comes too early in the application life cycle (i.e. there is no stage), 
		 * the activation is deferred until there is a stage. This is necessary because the tools
		 * usually listen the stage.
		 * @flowerModelElementId _wehx8dqWEd-ShIirE0EYeQ
		 */ 
		public function setRootModel(model:Object):void {			
			// if received a new model, create rootEditPart again and activate it
			if (model) {
				// if model was already set, deactivate existing editPart
				if (rootEditPart) {
					resetSelection();
					removeAllFiguresUnderRootEditPart(rootEditPart);
					rootEditPart.deactivate();
				}
				rootEditPart = getEditPartFactory().createEditPart(this, null, model);
				rootEditPart.setFigure(rootFigure);
				// if model was set to null before, and then passed a new value
				// then this viewer might be inactive so check and activate again
				if (_wasActiveAtLeastOnce) {
					// the wasActive check is necessary because when first activated the figure might not be
					// on the stage yet and handlers can't be initiated
					activate();
				}
				rootEditPart.activate();
			} else {
				// Luiza: Dont't change order again! Viewer must be deactivated before rootEditPart 
				// Otherwise you will obtain NPE when dataProvider is set to null
				
				// we don't need to activate the viewer because activation/
				// deactivation is handled by the MultiDiagramWorkAroundTool
				// TODO: de facut niste ordine; deocamdata ac. metoda nu e folosita decat de gantt; de aici si presupunerea din com. de mai sus
				deactivate();
				if (rootEditPart) {
					resetSelection();
					removeAllFiguresUnderRootEditPart(rootEditPart);
					rootEditPart.deactivate();
				}
				rootEditPart = null;
			}
		}
		
		/**
		 * After setting the model to another value (including null), we need to manually remove the remaining figures.
		 * This is done, because the EditPart.deactivate() method, leaves the orphaned windows on the screen. Its behavior
		 * is normal, because in general, .deactivate() (recursively) leaves figures on screen so that they can be reused.
		 */ 
		protected function removeAllFiguresUnderRootEditPart(rootEditPart:EditPart):void {
			for each (var childEditPart:EditPart in rootEditPart.getChildren()) {
				if (childEditPart.getFigure()) {
					AbsolutePositionEditPartUtils.removeChildFigure(IVisualElementContainer(rootEditPart.getFigure()), IVisualElement(childEditPart.getFigure()));
				}
			}
		}
		
		/**
		 * @flowerModelElementId _b4ov278REd6XgrpwHbbsYQ
		 */
		public function getEditPartFactory():IEditPartFactory {
			throw new Error("DiagramViewer.getEditPartFactory() shoud be implemented by subclasses.");
		}
		
		/**
		 * @flowerModelElementId _b4ov4L8REd6XgrpwHbbsYQ
		 */
		public function getModelToEditPartMap():Dictionary {
			return modelToEditPartMap;
		}
		
		public function addModelToEditPartMapping(model:Object, editPart:EditPart):void {
			modelToEditPartMap[model] = editPart;
		}
		
		public function removeModelToEditPartMapping(model:Object):void {
			delete modelToEditPartMap[model];
		}
		
		/**
		 * @flowerModelElementId _b4ov5L8REd6XgrpwHbbsYQ
		 */
		public function getRootEditPart():EditPart {
			return rootEditPart;
		}
		
		/**
		 * @flowerModelElementId __Lp4lAK1EeCQNuSCOeAuRQ
		 */
		public function getRootFigure():IFigure {
			return rootFigure;
		}
		
		/**
		 * Activates a tool in "exclusive" mode.
		 * 
		 * <p>
		 * Before activates a tool, deactivate the current tool (if one exist).
		 * Checks if the tool is not active on another viewer. Otherwise, throws runtime error.
		 *
		 * <p>
		 * May be called with a null param, meaning that the current tool is deactivated. If the viewer
		 * is not active, the tool is not activated right away. It's "marked" for activation, and it will
		 * become active when the viewer becomes active.
		 * 
		 * <p>
		 * Dispatches tool lifecycle events: <code>ToolEvent.EXCLUSIVE_TOOL_ACTIVATED</code>,  
		 * <code>ToolEvent.EXCLUSIVE_TOOL_DEACTIVATED</code>, <code>ToolEvent.EXCLUSIVE_TOOL_LOCKED</code>.
		 * 
		 * <p>
		 * <code>cycleThroughToolStates</code> => successive calls with the same parameter cycle to the states:
		 * tool activated->tool locked->tool deactivated/default tool (selectMoveResize) selected. "tool locked" state is used only
		 * if the tool allows it.
		 * 
		 * @flowerModelElementId _b4ov7b8REd6XgrpwHbbsYQ
		 */
		public function activateTool(tool:Tool, cycleThroughToolStates:Boolean = true):void {
			if (activeTool == tool) {
				if (cycleThroughToolStates && tool != null && tool != selectMoveResizeTool) {
					// the tool is the active tool (e.g. click twice on the button)
					if (activeTool.canLock() && !currentToolLocked) {
						// => the tool can be locked, and is not locked => we lock
						currentToolLocked = true;
						dispatchEvent(new ToolEvent(ToolEvent.EXCLUSIVE_TOOL_LOCKED, activeTool));
					} else {
						// either the tool cannot be locked => deactivate it + enable default, or
						// or the tool is locked already => deactivate it + enable default
						activateTool(selectMoveResizeTool);
					}
				}
			} else {
				if (tool != null && tool.getDiagramViewer() != null) {
					throw new Error("Tool already activated on another diagramViewer.");
				}
				
				// deactivate current tool
				if (activeTool != null) {
					dispatchEvent(new ToolEvent(ToolEvent.EXCLUSIVE_TOOL_DEACTIVATED, activeTool));
					activeTool.deactivate();
					activeTool = null;
					currentToolLocked = false;
				}
				
				// if the diagram is not active we can not activate the tool, but we register it as
				// as a deactivatedActiveTool in order for the diagram to activate it when on its first activation. 
				if (!isActive) {
					deactivatedActiveTool = tool;
				} else if (tool != null) {
					// activate the received tool on this diagram
					tool.activate(this);
					activeTool = tool;
					dispatchEvent(new ToolEvent(ToolEvent.EXCLUSIVE_TOOL_ACTIVATED, activeTool));
				}
			}
		}

		/**
		 * If the tool is not locked, activates selectMoveResizeTool (if not null).
		 * Dispatches <code>ToolEvent.EXCLUSIVE_TOOL_JOB_FINISHED</code>.
		 */
		public function jobFinishedForExclusiveTool(tool:Tool):void {
			dispatchEvent(new ToolEvent(ToolEvent.EXCLUSIVE_TOOL_JOB_FINISHED, tool));
			if (!currentToolLocked && selectMoveResizeTool != null) {
				activateTool(selectMoveResizeTool);
			}
		}
		
		/**
		 * For doc see <code>AbstractGanttDiagramFigure</code>. We put the doc. there
		 * and removed it from here to avoid duplication.
		 *     
		 * @flowerModelElementId _b4ov6L8REd6XgrpwHbbsYQ
		 */
		public function getSelectedElements():ArrayCollection {
			return selectedElements;
		}
		
		/**
		 * Given some editParts to be added, this method does the following:
		 * <ul>
		 *  <li> clears the selection if the <code>callResetSelection</code> parameter is true;
		 * 	<li> updates the selected state and the main selected state of each of the EditPart;
		 *  <li> updates the main selection of the DiagramViewer;
		 *  <li> updates the selected elements list of the DiagramViewer.
		 *  <li> call #notifySelectionHasChanged method to notify that the addition has been ended.
		 * </ul>
		 * 
		 * This method is in such way written so it will dispatch only one CollectionEvent when 
		 * inserting the editParts into the DiagramViewer's selectedElements collection.
		 * 
		 * This method ensures that the editParts to be added are not already in the DiagramViewer's selection list
		 * or if they are duplicated. If so, it ignores them.
		 * 
		 * @param object an <code>EditPart</code> or an <code>ArrayCollection</code> or 
		 * 			an <code>Array</code> of EditParts.
		 * @param callResetSelection by default false. If true it will reset the selection.
		 * 
		 * @private
		 * 
		 * @author Sorin
		 * @flowerModelElementId _b4yg0b8REd6XgrpwHbbsYQ
		 */
		public function addToSelection(object:Object, callResetSelection:Boolean=false, addToSelectionByRightClick:Boolean = false):void {
			// We keep this information in order to know if changes have been made if a reset is requested.
			// We need this information to know if a selection changed event is really needed.
			var selectionHadElementsBeforeReset:Boolean = selectedElements.length != 0;
			// Empty the actual selection.
			if (callResetSelection) {
				resetSelection(true, addToSelectionByRightClick);
			}

			var proposedEditPartsToAdd:ArrayCollection /* of EditPart */;
			if (object is ArrayCollection) {
				proposedEditPartsToAdd = object as ArrayCollection;
			} else if (object is Array) {
				proposedEditPartsToAdd = new ArrayCollection(object as Array); 
			} else {
				proposedEditPartsToAdd = new ArrayCollection([object]); // Atention to the syntax for flex begginers!
			}
			// This keeps the final list that are valid to be added to the selectedElements list of the DiagramViewer.
			var validEditPartsToAdd:ArrayCollection = new ArrayCollection();
			for each (var editPartToAdd:EditPart in proposedEditPartsToAdd) {
				// If the proposed editPart to be added is already selected, 
				// or it was already encountered earlier in the list of proposed editparts then we skip. 
				if (selectedElements.contains(editPartToAdd) || validEditPartsToAdd.contains(editPartToAdd))
					continue;
				// We have a valid editPart, so we add it to the list. 
				validEditPartsToAdd.addItem(editPartToAdd);
				// Do some magic and make the editPart know it is now selected.
				editPartToAdd.updateSelectedState(true, false); // For the moment we are not interested in the main one, we set it at the end.
			}
			
			// If some new valid edit parts must be added, then we operate on the selecteElements and on the main Selection.
			if (validEditPartsToAdd.length != 0) {
				// Do the adding of the valid EditParts to the DiagramViewer's selection.
				this.selectedElements.addAll(validEditPartsToAdd);
				
				// Next follows the setting of the main EditPart selected.
				// If a Main EditPart existed then make it normal.
				if (mainSelection > -1) {
					EditPart(selectedElements[mainSelection]).updateSelectedState(true, false);
				}
				
				// Now make the EditPart from the end the Main one. We do this only after we add to the selectedElements, all the
				// valid EditParts to be sure that the elements exist.
				mainSelection = selectedElements.length - 1;
				EditPart(selectedElements[mainSelection]).updateSelectedState(true, true);
			}
			
			// Now make the system know that we have finished adding or removing(if requested) to the selection and call a notification method.
			if 	(validEditPartsToAdd.length != 0 || callResetSelection && selectionHadElementsBeforeReset) {
				notifySelectionHasChanged(addToSelectionByRightClick);
			}
		}
		
		/**
		 * Removes an <code>EditPart</code> from selection,
		 * and calls the appropiate function from EditPart to update 
		 * its selected state.
		 * 
		 * @private
		 * 
		 * @flowerModelElementId _b4yg178REd6XgrpwHbbsYQ
		 */	
		public function removeFromSelection(editPart:EditPart):void {
			if (!selectedElements.contains(editPart)) {
				throw new Error("Attempting to unselect an element that is not selected");
			}
			removeFromSelectionAtIndex(selectedElements.getItemIndex(editPart));
		}
		
		
		// TODO sorin : refactor la fel sa poata se se stearga mai mult elemente. Asta ar fi nevoie 
		// la conexiuni dar fiindca la gantt nu avem momentan asa ceva nu am revizuit.
		/**
		 * Removes an EditPart from selection based on its index,
		 * and calls the appropiate function from Editpart to update 
		 * its selected state and the main selection.
		 * 
		 * @private
		 * 
		 * @flowerModelElementId _b4yg3b8REd6XgrpwHbbsYQ
		 */
		public function removeFromSelectionAtIndex(index:int):void {
			// check index to be inside selectedElements
			if (index < 0 || index >= selectedElements.length) {
				throw new Error("Attempting to remove an index of a non existent element");
			}
			
			var editPartToBeRemoved:EditPart = selectedElements[index] as EditPart;
			
			// Make the editPart know it is not selected anymore.
			editPartToBeRemoved.updateSelectedState(false, false);
			
			// Also update the DiagramViewer's list of selected items.
			selectedElements.removeItemAt(index);
			
			// Update the main selection from here in the following mode:
			// Because we know that in order to remove, there where at least one element, it means
			// that some element existed with the main selection. Further more it can happen
			// two things, either we have removed the last element which means we element before the last
			// must now become the main selection, or either an element before the last has been removed, so
			// the main selection state of the last stays the same. We do in this way so that we do not trigger
			// unnecessary updating of the state for the editpart.
			if (selectedElements.length == 0) { // No more elements, no one can be the main selection.
			 	mainSelection = -1;
			}  else {
				if (selectedElements.length == index) { // Take an example to understand this. More exactly the last was deleted.
					mainSelection = selectedElements.length - 1;
					EditPart(selectedElements[mainSelection]).updateSelectedState(true, true);
				} else { // The last one was not removed so it can stay selected, but the index for it in the list has been changed
					mainSelection--;
				}
				
			}
			
			// At the end dispatch notification that DiagramViewer's selectedElements has been changed.
			notifySelectionHasChanged();
		}
		
		/**
		 * Reset current selection.
		 * Remove every element from selection
		 * 
		 * @param calledDuringAdd Tells that this method was called during 
		 * addToSelection()
		 * 
		 * @private
		 * 
		 * @flowerModelElementId _b4yg478REd6XgrpwHbbsYQ
		 */
		public function resetSelection(calledDuringAdd:Boolean = false, resetSelectionByRightClick:Boolean = false):void {
			// First we update each EditPart to know that it is not selected or main selected any more.
			for each (var editPartToRemove:EditPart in selectedElements) {
				editPartToRemove.updateSelectedState(false, false);
			}
			// At the end remove all elements from the DiagramViewer's selectedElements list.
			selectedElements.removeAll();
			
			// Reset main selection index too
			mainSelection = -1;
			
			if (!calledDuringAdd) { // More exactly if it was called directly and not by add.
				notifySelectionHasChanged(resetSelectionByRightClick);
			}
		}	
		
		/**
		 * This method is called after some editParts have been added or removed or reset selection has happened 
		 * to the DiagramViewer's selectedElements.
		 * More exactly this method is called by:
		 * <ul>
		 *  <li> #addToSelection;
		 *  <li> #resetSelection when called dirrectly and not as a result of the addition to the selection;
		 *  <li> #removeFromSelectionAtIndex as a result of deleting only one item.
		 * </ul>
		 * Intended to be overriden by subclassing diagram viewers.
		 * @flowerModelElementId _QEScYGpuEeCjZqR9ugnK5Q
		 */ 
		protected function notifySelectionHasChanged(selectionChangedByRightClick:Boolean = false):void {
			// NOTE: use callLater to decrease the time for selection on mouseDown (see SelectMoveResizeTool)
			// this command has a visual effect and should not be affected by this modification
			// but decreases notably the time of the mouseDown call (from ~45 ms to ~5 ms). 
			// @author Luiza
			UIComponent(rootEditPart.getFigure()).callLater(ContextMenuManager.INSTANCE.refresh, [this, null, selectionChangedByRightClick]);
		}
		
		/**
		 * Getter for the current active tool.
		 * @flowerModelElementId _b4yg6L8REd6XgrpwHbbsYQ
		 */
		public function getActiveTool():Tool {
			return activeTool;
		}
		
		/**
		 * Sometimes, when the diagram is deactivated we need to know
		 * what tool will be reactivated when the diagram first become
		 * active again.
		 * Dana:this is temporary
		 */ 
		public function getDeactivatedTool():Tool {
			return deactivatedActiveTool;
		}
		
		/**
		 * Called when the diagram is first loaded, or at any time (by the user).
		 * 
		 * <p>
		 * This method actually performs the activation in real time, if 
		 * <code>MultiDiagramWorkAroundTool.INSTANCE.currentViewer == this</code> OR if
		 * <code>forceActivation</code> is <code>true</code>. Otherwise
		 * activation is deferred, and <code>MultiDiagramWorkAroundTool</code> when the user
		 * moves the mouse over the root figure corresponding to this viewer.
		 * 
		 * @flowerModelElementId _TSNhgMK_Ed6-yKDFMO4rZg
		 */
        // this parameter has to be on false, otherwise there will be problems during gantt activation,
        // because it is called during construction time, forcinga ctivation will try to add listeners on stage too soon
        // (the diagram is not stage any more)
		public function activate(forceActivation:Boolean = false):void {
			_activateRequested = true;
			if (!isActive) {
				
				if (forceActivation && MultiDiagramWorkAroundTool.INSTANCE.currentViewer != this) {
					// if the activation is forced, we simulate a mouse over the current viewer, i.e. the logic from
					// MultiDiagramWorkAroundTool.mouseMoveHandler(): we deactivate the old viewer with flag = false and 
					// we set the current viewer as being this
					if (MultiDiagramWorkAroundTool.INSTANCE.currentViewer != null) {
						MultiDiagramWorkAroundTool.INSTANCE.currentViewer.deactivate(false);
					}
					MultiDiagramWorkAroundTool.INSTANCE.currentViewer = this;
				}
				
				if (MultiDiagramWorkAroundTool.INSTANCE.currentViewer == this) {
					// performs the actual activation only if not already active and
					// if this is the current viewer of the application. Otherwise, only the
					// flag is set, and MultiDiagramWorkAroundTool will activate later, when
					// the user goes with the mouse over the diagram.
					isActive = true;
					activateTools();
					// When the diagram is activated we register the Context Menu if uses one.
					activateContextMenu();
				}
			}
		}
		
		/**
		 * Activates the "permanent" tools and the current tool.
		 */
		protected function activateTools():void {
			// reactivate the "permanent" Tools
			inplaceEditorTool.activate(this);
			permanentZoomTool.activate(this);
			activateTool(deactivatedActiveTool);
		}
		
		/**
		 * @flowerModelElementId _TdF70MK_Ed6-yKDFMO4rZg
		 */
		public function deactivate(updateActivateRequestedFlag:Boolean = true):void {
			if (updateActivateRequestedFlag) {
				_activateRequested = false;
			}
			if (isActive) {
				// When the diagram is deactivated we unregister the Context Menu if uses one.
				deactivateTools();
				deactivateContextMenu();
				isActive = false;
			}			
		}
		
		/**
		 * Deactivates the "permanent" tools and the current tool.
		 */
		protected function deactivateTools():void {
			// deactivate "permanent" Tools
			inplaceEditorTool.deactivate();
			permanentZoomTool.deactivate();
			
			// mark active tool to be reactivated			
			deactivatedActiveTool = activeTool;
			// deactivate the activeTool
			activateTool(null);
		}
		
		/**
		 * Called when the tab containing the diagram is closed.
		 * 
		 * @author mircea
		 * @flowerModelElementId _WeuEgAwjEd-zN75pwZjaVg
		 */
		public function closeDiagram():void {
			// do nothing				
		}
		
		/**
		 * In case we need to perform the same operations we
		 * perform after the selection is changed, but without
		 * changing the selection. 
		 * 
		 * @author mircea 
		 * @flowerModelElementId _EDoT4BGiEd-9IYezWV4Flw
		 */
		public function touchSelection():void {
			// do nothing
		}
		
		/**
		 * This method is intended to be called when activating a diagram
		 * and it will process if this diagram viewer supports context menu
		 * by checking if an action provider has been assigned.
		 * 
		 * @author Sorin 
		 * @flowerModelElementId _3u8rcM_pEd-LVLAmkCpx7g
		 */ 
		protected function activateContextMenu():void {			
			if (actionProvider != null) {
				var oldRightClickSetting:Boolean = false;
				if (_dataFromContextMenuManager != null) {
					oldRightClickSetting = _dataFromContextMenuManager.rightClickEnabled;
				} else {
					oldRightClickSetting = _rightClickEnabledSettedWhileCMWasDeactivated;
					_rightClickEnabledSettedWhileCMWasDeactivated = false;
				}
				
				_dataFromContextMenuManager = ContextMenuManager.INSTANCE.registerClient(getViewerForContextMenu(), false, this, actionProvider, beforeFillContextMenuFunction, beforeShowContextMenuFromFlashFunction, contextMenuEntryLabelFunction, this, false, 25);
				rightClickEnabled = oldRightClickSetting; 
			}
		}
		
		/**
		 * Method intended to be called on deactivation of the diagram.
		 * Will check if this diagram viewer supports the context menu in order 
		 * to stop the context menu framework from responding.
		 * 
		 * @author Sorin 
		 * @flowerModelElementId _3u8rc8_pEd-LVLAmkCpx7g
		 */ 
		protected function deactivateContextMenu():void {
			if (actionProvider != null) {
				ContextMenuManager.INSTANCE.unregisterClient(null, this);
			}
		}
		
		/**
		 * When this diagram viewer knows it can work with a context menu 
		 * then this method will  return the graphical component which represents the zone where
		 * the associated context menu will be active.
		 * 
		 * @author Sorin 
		 * @flowerModelElementId _3u9Sgs_pEd-LVLAmkCpx7g
		 */ 
		protected function getViewerForContextMenu():UIComponent {			
			return UIComponent(rootEditPart.getFigure());
		}

		/**
		 * If <code>rootFigure</code> is RootFigure, it returns the scale factor.
		 * Otherwise, returns 1.
		 * @author Cristina
		 * @flowerModelElementId _k76woJDQEeCQu7heibezUg
		 */ 
		public function getScaleForContextMenu():Number {
			if (rootFigure is RootFigure) {
				return RootFigure(rootFigure).getScaleFactor();
			}
			return 1;
		}
		/**
		 * This method must be implemented by each specific diagram viewer
		 * in order to return the coordinates relative to the stage of the selection.
		 * It is recommended to return the coordinates of the stage of the main selection and 
		 * a helper method is <code> getMainSelectedFigure()</code>.
		 * If there is nothing selected or the figure of the selection is not visible it is allright
		 * to return null because it will be interpreted as the figure is not visible on the screen. 
		 * @author Sorin
		 * @flowerModelElementId _3u95kM_pEd-LVLAmkCpx7g
		 */ 
		public function get displayAreaOfSelection():Rectangle {
			throw "DiagramViewer.displayAreaOfSelection() must be implemented";
		}
		
		/**
		 * This method must be implemented by each specific diagram viewer
		 * in order to return if a given point is over a selected object.
		 * @author Cristina
		 * @flowerModelElementId _yPuo8o3DEeCQhoqZFpXh6A
		 */ 
		public function isOverSelection(event:MouseEvent):Boolean {
			throw "DiagramViewer.isOverSelection() must be implemented";
		}
		
		/**
		 * Intended to be used by the subclases that override <code> displayAreaOfSelection()</code> method.
		 * It return null in the following cases:
		 * <ul>
		 * 	<li> nothing is selected;
		 * 	<li> the figure associated to the main selected edit part, is null, possibly because it was not instanciated or
		 * 		the area where the it should be shown is not visible. 
		 * </ul>   
		 * @author Sorin.
		 * @flowerModelElementId _AQTbUNU2Ed-_pe20qTUaAg
		 */ 
		public function getMainSelectedFigure():UIComponent {
			// There is nothign selected
			if (getMainSelection() == -1)
				return null;

			var mainSelectedEditPart:EditPart = EditPart(getSelection().getItemAt(getMainSelection()));
			var figure:UIComponent = UIComponent(mainSelectedEditPart.getFigure());
			return figure;
		}

		/**
		 * The selected editparts are exposed through this method of the <code>ISelectionProvider</code>
		 * interface because an action associated to a menu entry is run, we need to pass it the context.
		 * @see MenuEntry's mouse down handler.
		 * @author Sorin
		 * @flowerModelElementId _3u95k8_pEd-LVLAmkCpx7g
		 */ 
		public function getSelection():ArrayCollection {
			return selectedElements;
		}		
		
		/**
		 * This method can be overrided in order to provide custom behavior 
		 * before showing the context menu from Flash menu entry.
		 * <p/>
		 * By default, it selects the object found under the mouse position
		 * where the right click was pressed.
		 * <p/>
		 * If superclasses overrides it to return <code>false</code>, then the custom Context Menu entry will
		 * not be displayed in Flash menu.
		 * <p/>
		 * The method gets the editPart under a mouse position,
		 * walks on it's hierarchy until it gets a selectable editPart
		 * and adds it to selection.
		 * 
		 * @see ContextMenuManager
		 * @see #activateContextMenu()
		 * 
		 * @author Cristina
		 * @flowerModelElementId _yPuB4I3DEeCQhoqZFpXh6A
		 */ 
		protected function beforeShowContextMenuFromFlashFunction(point:Point):Boolean {
			if (point == null) {
				return true;
			}			
			var editPart:EditPart = SelectMoveResizeTool.getTargetEditPart(this, null, point.x, point.y);
			if (editPart == rootEditPart) {
				resetSelection(false);				
				return true;
			}
			
			// walk the hierarchy
			while (editPart != null && editPart != rootEditPart) {
				if (editPart.isSelectable()) {
					addToSelection(new ArrayCollection([editPart]), true);
					// We have found the editpart so we stop.
					return true;	
				}
				editPart = editPart.getParent();
			}
			return true;
		}
						
		public function getCreateElementActionProvider():IActionProvider2 {
			return createElementActionProvider;
		}
		
		/**
		 * @flowerModelElementId _-nI8E2ZnEeCop6HPVgFZ4g
		 */
		public function setCreateElementActionProvider(value:IActionProvider2):void {
			createElementActionProvider = value;
		}
		
		public function getCreateRelationActionProvider():IActionProvider2 {
			return createRelationActionProvider;
		}
		
		/**
		 * @flowerModelElementId _aDLqUQXxEeGWzty_NJz5PA
		 */
		public function setCreateRelationActionProvider(value:IActionProvider2):void {
			createRelationActionProvider = value;
		}
		
		/**
		 * @flowerModelElementId _OSKHoA7WEeGnv-PD6aKXzA
		 */
		public function getCreateRelationToExistingElementActionProvider():IActionProvider2 {
			return createRelationToExistingElementActionProvider;
		}
		
		/**
		 * @flowerModelElementId _OSoo4Q7WEeGnv-PD6aKXzA
		 */
		public function setCreateRelationToExistingElementActionProvider(value:IActionProvider2):void {
			createRelationToExistingElementActionProvider = value;
		}
		
		/**
		 * Factory method for providing an object that is of type <code>ActionContext</code>. 
		 * This context object will be used to be passed to the #fillCreateElementActionContext
		 * for filling it with properties computed at the end of the drop when creating by marquing.
		 * 
		 * <p/>
		 * After filling with properties, this context will be assigned to an action's context property.
		 * For more details see <code>FlowerContextMenu#addMenuEntryForActionIfVisible()</code>.
		 * 
		 * <p/>
		 * This method can be overrided to provide some custom properties in the context object that will
		 * be assigned to the action's context. Note that to also the #fillCreateElementActionContext method
		 * must be overriden to fill the custom properties, because this method is intended only for creaing a new instance. 
		 * @flowerModelElementId _e291AIhTEeC3D4t2GHNvxQ
		 */ 
		public function getCreateElementActionContext():ActionContext {
			return new CreateElementActionContext();
		}
		
		/**
		 * Factory method for providing an object that is of type <code>ActionContext</code>. 
		 * This context object will be used to be passed to the #fillCreateRelationActionContext
		 * for filling it with properties computed at the end of the drop when creating a relation by draging
		 * from an element relation anchor to another element.
		 * 
		 * <p/>
		 * After filling with properties, this context will be assigned to an action's context property.
		 * For more details see <code>FlowerContextMenu#addMenuEntryForActionIfVisible()</code>.
		 * 
		 * <p/>
		 * This method can be overrided to provide some custom properties in the context object that will
		 * be assigned to the action's context. Note that to also the #fillCreateRelationActionContext method
		 * must be overriden to fill the custom properties, because this method is intended only for creaing a new instance. 
		 * 
		 * @author Cristina
		 * @flowerModelElementId _fTshsAXxEeGWzty_NJz5PA
		 * 
		 * @private
		 */
		public function getCreateRelationActionContext():ActionContext {
			return new CreateRelationActionContext();
		}
				
		/**
		 * This method is called only once for filling a <code>ActionContext</code>, and will be assiged as context 
		 * for the actions provided by the user for available when creating by marquing the diagram.
		 * 
		 * <p/> 
		 * Contributes to the context with the following:
		 * <ul>
		 * 	<li> selection x coordinate;
		 *  <li> selection y coordinate;
		 *  <li> selection width coordinate;
		 *  <li> selection height coordinate;
		 *  <li> the diagram figure;
		 *  <li> selection start drag editpart;
		 *  <li> selection start drag model.
		 * </ul>
		 * 
		 * <p>
		 * For details about adding custom properties to the context check #getCreateElementActionContext.
		 *  
		 * <p>
		 * For more details @see CreateElementActionContext
		 * 
		 * @param context - context in which to insert computed informations about the current tool state
		 * @param marqueeRectangle - a graphical component that represents the marquing zone
		 * @author Sorin
		 * @flowerModelElementId _HoxOkAYfEeG32_L7nvkt_Q
		 */ 
		public function fillCreateElementActionContext(context:ActionContext, marqueeRectangle:UIComponent):void {
			var createActionContext:CreateElementActionContext = context as CreateElementActionContext;
			createActionContext.diagramFigure = this.getRootFigure();

			createActionContext.x = marqueeRectangle.x;
			createActionContext.y = marqueeRectangle.y;
			createActionContext.width = marqueeRectangle.width;
			createActionContext.height = marqueeRectangle.height;
			
			createActionContext.startDragEditPart = SelectMoveResizeTool.getEditPartUnderPointRelativeToContent(this, marqueeRectangle.x, marqueeRectangle.y);
			createActionContext.startDragModel = createActionContext.startDragEditPart.getModel(); 
		}
		
		/**
		 * This method is called only once for filling a <code>ActionContext</code>, and will be assiged as context 
		 * for the actions provided by the user for available when creating new relations on diagram by draging
		 * from an element relation anchor to another element.
		 * 
		 * <p/> 
		 * Contributes to the context with the following:
		 * <ul>
		 * 	<li> source x coordinate;
		 *  <li> cource y coordinate;
		 *  <li> target x coordinate;
		 *  <li> target y coordinate;
		 *  <li> source editpart;
		 *  <li> target editpart;
		 * </ul>
		 * 
		 * <p>
		 * For details about adding custom properties to the context check #getCreateRelationActionContext.
		 *  
		 * <p>
		 * For more details @see CreateRelationActionContext
		 * 
		 * @param context - context in which to insert computed informations about the current tool state
		 * @param connectionPlaceHolder - a graphical component that represents the relation
		 * @param sourceEditPart - the relation start editpart
		 * @param targetEditPart - the relation end editpart
		 * @author Cristina
		 * 
		 * @private
		 */ 
		public function fillCreateRelationActionContext(context:ActionContext, connectionPlaceHolder:ConnectionFigure, sourceEditPart:EditPart, targetEditPart:EditPart):void {
			var createActionContext:CreateRelationActionContext = context as CreateRelationActionContext;
			createActionContext.diagramFigure = this.getRootFigure();
			
			createActionContext.sourceX = connectionPlaceHolder._sourcePoint.x;
			createActionContext.sourceY = connectionPlaceHolder._sourcePoint.y;
			
			createActionContext.targetX = connectionPlaceHolder._targetPoint.x;
			createActionContext.targetY = connectionPlaceHolder._targetPoint.y;
			
			createActionContext.sourceEditPart = IConnectableEditPart(sourceEditPart);
			createActionContext.targetEditPart = IConnectableEditPart(targetEditPart);					
		}
		
		/**
		 * In the context that to this diagram viewer an actionProvider has been given in order to register as a client 
		 * to the ContextMenuManager, this method is called before calling the actionProvider's fillContextMenu and 
		 * it assigns to the contextMenu a function that sets the context of each action that is evaluated using the
		 * <code>FlowerContextMenu#addMenuEntryIfActionIsVisible</code> method.
		 */ 
		private function beforeFillContextMenuFunction(contextMenu:FlowerContextMenu):void {
			// Obtain a specialized instance of context.
			var context:ActionContext = actionProvider.getContext();
			// and delegate the filling to the client.
			fillActionContext(context);
			// Before ContextMenu checks the visibility of the action, the context must first be assigned. 
			contextMenu.beforeActionVisibilityEvaluatedFunction = 	
				function(action:IAction):void {
					action.context = context;
				};
		}
		/**
		 * Factory method for providing an object that is of type <code>ActionContext</code>. 
		 * This context object will be used to be passed to the #fillActionContext
		 * for filling it with properties.
		 * 
		 * <p/>
		 * After filling with properties, this context will be assigned to an action.
		 * For more details see <code>FlowerContextMenu#addMenuEntryForActionIfVisible()</code>.
		 * 
		 * <p/>
		 * This method can be overrided to provide custom properties in the context object that will
		 * be assigned to the action's context. Note that to also the #fillActionContext method
		 * must be overriden to fill the custom properties. 
		 * @flowerModelElementId _e3CtgIhTEeC3D4t2GHNvxQ
		 */ 
		public function getContext():ActionContext {
			return new ActionContext(); 
		}
		/**
		 * This method fills a context that will be used to be assign to the actions needed to be evaluated for visibility by using
		 * <code>FlowerContextMenu#addMenuEntryIfActionIsVisible</code> method. At the moment the mentioned method is used only be gantt.
		 *  
		 * <p>
		 * For details about adding custom properties to the context that will be passed to each action check #getContext.
		 * 
		 * @private
		 */ 
		public function fillActionContext(context:ActionContext):void {
			context.diagramFigure = this.getRootFigure();
		}
		/**
		 * @flowerModelElementId _yPxsQI3DEeCQhoqZFpXh6A
		 */
		protected function contextMenuEntryLabelFunction():String {
			return "Show Context Menu";
		}
		
		public function setFocusOnMainSelectedObject():void {
			var figure:UIComponent = getMainSelectedFigure();
			if (figure != null) {
				figure.setFocus();
			}
		}
	}

}