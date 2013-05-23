package com.crispico.flower.flexdiagram.action.zorder {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IEditPartWithOrderedElements;
	
	import mx.collections.ArrayCollection;	
	
	/**
	 * Action that sends one or more selected EditParts in the background.
	 * Keeps the order of the selected Editparts. For instace if  2, 4 are selected
	 * they are sent under all the others in the same order: 2 is under 4 and 4 is placed under all the rest.
	 * 
	 * @author Luiza
	 * @flowerModelElementId _KXzxUE5MEeCT05JFwInpQw
	 */
	public class SendToBackAction extends AbsoluteArrangeAction {
		
		[Embed(source = "/icons/send_to_back.gif")]
		private var IconClass:Class;
		
		public function SendToBackAction() {
			label = "Send To Back";
			image = IconClass;
		}
		
		public override function run(selection:ArrayCollection):void {
			// make a copy of the selection to avoid modifications
			var selectedEditParts:Array = selection.source.slice();
			
			// sort the selected edsitParst descending based on theit indexInParentList
			if (selectedEditParts.length > 1) {			
				selectedEditParts.sort(compareEditParts, Array.DESCENDING);
			}
			
			var parentEditPart:IEditPartWithOrderedElements = IEditPartWithOrderedElements(EditPart(selectedEditParts[0]).getParent());
			
			// order movements
			parentEditPart.repositionChildren(selectedEditParts, [0]);
		}

	}
}