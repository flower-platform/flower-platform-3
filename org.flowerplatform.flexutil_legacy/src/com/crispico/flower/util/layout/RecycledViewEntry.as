package  com.crispico.flower.util.layout {
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import mx.core.UIComponent;
	
	/**
	 * @author Cristina
	 * @flowerModelElementId _pp59wOI-EeGF46ujw3kLCA
	 */
	public class RecycledViewEntry {
		
		/**
		 * @flowerModelElementId _r5FLEOI-EeGF46ujw3kLCA
		 */
		public var oldViewLayoutData:ViewLayoutData;
		
		/**
		 * @flowerModelElementId _uyscgOI-EeGF46ujw3kLCA
		 */
		public var existingView:UIComponent;
				
		public function RecycledViewEntry(oldViewLayoutData:ViewLayoutData, existingView:UIComponent) {
			this.oldViewLayoutData = oldViewLayoutData;
			this.existingView = existingView;			
		}
	}
	
}