package com.crispico.flower.flexdiagram.print {
	
	/**
	 * Object embeding the export options. Returned by <code>ExportOptionsDialog</code> and handled by the </code>ExportPDFManager</code>.
	 * 
	 * @flowerModelElementId _1hGHMJaDEeCy8_KPvpsIEQ
	 */
	public class ExportOptions {
				
		/**
		 * Constant defining Landscape page orientation.
		 * 
		 * @flowerModelElementId _-MMZoJaEEeCy8_KPvpsIEQ
		 */ 
		public static const LANDSCAPE:String = "L";
		
		/**
		 * Constant defining Portrait page orientation
		 * @flowerModelElementId _-My2kZaEEeCy8_KPvpsIEQ
		 */ 
		public static const PORTRAIT:String = "P";
		
		
		public static const MIN_QUALITY:Number = 1;
		
		
		public static const MAX_QUALITY:Number = 2.4;
				
		/**
		 * The exported page orientation (#PORTRAIT or #LANDSCAPE).
		 * 
		 * @flowerModelElementId _-M2g8ZaEEeCy8_KPvpsIEQ
		 */
		public var pageOrientation:String;
		
		/**
		 * The exported image quality. Maximum accepted is 300 dpi. (Flex limitation). 
		 * 
		 * @flowerModelElementId _-M4WIZaEEeCy8_KPvpsIEQ
		 */
		public var quality:Number;
			
		public var removeIcons:Boolean;
		
		
		public function ExportOptions(pageOrientation:String=PORTRAIT, quality:Number=MIN_QUALITY, removeIcons:Boolean=false) {
			this.pageOrientation = pageOrientation;
			this.quality = quality;
			this.removeIcons = removeIcons; 
		}
		
///////////////////////////////////////////////////////////////////////////////////////////////
// keep this to use later when refactor. This options can be common with the export as image		
//		/**
//		 * @flowerModelElementId _wVzLUJdlEeCKA_g6f9YZjA
//		 */
//		public static var PNG:String="png";
//		
//		/**
//		 * @flowerModelElementId _zio6wJdlEeCKA_g6f9YZjA
//		 */
//		public static var JPG:String="jpg";
//		
//		/**
//		 * @flowerModelElementId _pSeSgJdlEeCKA_g6f9YZjA
//		 */
//		public static var PDF:String="pdf";

//		/**
//		 * Defines the type of file that will result when encoding. Can be one of the three options:
//		 * <ul>
//		 * 	<li>#PNG
//		 * 	<li>#JPG
//		 * 	<li>#PDF
//		 * </ul>
//		 * @flowerModelElementId _mcC38JdlEeCKA_g6f9YZjA
//		 */
//		public var type:String; 
/////////////////////////////////////////////////////////////////////////////////////////////////	

	}
}