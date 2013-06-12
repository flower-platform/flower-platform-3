package com.crispico.flower.util.layout.actions
{
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import mx.collections.ArrayCollection;

	public class RestoreAction extends BaseAction {
		
		private var workbench:Workbench;
		
		public function RestoreAction(workbench:Workbench) {
			this.workbench = workbench;
			label = UtilAssets.INSTANCE.getMessage("layout.action.restore");
			image = UtilAssets.INSTANCE._restoreViewIcon;
			sortIndex = 10;
		}
			
		public override function isVisible(selectedEditParts:ArrayCollection):Boolean {	
			var viewLayoutData:ViewLayoutData = selectedEditParts[0];
			if (StackLayoutData(viewLayoutData.parent).mrmState == StackLayoutData.MAXIMIZED) {
				return true;
			}
			return false;
		}
		
		public override function run(selectedEditParts:ArrayCollection):void {	
			var viewLayoutData:ViewLayoutData = selectedEditParts[0];
			workbench.restore(StackLayoutData(viewLayoutData.parent));			
		}
	}
}