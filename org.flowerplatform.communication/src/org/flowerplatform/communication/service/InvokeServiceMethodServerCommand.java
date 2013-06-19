package org.flowerplatform.communication.service;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.callback.InvokeCallbackClientCommand;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.command.CompoundClientCommand;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class encapsulates the service method invocation logic.
 * 
 * <p>
 * The corresponding service is found by the ID in the registry ({@link ServiceRegistry}).
 * The corresponding method is invoked by reflection.
 * 
 * <p>
 * If {@link #getCallbackId()} <code> == 0</code>, that means that the Flex client
 * won't be called back (i.e. it has is no callback registered/pending for answer).
 * 
 * <p>
 * If the service method has the first parameter of type {@link ServiceInvocationContext},
 * it will receive an object of this type during the call followed by the rest of the parameters.
 * 
 * <p>
 * If there is an exception (service/method not found or the invoked method threw an exception),
 * it is logged and a message is sent to the client (using {@link DisplaySimpleMessageClientCommand}).
 * In this case, if the client was waiting for a callback, it will receive a command that will
 * dispose that callback from the memory (but the handler won't be called).
 * 
 * <p>
 * <strong>IMPORTANT NOTE:</strong> The service mechanism is a replacement/short hand of the 
 * original command system. The advantage is that there is less code to write (e.g. 3 methods
 * instead of 3 + 3 classes). The disadvantage is that there is no type checking (on the Flex side).
 * So <strong>please pay special attention when the signature of a service method changes</strong>,
 * because you won't get any compiler error. Look carefully in the whole workspace (using CTRL + H
 * and the name of the service or method), in order to modify ALL places that invokes that particular
 * method.
 * 
 * @see ServiceRegistry
 * 
 * @author Cristi
 * @flowerModelElementId _2YoDcFZiEeGL3vi-zPhopA
 */
public class InvokeServiceMethodServerCommand extends AbstractServerCommand {

	private static Logger logger = LoggerFactory.getLogger(InvokeServiceMethodServerCommand.class);
	
	/**
	 * @see Getter doc.
	 * 
	 * @flowerModelElementId _8lJQEFZiEeGL3vi-zPhopA
	 */
	private String serviceId;

	/**
	 * @see Getter doc.
	 * 
	 * @flowerModelElementId _uigegFbgEeGL3vi-zPhopA
	 */
	private String methodName;
	
	/**
	 * @see Getter doc.
	 * 
	 * @flowerModelElementId _-u_RcFZiEeGL3vi-zPhopA
	 */
	private List<Object> parameters;

	/**
	 * @see Getter doc.
	 * 
	 * @flowerModelElementId _uikv8FbgEeGL3vi-zPhopA
	 */
	private long callbackId;
	
	private long exceptionCallbackId;
	
	/**
	 * The string service id of the service, as registered in {@link ServiceRegistry}.
	 * 
	 * @flowerModelElementId _HAaVEFZjEeGL3vi-zPhopA
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @flowerModelElementId _HAjfBVZjEeGL3vi-zPhopA
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * The name of the method to invoke.
	 * 
	 * @flowerModelElementId _uiq2kFbgEeGL3vi-zPhopA
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @flowerModelElementId _uiug8FbgEeGL3vi-zPhopA
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * The list of parameters, sent from the Flex client.
	 * 
	 * @flowerModelElementId _HAtQCVZjEeGL3vi-zPhopA
	 */
	public List<Object> getParameters() {
		return parameters;
	}

	/**
	 * @flowerModelElementId _HBAK91ZjEeGL3vi-zPhopA
	 */
	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * The id of the callback. May be 0, meaning that 
	 * the client shouldn't be called back with a result.
	 * 
	 * @flowerModelElementId _ui11sFbgEeGL3vi-zPhopA
	 */
	public long getCallbackId() {
		return callbackId;
	}

	/**
	 * @flowerModelElementId _ui78UlbgEeGL3vi-zPhopA
	 */
	public void setCallbackId(long callbackId) {
		this.callbackId = callbackId;
	}

	public long getExceptionCallbackId() {
		return exceptionCallbackId;
	}

	public void setExceptionCallbackId(long exceptionCallbackId) {
		this.exceptionCallbackId = exceptionCallbackId;
	}

	/**
	 * @see Class documentation.
	 * 
	 * @flowerModelElementId _VvEh4FZjEeGL3vi-zPhopA
	 */
	public void executeCommand() {
		Object result = null;
		AbstractClientCommand commandToSendBecauseException = null;
		try {
			// find the service
			Object service = CommunicationPlugin.getInstance().getServiceRegistry().getService(getServiceId());
			if (service == null)
				throw new RuntimeException(String.format("The service with id='%s' was not found in the service registry.", getServiceId()));
			
			// find the method
			Method foundMethod = null;
			for (Method method : service.getClass().getMethods())
				if (method.getName().equals(getMethodName())) {
					foundMethod = method;
					break;
				}
			if (foundMethod == null)
				throw new RuntimeException(String.format("The service with id='%s' doesn't contain the method '%s'", getServiceId(), getMethodName()));
			
			// check to see if the first parameter is a ServiceInvocationContext
			Type[] parameterTypes = foundMethod.getGenericParameterTypes();
			if (parameterTypes.length > 0 && 
					parameterTypes[0] instanceof Class<?> && 
					ServiceInvocationContext.class.isAssignableFrom((Class<?>) parameterTypes[0])) {
				// in this case, we inject a ServiceInvocationContext
				ServiceInvocationContext context = createServiceInvocationContext(getCommunicationChannel(), this);
				if (getParameters() == null)
					setParameters(new ArrayList<Object>());
				if (!(getParameters().size() > 1 && getParameters().get(0) instanceof ServiceInvocationContext)) {
					getParameters().add(0, context);
				}
			}
			
			// invoke the method
			result = foundMethod.invoke(service, getParameters() != null ? getParameters().toArray() : null);
		} catch (Throwable e) {
			logger.error(String.format("Exception caught while invoking service %s, method %s", getServiceId(), getMethodName()), e);
			if (e instanceof InvocationTargetException && e.getCause() != null)
				e = e.getCause();
			commandToSendBecauseException = new DisplaySimpleMessageClientCommand(
					"Service method invocation error", 
					String.format("Invocation of the service '%s', method '%s' failed, throwing exception:\n '%s'\n Please see the error log for additional information and stacktrace.",
							getServiceId(), getMethodName(), e.toString()),
					DisplaySimpleMessageClientCommand.ICON_ERROR);
		}

		AbstractClientCommand commandToSend = null;
		
		if (commandToSendBecauseException != null) {
			if (getCallbackId() == 0 && getExceptionCallbackId() == 0) {
				// no callback expected; we send only the message
				commandToSend = commandToSendBecauseException;
			} else {
				// a callback was expected
				CompoundClientCommand compoundCommand = new CompoundClientCommand().appendCommand(commandToSendBecauseException);
				commandToSend = compoundCommand;
				if (getCallbackId() != 0) {
					compoundCommand.appendCommand(new InvokeCallbackClientCommand(getCallbackId(), null, true));
				}
				if (getExceptionCallbackId() != 0) {
					// we don't send the error because a quick look showed that it's not serialized correctly
					// e.g. no name, cause, etc => useless on client
					compoundCommand.appendCommand(new InvokeCallbackClientCommand(getExceptionCallbackId(), null));
				}
			}
		} else {
			if (getCallbackId() != 0) {
				// normal execution and callback expected
				commandToSend = new InvokeCallbackClientCommand(getCallbackId(), result);
			}
			if (getExceptionCallbackId() != 0) {
				AbstractClientCommand removeExceptionCallbackCommand = new InvokeCallbackClientCommand(getExceptionCallbackId(), null, true);
				if (commandToSend == null) {
					commandToSend = removeExceptionCallbackCommand;
				} else {
					commandToSend = new CompoundClientCommand().
							appendCommand(commandToSend).
							appendCommand(removeExceptionCallbackCommand);
				}
			}
		}
		
		
		if (commandToSend != null) {
			// send back the result 
			getCommunicationChannel().appendCommandToCurrentHttpResponse(commandToSend);
		}

	}
	
	protected ServiceInvocationContext createServiceInvocationContext(CommunicationChannel communicationChannel, InvokeServiceMethodServerCommand command) {
		return new ServiceInvocationContext(communicationChannel, command);
	}
	
	@Override
	public String toString() {
		return "InvokeServiceMethodServerCommand with service=" + serviceId + " and method=" + methodName;
	}
}