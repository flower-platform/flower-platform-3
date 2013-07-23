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
package org.flowerplatform.communication.command {
	import mx.collections.ArrayCollection;
	
	/**
	 * This is a command received from java that contains a list of commands to be executed.
	 * @author Sorin
	 * @flowerModelElementId _l8H_gPuBEd6xsfFLsx1UvQ
	 */
	[RemoteClass]
	public class CompoundServerCommand extends AbstractClientCommand {
		
		public var commandsList:ArrayCollection = new ArrayCollection();
		
		public function append(command:Object):CompoundServerCommand {
			commandsList.addItem(command);
			return this;
		}
	}
}