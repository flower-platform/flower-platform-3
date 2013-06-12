package org.flowerplatform.communication.stateful_service;

import java.util.Map;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 * 
 * @param <DT>
 * @flowerModelElementId _6MHx4AlZEeKViIirpKcsaw
 */
public abstract class RegularStatefulService<CT extends CommunicationChannel, DT> extends StatefulService {
	
	protected Logger logger;
	
	protected Map<CT, DT> clients;
	
	public RegularStatefulService() {
		super();
		logger = LoggerFactory.getLogger(this.getClass());
	}

	///////////////////////////////////////////////////////////////
	// JMX Methods
	///////////////////////////////////////////////////////////////	
	
	public String printStatefulDataPerCommunicationChannel(String communicationChannelIdFilter, String linePrefix) {
		// clean parameters
		if ("".equals(communicationChannelIdFilter) || "String".equals(communicationChannelIdFilter)) {
			communicationChannelIdFilter = null;
		}
		if ("String".equals(linePrefix)) { 
			linePrefix = "";
		}
				
		StringBuffer sb = new StringBuffer();
		
		for (Map.Entry<CT, DT> entry : clients.entrySet()) {
			if (communicationChannelIdFilter == null || communicationChannelIdFilter.equals(entry.getKey().getId())) {
				printStatefulDataForClient(sb, linePrefix, entry.getKey(), entry.getValue());
			}
		}
		return sb.toString();
	}
	
	protected void printStatefulDataForClient(StringBuffer stringBuffer, String linePrefix, CT client, DT data) {
		stringBuffer.append(linePrefix).append(client).append("\n");		
	}

	///////////////////////////////////////////////////////////////
	// Normal methods
	///////////////////////////////////////////////////////////////
	
	protected DT getDataFromStatefulClientLocalState(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		return null;
	}
	
	protected abstract String getStatefulServiceId();

	///////////////////////////////////////////////////////////////
	//@RemoteInvocation methods
	///////////////////////////////////////////////////////////////

	@RemoteInvocation
	@SuppressWarnings("unchecked")
	public void subscribe(StatefulServiceInvocationContext context,	IStatefulClientLocalState statefulClientLocalState) {
		DT dt = getDataFromStatefulClientLocalState(context, statefulClientLocalState);
		logger.info("Subscribing to {} with client data {}", getStatefulServiceId(), dt);
		clients.put((CT) context.getCommunicationChannel(), dt);
	}

	@RemoteInvocation
	public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		DT dt = getDataFromStatefulClientLocalState(context, statefulClientLocalState);
		if (clients.containsKey(context.getCommunicationChannel())) {
			// we check this, because we don't want to pollute the log and
			// actually this is not correct: unsubscribe makes sense only after a subscribe; someone
			// reading the log would be misleaded, thinking that the element was subscribed
			logger.info("Unsubscribing from {} with client data {}", getStatefulServiceId(), dt);
			clients.remove(context.getCommunicationChannel());
		}
	}
	
}
