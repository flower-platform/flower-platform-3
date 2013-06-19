package org.flowerplatform.flexutil {
	import flash.display.DisplayObject;
	import flash.utils.getQualifiedClassName;
	
	import mx.controls.TextInput;
	import mx.core.ITextInput;
	import mx.core.UIComponent;
	import mx.utils.DescribeTypeCache;
	
	/**
	 * @author Cristina
	 */ 
	public class Utils {
		
		/**
		 * Makes the given text input non-editable and applies a grey color as background.
		 */ 
		public static function makePseudoDisabled(object:ITextInput):void {
			object.editable = false;
			object.setStyle("backgroundColor", "#DEDCDC");
			object.setStyle("color", "#666666");
		}
		
		/**
		 * @author Cristi
		 */
		public static function beginsWith(target:String, beginsWithWhat:String):Boolean {
			if (beginsWithWhat.length > target.length) {
				return false;
			} else if (target.substr(0, beginsWithWhat.length) == beginsWithWhat) {
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * @author Cristi
		 */
		public static function endsWith(target:String, endsWithWhat:String):Boolean {
			if (endsWithWhat.length > target.length) {
				return false;
			} else if (target.substr(target.length - endsWithWhat.length) == endsWithWhat) {
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * @author Cristi
		 */
		public static function getClassNameForObject(item:Object, fullyQualified:Boolean):String {
//			var classInfo:XML = DescribeTypeCache.describeType(item).typeDescription;
//			var simpleClassName:String = classInfo.@name;
			var simpleClassName:String = getQualifiedClassName(item);
			
			if (!fullyQualified) {
				simpleClassName = simpleClassName.substr(simpleClassName.search("::") + 2);
			}
			
			return simpleClassName;
		}
		
	}
}