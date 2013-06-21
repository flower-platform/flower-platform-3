package org.flowerplatform.editor.model {
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	import mx.messaging.errors.NoChannelAvailableError;
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	
	import spark.components.Label;
	
	public class DiagramEditorFrontend extends EditorFrontend {
	
		public var diagramShell:DiagramShell;
		
		override protected function createChildren():void {
			var scroller:InfiniteScroller = new InfiniteScroller();
			editor = scroller;
			
			var diagramRenderer:DiagramRenderer = new DiagramRenderer();
//			diagramRenderer.contentRect=new Rectangle(0, 0, 9000, 9000);
			scroller.viewport = diagramRenderer;
			diagramRenderer.horizontalScrollPosition = diagramRenderer.verticalScrollPosition = 0;
			
//			var label:Label = new Label(); 
//			label.text = "end";
//			label.x = 9000;
//			label.y = 9000;
			
//			var bounds:Bounds = new Bounds();
//			
//			bounds.x = bounds.y = 20;
//			bounds.width = bounds.height = 20;
//			
//			var node:Node = new Node();
//			node.layoutConstraint = bounds;
//			node.viewType = "class";
//			
//			var diagram:Diagram = new Diagram();
//			diagram.viewType = "classDiagram";
//			diagram.persistentChildren = new ArrayList([node]);
//			
			diagramShell = new NotationDiagramShell();
			diagramShell.diagramRenderer = diagramRenderer;
//			diagramShell.rootModel = diagram;

			super.createChildren();
		}
		
		override public function executeContentUpdateLogic(content:Object, isFullContent:Boolean):void {
		}

		override public function disableEditing():void {
		}
		
		override public function enableEditing():void {
		}
	}
}