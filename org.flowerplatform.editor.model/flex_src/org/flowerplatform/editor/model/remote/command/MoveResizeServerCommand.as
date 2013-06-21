package org.flowerplatform.editor.model.remote.command {
	
	[RemoteClass]
	public class MoveResizeServerCommand {
		public var id:String;
		public var newX:int;
		public var newY:int;
		public var newHeight:int = -1;
		public var newWidth:int = -1;

	}
}