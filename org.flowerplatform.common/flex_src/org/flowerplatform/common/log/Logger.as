package org.flowerplatform.common.log {
	
	import mx.controls.Alert;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Sorin
	 */ 
	public class Logger {
		
		private var tag : Object;
		
		private var logEnabled : Boolean;
		
		private var debugEnabled : Boolean;
		
		private var stackLogEnabled : Boolean;
		
		private var allMessages:String = "";
		
		/**
		 * @param tag the tag to be printed in front of the message
		 * @param logEnabled flag to know if to process logging
		 * @param debugEnabled usefull for enabling some certain log like custom class implementations etc.
		 * @param stackLogEnabled Shows also stacktrace 
		 */ 
		public function Logger(tag : Object, logEnabled : Boolean = false, debugEnabled : Boolean = false, stackLogEnabled : Boolean = false) : void {
			this.tag = tag;
			this.logEnabled = logEnabled;
			this.debugEnabled = debugEnabled;
			this.stackLogEnabled = stackLogEnabled;
			trace("Using logger for " + tag + ", logEnabled = " + logEnabled + ", debugEnabled = " + debugEnabled + ", stackLogEnabled = " + stackLogEnabled);
		}
		
		public function log(message:String) : void {
			message = getTime() + " " + tag + " : " + message;
			if (stackLogEnabled) 
				message = new Error(message).getStackTrace().substring(7 /* Skip "Error :" */) + "\n";
			trace(message);
			allMessages += message + "\n";
		}
		
		public function debug(message : String) : void {
			if (debugEnabled)
				Alert.show(tag + " : " + message);
		}
		
		public function isLogEnabled() : Boolean {
			return logEnabled;
		}
		
		public function isDebugEnabled() : Boolean {
			return debugEnabled;
		}
		
		public function showAllMessages():void {
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(loggerName)
				.setText(allMessages)
				.setWordWrap(true)
				.setWidth(1000)
				.setHeight(600)
				.showMessageBox();
		}
		
		public function get loggerName():String {
			return tag.toString() + " Logger";
		}
		
		private function getTime():String {
			var date:Date = new Date();
			return date.hours + ":" + date.minutes + ":" + date.seconds + ":" + date.milliseconds;
		}
	}
}
