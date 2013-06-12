package org.flowerplatform.communication.sequential_execution {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.callback.FlexCallback;

	/**
	 * Queue for commands that will be executed on the server side. Ensures that the execution is done
	 * sequentially, only after the server has signaled that it had finnished the execution of the previously sent command.
	 * 
	 * <p>
	 * Example of usage:
	 * <pre>
	 * var seq : SequentialExecutionQueue = new SequentialExecutionQueue();
	 * 
	 * seq.send(command1);
	 * seq.send(command2);
	 * </pre
	 * 
	 * <p>
	 * If the communication channel is reinitialized, the scheduled commands are disposed.
	 * If this queue is not referenced anymore, it will be safely garbage collected. 
	 * 
	 * <p>
	 * Note : this queue can't ensure the sequentiality of results produced by asynchronous processing.
	 * 
	 * @see SequentialExecutionServerCommand
	 * 
	 * @author Sorin
	 */ 
	public class SequentialExecutionQueue {
		
		private static const allSEQ:Dictionary = new Dictionary(true); // weak reference for keys.
		
		private var queue:ArrayCollection /* of FlexToJavaCommand */ = new ArrayCollection();
		
		private var waitingForCommandToBeExecuted:Boolean = false;
		
		public function SequentialExecutionQueue() {
			allSEQ[this] = null;
		}
		
		/**
		 * @see WebSequentialExecutionQueueExtension
		 */
		public static function cleanAllSEQs():void {
			for (var key:Object in allSEQ) {
				var seq:SequentialExecutionQueue = SequentialExecutionQueue(key);
				seq.queue.removeAll(); 
				seq.waitingForCommandToBeExecuted = false;
			}
		}
	
		public function send(object:Object):void {
			queue.addItem(object);
			sendFirstCommand();
		}

		private function sendFirstCommand():void {
			if (waitingForCommandToBeExecuted)
				return; // Waiting for server to execute and notify us.
			if (queue.length == 0)
				return; // Nothing to be executed.
			
			var firstCommand:Object = queue.removeItemAt(0);
			waitingForCommandToBeExecuted = true;
			
			CommunicationPlugin.getInstance().bridge.sendObject(
				new SequentialExecutionServerCommand (
					firstCommand, 
					new FlexCallback(handleSequentialCommandExecutedSignal, this).callbackId
				)
			);
		}
		
		private function handleSequentialCommandExecutedSignal(result:Object = null):void {
			waitingForCommandToBeExecuted = false;
			sendFirstCommand();
		}
	}
}