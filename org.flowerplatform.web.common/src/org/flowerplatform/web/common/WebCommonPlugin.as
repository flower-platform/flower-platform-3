package org.flowerplatform.web.common {
	
	import flash.net.registerClassAlias;
	
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.communication.AuthenticationManager;
	import org.flowerplatform.web.common.communication.heartbeat.HeartbeatStatefulClient;
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
	import org.flowerplatform.web.common.security.dto.GroupAdminUIDto;
	import org.flowerplatform.web.common.security.dto.OrganizationAdminUIDto;
	import org.flowerplatform.web.common.security.dto.OrganizationUserAdminUIDto;
	import org.flowerplatform.web.common.security.dto.PermissionAdminUIDto;
	import org.flowerplatform.web.common.security.dto.PermissionsByResourceFilter;
	import org.flowerplatform.web.common.security.dto.UserAdminUIDto;
	import org.flowerplatform.web.common.security.dto.User_CurrentUserLoggedInDto;
	
	/**
	 * @author Cristi
	 */
	public class WebCommonPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:WebCommonPlugin;
		
		public static function getInstance():WebCommonPlugin {
			return INSTANCE;
		}
		
		public var authenticationManager:AuthenticationManager;
		
		public var heartbeatStatefulClient:HeartbeatStatefulClient;
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;
			authenticationManager = new AuthenticationManager();
//			heartbeatStatefulClient = new HeartbeatStatefulClient();
		}
		
		override public function setupExtensionPointsAndExtensions():void {
			super.setupExtensionPointsAndExtensions();
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new ExplorerViewProvider());
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
		override protected function registerClassAliases():void {
			super.registerClassAliases();
			registerClassAlias("org.flowerplatform.web.entity.dto.NamedDto", NamedDto);
			registerClassAlias("org.flowerplatform.web.security.dto.UserAdminUIDto", UserAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.User_CurrentUserLoggedInDto", User_CurrentUserLoggedInDto);
			registerClassAlias("org.flowerplatform.web.security.dto.GroupAdminUIDto", GroupAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.OrganizationAdminUIDto", OrganizationAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.OrganizationUserAdminUIDto", OrganizationUserAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.PermissionAdminUIDto", PermissionAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.PermissionsByResourceFilter", PermissionsByResourceFilter);
		}
		
	}
}