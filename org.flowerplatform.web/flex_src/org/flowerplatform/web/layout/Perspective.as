package org.flowerplatform.web.layout {
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	/**
	 * An instance for this class should exist for each perspective.
	 * 
	 * Holds specific information for the perspective 
	 * (e.g. name and icon url that will appear in the menu).
	 * 
	 * <p>
	 * It should also know how to reset the perspective by setting programatically 
	 * the initial layout data on workbench.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _VuR4wFCyEeGsUPSh9UfXpw
	 */
	public class Perspective {
		
		public function get id():String {
			throw new Error("This method needs to be implemented.");
		}
		
		/**
		 * Default name.
		 * 
		 * @flowerModelElementId _URGZAFDGEeGMrNbRkxqlAA
		 */
		public function get name():String {
			throw new Error("This method needs to be implemented.");
		}
		
		/**
		 * Default icon.
		 * 
		 * @flowerModelElementId _WZUpwFDGEeGMrNbRkxqlAA
		 */
		public function get iconUrl():String {			
			throw new Error("This method needs to be implemented.");
		}
		
		/**
		 * Abstract method.
		 * 
		 * <p>
		 * Should clear the workbench and build the layout for this perspective.
		 * 
		 * @flowerModelElementId _mPN7gFCyEeGsUPSh9UfXpw
		 */
		public function resetPerspective(workbench:Workbench):void {
			throw new Error("This method needs to be implemented.");
		}
		
		protected function load(workbench:Workbench, workbenchLayoutData:WorkbenchLayoutData, editorSashLayoutData:SashLayoutData):void {
			if (editorSashLayoutData == null) {
				throw new Error("A sash editor must exist on a perspective!");
			}
			workbench.load(workbenchLayoutData, true, true);
		}
	}
}