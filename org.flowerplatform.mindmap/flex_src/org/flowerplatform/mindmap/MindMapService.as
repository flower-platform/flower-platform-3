package org.flowerplatform.mindmap {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.mindmap.remote.Node;
	
	public class MindMapService {
		
		public function getNodeFromId(node:Node, nodeId:String):Node {
			if (node.id == nodeId) {
				return node;
			}
			if (node.children != null) {
				for each (var child:Node in node.children) {
					var n:Node = getNodeFromId(child, nodeId);
					if (n != null) {
						return n;
					}
				}
			}
			return null;
		}
		
		public function getChildrenForNodeId(nodeId:String, callbackObject:Object, callbackFunction:Function):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"mindMapService", 
					"getChildrenForNodeId", 
					[nodeId], callbackObject, callbackFunction));
		}
		
		public function reload(callbackObject:Object, callbackFunction:Function):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"mindMapService", 
					"reload", 
					null, callbackObject, callbackFunction));
		}
		
		public function save():void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"mindMapService", 
					"save"));
		}
		
		public function refresh(nodeId:String, callbackObject:Object, callbackFunction:Function):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"mindMapService", 
					"refresh", 
					[nodeId], callbackObject, callbackFunction));
		}
		
		public function setBody(nodeId:String, newBodyValue:String):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"mindMapService", 
					"setBody", 
					[nodeId, newBodyValue]));
		}
		
		public function addNode(parentNodeId:String, type:String):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"mindMapService", 
					"addNode", 
					[parentNodeId, type]));
		}
		
		public function removeNode(nodeId:String):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"mindMapService", 
					"removeNode", 
					[nodeId]));
		}
		
		public function moveNode(nodeId:String, newParentNodeId:String, newIndex:int):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"mindMapService", 
					"moveNode", 
					[nodeId, newParentNodeId, newIndex]));
		}
	}
}