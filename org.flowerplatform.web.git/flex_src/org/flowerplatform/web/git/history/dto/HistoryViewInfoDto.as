package org.flowerplatform.web.git.history.dto {
	import org.flowerplatform.web.git.dto.ViewInfoDto;
			
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class HistoryViewInfoDto extends ViewInfoDto {
		
		public static const SHOWALLRESOURCE:int = 0;
		
		public static const SHOWALLFOLDER:int = 1;
		
		public static const SHOWALLPROJECT:int = 2;
		
		public static const SHOWALLREPO:int = 3;
				
		public var path:String;
		
		public var info:String;		
							
		public var filter:int;
	}
	
}