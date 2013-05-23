package com.crispico.flower.flexdiagram.action.zorder {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IEditPartWithOrderedElements;
	
	import mx.collections.ArrayCollection;
	
	
	/**
	 * Action that brings one or more selected EditParts in front of all the others.
	 * Keeps the order of the selected Editparts. For instace if  2, 4 are selected
	 * they are brought on top in the same order 2 is under 4 but it is placed over all the others.
	 * 
	 * @author Luiza
	 * @flowerModelElementId _FKDTAE5MEeCT05JFwInpQw
	 */
	public class BringToFrontAction extends AbsoluteArrangeAction {
		
		[Embed(source = "/icons/bring_to_front.gif")]
		private var IconClass:Class;
		
		public function BringToFrontAction() {
			label = "Bring To Front";
			image = IconClass;
		}	
		
		public override function run(selection:ArrayCollection):void {
			// duplicate contents to avoid modifications on the selection list
			var selectedEditParts:Array = selection.source.slice();
			
			// rank EditParts using indexInParentList
			if (selectedEditParts.length > 1) {			
				selectedEditParts.sort(compareEditParts);
			}
			
			var parentEditPart:IEditPartWithOrderedElements = IEditPartWithOrderedElements(EditPart(selectedEditParts[0]).getParent());
			var indexList:Array = new Array(selectedEditParts.length);
			
			// compute the indexes
			for (var i:int; i < selectedEditParts.length; i++) {
				var ep:EditPart = selectedEditParts[i];
				indexList[i] = parentEditPart.getModelHolderList(ep).length - 1;
			}
			parentEditPart.repositionChildren(selectedEditParts, indexList);
		}

	}
}