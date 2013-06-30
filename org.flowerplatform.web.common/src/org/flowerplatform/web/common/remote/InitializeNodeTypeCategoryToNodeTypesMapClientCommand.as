package org.flowerplatform.web.common.remote {
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	[RemoteClass(alias="org.flowerplatform.web.remote.InitializeNodeTypeCategoryToNodeTypesMapClientCommand")]
	public class InitializeNodeTypeCategoryToNodeTypesMapClientCommand extends AbstractClientCommand {
		
		public var nodeTypeCategoryToNodeTypesMap:Object;
		
		override public function execute():void {
			WebCommonPlugin.getInstance().nodeTypeCategoryToNodeTypesMap = nodeTypeCategoryToNodeTypesMap;
		}
		
		
	}
}