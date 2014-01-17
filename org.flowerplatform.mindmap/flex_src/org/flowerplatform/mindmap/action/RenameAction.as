package org.flowerplatform.mindmap.action
{
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.popup.IMessageBox;
	import org.flowerplatform.mindmap.MindMapPlugin;
	import org.flowerplatform.mindmap.remote.Node;
	
	import spark.components.TextArea;
	
	public class RenameAction extends ActionBase {
		
		public function RenameAction() {			
			label = "Rename";
			orderIndex = 30;
		}
		
		protected function askForTextInput(defaultText:String, title:String, button:String, handler:Function):IMessageBox {
			var textArea:Object;
			var name:String = defaultText;
			var messageBox:Object = FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(title)
				.setText(name)
				.setWidth(300)
				.setHeight(200)
				.setSelectText(true)
				.addButton(button, function(evt:MouseEvent = null):void {
					if (textArea != null) {
						name = textArea.text;
					}
					handler(name);
				})
				.addButton("Cancel");
			if (messageBox.hasOwnProperty("textArea")) {
				textArea = messageBox.textArea;
				if (textArea.hasOwnProperty("editable")) {
					textArea.editable = true;
				}				
			}
			IMessageBox(messageBox).showMessageBox();
			return IMessageBox(messageBox);
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var messageBox:IMessageBox = askForTextInput(node.body, "Rename", "Rename",
				function(name:String):void {
					MindMapPlugin.getInstance().service.setBody(node.id, name);
				});		
		}
				
	}
}