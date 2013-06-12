package org.flowerplatform.communication.command {
	import mx.controls.Alert;
		
	/**
	 * @see JavaDoc
	 * @author Cristina
	 * @author Sorin
	 * @flowerModelElementId _gzb_gOXCEd-TPrek0YQo5A
	 */
	[RemoteClass]
	public class DisplaySimpleMessageClientCommand extends AbstractClientCommand  {
		
		private static const ICON_ERROR:int = 1;
		
		private static const ICON_INFORMATION:int = 2;
		
		private static const ICON_WARNING:int = 8;
		
		private var _title:String;
		
		private var _message:String;
		
		private var _icon:int;
		
		public var details:String;
		
		public function get title():String {
			return _title;
		}
		
		public function set title(value:String):void {
			_title = value;
		}
		
		public function get message():String {
			return _message;
		}
		
		public function set message(value:String):void {
			_message = value;
		}
		
		public function get icon():int {
			return _icon;
		}
		
		public function set icon(value:int):void {
			_icon = value;
		}
		
		/**
		 * Shows the message with the title and with appropiate icon.
		 */
		public override function execute():void {
			var image:Class;
			// TODO CS/COM: tr sa activam inapoi iconitele; poate sa afisam mereu noul dialog, in locul alertei?
//			switch (_icon) {
//				case ICON_ERROR :
//					image = FlowerMetamodelFlexModuleAssets.getImage("/full/obj16/error_obj.gif");
//					break;
//				case ICON_INFORMATION :								
//					image = FlowerMetamodelFlexModuleAssets.getImage("/full/obj16/info_obj.gif");
//					break;
//				case ICON_WARNING :
//					image = FlowerMetamodelFlexModuleAssets.getImage("/full/obj16/warning_obj.gif");
//					break;
//			}
			// TODO CS/FP2: copy DisplayMessageDialog; but more precisely: make this class compatible with mobile
//			if (details == null) // We use the Alert because the custom dialog doesn't know how to use establish it's size
				Alert.show(_message, _title, Alert.OK, null, null, image); 
//			else
//				DisplayMessageDialog.show(title, image, message, details); 
		}
	}
	
}