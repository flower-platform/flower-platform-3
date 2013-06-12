package com.crispico.flower.util {
	import mx.resources.ResourceManager;
	
	[ResourceBundle("com_crispico_flower_util")]
	/**
	 * @author Cristi
	 */
	public class UtilAssets {
		
		[Bindable]
		public static var INSTANCE:UtilAssets = new UtilAssets(); 
		
		/**
		 * Retrieves a message from the properties files. Parameters can be passed
		 * and the {?} place holders will be replaced with them.
		 * 
		 * Copied from MP.
		 */
		public function getMessage(messageId:String, params:Array=null):String {				
			return ResourceManager.getInstance().getString("com_crispico_flower_util", messageId, params);
		}
		
		[Embed(source="/view.png")]
		public var _viewIcon:Class;
		
		[Embed(source="/views.png")]
		public var _viewsIcon:Class;
		
		[Embed(source="/close_tab.png")]
		public var _closeTabIcon:Class;
		
		[Embed(source="/close_view.png")]
		public var _closeViewIcon:Class;
		
		[Embed(source="/close_all_views.png")]
		public var _closeAllViewIcon:Class;
		
		[Embed(source="/move_view.png")]
		public var _moveViewIcon:Class;
				
		[Embed(source="/tab_min.gif")]      
		public var tabMin:Class;
				
		[Embed(source="/tab_max.gif")]      
		public var tabMax:Class;
		
		[Embed(source="/tab_res.gif")]      
		public var tabRes:Class;
		
		[Embed(source="/maximize_view.png")]      
		public var _maximizeViewIcon:Class;
		
		[Embed(source="/restore_view.png")]      
		public var _restoreViewIcon:Class;
		
		[Embed(source="/minimize_view.png")]      
		public var _minimizeViewIcon:Class;
		
		[Embed(source="/dock.png")]      
		public var _dockIcon:Class;
		
		[Embed(source="/calendar.png")]
		public var _calendar:Class;
		
	}
}