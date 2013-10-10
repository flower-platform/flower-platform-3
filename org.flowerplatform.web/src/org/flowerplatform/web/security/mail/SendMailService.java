/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.security.mail;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.FlowerProperties.AddProperty;
import org.flowerplatform.communication.CommunicationPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Mariana
 */
public class SendMailService {
	
	public static final String SERVICE_ID = "sendMailService";
	
	private static boolean initialized = false;
	
	private static final Logger logger = LoggerFactory.getLogger(SendMailService.class);
	
	// Server properties
	
	private final String SERVER_URL = "server.url";
	private final String SERVER_URL_GLOBAL = "server.url.global";
	private final String SERVER_URL_FOR_ORGANIZATION = "server.url.for-organization";
	
	// Mail server properties
	
	private final String HOST = "mail.smtp.host";
	private final String PORT = "mail.smtp.port";
	private final String USER = "mail.smtp.user";
	private final String PASSWORD = "mail.smtp.password";
	private final String SECURITY = "mail.smtp.security";
	
	private String host;
	private String user;
	private String password;
	
	private Properties properties = new Properties();
	
	public static SendMailService getInstance() {
		return (SendMailService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
	
	/**
	 * Validate properties.
	 */
	public void initializeProperties() {
		initialized = true;
		
		// Initialize server settings; these are used to get the server address that will be sent to the users by mail
		CommonPlugin.getInstance().getFlowerProperties().addProperty(getDefaultAddProperty(SERVER_URL, "").setInputFromFileMandatory(true));
		CommonPlugin.getInstance().getFlowerProperties().addProperty(getDefaultAddProperty(SERVER_URL_GLOBAL, "{0}/{1}"));
		CommonPlugin.getInstance().getFlowerProperties().addProperty(getDefaultAddProperty(SERVER_URL_FOR_ORGANIZATION, "{0}/{1}/org/{2}"));
		
		// Initialize mail server settings
		CommonPlugin.getInstance().getFlowerProperties().addProperty(getDefaultAddProperty(HOST, "").setInputFromFileMandatory(true));
		CommonPlugin.getInstance().getFlowerProperties().addProperty(getDefaultAddProperty(PORT, "Default"));
		CommonPlugin.getInstance().getFlowerProperties().addProperty(getDefaultAddProperty(USER, "").setInputFromFileMandatory(true));
		CommonPlugin.getInstance().getFlowerProperties().addProperty(getDefaultAddProperty(PASSWORD, "").setInputFromFileMandatory(true));
		CommonPlugin.getInstance().getFlowerProperties().addProperty(getDefaultAddProperty(SECURITY, "None"));
		
		host = CommonPlugin.getInstance().getFlowerProperties().getProperty(HOST);
		String port = CommonPlugin.getInstance().getFlowerProperties().getProperty(PORT);
		user = CommonPlugin.getInstance().getFlowerProperties().getProperty(USER);
		password = CommonPlugin.getInstance().getFlowerProperties().getProperty(PASSWORD);
		String security = CommonPlugin.getInstance().getFlowerProperties().getProperty(SECURITY);
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", user);
		properties.put("mail.smtp.password", password);
		
		if (!port.equals("Default")) {
			properties.put("mail.smtp.port", port);
		}

		if (security.equals("SSL/TLS")) {
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		} else {
			if (security.equals("STARTTLS")) {
				properties.put("mail.smtp.starttls.enable", "true");
			}
		}
	}
	
	private AddProperty getDefaultAddProperty(String propertyName, String defaultValue) {
		return new AddProperty(propertyName, defaultValue) {
			@Override
			protected String validateProperty(String input) {
				return null;
			}
		};
	}
	
	public void send(String to, String subject, String content) {
		if (initialized) {
			send(Collections.singletonList(to), subject, content);
		}
	}
	
	/**
	 * Send in a parallel job to ensure that the users administration logic
	 * will not be blocked.
	 */
	private void send(final List<String> to, final String subject, final String content) {
		Job job = new Job("Send mail") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Session session = Session.getDefaultInstance(properties);
				Message message = new MimeMessage(session);
		 
				InternetAddress fromAddress = null;
				List<Address> toAddresses = new ArrayList<Address>();
				try {
					fromAddress = new InternetAddress(user);
					for (String add : to) {
						toAddresses.add(new InternetAddress(add));
					}
				} catch (AddressException e) {
					logger.error(e.getMessage());
					return Status.CANCEL_STATUS;
				}
		 
				try {
					message.setFrom(fromAddress);
					message.setRecipients(RecipientType.TO, toAddresses.toArray(new Address[0]));
					message.setSubject(subject);
					message.setContent(content, "text/html");
		 
					Transport transport = session.getTransport("smtp");
					transport.connect(host, user, password);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();
					
					if (logger.isDebugEnabled()) {
						String[] params = {to.toString(), subject, content};
						logger.debug("Send mail to {} with subject = {} and content = {}", params);
					}
					
					return Status.OK_STATUS;
				} catch (MessagingException e) {
					logger.error(e.getMessage());
					return Status.CANCEL_STATUS;
				}
			}
		};
		
		job.schedule();
	}
	
	/**
	 * Returns <code>true</code> if email is valid, <code>false</code> otherwise.
	 */
	public boolean validate(String email) {
		try {
			InternetAddress address = new InternetAddress(email);
			address.validate();
			return true;
		} catch (AddressException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	public String getServerUrl() {
		if (!initialized)
			return null;
		return MessageFormat.format(
				CommonPlugin.getInstance().getFlowerProperties().getProperty(SERVER_URL_GLOBAL), 
				CommonPlugin.getInstance().getFlowerProperties().getProperty(SERVER_URL),
				FrameworkProperties.getProperty("flower.server.app.context"));
	}
	
	public String getServerUrlForOrganization(String organizationUrl) {
		if (!initialized)
			return null;
		return MessageFormat.format(
				CommonPlugin.getInstance().getFlowerProperties().getProperty(SERVER_URL_FOR_ORGANIZATION), 
				CommonPlugin.getInstance().getFlowerProperties().getProperty(SERVER_URL),
				FrameworkProperties.getProperty("flower.server.app.context"),
				organizationUrl);
	}
}