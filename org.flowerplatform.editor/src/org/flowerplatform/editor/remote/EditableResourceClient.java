package org.flowerplatform.editor.remote;

import org.flowerplatform.communication.channel.CommunicationChannel;

/**
 * Represents a client that is connected to an {@link EditableResource}.
 * 
 * <p>
 * Besides {@link #communicationChannel}, all other fields exist in the
 * corresponding AS class.
 * 
 * @see EditableResource#getClients()
 * @author Cristi
 * @flowerModelElementId _UUK4YKW7EeGAT8h2VXeJdg
 */
public class EditableResourceClient {
	
//	private String cachedClientId;
//	
//	private String cachedLogin;
//	
//	private String cachedName;
	
	/**
	 * @flowerModelElementId _SNPLoKW8EeGAT8h2VXeJdg
	 */
	private CommunicationChannel communicationChannel;
	
	private String statefulClientId;
	
	public EditableResourceClient(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
		
//		cachedClientId = communicationChannel.getClientId();
//		
//		IPrincipal principal = communicationChannel.getPrincipal();
//		if (principal != null) {
//			IUser user = communicationChannel.getPrincipal().getUser();
//			cachedLogin = user.getLogin();
//			cachedName = user.getName();
//		}
	}
	
	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}

	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
	}
	
	/**
	 * @flowerModelElementId _O0sFULV8EeGL6Md0-RjoBw
	 */
	public String getCommunicationChannelId() {
		return (String) communicationChannel.getId();
	}
	
//	/**
//	 * @flowerModelElementId _O0sFUrV8EeGL6Md0-RjoBw
//	 */
//	public String getLogin() {
//		return cachedLogin;
//	}
//	
//	/**
//	 * @flowerModelElementId _O0ssYbV8EeGL6Md0-RjoBw
//	 */
//	public String getName() {
//		return cachedName;
//	}

	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}

	@Override
	public String toString() {
		return String.format("EdResCl[%s,stClId=%s]", getCommunicationChannel(), getStatefulClientId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EditableResourceClient) {
			return communicationChannel.equals(((EditableResourceClient) obj).getCommunicationChannel());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return communicationChannel.hashCode();
	}
}