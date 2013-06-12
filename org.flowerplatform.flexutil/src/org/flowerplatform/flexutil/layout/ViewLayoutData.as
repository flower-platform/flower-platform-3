package  org.flowerplatform.flexutil.layout {
	import mx.collections.ArrayCollection;
	
	/**
	 * Represents the <code>LayoutData</code> entity without children.
	 * <p>
	 * Stores information used for entity identification.
	 *  
	 * @author Cristina
	 * @flowerModelElementId _JkFDcEslEeGoH-1QrqBGdw
	 */ 
	public class ViewLayoutData extends LayoutData {
		
		/**
		 * Represents the id of this layout.
		 * @flowerModelElementId _JkFqgkslEeGoH-1QrqBGdw
		 */ 
		public var viewId:String;
		
		/**
		 * For the moment, could be the node id corresponding to the file to be opened for an editor.
		 * @flowerModelElementId _JkFqhEslEeGoH-1QrqBGdw
		 */
		public var customData:String;
		
		/**
		 * @flowerModelElementId _xUkt8OI_EeGF46ujw3kLCA
		 */
		public var isEditor:Boolean;
		
		/**
		 * A view is "undocked" when it is displayed as a popup.
		 * It isn't contained in the workspace data.
		 * 
		 * @see ViewPopupWindow
		 */ 
		public var isUndocked:Boolean;
		
		/**
		 * Stores the coordonates and dimensions of an undock view.
		 * 
		 * <p>
		 * Format: [x, y, width, height]
		 */ 
		public var dimensions:ArrayCollection = new ArrayCollection();
		
		/**
		 * @flowerModelElementId _nPz5EJROEeGt-rlvXRfyYQ
		 */
		public function ViewLayoutData(viewId:String=null, customData:String=null) {
			this.viewId = viewId;
			this.customData = customData;
		}
		
	}
	
}