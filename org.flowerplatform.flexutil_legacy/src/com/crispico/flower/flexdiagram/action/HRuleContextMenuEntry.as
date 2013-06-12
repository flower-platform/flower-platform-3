package com.crispico.flower.flexdiagram.action {
	import com.crispico.flower.flexdiagram.action.IMenuEntrySortable;
	
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.controls.HRule;
	import mx.controls.Spacer;
	
	/**
	 * @author Daniela
	 */ 
	public class HRuleContextMenuEntry extends VBox implements IMenuEntrySortable {
		
		private var _sortIndex:int;
		
		protected static const WHITE_SPACE_PADDING_TOP:int = 1;
		
		protected static const WHITE_SPACE_PADDING_BOTTOM:int = 1;
		
		protected static const RULE_THICKNESS:int = 2;
		
		public function HRuleContextMenuEntry(sortIndex:int = int.MAX_VALUE) {
			super();
			_sortIndex = sortIndex;

			this.percentWidth = 100;
			this.setStyle("verticalGap", 0);
			
			var spacer:Spacer = new Spacer();
			spacer.minHeight = 0;
			spacer.height = WHITE_SPACE_PADDING_TOP;
			this.addChild(spacer);
			
			var hRule:HRule = new HRule();
			hRule.minHeight = 0;
			hRule.height = RULE_THICKNESS;
			hRule.percentWidth = 100;
			hRule.setStyle("strokeWidth", RULE_THICKNESS);
			hRule.setStyle("shadowColor", 0xFFFFFF);
			this.addChild(hRule);
			spacer = new Spacer();
			spacer.minHeight = 0;
			spacer.height = WHITE_SPACE_PADDING_BOTTOM;
			this.addChild(spacer);
		}
		
		public function get sortIndex():int {
			return _sortIndex;
		}
	}
}