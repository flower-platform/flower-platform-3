package com.crispico.flower.flexdiagram.toolbar {
	
	import com.crispico.flower.flexdiagram.tool.Tool;
	
	/**
	 * ToolbarButton with Tool assigned.
	 * Setting the tool automatically updates the icon and the label of the ToolbarButton with the information in the received Tool.
	 * <p>
	 * When the attached Tool finishes work this component might dispatch a ToolbarButtonJobFinishedEvent.
	 *  
	 * @flowerModelElementId _wCmccEVpEeCoWZkKIB9exA
	 * 
	 */
	public class ToolbarButtonWithTool extends ToolbarButton {
		
		private var _tool:Tool;
		
		/**
		 * The attached Tool
		 */ 
		public function get tool():Tool {
			return _tool;
		}
		
		public function set tool(newTool:Tool):void { 
			this._tool = newTool;
		
			if (_tool != null) {
				setLabel(_tool.getLabel());
				setIcon(_tool.getIcon());
			}
		}
		
	}
}