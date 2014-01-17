package org.flowerplatform.mindmap.action {
	
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.utils.DescribeTypeCache;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.communication.transferable_object.TransferableObjectUpdatedEvent;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.mindmap.Diagram;
	import org.flowerplatform.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.mindmap.MindMapPlugin;
	import org.flowerplatform.mindmap.remote.Node;
		
	public class RefreshAction extends ActionBase {
		
		private var recursive:Boolean;
		
		private var editorFrontend:MindMapEditorFrontend;
				
		public function RefreshAction(editorFrontend:MindMapEditorFrontend, recursive:Boolean = false) {
			this.editorFrontend = editorFrontend;
			this.recursive = recursive;
			label = recursive ? "Refresh Recursive" : "Refresh";
			orderIndex = recursive ? 50 : 40;
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			refresh(Node(selection.getItemAt(0)));
		}
		
		private function refresh(node:Node):void {			
			if (recursive) {
				for each (var child:Node in node.children) {
					refresh(child);
				}
			}
			MindMapPlugin.getInstance().service.refresh(node.id, this, refreshCallbackHandler);
		}
		
		private function refreshCallbackHandler(result:ArrayCollection):void {
			var diagramShell:MindMapDiagramShell = MindMapDiagramShell(editorFrontend.diagramShell);
			
			var oldNode:Node = MindMapPlugin.getInstance().service.getNodeFromId(Node(Diagram(diagramShell.rootModel).rootNode), result[0]);
			var newNode:Node = result[1];
			copyProperties(newNode, oldNode, false);			
		}
		
		protected function copyProperties(source:IEventDispatcher, dest:IEventDispatcher, postProcessOnly:Boolean):void {			
			var classInfo:XML = DescribeTypeCache.describeType(dest).typeDescription;
			for each (var v:XML in classInfo..accessor) {
				if (v.@name != null && v.@access != 'readonly' && !ObjectUtil.hasMetadata(dest, v.@name, 'Transient')) {
					copyProperty(source, dest, v.@name, postProcessOnly);
				}
			}
		}
		
		protected function copyProperty(source:Object, dest:Object, propertyName:String, postProcessOnly:Boolean):void {
			if (!postProcessOnly && dest[propertyName] != source[propertyName]) {
				dest[propertyName] = source[propertyName];
			}
		}
	}
}