/**
 * 
 */
package org.flowerplatform.communication.stateful_service;

import java.util.Map;

import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;

/**
 * @author Cristi
 */
// TODO CS/JS: we should have this as a map; and put the items in the map; and not have any more SIC and SSIC; and get rid of additional data
public class ServiceInvocationContextUpgradedToStatefulServiceInvocationContext
		extends StatefulServiceInvocationContext {

	protected ServiceInvocationContext delegate;

	public ServiceInvocationContextUpgradedToStatefulServiceInvocationContext(ServiceInvocationContext delegate) {
		super(delegate.getCommunicationChannel());
		this.delegate = delegate;
	}

	@Override
	public String getStatefulClientId() {
		throw new UnsupportedOperationException("This is a plain ServiceInvocationContext, and doesn't know how to perform this operation");
	}

	@Override
	public void setStatefulClientId(String statefulClientId) {
		throw new UnsupportedOperationException("This is a plain ServiceInvocationContext, and doesn't know how to perform this operation");
	}

	@Override
	public InvokeServiceMethodServerCommand getCommand() {
		return delegate.getCommand();
	}

	@Override
	public Map<String, Object> getAdditionalData() {
		return delegate.getAdditionalData();
	}

	@Override
	public void setAdditionalData(Map<String, Object> additionalData) {
		delegate.setAdditionalData(additionalData);
	}
	
	
}
