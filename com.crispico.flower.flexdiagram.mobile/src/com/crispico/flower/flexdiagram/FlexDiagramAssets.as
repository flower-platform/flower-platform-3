package com.crispico.flower.flexdiagram {
	
	import mx.resources.ResourceManager;
	
	/**
	 * @flowerModelElementId _LzYtoJHbEeC8Zpq2X3Nlbg
	 */
	[ResourceBundle("com_crispico_flower_flexdiagram")]
	
	/**
	 * General class that  holds the assets (images, messages).
	 */ 
	public class FlexDiagramAssets {
		
		public static const INSTANCE:FlexDiagramAssets = new FlexDiagramAssets();
		
		private const MESSAGE_FILE:String = "com_crispico_flower_flexdiagram";
		
		/**
		 * @flowerModelElementId _LzZUs5HbEeC8Zpq2X3Nlbg
		 */
		[Embed(source='/icons/print.png')]
		public var print_icon:Class;
		
		/**
		 * @flowerModelElementId _LzZ7wpHbEeC8Zpq2X3Nlbg
		 */
		[Embed(source='/icons/info.png')]
		public var info_icon:Class;
		
		[Embed(source='/icons/exportpdf.gif')]	
		public var pdf_icon:Class;
		
		[Embed(source='/icons/exportimage.png')]	
		public var export_img_icon:Class;
		
		[Embed(source='/icons/align_bottom.png')]
		public var align_bottom_icon:Class;
		
		[Embed(source='/icons/align_middle.png')]
		public var align_middle_icon:Class;
		
		[Embed(source='/icons/align_top.png')]
		public var align_top_icon:Class;
		
		[Embed(source='/icons/align_left.png')]
		public var align_left_icon:Class;
		
		[Embed(source='/icons/align_center.png')]
		public var align_center_icon:Class;
		
		[Embed(source='/icons/align_right.png')]
		public var align_right_icon:Class;
		
		[Embed(source='/icons/resize_width.png')]
		public var resize_width_icon:Class;
		
		[Embed(source='/icons/resize_height.png')]
		public var resize_height_icon:Class;
		
		[Embed(source='/icons/resize_both.png')]
		public var resize_both_icon:Class;
		
		[Embed(source='/icons/distribute_horizontal.png')]
		public var distribute_horizontal_icon:Class;
		
		[Embed(source='/icons/distribute_vertical.png')]
		public var distribute_vertical_icon:Class;
		
		[Embed(source='/icons/distribute_auto.png')]
		public var distribute_auto_icon:Class;
				
		/**
		 * Retrieves a message from the properties files. Parameters can be passed
		 * and the {?} place holders will be replaced with them.
		 * @flowerModelElementId _LzZ7xZHbEeC8Zpq2X3Nlbg
		 */
		public function getMessage(messageId:String, params:Array=null):String {				
			return ResourceManager.getInstance().getString(MESSAGE_FILE, messageId, params);
		}

	}
}