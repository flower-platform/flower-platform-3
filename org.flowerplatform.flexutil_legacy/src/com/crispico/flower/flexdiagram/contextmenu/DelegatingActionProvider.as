package com.crispico.flower.flexdiagram.contextmenu {
	
	import com.crispico.flower.flexdiagram.action.ActionContext;
	import com.crispico.flower.flexdiagram.action.IActionProvider2;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	public class DelegatingActionProvider implements IActionProvider2 {
		
		private var getContextFunction:Function;
		
		private var fillContextMenuFunction:Function;
		
		public function DelegatingActionProvider(getContextFunction:Function, fillContextMenuFunction:Function) {
			this.getContextFunction = getContextFunction;
			this.fillContextMenuFunction = fillContextMenuFunction;
		}

		public function getContext():ActionContext {
			if (this.getContextFunction != null)
				return this.getContextFunction();
			return new ActionContext();
		}

		public function fillContextMenu(contextMenu:FlowerContextMenu):void {
			this.fillContextMenuFunction(contextMenu);
		}
	}
}