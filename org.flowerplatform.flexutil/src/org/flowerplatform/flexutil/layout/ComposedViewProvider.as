package  org.flowerplatform.flexutil.layout {
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * Singleton class representing a registry that stores data about
	 * layout (e.g. perspectives, view providers, etc.)
	 * 
	 * <p>
 	 * Implements of <code>IViewProvider</code> and delegates 
	 * to <code>IViewProvider</code>s registered by plugins.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _C1mtIFCzEeGsUPSh9UfXpw
	 */
	public class ComposedViewProvider implements IViewProvider {
		
		/**
		 * @see Getter doc.
		 * 
		 * @flowerModelElementId _KyV6MFCzEeGsUPSh9UfXpw
		 */
		private var _perspectives:ArrayCollection = new ArrayCollection();
		
		/**
		 * @see Getter doc.
		 * 
		 * @flowerModelElementId _32mHQFDFEeGMrNbRkxqlAA
		 */
		private var _viewProviders:ArrayCollection = new ArrayCollection();
		
		/**
		 * Holds a list of available <code>IViewProvider</code>s.
		 * 
		 * <p>
		 * The view providers registered here should accept <code>null</code>
		 * as parameter for <code>IViewProvider.getTitle()</code> and 
		 * <code>IViewProvider.getIcon()</code> and return not <code>null</code>
		 * results. Except editors, that should return <code>null</code>.
		 * 
		 * @see #addViewProvider()
		 * @flowerModelElementId _jnR40FDGEeGMrNbRkxqlAA
		 */
		public function get viewProviders():ArrayCollection {
			return _viewProviders;
		}
		
		/**
		 * Used by plugins to add new <code>IViewProvide</code>s.
		 * 
		 * @see #viewProviders
		 * @flowerModelElementId _dJQFcFDGEeGMrNbRkxqlAA
		 */
		public function addViewProvider(viewProvider:IViewProvider):void {
			viewProviders.addItem(viewProvider);
		}
		
		/**
		 * Returns null, it isn't used.
		 * 
		 * @flowerModelElementId _aVKM8FPWEeG3TZATXzuYYg
		 */
		public function getId():String {
			return null;	
		}
		
		/**
		 * Creates the graphical component (specified by the parameter) by
		 * iterating through all <code>viewProviders</code> in searching for the
		 * corresponding view provider id and returns <code>IViewProvider#createView()</code>.
		 * 
		 * <p>
		 * If none exists, returns null.
		 * 
		 * @flowerModelElementId _WVhjUFDQEeGMrNbRkxqlAA
		 */
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {	
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {
					return viewProvider.createView(viewLayoutData);	
				}							
			}
			return null;
		}
		
		/**
		 * Gets the title of the view (corresponding to the parameter) by
		 * iterating through all <code>viewProviders</code> in searching for the
		 * corresponding view provider id and returns <code>IViewProvider#getTitle()</code>.
		 * 
		 * <p>
		 * If none exists, returns null.
		 * 
		 * <p>
		 * If the component this view provides implements the <code>IDirtyStateProvider</code>
		 * interface, and it is dirty, then a "*" is appended to the original title
		 * 
		 * @flowerModelElementId _WVKW8FDQEeGMrNbRkxqlAA
		 */
		public function getTitle(viewLayoutData:ViewLayoutData = null):String {	
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {			
					var title:String = viewProvider.getTitle(viewLayoutData);
//					var component:UIComponent = SingletonRefsFromPrePluginEra.workbench.layoutDataToComponent[viewLayoutData];
//					if (component is IDirtyStateProvider) {
//						if (IDirtyStateProvider(component).isDirty()) {
//							title = "*" + title;
//						}
//					}
					return title;
				}			
			}
			return null;
		}
		
		/**
		 * Gets the icon of the view (corresponding to the parameter) by
		 * iterating through all <code>viewProviders</code> in searching for the
		 * corresponding view provider id and returns <code>IViewProvider#getIcon()</code>.
		 * 
		 * <p>
		 * If none exists, returns null.
		 * 
		 * @flowerModelElementId _WVegAFDQEeGMrNbRkxqlAA
		 */
		public function getIcon(viewLayoutData:ViewLayoutData = null):Object {	
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {					
					return viewProvider.getIcon(viewLayoutData);	
				}			
			}
			return null;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {					
					return viewProvider.getTabCustomizer(viewLayoutData);	
				}			
			}
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {					
					return viewProvider.getViewPopupWindow(viewLayoutData);	
				}			
			}
			return null;
		}
		
		public function getViewProvider(id:String):IViewProvider {
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (id == viewProvider.getId()) {					
					return viewProvider;	
				}			
			}
			return null;
		}
		
	}
	
}