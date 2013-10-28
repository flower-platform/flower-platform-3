package org.flowerplatform.editor.model.renderer {
	import flash.filters.DropShadowFilter;
	
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.editor.model.BoxRendererLayout;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.remote.command.MoveResizeServerCommand;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.Note;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexdiagram.renderer.NoteRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DiagramNoteRenderer extends NoteRenderer implements IDiagramShellAware {
				
		private var shadow:DropShadowFilter;
		
		private var _diagramShell:DiagramShell;
		
		public function DiagramNoteRenderer() {
			super();		
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			alphas = [1, 1];
		}
		
		public function get diagramShell():DiagramShell {
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}
		
		protected function creationCompleteHandler(event:FlexEvent):void {			
			addShadow();			
		}
		
		protected function addShadow():void {
			shadow = new DropShadowFilter();
			shadow.alpha = 0.55;
			shadow.blurX = 5;
			shadow.blurY = 5;
			shadow.distance = 6;
			shadow.color = 0x000000;
			shadow.angle = 35; 
			this.filters = [shadow];
		}
		
		override public function set data(value:Object):void {
			if (super.data == value) {
				return;
			}
			if (super.data != null) {
				data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				if (Node(data).layoutConstraint_RH != null) {
					var bounds:Bounds = Bounds(ReferenceHolder(Node(data).layoutConstraint_RH).referencedObject);
					bounds.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				}
			}
			
			super.data = value;
			
			if (data != null) {
				data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				if (Node(data).layoutConstraint_RH != null) {					
					bounds = Bounds(ReferenceHolder(Node(data).layoutConstraint_RH).referencedObject);
					bounds.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
					x = bounds.x;
					y = bounds.y;
					
					height = bounds.height;
					width = bounds.width;					
				}
				label = data.text;
			}
		}
		
		override protected function measure():void {
			super.measure();
			
			var command:MoveResizeServerCommand = new MoveResizeServerCommand();
			command.id = Node(data).layoutConstraint_RH.referenceIdAsString;
			command.newWidth = measuredWidth;
			command.newHeight = measuredHeight;
			command.newX = x;
			command.newY = y;						
			NotationDiagramShell(diagramShell).editorStatefulClient.attemptUpdateContent(null, command);	
		}
		
		private function modelChangedHandler(event:PropertyChangeEvent):void {			
			switch (event.property) {
				case "text":
					label = String(event.newValue);
					measure();
					break;
				case "x":					
					x = Bounds(event.target).x;
					break;
				case "y":							
					y = Bounds(event.target).y;
					break;
				case "height":	
					height = Bounds(event.target).height;		
					break;
				case "width":	
					width = Bounds(event.target).width;	
					break;
			}
		}	
		
	}
}