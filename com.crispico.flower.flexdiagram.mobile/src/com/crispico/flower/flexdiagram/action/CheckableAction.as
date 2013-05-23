package com.crispico.flower.flexdiagram.action
{
	import mx.collections.ArrayCollection;
	
	/**
	 * This class is extended by FlowerContribution.
	 * @author Cristina
	 * @flowerModelElementId _-n-lcdzyEd-dzN7pKRI8pQ
	 */ 
	public class CheckableAction extends Action2 implements ICheckableAction {

		/**
		 * @flowerModelElementId _-oIWcdzyEd-dzN7pKRI8pQ
		 */
		public function isChecked(selection:ArrayCollection):Boolean {
			return true;
		}
	}
		
}