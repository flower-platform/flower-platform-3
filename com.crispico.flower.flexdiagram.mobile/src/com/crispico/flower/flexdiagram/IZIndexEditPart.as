package com.crispico.flower.flexdiagram {
	/**
	 * EditPart that provides a sort index. This is used by its parent EditPart to sort its children so that EditParts
	 * with smaller sort index will always be added before those with bigger sort index.
	 * As a result, EditParts with bigger sort index will display their figures in front of others with smaller
	 * sort index.
	 * 
	 * @flowerModelElementId _xDCHMEZEEeCyCbHdiVjmSg
	 */
	public interface IZIndexEditPart {
		/**
		 * @flowerModelElementId _eLTWwEZFEeCyCbHdiVjmSg
		 */
		 function getZIndex():int;
	}
}