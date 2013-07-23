package org.flowerplatform.flexdiagram.renderer.selection {
	
	import flash.events.Event;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ChildAnchorsSelectionRenderer extends StandardAnchorsSelectionRenderer {
		
		override protected function handleTargetMoveResize(event:Event):void {
			setLayoutBoundsPosition(target.parent.x + target.x, target.parent.y + target.y);
			setLayoutBoundsSize(target.width, target.height);
		}
		
	}
}