package org.flowerplatform.web.entity.dto {
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 	 
	 * @flowerModelElementId _rf68wFfcEeG3xJSMfQ3HWg
	 */
	[RemoteClass(alias="com.crispico.flower.mp.web.entity.dto.NamedDto")]
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