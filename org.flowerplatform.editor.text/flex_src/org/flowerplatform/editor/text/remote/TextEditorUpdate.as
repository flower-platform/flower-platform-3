package org.flowerplatform.editor.text.remote {
	
	/**
	 * @see corresponding Java class.
	 * 
	 * @author Mariana
	 */	
	[RemoteClass]
	[SecureSWF(rename="off")]
	public class TextEditorUpdate  {
		
		[SecureSWF(rename="off")]
		public var offset:int;
		
		[SecureSWF(rename="off")]
		public var oldTextLength:int;
		
		[SecureSWF(rename="off")]
		public var newText:String;
	}
}