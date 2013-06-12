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