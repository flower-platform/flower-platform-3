package org.flowerplatform.mindmap.action {
	
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.mindmap.MindMapPlugin;
	import org.flowerplatform.mindmap.remote.Node;
	
	public class RemoveNodeAction extends ActionBase {
		
		public function RemoveNodeAction() {
			super();
			label = "Remove Node";	
			orderIndex = 20;
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			MindMapPlugin.getInstance().service.removeNode(Node(selection.getItemAt(0)).id);
		}
	}
}