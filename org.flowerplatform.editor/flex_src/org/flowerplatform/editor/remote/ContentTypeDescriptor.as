package  org.flowerplatform.editor.remote {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	/**
	 * @see Corresponding Java class. 
	 * 
	 * @author Cristi
	 */
	[RemoteClass] 
	public class ContentTypeDescriptor {
		public var index:int;
		public var contentType:String;
		public var compatibleEditors:IList;
	}
}