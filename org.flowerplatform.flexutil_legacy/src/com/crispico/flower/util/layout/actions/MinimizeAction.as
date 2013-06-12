package  com.crispico.flower.util.layout.actions {
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import mx.collections.ArrayCollection;

	/**
	 * @flowerModelElementId _RhkyIOCZEeGdYcOEhSk3ug
	 */
	public class MinimizeAction extends BaseAction  {
		
		/**
		 * @flowerModelElementId _thvC0OtxEeGb_JdgRgmL9A
		 */
		private var workbench:Workbench;
		
		/**
		 * @flowerModelElementId _thvp4OtxEeGb_JdgRgmL9A
		 */
		public function MinimizeAction(workbench:Workbench) {
			this.workbench = workbench;
			label = UtilAssets.INSTANCE.getMessage("layout.action.minimize");
			image = UtilAssets.INSTANCE._minimizeViewIcon;
			sortIndex = 30;
		}
			
		/**
		 * @flowerModelElementId _UKy3IOCZEeGdYcOEhSk3ug
		 */
		public override function run(selectedEditParts:ArrayCollection):void {	
			var viewLayoutData:ViewLayoutData = selectedEditParts[0];
			workbench.minimize(StackLayoutData(viewLayoutData.parent));			
		}
	}
}