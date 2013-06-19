package org.flowerplatform.communication.sequential_execution {
	/**
	 * Helper interface for BaseFlowerDiagramEditor#sendObject()
	 * to know how to insert details like diagram id into an object
	 * that wrappes a command.
	 * 
	 * @see BaseFlowerDiagramEditor#sendObject()
	 * @author Sorin
	 */
	public interface IServerCommandWrapper {
		
		function set command(value:Object):void;
		
		function get command():Object;
	}
}