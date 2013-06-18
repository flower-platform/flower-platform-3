package org.flowerplatform.flexdiagram.renderer.selection {
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**	
	 * @author Cristina Constantinescu
	 */
	public class AbstractSelectionRenderer extends UIComponent {
		
		/**
		 * The figure where the anchors will be shown.
		 */
		protected var target:IVisualElement;
		
		protected var diagramShell:DiagramShell;
		
		/**
		 * Main selection influences the aspect of the anchors.
		 */
		protected var isMainSelection:Boolean;
		
		public function activate(diagramShell:DiagramShell, target:IVisualElement):void {
			this.target = target;
			this.diagramShell = diagramShell;
			
			diagramShell.diagramRenderer.addElement(this);
		}
		
		public function deactivate():void {
			diagramShell.diagramRenderer.removeElement(this);
		}

		public function getTargetModel():Object {
			if (target != null) 
				return IDataRenderer(target).data;
			else 
				return null;
		}
		
		public function getMainSelection():Boolean {
			return isMainSelection;
		}
		
		public function setMainSelection(value:Boolean):void {
			
			var oldValue:Boolean = isMainSelection;
			
			isMainSelection = value;
			
			if (oldValue != value) {
				// announce ResizeAnchors that the value has been modified
				invalidateActiveAnchors();
			}
		}		
		
		/**
		 * This can be implemented by subclasses to update anchors display.
		 */ 
		protected function invalidateActiveAnchors():void {			
		}
		
	}	
}