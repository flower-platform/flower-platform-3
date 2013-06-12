package org.flowerplatform.flexutil.plugin {
	
	import flash.net.registerClassAlias;
	
	import mx.utils.DescribeTypeCache;
	
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class AbstractFlexPlugin {
	
		protected var _flexPluginDescriptor:FlexPluginDescriptor;
		
		public function get flexPluginDescriptor():FlexPluginDescriptor {
			return _flexPluginDescriptor;
		}
		
		public function set flexPluginDescriptor(value:FlexPluginDescriptor):void {
			_flexPluginDescriptor = value;
		}
		
		/**
		 * Subclasses should call super at the beginning.
		 */
		public function setupExtensionPointsAndExtensions():void {
			// nothing to do (yet)
		}
		
		/**
		 * Subclasses should call super at the beginning.
		 */
		public function start():void {
			// nothing to do (yet)
		}
		
		/**
		 * Registers the given class with an alias defined in the RemoteClass annotation (e.g. 
		 * <code>[RemoteClass(alias="my.custom.alias")]</code>. If alias is not defined,
		 * it uses the fully qualified name of the given class as alias (e.g. <code>
		 * [RemoteClass]</code>.
		 * 
		 * <p>
		 * <b>NOTE: </b> Don't forget to add to the current SWC the following
		 * compiler option: -keep-as3-metadata+=RemoteClass
		 */ 
		protected function registerClassAliasFromAnnotation(flexClass:Class):void {
			var flexClassInfo:XML = DescribeTypeCache.describeType(flexClass).typeDescription;
			var remoteClassAnnotation:XMLList = flexClassInfo.factory.metadata.(@name == "RemoteClass");
			
			if (remoteClassAnnotation.length() == 0) {
				throw new Error("The class " + Utils.getClassNameForObject(flexClass, true) + " should be annotated with '[RemoteClass]' or '[RemoteClass(alias='my.custom.alias')]'");
			}
					
			var alias:String = remoteClassAnnotation.arg.(@key == "alias").@value.toString();
			
			if (alias == null || alias.length == 0) {
				// no RemoteClass annotation => use the fq name of this class
				alias = Utils.getClassNameForObject(flexClass, true).replace("::", ".");
			}
			registerClassAlias(alias, flexClass);
		}
	}
}