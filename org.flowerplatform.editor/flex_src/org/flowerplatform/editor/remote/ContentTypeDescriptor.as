package  org.flowerplatform.editor.remote {
	import mx.collections.ArrayCollection;
	
	/**
	 * @see Corresponding Java class. 
	 * 
	 * @author Cristi
	 */
	[RemoteClass] 
	public class ContentTypeDescriptor {
		public var contentType:String;
		public var compatibleEditors:Vector.<String>;
	}
}