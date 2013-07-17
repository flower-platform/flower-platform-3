package org.flowerplatform.flexdiagram.mindmap {
	import flash.geom.Point;
	
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapConnector extends UIComponent {
		
		public var source:Object;
		
		public var target:Object;
				
		public function setSource(value:Object):MindMapConnector {
			this.source = value;
			return this;
		}
		
		public function setTarget(value:Object):MindMapConnector {
			this.target = value;
			return this;
		}
		
		private function getDiagramShell():MindMapDiagramShell {
			return MindMapDiagramShell(DiagramRenderer(this.parent).diagramShell);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			var sourceElement:IVisualElement = getDiagramShell().getRendererForModel(source);
			var targetElement:IVisualElement = getDiagramShell().getRendererForModel(target);
			
			// verify if source/target renderers are still visible
			if (sourceElement == null || targetElement == null) {
				return;
			}
			graphics.clear();
			graphics.lineStyle(1, 0x808080);
			
			var sourcePoint:Point;
			var targetPoint:Point;
			
			if (getDiagramShell().getModelController(source).getSide(source) == MindMapDiagramShell.LEFT) {
				sourcePoint = new Point(sourceElement.x + sourceElement.width, sourceElement.y + sourceElement.height/2);
				targetPoint = new Point(targetElement.x, targetElement.y + targetElement.height/2);
				graphics.moveTo(sourcePoint.x, sourcePoint.y);
				
				if (sourceElement.y == targetElement.y) {
					graphics.lineTo(targetPoint.x, targetPoint.y);
				} else if (sourceElement.y < targetElement.y) {
					graphics.cubicCurveTo(					
						getRightTopControlPoint(sourcePoint.x, sourcePoint.y).x, 
						getRightTopControlPoint(sourcePoint.x, sourcePoint.y).y,
						getRightBottomControlPoint(targetPoint.x, targetPoint.y).x,
						getRightBottomControlPoint(targetPoint.x, targetPoint.y).y,
						targetPoint.x, 
						targetPoint.y);
				} else if (sourceElement.y > targetElement.y) {
					graphics.cubicCurveTo(					
						getLeftBottomControlPoint(sourcePoint.x, sourcePoint.y).x, 
						getLeftBottomControlPoint(sourcePoint.x, sourcePoint.y).y,
						getLeftTopControlPoint(targetPoint.x, targetPoint.y).x,
						getLeftTopControlPoint(targetPoint.x, targetPoint.y).y,
						targetPoint.x, 
						targetPoint.y);
				}			
			} else {
				sourcePoint = new Point(sourceElement.x, sourceElement.y + sourceElement.height/2);
				targetPoint = new Point(targetElement.x + targetElement.width, targetElement.y + targetElement.height/2);
				graphics.moveTo(sourcePoint.x, sourcePoint.y);			
				if (sourceElement.y == targetElement.y) {
					graphics.lineTo(targetPoint.x, targetPoint.y);
				} else if (sourceElement.y < targetElement.y) {
					graphics.cubicCurveTo(					
						getLeftTopControlPoint(sourcePoint.x, sourcePoint.y).x, 
						getLeftTopControlPoint(sourcePoint.x, sourcePoint.y).y,
						getLeftBottomControlPoint(targetPoint.x, targetPoint.y).x,
						getLeftBottomControlPoint(targetPoint.x, targetPoint.y).y,
						targetPoint.x, 
						targetPoint.y);
				} else if (sourceElement.y > targetElement.y) {
					graphics.cubicCurveTo(					
						getRightBottomControlPoint(sourcePoint.x, sourcePoint.y).x, 
						getRightBottomControlPoint(sourcePoint.x, sourcePoint.y).y,
						getLeftBottomControlPoint(targetPoint.x, targetPoint.y).x,
						getLeftBottomControlPoint(targetPoint.x, targetPoint.y).y,
						targetPoint.x, 
						targetPoint.y);
				}
			}					
		}
		
		private function getLeftTopControlPoint(x:Number, y:Number):Point {
			return new Point(x - getDiagramShell().horizontalPadding/2, y);
		}
		
		private function getRightTopControlPoint(x:Number, y:Number):Point {
			return new Point(x + getDiagramShell().horizontalPadding/2, y);
		}
		
		private function getLeftBottomControlPoint(x:Number, y:Number):Point {
			return new Point(x + getDiagramShell().horizontalPadding/2, y);
		}
		
		private function getRightBottomControlPoint(x:Number, y:Number):Point {
			return new Point(x - getDiagramShell().horizontalPadding/2, y);
		}
	}
}