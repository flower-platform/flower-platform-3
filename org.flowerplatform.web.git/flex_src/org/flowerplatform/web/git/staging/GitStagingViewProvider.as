package org.flowerplatform.web.git.staging {
	
	import com.crispico.flower.util.layout.view.ITabCustomizer;
	import com.crispico.flower.util.layout.view.ViewPopupWindow;
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.web.git.GitPlugin;
	
	/**
	 *	@author Cristina Constantinescu
	 */
	public class GitStagingViewProvider implements IViewProvider {
		
		public static const ID:String = "gitStaging";
		
		public function getId():String {				
			return ID;
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData = null):Object {	
			return GitPlugin.getInstance().getResourceUrl("images/eview16/staging.png");
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData = null):String {	
			return GitPlugin.getInstance().getMessage("git.staging.view");
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			var view:GitStagingView = new GitStagingView();
			view.label = getTitle(viewLayoutData);
			return view;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {	
			return null;
		}	
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
	
}