package org.flowerplatform.flexutil.popup {
	public class ComposedAction extends ActionBase implements IComposedAction {

		private var _childActions:Vector.<IAction>;

		public function get childActions():Vector.<IAction>
		{
			return _childActions;
		}

		public function set childActions(value:Vector.<IAction>):void
		{
			_childActions = value;
		}
		
		override public function get visible():Boolean {
			if (childActions == null) {
				return false;
			} else {
				for (var i:int = 0; i < childActions.length; i++) {
					if (childActions[i].visible) {
						// at least one visible => the composed action is visible
						return true;
					}
				}
				return false;
			}
		}
		
		
	}
}