package com.crispico.flower.util.tree.checkboxtree {
	
	import mx.controls.listClasses.ListBase;
	import mx.controls.treeClasses.TreeListData;

	/**	 
	 * @see http://www.sephiroth.it/file_detail.php?id=151#
	 * 
	 * @author Cristina
	 */
	public class CheckBoxTreeListData extends TreeListData {
		
		public var checkedState: uint = 0;
		
		public function CheckBoxTreeListData(text: String, uid: String, owner: ListBase, rowIndex: int = 0, columnIndex: int = 0) {
			super(text, uid, owner, rowIndex, columnIndex);
		}		
	}
	
}