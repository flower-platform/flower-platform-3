package com.crispico.flower.flexdiagram.ui {
	import com.crispico.flower.flexdiagram.IFigure;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.geom.Point;
	
	import mx.events.MoveEvent;
	import mx.events.ResizeEvent;
	
	
	/**
	 * @flowerModelElementId __hPQQDnaEeCeSf7Oz12Z6g
	 */
	public class CreateRelationAnchors extends AbstractSelectionAnchors {
		
		public static const ANCHOR_X_OFFSET:int = 2; 
		
		protected var anchor:RelationAnchor;
		
		public function CreateRelationAnchors() {
			super();
			anchor = new RelationAnchor(true);
		}
		
		override public function initialize():void {
			super.initialize();
			this.addChild(anchor);
		}
		
		override public function activate(target:IFigure):void {
			super.activate(target);	
								
			// set the handler that move/resize anchors with parent figure.
			DisplayObject(target).addEventListener(MoveEvent.MOVE, handleTargetMoveResize); 
			DisplayObject(target).addEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);
			handleTargetMoveResize(null);
		}
		
		override public function deactivate():void {
			// remove move/resize listeners
			DisplayObject(target).removeEventListener(MoveEvent.MOVE, handleTargetMoveResize);
			DisplayObject(target).removeEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);
			
			super.deactivate();
		}
		
		public function handleTargetMoveResize(event:Event):void {
			// the anchor is positioned right, next to target, at middle height
			var p:Point = new Point(DisplayObject(target).x + DisplayObject(target).width + ANCHOR_X_OFFSET, DisplayObject(target).y + DisplayObject(target).height/2 - anchor.height/2);
			p = parent.globalToLocal(DisplayObject(target).parent.localToGlobal(p));
			// set anchor coordinates
			anchor.x = p.x;
			anchor.y = p.y;
			anchor.invalidateDisplayList();
		}
	}
}