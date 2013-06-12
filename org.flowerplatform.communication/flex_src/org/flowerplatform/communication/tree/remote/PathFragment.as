package org.flowerplatform.communication.tree.remote {
	
	/**
	 * @see Corresponding Java doc.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 
	 * @flowerModelElementId _cLb0sKP8EeGeHqktJlHXmA
	 */
	[RemoteClass]
	[Bindable]
	[SecureSWF(rename="off")]
	public class PathFragment {
		
		/**
		 * @flowerModelElementId _cLcbw6P8EeGeHqktJlHXmA
		 */
		[SecureSWF(rename="off")]
		public var name:String;
		
		/**
		 * @flowerModelElementId _cLdC0KP8EeGeHqktJlHXmA
		 */
		[SecureSWF(rename="off")]
		public var type:String;
		
		public function PathFragment(name:String = null, type:String = null) {
			this.name = name;
			this.type = type;
		}
	}
	
}