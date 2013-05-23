package com.crispico.flower.flexdiagram.action {

	import mx.collections.ArrayCollection;

	/**
	 * This interface is implemented by {@link CheckableAction}.
	 * It's method is used to provide the state to be represent by the 2 submenu entries.
	 * 
	 * @author Sorin
	 * @author Cristina
	 * @flowerModelElementId _VYacsLqLEd-gStBwvLC3Ug
	 */
	public interface ICheckableAction extends IAction {

		function isChecked(selection:ArrayCollection):Boolean;
	}

}