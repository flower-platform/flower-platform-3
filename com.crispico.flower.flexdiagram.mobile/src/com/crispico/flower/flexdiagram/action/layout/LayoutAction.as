package com.crispico.flower.flexdiagram.action.layout {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.action.Action2;
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Abstract action defining a skeleton for basic layout actions like align, resize etc.
	 * Subclasses should override the function provided:
	 * <ul>
	 * <li>#canApplyLayout(EditPart)</li>
	 * <li>#getBoundsFromEditPart(EditPart)</li>
	 * <li>#computeNewBounds(ArrayCollection)</li>
	 * <li>#applyNewBounds(Dictionary)</li>
	 * </ul>
	 * 
	 * They should also override <code>isVisible()</code> and use canApplyLayout to obtain the
	 * valid EditParts for this action. 
	 */ 
	public class LayoutAction extends Action2 {
		
		private var _layoutStyle:String;
		
		public function LayoutAction(style:String) {
			super();
			layoutStyle = style;
		}
		
		/**
		 * A constant defined in LayoutStyles speciffic for the type of layout this action will perform.
		 */ 
		protected function get layoutStyle():String {
			return this._layoutStyle;
		}
		
		/**
		 * Each speciffic LayoutAction should override this method and check 
		 * the validity of the received value. Some may expect resize options,
		 * while others may expect align options etc.
		 */ 
		protected function set layoutStyle(value:String):void {
			this._layoutStyle = value;
		}
		
		/**
		 * Calls <code>computeNewBounds(selection)</code> to obtain the new bounds for eligible EditParts then calls
		 * <code>applyNewBounds()</code> on the obtained result.
		 */ 
		override public function run(selection:ArrayCollection):void {
			var eligibleSelection:ArrayCollection = new ArrayCollection;
			for each (var editPart:EditPart in selection) {
				if (canApplyLayout(editPart)) {
					eligibleSelection.addItem(editPart);
				}
			}
			var eligiblePartsToNewBounds:Dictionary = computeNewBounds(eligibleSelection);
			applyNewBounds(eligiblePartsToNewBounds);
		}
		
		/**
		 * This function should return <code>true</code> if the given EditPart is 
		 * eligible for this kind of layout action. By default returns <code>false</code>.
		 * <p>
		 * This function should be overriden in subclasses to select the right EditParts,
		 * depending on their attached model and their moving behavior.
		 * <p> 
		 * Note that this function may be called from isVisible()
		 * as well and must help decide if this action is available or not. 
		 */ 
		protected function canApplyLayout(part:EditPart):Boolean {
			throw new Error("LayoutAction.canApplyLayout() has no implementation");
		}
		
		/**
		 * Computes an Array of maximum 4 positions [x, y, with, height] defining the bounds of the 
		 * given EditPart.
		 * <p>
		 * This function must be overriden to provide the current position and size of the given EditPart.
		 */ 
		protected function getBoundsFromEditPart(part:EditPart):Array {
			throw new Error("LayoutAction.getBoundsFromEditPart() has no implementation");
		}
		
		/**
		 * This function should iterate the selection and obtain eligible EditParts by calling <code>canApplyLayout(editPart)</code>.
		 * For each such EditPart should compute a new bounds Array and put it in the dictionary. The Dictionary returned is of form
		 * [key:EditPart, value:Array]  where the value represents the new bounds for the EditPart.
		 */ 
		protected function computeNewBounds(selection:ArrayCollection):Dictionary {
			throw new Error("LayoutAction.computeNewBounds() has no implementation");
		}
		
		/**
		 * Receives a Dictionary of pairs [key:EditPart, value:Array] where the "key" is an EditPart 
		 * eligible for alignment, taken from the initial selection and "value" is an Array representing
		 * the new computed bounds [x, y, width, height] for this EditPart.
		 */
		protected function applyNewBounds(newBoundsCollection:Dictionary):void {
			throw new Error("LayoutAction.applyNewBounds() has no implementation");
		}
		
	}
}