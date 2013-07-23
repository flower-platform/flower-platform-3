/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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