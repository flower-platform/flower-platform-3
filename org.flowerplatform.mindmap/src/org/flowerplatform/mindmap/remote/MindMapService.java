package org.flowerplatform.mindmap.remote;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.mindmap.MindMapNodeDAO;

public class MindMapService {
	
	private MindMapNodeDAO dao = new MindMapNodeDAO();
	
	@RemoteInvocation
	public List<Object> getChildrenForNodeId(String nodeId) {
		return dao.getChildren(nodeId);		
	}
	
	@RemoteInvocation
	public void reload() {
		try {
			dao.load(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RemoteInvocation
	public List<Object> refresh(String nodeId) {
		return Arrays.asList(nodeId, dao.getNode(nodeId));
	}
	
	@RemoteInvocation
	public void setBody(String nodeId, String newBodyValue) {
		dao.setBody(nodeId, newBodyValue);
	}
	
	@RemoteInvocation
	public Node addNode(String parentNodeId, String type) {
		return dao.addNode(parentNodeId, type);
	}
	
	@RemoteInvocation
	public void removeNode(String nodeId) {		
		dao.removeNode(nodeId);
	}	
	
	@RemoteInvocation
	public void moveNode(String nodeId, String newParentId, int newIndex) {
		dao.moveNode(nodeId, newParentId, newIndex);
	}
	
	@RemoteInvocation
	public void save() {		
		dao.save();
	}	
	
}
