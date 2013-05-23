package com.crispico.flower.flexdiagram.util.treegrid {

	import mx.collections.ArrayCollection;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.core.IUIComponent;

	/**
	 * The class is used as a connection between a <code>TreeGrid</code> and a <code>TreeGridItemRenderer</code>. 
	 * <p>The informations from the <code>TreeGrid</code> are stored in the class attributes and they are used 
	 * by the <code>TreeGridItemRenderer</code>. 
	 * 
	 * NOTE :
	 * This class was created by making a copy/paste from flexLib <code>mx.controls.treeGridClasses.TreeGridListData</code>.
	 * The action was done in order to provide functionality for icons to work with URLs, 
	 * not only with Classes.
	 * 
	 * @flowerModelElementId _X7qLr-waEd-Mq65kNpXUPA
	 */
	public class FlowerTreeGridListData extends DataGridListData {
			
		public function FlowerTreeGridListData(
			text : String, 
			dataField : String,
			columnIndex : int, 
			uid : String,
			owner : IUIComponent, 
			rowIndex : int = 0)	{
			super( text, dataField, columnIndex, uid, owner, rowIndex );
		}
	
		/**
		 *  The level of the item in the tree. The top level is 1.
		 */
		public var depth:int;
		
		/**
		 *  Contains <code>true</code> if the node has children.
		 */
		public var hasChildren:Boolean;
		
		public var hasSibling:Boolean;
		
		/**
		 *  The default indentation for this row of the TreeGrid control.
		 */
		public var indent:int;
		
		public var indentationGap:int;
		
		/**
		 *  A String that enumerate the trunk style for the item in the TreeGrid control.
		 */
		public var trunk:String;
		
		public var trunkOffsetTop : Number;
		
		public var trunkOffsetBottom : Number;
		
		public var trunkColor:uint = 0xffffff;
		
		/**
		 *  The data for this item in the TreeGrid control.
		 */
		public var item:Object;
		
		/**
		 *  Contains <code>true</code> if the node is open.
		 */
		public var open:Boolean;
	
		/**
		 * Represents the disclosure icon's URL of the item in the TreeGrid control.
		 * A disclosure icon can be represented be the following signs : +/- (plus/minus).
		 * 
		 * @author Cristina
		 * @flowerModelElementId _pNQxoOybEd-OXKbnT5tcWg
		 */
		public var disclosureIconURL:String;
		
		/**
		 * Represents the icon's URL for the item in the TreeGrid control.
		 * 
		 * @author Cristina
		 * @flowerModelElementId _rsvr0OybEd-OXKbnT5tcWg
		 */
		public var iconURLs:ArrayCollection;
		
		/**
		 * A Class representing the disclosure icon for the item in the TreeGrid control.
		 * @flowerModelElementId _F3pnoO5LEd-AG91j4TO4_w
		 */
		public var disclosureIcon:Class;
		
		/**
		 * A Class representing the icon for the item in the TreeGrid control.
		 * @flowerModelElementId _IGBjsO5LEd-AG91j4TO4_w
		 */
		public var icon:Class; 		
	} 
	
} 