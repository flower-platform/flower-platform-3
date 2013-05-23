package com.crispico.flower.flexdiagram.action.layout.resize {
	
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.FlexDiagramAssets;
	import com.crispico.flower.flexdiagram.action.layout.LayoutAction;
	import com.crispico.flower.flexdiagram.action.layout.LayoutStyle;
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;

	/**
	 * Basic Resize Action. Creates the main steps of the resize alorith.
	 * Subclasses must still override:
	 * 
	 * <ul>
	 * <li>canApplyLayout()</li>
	 * <li>getBoundsFromEditPart()</li>
	 * <li>applyNewBounds()</li>
	 * </ul> 
	 */ 
	public class AbstractResizeAction extends LayoutAction {
		
		/**
		 * @param resizeStyle - defines the Action's behavior. It can be one of the resize
		 * options defined in com.crispico.flower.flexdiagram.action.layout.LayoutStyle.
		 * 
		 * <ul>
		 * <li>RESIZE_WIDTH</li>
		 * <li>RESIZE_HEIGHT</li>
		 * <li>RESIZE_BOTH</li>
		 * </ul>
		 */
		public function AbstractResizeAction(resizeStyle:String) {
			super(resizeStyle);
			
			// set label and icon depending on the style
			switch(resizeStyle) {
				case LayoutStyle.RESIZE_WIDTH:
					label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Resize_Width");
					image = FlexDiagramAssets.INSTANCE.resize_width_icon;
					break;
				case LayoutStyle.RESIZE_HEIGHT:
					label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Resize_Height");
					image = FlexDiagramAssets.INSTANCE.resize_height_icon;
					break;
				case LayoutStyle.RESIZE_BOTH:
					label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Resize_Both");
					image = FlexDiagramAssets.INSTANCE.resize_both_icon;
					break;
			}
		}
		
		override protected function set layoutStyle(style:String):void {
			if (style == LayoutStyle.RESIZE_WIDTH || style == LayoutStyle.RESIZE_HEIGHT || style == LayoutStyle.RESIZE_BOTH) {
				super.layoutStyle = style;
			} else {
				throw new Error("Unexpected resizeStyle");
			}
		}
		
		override public function isVisible(selection:ArrayCollection):Boolean {
			if (selection.length < 2) {
				return false;
			}
			
			var counter:int = 0;					
			// at least two elements that accept this action must be selected
			for (var i:int; i < selection.length; i++) {
				var selectedPart:EditPart = EditPart(selection.getItemAt(i));
				
				if (canApplyLayout(selectedPart)) {
					counter ++;
					if (counter == 2) {
						return true;
					}
				} 
			}
			return false;
		}
				
		/**
		 * This function should return <code>true</code> if the given EditPart is 
		 * eligible for resize. By default returns <code>false</code>.
		 * <p>
		 * It must be overriden in subclasses to select the right EditParts,
		 * depending on their attached model and their resize behavior.
		 * <p> 
		 * Note that this function is called from isVisible()
		 * as well and will help to decide if this action is available or not. 
		 */ 
		override protected function canApplyLayout(part:EditPart):Boolean {
			return false;
		}
		
		override protected function computeNewBounds(selection:ArrayCollection):Dictionary {
			var refBounds:Array = getBoundsFromEditPart(selection[selection.length - 1]);
			var refW:int = refBounds[2];
			var refH:int = refBounds[3];
			
			var editPartToNewSize:Dictionary = new Dictionary();
			
			for (var i:int = 0; i < selection.length - 1; i++) {
				var ep:EditPart = selection[i];
				if (canApplyLayout(ep)) {
					editPartToNewSize[ep] = getNewSize(getBoundsFromEditPart(ep), refW, refH);			
				}
			}
			return editPartToNewSize;
		}
		
		private function getNewSize(currentBounds:Array, referenceWidth:int, referenceHeight:int):Array {
			var x:int = currentBounds[0];
			var y:int = currentBounds[1];
			var width:int = currentBounds[2];
			var height:int = currentBounds[3];
			var resizeStyle:String = super.layoutStyle;
			
			switch(resizeStyle) {
				case LayoutStyle.RESIZE_WIDTH:
					return [x, y , referenceWidth, height];
				case LayoutStyle.RESIZE_HEIGHT:
					return [x, y, width, referenceHeight];
				case LayoutStyle.RESIZE_BOTH:
					return [x, y, referenceWidth, referenceHeight];
			}
			
			return [x, y, width, height];
		}
	}
}