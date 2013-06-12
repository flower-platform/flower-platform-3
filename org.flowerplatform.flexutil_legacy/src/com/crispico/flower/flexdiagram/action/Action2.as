package com.crispico.flower.flexdiagram.action {
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	import mx.collections.ArrayCollection;
	
	
	// TODO sorin : la stergerea vechilor actiuni ar trebui facut ca FlowerContribution 
	// sa extinda BaseAction. 

	/**
	 * @author Cristina
	 * @flowerModelElementId _j2HI0L_PEd-259cbnb9fYw
	 */
	[SecureSWF(rename="off")]
	public class Action2 implements IAction {
	
		/**
		 * @flowerModelElementId _j2HI0r_PEd-259cbnb9fYw
		 */
		private var _image:Object;
		
		/**
		 * @flowerModelElementId _j2HI07_PEd-259cbnb9fYw
		 */
		private var _label:String;
		
		protected var _context:ActionContext;
		
		private var _sortIndex:int = FlowerContextMenu.DEFAULT_SORT_INDEX;

		/**
		 * @flowerModelElementId _j2HI1L_PEd-259cbnb9fYw
		 */
		[SecureSWF(rename="off")]
		public function get label():String {
			return _label;
		}
		
		/**
		 * @private 
		 * somne actions might need to reset their label or icon
		 * especially outside our framework (eg: gantt)
		 * @author Luiza
		 */
		[SecureSWF(rename="off")]
		public function set label(newLabel:String):void {
			this._label = newLabel;
		}
		
		/**
		 * @flowerModelElementId _j2HI1r_PEd-259cbnb9fYw
		 */
		[SecureSWF(rename="off")]
		public function get image():Object {
			return _image;
		}
		
		/**
		 * @private
		 * @author Luiza
		 */
		[SecureSWF(rename="off")]
		public function set image(newImage:Object):void {
			this._image = newImage;
		}
		
		public function getContext():ActionContext {
			return _context;	
		}
		
		/**
		 * Note:Getter does not exist because it would be transmited to Java and it would
		 * request the value for the <code>context</code>.
		 */ 		
		public function set context(value:ActionContext):void {
			_context = value;	
		}
		
		[SecureSWF(rename="off")]
		public function get sortIndex():int {
			return _sortIndex;
		}
		
		[SecureSWF(rename="off")]
		public function set sortIndex(sortIndex:int):void {
			this._sortIndex = sortIndex;
		}
		
		/**
		 * @flowerModelElementId _j2HI2L_PEd-259cbnb9fYw
		 */
		public function isVisible(selection:ArrayCollection):Boolean {
			return true;
		}
		
		// TODO Cristina : metoda nu corespunde cu noul mecanism de actiuni
		// va trebui stearsa 
		public function isEnabled(selection:ArrayCollection):Boolean {
			return true;
		}
		
		/**
		 * @flowerModelElementId _j2HI3r_PEd-259cbnb9fYw
		 */
		public function run(selection:ArrayCollection):void {
		}
	}
}