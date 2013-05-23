package  com.crispico.flower.flexdiagram.action {

	import com.crispico.flower.flexdiagram.contextmenu.ActionEntry;

	/**
	 * Represents a children coresponding to a check style action.
	 * It provides a menu entry with a "check" icon that can be executed only if 
	 * the state isn't the same with the checkable action's state, 
	 * at execution the state will be changed with it's opposite (e.g. enabled to disabled, disabled to enabled).
	 * 
	 * By default, the menu entry is disabled and the "check" icon isn't visible.
	 * When the action's state is received, the menu entry becomes enabled and 
	 * the "check" icon will be shown next to the menu entry that represents state of the action. 
	 * 
	 * If the menu entry corresponds to a state of action that is already checkable, then it cannot be executed.
	 * @author Sorin
	 * @author Cristina
	 * @flowerModelElementId _7OoRYLp_Ed-gStBwvLC3Ug
	 */
	public class CheckboxMenuEntry extends ActionEntry {
	
		private const ENABLE_LABEL:String = "Enabled";
		
		private const DISABLE_LABEL:String = "Disabled";
		
		/**
		 * This flag keeps the information about the type of this CheckableMenuEntry, 
		 * true for Enabled representation or false for Disabled representation.
		 */
		private var stateToRepresent:Boolean;

		/**
		 * It is a constant embeded "check" icon that can be shown/hidden depending on the state of the action
		 * that this menu entry represents.
		 */
		 [Embed(source='/icons/checkbox.gif')]
		private var icon:Class;
	
		/**
		 * The contructor will do the following :
		 * <ul>
		 * 	<li> depeding on the stateToRepresent parameter the label of this menu entry will be Enabled for true or Disabled for false;
		 * 	<li> take the action parameter and it will store it for running it later;
		 * </ul>
		 * 
		 * The implementation sets automatically the checked icon, but it makes it invisible, and makes the label grayed.
		 * The method setChecked() takes care of setting the visibility of the icon and of the grayness of the label.
		 * @flowerModelElementId _E7tasLqAEd-gStBwvLC3Ug
		 */
		public function CheckboxMenuEntry(stateToRepresent:Boolean, action:IAction) {
			super(action);
			this.label = (stateToRepresent) ? ENABLE_LABEL : DISABLE_LABEL; 
			this.stateToRepresent = stateToRepresent;
			setShowCheckIcon(false);
			enabled = false;
		}
	
		/**
		 * This method is used to update the appearance of a checkbox menu entry in the following way:
		 * <ul>
		 * 	<li>if the state to represent is different to the action's state then the menu entry can be clicked (the action can be executed), 
		 * 	<li>otherwise the action cannot be executed and a "check" icon can be attached to the menu entry.
		 * </ul>
		 * 
		 * The purpose of this method is to be used to let the 2 checkable menu entries corresponding to a check style action know what state they represent.
		 * The state that a CheckedStateMenuEntry can represent can be known from the beginning or not which can be set later usually using an asynchronous call.
		 * 
		 * @param value - represents the state of the action (enabled or disabled)
		 */
		public function checkStateRequestHandler(value:Boolean):void {
			enabled = (stateToRepresent != value);
			setShowCheckIcon(stateToRepresent == value);
		}	
		
		/**
		 * Updates menu style depending on the state of checkness of the action.
		 */
		private function setShowCheckIcon(value:Boolean):void {
			if (!value) {
				setStyle("icon", null);
				setStyle("paddingLeft", 20);		
			} else {
				setStyle("icon", icon);
				setStyle("paddingLeft", 2);
			}
		}
	}
	
}