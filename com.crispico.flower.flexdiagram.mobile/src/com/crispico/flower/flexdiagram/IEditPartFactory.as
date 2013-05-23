package com.crispico.flower.flexdiagram {

	/**
	 * 
	 * @author crist
	 * @flowerModelElementId _31vw7shaEd6f3oj4VmBqug
	 */
	public interface IEditPartFactory {
		function createEditPart(viewer:DiagramViewer, context:EditPart, model:Object):EditPart;

	}
}