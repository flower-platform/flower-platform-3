package  com.crispico.flower.util.layout.actions {
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.Workbench;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import com.crispico.flower.util.layout.view.LayoutTabNavigator;
	
	import flexlib.containers.SuperTabNavigator;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	/**
	 * @flowerModelElementId _ffhTsOCZEeGdYcOEhSk3ug
	 */
	public class CloseAction extends BaseAction  {
		
		/**
		 * @flowerModelElementId _jEi5oOCZEeGdYcOEhSk3ug
		 */
		public static const CLOSE:int = 0;
		
		/**
		 * @flowerModelElementId _kbal8OCZEeGdYcOEhSk3ug
		 */
		public static const CLOSE_ALL:int = 1;
		
		/**
		 * @flowerModelElementId _m_u80OCZEeGdYcOEhSk3ug
		 */
		public static const CLOSE_OTHERS:int = 2;
		
		/**
		 * @flowerModelElementId _hzGf4OCZEeGdYcOEhSk3ug
		 */
		private var type:int;
		
		/**
		 * @flowerModelElementId _thpjQOtxEeGb_JdgRgmL9A
		 */
		private var workbench:Workbench;
				
		/**
		 * @flowerModelElementId _thpjQetxEeGb_JdgRgmL9A
		 */
		public function CloseAction(workbench:Workbench, type:int) {
			this.workbench = workbench;
			this.type = type;
			switch(type) {
				case CLOSE:	{
					label = UtilAssets.INSTANCE.getMessage("layout.action.close");
					image = UtilAssets.INSTANCE._closeTabIcon;
					sortIndex = 50;
					break;
				}
				case CLOSE_ALL:	{
					label = UtilAssets.INSTANCE.getMessage("layout.action.closeAll");
					image = UtilAssets.INSTANCE._closeAllViewIcon;
					sortIndex = 60;
					break;
				}
				case CLOSE_OTHERS:	{
					label = UtilAssets.INSTANCE.getMessage("layout.action.closeOthers");
					image = UtilAssets.INSTANCE._closeViewIcon;
					sortIndex = 70;
					break;
				}
			}			
		}
		
		/**
		 * @flowerModelElementId _1TYooOCZEeGdYcOEhSk3ug
		 */
		public override function run(selectedEditParts:ArrayCollection):void {				
			var viewLayoutData:ViewLayoutData = ViewLayoutData(selectedEditParts[0]);
			var views:ArrayCollection = new ArrayCollection();
			switch(type) {
				case CLOSE:	{
					views.addItem(viewLayoutData);
					break;
				}
				case CLOSE_ALL:	{
					views = viewLayoutData.parent.children;
					for each (var child:ViewLayoutData in views) {
						if (workbench.activeViewList.getActiveView() == workbench.layoutDataToComponent[child]) {
							workbench.activeViewList.removeActiveView(false);
							break;
						}
					}
					
					break;
				}
				case CLOSE_OTHERS:	{
					for each (var child:ViewLayoutData in viewLayoutData.parent.children) {
						if (child != viewLayoutData) {
							views.addItem(child);
						}
					}
					break;
				}
			}
			var components:ArrayCollection = new ArrayCollection();			
			for each (var view:ViewLayoutData in views) {
				components.addItem(workbench.layoutDataToComponent[view]);							
			}
			workbench.closeViews(components);			
		}
	}
}