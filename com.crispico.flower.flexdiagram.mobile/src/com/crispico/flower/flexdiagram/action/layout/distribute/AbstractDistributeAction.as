package com.crispico.flower.flexdiagram.action.layout.distribute {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.FlexDiagramAssets;
	import com.crispico.flower.flexdiagram.action.layout.LayoutAction;
	import com.crispico.flower.flexdiagram.action.layout.LayoutStyle;
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;

	/**
	 * Basic DistributeAction that implements the main steps of the distribute algorithm.
	 * Subclasses stil need to override:
	 * <ul>
	 * <li>canApplyLayout()</li>
	 * <li>getBoundsFromEditPart()</li>
	 * <li>applyNewBounds()</li>
	 * </ul>  
	 * 
	 * For details see com.crispico.flower.flexdiagram.action.layout.LayoutAction.
	 * 
	 * @author Luiza
	 */ 
	public class AbstractDistributeAction extends LayoutAction {
		
		public function AbstractDistributeAction(distributeStyle:String) {
			super(distributeStyle);
		
			// set label and icon depending on the style
			switch(distributeStyle) {
				case LayoutStyle.DISTRIBUTE_HORIZONTAL:
					label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Distribute_Horizontal");
					image = FlexDiagramAssets.INSTANCE.distribute_horizontal_icon;
					break;
				case LayoutStyle.DISTRIBUTE_VERTICAL:
					label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Distribute_Vertical");
					image = FlexDiagramAssets.INSTANCE.distribute_vertical_icon;
					break;
				case LayoutStyle.DISTRIBUTE_AUTO:
					label = FlexDiagramAssets.INSTANCE.getMessage("UI_Action_Distribute_Auto");
					image = FlexDiagramAssets.INSTANCE.distribute_auto_icon;
					break;
			}
		}
		
		override protected function set layoutStyle(style:String):void {
			if (style == LayoutStyle.DISTRIBUTE_HORIZONTAL || style == LayoutStyle.DISTRIBUTE_VERTICAL || style == LayoutStyle.DISTRIBUTE_AUTO) {
				super.layoutStyle = style;
			} else {
				throw new Error("Unexpected distributeStyle");
			}
		}
		
		override public function isVisible(selection:ArrayCollection):Boolean {		
			var counter:int = 0;
				
			// at least 3 elements that accept this action must be selected
			for (var i:int; i < selection.length; i++) {
				var selectedPart:EditPart = EditPart(selection.getItemAt(i));
				
				if (canApplyLayout(selectedPart)) {
					counter ++;
					if (counter == 3) {
						return true;
					}
				} 
			}
			
			return false;
		}
				
		/**
		 * This function should return <code>true</code> if the given EditPart is 
		 * eligible for distribution. By default returns <code>false</code>.
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
			var autoDistribute:Boolean = false;
			if (super.layoutStyle == LayoutStyle.DISTRIBUTE_AUTO) {
				autoDistribute = true;
				// try to find which layout best fits on the selection: vertical or horizontal
				// order selection by position
				
				// try horizontal first
				super.layoutStyle = LayoutStyle.DISTRIBUTE_HORIZONTAL;
				
				var orderedSelection1:ArrayCollection = orderSelection(selection);
				// compute distance between elements
				var distribution1:Number = getDistribution(orderedSelection1);
				
				super.layoutStyle = LayoutStyle.DISTRIBUTE_VERTICAL;
				
				var orderedSelection2:ArrayCollection = orderSelection(selection);
				// compute distance between elements
				var distribution2:Number = getDistribution(orderedSelection2);
				
				if (distribution1 > distribution2) {
					var distribution:Number = distribution1;
					super.layoutStyle = LayoutStyle.DISTRIBUTE_HORIZONTAL;
					var orderedSelection:ArrayCollection = orderedSelection1;
				} else {
					distribution = distribution2;
					orderedSelection = orderedSelection2;
				}
			} else {
			
				// order selection by position
				var orderedSelection:ArrayCollection = orderSelection(selection);
				// compute distance between elements
				var distribution:Number = getDistribution(orderedSelection);
			}
			
			var editPartToNewBounds:Dictionary = new Dictionary();
			var prevBounds:Array = getBoundsFromEditPart(EditPart(orderedSelection.getItemAt(0))); 
			// first and last elements stay in same place
			// reposition only the one in center
			for (var i:int = 1; i < orderedSelection.length - 1; i++) {
				var ep:EditPart = EditPart(orderedSelection.getItemAt(i));
				prevBounds = getNewBoundsForEditPart(ep, prevBounds, distribution);
				editPartToNewBounds[ep] = prevBounds;
			}
			
			if (autoDistribute) {
				// go back to real layout style
				super.layoutStyle = LayoutStyle.DISTRIBUTE_AUTO;
			}
			return editPartToNewBounds;
		}
		
		/**
		 * @private
		 * Computes the new position for <code>editPart</code> by taking as reference the bounds of 
		 * the element positioned before this one - <code>prevBounds</code> - and adding the distribution.
		 * Note that distribution can be negative as well.
		 */ 
		private function getNewBoundsForEditPart(editPart:EditPart, prevBounds:Array, distribution:Number):Array {
			var position:int = 0;
			var size:int = 2;
			
			if (super.layoutStyle == LayoutStyle.DISTRIBUTE_VERTICAL) {
				position = 1;
				size = 3;
			}
			
			var currentBounds:Array = getBoundsFromEditPart(editPart);
			
			currentBounds[position] = Math.round(prevBounds[position] + prevBounds[size] + distribution);
			
			return currentBounds;
		}
		
		/**
		 * @private
		 * Computes the distribution of the selected elements. This is actually the equal amount of space (in pixels)
		 * between the distributed elements. This space is computed on one of the axes (OX or OY), depending on the 
		 * distribution style of this action (see layouStyle).
		 */ 
		private function getDistribution(orderedSelection:ArrayCollection):Number {
			var position:int = 0; // identifies x position  index in bounds
			var size:int = 2; // identifies width index in bounds
			
			if (super.layoutStyle == LayoutStyle.DISTRIBUTE_VERTICAL) {
				position = 1; // y position index in bounds
				size = 3; // height index in bounds
			} 
			
			var sumSize:Number = 0;
			for (var i:int = 0; i < orderedSelection.length - 1; i++) {
				sumSize += getBoundsFromEditPart(EditPart(orderedSelection.getItemAt(i)))[size];
			}
			
			var totalElements:int = orderedSelection.length; 
			// get position for last ep
			var lastPos:int = getBoundsFromEditPart(EditPart(orderedSelection.getItemAt(totalElements - 1)))[position];
			
			var firstPos:int = getBoundsFromEditPart(orderedSelection[0])[position];
			
			return (lastPos - firstPos - sumSize) / (totalElements - 1);
		}
		
		/**
		 * Order the selected EditParts by their position. Consider the size as well tyo find the last one
		 * - the biggest EditParts is considered max(position + size).
		 */ 
		private function orderSelection(selection:ArrayCollection):ArrayCollection {
			
			var orderedSelection:ArrayCollection = new ArrayCollection();
			
			// take the the max as last element in the selection
			var maxEp:EditPart = EditPart(selection.getItemAt(selection.length - 1));		
			orderedSelection.addItem(selection.getItemAt(0));
			
			for (var i:int = 1; i < selection.length; i++) {
				var ep:EditPart = EditPart(selection.getItemAt(i));
				if (canApplyLayout(ep)) {
					var added:Boolean = false;
					for (var j:int = 0; j < orderedSelection.length; j++) {	
						if (compareByPosition(ep, EditPart(orderedSelection.getItemAt(j))) < 0) {
							orderedSelection.addItemAt(ep, j);
							added = true;
							break;
						}
					}
					if (!added) {
						orderedSelection.addItem(ep);
					}
					if (compareByPositionAndSize(maxEp, ep) < 0) {
						maxEp = ep;
					}
				}
			}
			
			orderedSelection.removeItemAt(orderedSelection.getItemIndex(maxEp));
			orderedSelection.addItem(maxEp); // place the maximum on the last position
			
			return orderedSelection;
		}
		
		private function compareByPosition(ep1:EditPart, ep2:EditPart):int {
			var bounds1:Array = getBoundsFromEditPart(ep1);
			var bounds2:Array = getBoundsFromEditPart(ep2);
			
			var distributeStyle:String = super.layoutStyle;
			var position:int = 0; // check x position
			
			if (distributeStyle == LayoutStyle.DISTRIBUTE_VERTICAL) {
				position = 1; // check y position
			}
						
			if (bounds1[position] == bounds2[position]) {
				return 0;
			} else if (bounds1[position] < bounds2[position]) {
				return -1;	
			} else {
				return 1;
			}
		}	
		
		private function compareByPositionAndSize(ep1:EditPart, ep2:EditPart):int {
			var bounds1:Array = getBoundsFromEditPart(ep1);
			var bounds2:Array = getBoundsFromEditPart(ep2);
			
			var distributeStyle:String = super.layoutStyle;
			var position:int = 0; // chack x position
			var size:int = 2; //  and width
			
			if (distributeStyle == LayoutStyle.DISTRIBUTE_VERTICAL) {
				position = 1; // check y position
				size = 3; 	// and height
			}
			
			if (bounds1[position] + bounds1[size] == bounds2[position] + bounds2[size]) {
				return 0;
			} else if (bounds1[position] + bounds1[size] < bounds2[position] + bounds2[size]) {
				return -1;	
			} else {
				return 1;
			}
		}
		
	}
}