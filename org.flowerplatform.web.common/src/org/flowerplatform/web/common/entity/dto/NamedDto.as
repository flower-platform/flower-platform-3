package org.flowerplatform.web.common.entity.dto {
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 	 
	 * @flowerModelElementId _rf68wFfcEeG3xJSMfQ3HWg
	 */
	[RemoteClass(alias="org.flowerplatform.web.entity.dto.Dto")]
	[Bindable]	
	public class NamedDto extends Dto {
		
		/**
		 * @flowerModelElementId _wjOekFfcEeG3xJSMfQ3HWg
		 */
		public var name:String;
		
		public function toString():String {
			return name;
		}
		
		
	}
	
}