package org.flowerplatform.web.git.history {
	
	import com.crispico.flower.util.layout.view.ITabCustomizer;
	import com.crispico.flower.util.layout.view.ViewPopupWindow;
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.web.git.GitPlugin;

	/**
	 *	@author Cristina Constantinescu
	 */ 
	public class GitHistoryViewProvider implements IViewProvider {
		
		public static const ID:String = "gitHistory";
		
		public function getId():String {				
			return ID;
		}
				
		public function getIcon(viewLayoutData:ViewLayoutData = null):Object {	
			return GitPlugin.getInstance().getResourceUrl("images/full/obj16/history.gif");
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData = null):String {	
			return GitPlugin.getInstance().getMessage("git.history.view");
		}
				
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			var view:GitHistoryView = new GitHistoryView();
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