package org.flowerplatform.communication.command.flextojava {
	
	import org.flowerplatform.communication.command.flextojava.FlexToJavaDiagramCommand; 
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.callback.FlexCallback;
	
	/**
	 * See the Java corresponding class for doc
	 * 
	 * @author Georgi
	 * @author Cristi
	 * @flowerModelElementId _cUWbcL8REd6XgrpwHbbsYQ
	 */
	[RemoteClass(alias="com.crispico.flower.mp.command.flextojava.FlexToJavaCompoundCommand")]
	public class FlexToJavaCompoundCommand extends FlexToJavaDiagramCommand {
		
		public var commandsList:ArrayCollection;
		
		public var mergeWithPreviousCommand:Boolean;
		
		public var dontExecuteInCommandStack:Boolean;
		
		public var callbackId:Number;
		
		public function FlexToJavaCompoundCommand(mergeWithPreviousCommand:Boolean = false, dontExecuteInCommandStack:Boolean = false) {
			super();
			this.commandsList = new ArrayCollection();	
			this.mergeWithPreviousCommand = mergeWithPreviousCommand;
			this.dontExecuteInCommandStack = dontExecuteInCommandStack;
		}
		
		public function addResultCallback(resultCallbackObject:Object = null, resultCallbackFunction:Function = null):void {
			if (resultCallbackFunction != null) {
				var flexCallback:FlexCallback = new FlexCallback(resultCallbackFunction, resultCallbackObject);
				callbackId = flexCallback.callbackId;
			}
		}

		public function append(command:Object):FlexToJavaCompoundCommand {
			commandsList.addItem(command);
			return this;
		}
	}
}