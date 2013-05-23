package com.crispico.flower.flexdiagram.action.layout.align {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.FlexDiagramAssets;
	import com.crispico.flower.flexdiagram.action.layout.LayoutAction;
	import com.crispico.flower.flexdiagram.action.layout.LayoutStyle;
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;

	/**
	 * Basic AlignAction that implements the main steps of the align algorithm.
	 * Subclasses stil need to override:
	 * <ul>
	 * <li>canApplyLayout()</li>
	 * <li>getBoundsFromEditPart()</li>
	 * <li>applyNewBounds()</li>
	 * </ul>  
	 * 
	 * For details about them see com.crispico.flower.flexdiagram.action.layout.LayoutAction.
	 * 
	 * @author Luiza
	 */ 
	public class AbstractAlignAction extends LayoutAction {
		
		public function AbstractAlignAction(alignStyle:String) {
			super(alignStyle);
			
			switch(alignStyle) {
				case LayoutStyle.ALIGN_LEFT: 
					this.label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Align_Left");
					this.image = FlexDiagramAssets.INSTANCE.align_left_icon;
					break;
				case  LayoutStyle.ALIGN_CENTER:
				  	this.label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Align_Center");
					this.image = FlexDiagramAssets.INSTANCE.align_center_icon;
					break;
				case  LayoutStyle.ALIGN_RIGHT:
					this.label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Align_Right");
					this.image = FlexDiagramAssets.INSTANCE.align_right_icon;
					break;
				case LayoutStyle.ALIGN_TOP:
					this.label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Align_Top");
					this.image = FlexDiagramAssets.INSTANCE.align_top_icon;
					break;
				case LayoutStyle.ALIGN_MIDDLE:
					this.label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Align_Middle");
					this.image = FlexDiagramAssets.INSTANCE.align_middle_icon;
					break;
				case LayoutStyle.ALIGN_BOTTOM:
					this.label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Align_Bottom");
					this.image = FlexDiagramAssets.INSTANCE.align_bottom_icon;
					break;
			}
		}
		
		/**
		 * Defines the style of alignment this action will perform.
		 * It can get one of the LayoutStyle predefined values:
		 * <ul>
		 * <li>ALIGN_LEFT</li>
		 * <li>ALIGN_CENTER</li>
		 * <li>ALIGN_RIGHT</li>
		 * <li>ALIGN_TOP</li>
		 * <li>ALIGN_MIDDLE</li>
		 * <li>ALIGN_BOTTOM</li>
		 * </ul>
		 */ 
		override protected function set layoutStyle(value:String):void {
			if (value == LayoutStyle.ALIGN_LEFT || value == LayoutStyle.ALIGN_CENTER || value == LayoutStyle.ALIGN_RIGHT 
				|| value == LayoutStyle.ALIGN_TOP || value == LayoutStyle.ALIGN_MIDDLE || value == LayoutStyle.ALIGN_BOTTOM) {					
				super.layoutStyle = value;

			} else {
				throw new Error("Unexpected alignStyle");
			}
		}
		
		/**
		 * This function should return <code>true</code> if the given EditPart is 
		 * eligible for alignment. By default returns <code>false</code>.
		 * <p>
		 * This function should be overriden in subclasses to select the right EditParts,
		 * depending on their attached model and their moving behavior.
		 * <p> 
		 * Note that this function is called from isVisible()
		 * as well and will help to decide if this action is available or not. 
		 */ 
		override protected function canApplyLayout(part:EditPart):Boolean {
			return false;	
		}
		
		override public function isVisible(selection:ArrayCollection):Boolean {
			var count:int = 0;
			// iterate selection to find EditParts eligible for alignment
			// the selection may contain connections as well and they must not be considered
			for (var i:int; i < selection.length; i++) {
				var ep:EditPart = EditPart(selection[i]);
				
				if (canApplyLayout(ep)) {
					count ++;
					
					// if found at least two EditParts that accept alignment
					// this action is visible
					if (count == 2) {
						return true;
					}
				}
			}
			return false;
		}
		
		override protected function computeNewBounds(selection:ArrayCollection):Dictionary {
			var reference:int = 0;
			var alignStyle:String = super.layoutStyle;
			
			switch(alignStyle) {
				case LayoutStyle.ALIGN_LEFT:
					reference = findMinXPosition(selection);
					break;
				case LayoutStyle.ALIGN_CENTER:
				 	reference = findCenterXPosition(selection);
				 	break;
				case LayoutStyle.ALIGN_RIGHT:
				 	reference = findMaxXPosition(selection);
				 	break;
				case LayoutStyle.ALIGN_TOP:
					reference = findMinYPosition(selection);
					break;
				case LayoutStyle.ALIGN_MIDDLE:
					reference = findMiddleYPosition(selection);
					break;
				case LayoutStyle.ALIGN_BOTTOM:
					reference = findMaxYPosition(selection);		  
			}
			
			var editPartToNewBounds:Dictionary = new Dictionary();
			
			for (var i:int = 0; i < selection.length; i++) {
				var part:EditPart = EditPart(selection[i]);
				
				if (canApplyLayout(part)) {
					editPartToNewBounds[part] = getNewBounds(getBoundsFromEditPart(part), reference);
				}				
			}
			
			return editPartToNewBounds;
		}
		
		private function findMinYPosition(selection:ArrayCollection):int {
			var minY:Number = NaN;
			
			for (var i:int; i < selection.length; i++) {
				var part:EditPart = EditPart(selection[i]);
				
				if (canApplyLayout(part)) {
					var bounds:Array = getBoundsFromEditPart(part);
					if (isNaN(minY) || bounds[1] < minY) {
						minY = bounds[1];
					}
				}
			}
			return minY;
		}
		
		private function findMaxYPosition(selection:ArrayCollection):int {
			var maxY:Number = NaN;
			
			for (var i:int; i < selection.length; i++) {
				var part:EditPart = EditPart(selection[i]);
				
				if (canApplyLayout(part)) {
					var bounds:Array = getBoundsFromEditPart(part);
					if (isNaN(maxY) || bounds[1] + bounds[3] > maxY) {
						maxY = bounds[1] + bounds[3];
					}
				}
			}
			return maxY;
		}
		
		private function findMiddleYPosition(selection:ArrayCollection):int {
			var minY:Number = NaN;
			var maxY:Number = NaN;
			
			for (var i:int; i < selection.length; i++) {
				var part:EditPart = EditPart(selection[i]);
				
				if (canApplyLayout(part)) {
					var bounds:Array = getBoundsFromEditPart(part);
					if (isNaN(minY) || bounds[1] < minY) {
						minY = bounds[1];
					}
					if (isNaN(maxY) || bounds[1] + bounds[3] > maxY) {
						maxY = bounds[1] + bounds[3];
					}
				}
			}
			return Math.round((minY + maxY) / 2);
		}
		
		private function findMinXPosition(selection:ArrayCollection):int {
			var minX:Number = NaN;
			
			for (var i:int; i < selection.length; i++) {
				var part:EditPart = EditPart(selection[i]);
				
				if (canApplyLayout(part)) {
					var bounds:Array = getBoundsFromEditPart(part);
					if (isNaN(minX) || bounds[0] < minX) {
						minX = bounds[0];
					}
				}
			}
			return minX;
		}
		
		private function findMaxXPosition(selection:ArrayCollection):int {
			var maxX:Number = NaN;
			
			for (var i:int; i < selection.length; i++) {
				var part:EditPart = EditPart(selection[i]);
				
				if (canApplyLayout(part)) {
					var bounds:Array = getBoundsFromEditPart(part);
					if (isNaN(maxX) || bounds[0] + bounds[2] > maxX) {
						maxX = bounds[0] + bounds[2];
					}
				}
			}
			return maxX;
		}
		
		private function findCenterXPosition(selection:ArrayCollection):int {
			var minX:Number = NaN;
			var maxX:Number = NaN;
			
			for (var i:int; i < selection.length; i++) {
				var part:EditPart = EditPart(selection[i]);
				
				if (canApplyLayout(part)) {
					var bounds:Array = getBoundsFromEditPart(part);
					if (isNaN(minX) || bounds[0] < minX) {
						minX = bounds[0];
					}
					if (isNaN(maxX) || bounds[0] + bounds[2] > maxX) {
						maxX = bounds[0] + bounds[2];
					}
				}
			}
			return Math.round((minX + maxX) / 2);
		}
		
		private function getNewBounds(currentBounds:Array, referenceValue:Number):Array {
			var x:int = currentBounds[0];
			var y:int = currentBounds[1];
			var width:Number = currentBounds[2];
			var height:Number = currentBounds[3];	
			
			var alignStyle:String = super.layoutStyle;
			
			switch(alignStyle) {
				case LayoutStyle.ALIGN_LEFT:
				// whe aligning left the referenceValue is the min X position of the selected elements 
					return [referenceValue, y, width, height];
				case LayoutStyle.ALIGN_CENTER:
				// when aligning center the referenceValue is the center of the distance between the 
				// left-most point and the right-most point of the selection
					return [referenceValue - Math.round(width / 2), y, width, height];
				case LayoutStyle.ALIGN_RIGHT:
				// when aligning right the referenceValue keeps the right-most point of the selection 
					return [referenceValue - width, y, width, height];
				case LayoutStyle.ALIGN_TOP:
				// when aligning top the referenceValue is the min Y position of the selected elements
					return [x, referenceValue, width, height];
				case LayoutStyle.ALIGN_MIDDLE:
				// when aligning middle the referenceValue is the middle point between the top-most
				// point of the selection and the bottom-most point
					return [x, referenceValue - Math.round(height / 2), width, height];
				case LayoutStyle.ALIGN_BOTTOM:
				// when aligning bottom the referenceValue is the bottom most point of the selection
					return [x, referenceValue - height, width, height];	
			}
			// it should enter one of the 6 options and not return this
			return currentBounds;
		}
		
	}
}