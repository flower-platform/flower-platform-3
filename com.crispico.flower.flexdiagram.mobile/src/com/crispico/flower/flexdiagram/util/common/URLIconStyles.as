package  com.crispico.flower.flexdiagram.util.common {
	
	import mx.collections.ArrayCollection;
	
	/**
	 * The class provides icons styles for a given tree list data.
	 * It is used by a <code>TreeGrid</code> or a <code>CustomTree</code> object.
	 * 
	 * <p>If one of those has items with children then 
	 * <code>disclosureOpenIconURL, disclosureClosedIconURL</code> must be initialized.
	 * The icons can be set in two ways :
	 * <ul>
	 * 	<li>using the attributes : <code>folderOpenedIconURL, folderClosedIconURL, defaultLeafIconURL</code>.
	 * 	<li>setting the <code>iconURLFunction</code> in order to get the item icon URL dynamically. 
	 * 		Used when we want different icon for each leaf/folder.
	 * <ul>
	 * </p>
	 * @see CustomTree
	 * @see TreeGrid
	 * 
	 * @author Cristina
	 * @flowerModelElementId _K_fWEO5JEd-AG91j4TO4_w
	 */
	public class URLIconStyles {
		
		public var disclosureClosedIconURL:String;
		
		public var disclosureOpenedIconURL:String;
		
		public var iconURLs:ArrayCollection;
		
		public var folderOpenedIconURL:String;
		
		public var folderClosedIconURL:String;
		
		public var defaultLeafIconURL:String;
		
		/**
		 * <p>The iconURLFunction takes a single argument which is the item in the data provider and returns a 
		 * collection of String elements.</p>
		 * <blockquote>
		 * 	<code>iconURLsFunction(item:Object):ArrayCollection</code>
		 * </blockquote>
		 */
		public var iconURLsFunction:Function;
		
		/**
		 * Based on the first three parameters, the method finds the <code>disclosureIconURL</code> and <code>iconURLs</code>
		 * for a given <code>treeListData</code>.
		 * @flowerModelElementId _Tu4IEO5JEd-AG91j4TO4_w
		 */
		public function fillListDataWithIconURLs(open:Boolean, branch:Boolean, item:Object, treeListData:Object):void {
			var urlIcon:String;	
			
			// sets the desclosure icon for a treeListData		
			urlIcon = open ? disclosureOpenedIconURL : disclosureClosedIconURL;
			treeListData.disclosureIconURL = urlIcon;
			
			// sets the icon for a treeListData
			urlIcon = null;
			
			// if the item is not null and a function that returns the item's icon exists
			if (iconURLsFunction != null && item != null) { 
				var icons:ArrayCollection = iconURLsFunction(item);
				if (icons != null) {
					treeListData.iconURLs = icons;
					return;
				}
			}
			
			// if the function didn't provide a value then try with the defaults.
			if (urlIcon == null) {
				// if it's a folder and the folder styles are set
				if (branch && !(folderOpenedIconURL == null || folderClosedIconURL == null)) {
					urlIcon = open ? folderOpenedIconURL : folderClosedIconURL;
					
				// if leaf deafault style is set
				} else if (defaultLeafIconURL != null) { 
					urlIcon = defaultLeafIconURL;
				} 
			}
			
			// no icon to add
			if (urlIcon == null) {
				treeListData.iconURLs = null;
			} else {
				treeListData.iconURLs = new ArrayCollection([urlIcon]);
			}
		}
	}
	
}