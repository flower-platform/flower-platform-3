package com.crispico.flower.flexdiagram.samples.basic.editpart
{
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.connection.ConnectionLabelEditPart;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicConnection;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicModel;
	import com.crispico.flower.flexdiagram.ui.ConnectionLabelFigure;
	
	import mx.events.PropertyChangeEvent;

	public class BasicConnectionLabelEditPart extends ConnectionLabelEditPart {
			
		public function BasicConnectionLabelEditPart(model:Object, viewer:DiagramViewer) {
			super(model, viewer);
		}
		
		override public function getFigureClass():Class {
			return ConnectionLabelFigure;
		}
		
		override protected function getPositionType():int {
			return MIDDLE_UP;
		}
		
		/**
		 * Override the <code>activate()</code> method from superclass in order
		 * to add a <code>PropertyChangeEvent</code> listener for the model, in order to 
		 * receive notification whenever the label changes;
		 */
		override public function activate():void {
			super.activate();
			BasicModel(model).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateNodeHandler);
		}
		
		/**
		 * Override the <code>deactivate()</code> method from superclass in order
		 * to remove the listener added in the <code>activate()</code> method.
		 */
		override public function deactivate():void {
			BasicModel(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateNodeHandler);
			super.deactivate();
		}
		
		/**
		 * The handler for <code>PropertyChangeEvent</code>. The method checks the property 
		 * that changed. If 'viewDetails' changed, the method calls 
		 * <code>refreshVisualChildren()</code> method.
		 */
		private function updateNodeHandler(event:PropertyChangeEvent):void {
			if (event.property == "name")
				refreshVisualDetails(false); 
		}
		
		/**
		 * The method retrieves the model's viewDetails and updates the figure with the newly
		 * changed label.
		 */
		override public function refreshVisualDetails(calledDuringFigureSet:Boolean):void {
			var node:BasicModel = BasicModel(model);
			var fig:ConnectionLabelFigure = ConnectionLabelFigure(getFigure());
			if (fig != null) {
				fig.text = node.name;				
				fig.invalidateSize();
				updatePositionHandler();
			}
		}	
	}
	
}