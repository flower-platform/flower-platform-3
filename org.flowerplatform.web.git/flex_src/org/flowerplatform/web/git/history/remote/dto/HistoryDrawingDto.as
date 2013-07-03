package org.flowerplatform.web.git.history.remote.dto {
	import mx.collections.ArrayCollection;

	/**
	 *	@author Cristina Constantinescu
	 */
	[RemoteClass]
	public class HistoryDrawingDto {
		
		public static const DRAW_LINE:String = "drawLine";
		
		public static const DRAW_DOT:String = "drawDot";
		
		public var type:String;
		
		public var params:ArrayCollection;
		
	}
}