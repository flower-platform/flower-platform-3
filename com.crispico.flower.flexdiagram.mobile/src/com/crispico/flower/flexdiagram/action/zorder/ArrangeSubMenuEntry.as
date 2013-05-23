package com.crispico.flower.flexdiagram.action.zorder {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IEditPartWithOrderedElements;
	import com.crispico.flower.flexdiagram.contextmenu.ActionEntry;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	import com.crispico.flower.flexdiagram.contextmenu.SubMenuEntry;
	import com.crispico.flower.flexdiagram.contextmenu.SubMenuEntryModel;
	
	import mx.collections.ArrayCollection;
		
	/**
	 * Submenu containing the Arrange Actions.
	 * @see BringToFrontAction
	 * @see SendToBackAction
	 * @see BringForwardAction
	 * @see SendBackwardAction
	 * 
	 * @author Luiza
	 * @flowerModelElementId _u8f0oFOOEeCqc81Ch_V53g
	 */
	public class ArrangeSubMenuEntry extends SubMenuEntry {
		
		[Embed(source = "/icons/bring_to_front.gif")]
		private var IconClass:Class;
		
		
		private var actions:Array;
		
		public function ArrangeSubMenuEntry(parentContextMenu:FlowerContextMenu, sortIndex:int = int.MAX_VALUE) {
			super(new SubMenuEntryModel(IconClass, "Arrange", sortIndex), parentContextMenu);
			actions = [new BringToFrontAction(), new BringForwardAction(), new SendToBackAction(), new SendBackwardAction()];
		}
		
		override protected function createChildren():void {
			super.createChildren();
			getSubMenu().addChild(new ActionEntry(actions[0]));
			getSubMenu().addChild(new ActionEntry(actions[1]));
			getSubMenu().addChild(new ActionEntry(actions[2]));
			getSubMenu().addChild(new ActionEntry(actions[3]));
		}
		
		/**
		 * This kind of actions are enabled on one EditPart or a group of EditParts
		 * that are placed inside the same parent. The parent must subclass IEditPartWithChildOrder.
		 * 
		 * @flowerModelElementId _u8yvkVOOEeCqc81Ch_V53g
		 */ 
		public function isVisible(selection:ArrayCollection):Boolean {
			
			var parentEditPart:EditPart = EditPart(selection.getItemAt(0)).getParent();
			
			if (!(parentEditPart is IEditPartWithOrderedElements)) {
				return false;
			}
			
			for (var i:int = 0; i < selection.length; i++) {
				var ep:EditPart = EditPart(selection.getItemAt(i));
							
				if (ep.getParent() != parentEditPart) {
					return false;
				}
			}
			
			return true;
		}

	}
}