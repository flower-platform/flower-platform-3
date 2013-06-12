package org.flowerplatform.communication.command {
	import mx.collections.ArrayCollection;
	
	/**
	 * This is a command received from java that contains a list of commands to be executed.
	 * @author Sorin
	 * @flowerModelElementId _l8H_gPuBEd6xsfFLsx1UvQ
	 */
	[RemoteClass]
	public class CompoundClientCommand extends AbstractClientCommand {
		
		public var commandsList:ArrayCollection;
		
		/**
		 * @flowerModelElementId _l8H_hPuBEd6xsfFLsx1UvQ
		 */
		override public function execute():void {
			for each (var command:AbstractClientCommand in commandsList) {
				command.execute();
			}	
		}
	}
}