package  org.flowerplatform.web.git.repository {
	import mx.controls.Button;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.web.git.GitCommonPlugin;
	
	/**
	 * @author Cristina	
	 */
	public class GitRepositoriesViewProvider implements IViewProvider {
		
		public static const ID:String = "gitRepositories";
		
		public function getId():String {				
			return ID;
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {	
			return GitCommonPlugin.getInstance().getResourceUrl("images/eview16/repo_rep.gif");
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {	
			return GitCommonPlugin.getInstance().getMessage("git.repositories.view.name");
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {	
			return null;
		}	
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {			
			return new Button();
		}
	}
	
}