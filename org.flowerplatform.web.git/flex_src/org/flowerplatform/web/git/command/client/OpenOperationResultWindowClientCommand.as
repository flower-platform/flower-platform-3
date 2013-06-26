package org.flowerplatform.web.git.command.client {
	
	import com.crispico.flower.util.popup.TextAreaPopup;
	
	import org.flowerplatform.communication.command.AbstractClientCommand;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass]
	public class OpenOperationResultWindowClientCommand extends AbstractClientCommand {
		
		public var title:String;
		
		public var message:String;
		
		public override function execute():void {
			var popup:TextAreaPopup = new TextAreaPopup();
			popup.title = title;
			popup.width = 500;
			popup.height = 400;
			popup.text = message;
			popup.showPopup();
		}
	}
}