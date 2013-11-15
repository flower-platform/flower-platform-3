/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation version 3.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
* 
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package  org.flowerplatform.web.action {

	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	
	/**
	 * Menu action displayed in the menu bar.
	 * 
	 * <p>
	 * If the corresponding <code>viewProvider</code> graphical component already exists on workbench,
	 * does nothing.
	 * Otherwise, adds the component near the current active view. 
	 * If none exists, add it as the first child in <code>WorkbenchLayoutData</code>.
	 * 
	 * <p>	 
	 * The action has the view's name and icon.
	 * 
	 * @author Cristina	Constatinescu
	 */
	public class ShowViewAction extends ActionBase {
				
		public static const DEFAULT:int = 0;		
		public static const BELOW_EDITOR:int = 1;
		
		protected var viewProvider:IViewProvider;
		
		private var side:int;
		
		public function ShowViewAction(viewProvider:IViewProvider=null, side:int = 0) {
			if (viewProvider != null) {
				label = viewProvider.getTitle()
				icon = viewProvider.getIcon();
			}
			this.viewProvider = viewProvider;
			this.side = side;
			this.parentId = "show_view";
		}
		
		/**
		 * @see Workbench#addNearWorkbenchLayoutData()
		 * @see Workbench#addOverStackLayoutData()		
		 */
		public override function run():void {
			var workbench:Workbench = Workbench(FlexUtilGlobals.getInstance().workbench);		
			var component:UIComponent = workbench.getComponent(viewProvider.getId());
			
			// verifies if the view is already on workbench
			if (component == null) {
				var parentStack:StackLayoutData = null;
				if (side == BELOW_EDITOR) {
					parentStack = workbench.getStackBelowEditorSash();
				}
				component = workbench.addNormalView(viewProvider.getId(), false, -1, false, parentStack);
			}
			workbench.callLater(workbench.activeViewList.setActiveView, [component]);
		}
	}
	
}