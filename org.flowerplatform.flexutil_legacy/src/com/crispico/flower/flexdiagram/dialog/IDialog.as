package com.crispico.flower.flexdiagram.dialog {
	
	/**
	 * Dialog that passes the result to an <code>IDialogResultHandler</code> when closed.
	 * The result can be a pressed button, field values or other components depending on the Dialog scope.
	 * 
	 * <p> If the <code>IDialog</code> is closed without a valid result - action aborted, or "Cancel" button is pressed, 
	 * the expected result is <code>null</code>.
	 * 
	 *  
	 * @author Luiza
	 * @flowerModelElementId _Qb-bEFlAEeCimMujaloQ8A
	 */ 
	public interface IDialog {
		
		function setResultHandler(resultHandler:IDialogResultHandler):void;
	}
}