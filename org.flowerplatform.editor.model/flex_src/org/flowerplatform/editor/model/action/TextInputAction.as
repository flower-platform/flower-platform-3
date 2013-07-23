package org.flowerplatform.editor.model.action {
	
	import flash.events.MouseEvent;
	
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.IMessageBox;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class TextInputAction extends ActionBase {
		
		public function TextInputAction() {
			super();
		}
		
		protected function askForTextInput(defaultText:String, title:String, button:String, handler:Function):void {
			var textArea:Object;
			var name:String = defaultText;
			var messageBox:Object = FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(title)
				.setText(name)
				.addButton(button, function(evt:MouseEvent = null):void {
					if (textArea != null) {
						name = textArea.text;
					}
					handler(name);
				})
				.addButton("Cancel");
			if (messageBox.hasOwnProperty("textArea")) {
				textArea = messageBox.textArea;
			}
			IMessageBox(messageBox).showMessageBox();
		}
	}
}