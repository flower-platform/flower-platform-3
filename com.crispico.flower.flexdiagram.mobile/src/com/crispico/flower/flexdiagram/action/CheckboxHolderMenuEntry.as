package  com.crispico.flower.flexdiagram.action {

	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuManager;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	import com.crispico.flower.flexdiagram.contextmenu.SubMenuEntry;
	import com.crispico.flower.flexdiagram.contextmenu.SubMenuEntryModel;
	
	import flash.events.Event;
	import flash.sampler.NewObjectSample;

	/**
	 * This class corresponds to an action that can be checkable.
	 * It creates 2 menu entries (enabled and disabled), 
	 * it knows how to request the state of an action and also to update the menu entries.
	 * 
	 * If the action implements ICheckableAction, the state is requested automatically,
	 * otherwise this submenu entry must be extended.
	 *  
	 * @author Sorin
	 * @author Cristina
	 * @flowerModelElementId _UVWkkLqIEd-gStBwvLC3Ug
	 */
	public class CheckboxHolderMenuEntry extends SubMenuEntry {
	
		/**
		 * Keeps the action to be propagated to both enabled and disabled menu entry and for finding the state of the checkness in the 
		 * <code> requestCheckStateFromAction()</code> method.
		 * @flowerModelElementId _qaLtsLqIEd-gStBwvLC3Ug
		 */
		protected var action:IAction;  

		/**
		 * @flowerModelElementId _jF5AsLqIEd-gStBwvLC3Ug
		 */
		private var enabledMenuEntry:CheckboxMenuEntry;

		/**
		 * @flowerModelElementId _jIoysLqIEd-gStBwvLC3Ug
		 */
		private var disableMenuEntry:CheckboxMenuEntry;

		/**
		 * This flag is necessary to request only once the state of the checkness of the action and it is update by the <code>rollOverHandler()</code> method.
		 * @flowerModelElementId _Oh1RULqJEd-gStBwvLC3Ug
		 */
		private var checkedStateRequested:Boolean;

		/**
		 * This constructor adds to the initial behavior of creation of the FlowerContextMenu the initialization and of the addition of the 
		 * <code> enabledMenuEntry</code> and <code>disabledMenuEntry</code> with the state to represent and with the same action as in the checkbox holder menu entry. 
		 * More exactly this menu entry holder it contains an action but it does not run it, and only one of it's child menu entries will execute the same action.
		 * @flowerModelElementId _vGZlMLqIEd-gStBwvLC3Ug
		 */
		public function CheckboxHolderMenuEntry(action:IAction, parentContainer:FlowerContextMenu, sortIndex:int = int.MAX_VALUE) {
			super(new SubMenuEntryModel(action.image, action.label, sortIndex), parentContainer);
			this.action = action;
			// add Enabled menu entry
			enabledMenuEntry = new CheckboxMenuEntry(true, action);			
			this.getSubMenu().addChild(enabledMenuEntry);
			// add Disabled menu entry
			disableMenuEntry = new CheckboxMenuEntry(false, action);
			this.getSubMenu().addChild(disableMenuEntry);			
		}

		/**
		 * Adds to the initial behavior the verification if the <code> checkedStateRequested</code> flag is not set and if so it will call
		 * the <code> requestCheckStateFromAction()</code> method in order to know each menu entry what check state of the action represents.
		 * @flowerModelElementId _2Olv4LqIEd-gStBwvLC3Ug
		 */
		public override function rollOverHander(event:Event):void {
			super.rollOverHander(event);
			if (!checkedStateRequested) { 
				requestCheckStateFromAction();	
				checkedStateRequested = true;
			}			
		}

		/**
		 * This method is called in order to obtain the state of checkness for the action for updating the child menu entries.
		 * It will test if the <code>action </code> is instance of ICheckableAction and if so it will request the checkness state, and after it will call
		 * the handler method <code> checkStateRequestHandler()</code>.
		 * 
		 * <p/> This method makes a synchronous requesting of the state of checkness of the action. It can be extended in order
		 * to offer an asynchronous implementation of obtaining the state, but at the end after obtaining the state it is necessary to call
		 * the <code> checkStateRequestHandler()</code> method for updating the child menu entries.
		 * @flowerModelElementId _A1j5ILqKEd-gStBwvLC3Ug
		 */
		protected function requestCheckStateFromAction():void {
			if (action is ICheckableAction)
				checkStateRequestHandler(ICheckableAction(action).isChecked(this.getSubMenu().getSelection()));	
		}

		/**
		 * This method should be called with the state of checkness of the action as parameter by the <code> requestCheckStateFromAction()>/code> method.
		 * 
		 * It calls for each enabled and disabled menu entry the <code>checkStateRequestHandler()</code> for updating the menu entry.
		 * @flowerModelElementId _zaO8cLqJEd-gStBwvLC3Ug
		 */
		protected function checkStateRequestHandler(value:Boolean):void {
			enabledMenuEntry.checkStateRequestHandler(value);
			disableMenuEntry.checkStateRequestHandler(value);
			
			// this calllater is done so that the submenu will be displayed in the correct position
			getSubMenu().callLater(getSubMenu().updateLocationAsSubContextMenu);	
		}
	}
	
}