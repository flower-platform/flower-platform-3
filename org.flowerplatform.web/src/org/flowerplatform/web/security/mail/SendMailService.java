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
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.web.FlowerWebProperties.AddProperty;
import org.flowerplatform.web.WebPlugin;
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
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(getDefaultAddProperty(SERVER_URL, "").setInputFromFileMandatory(true));
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(getDefaultAddProperty(SERVER_URL_GLOBAL, "{0}/{1}"));
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(getDefaultAddProperty(SERVER_URL_FOR_ORGANIZATION, "{0}/{1}/org/{2}"));
		
		// Initialize mail server settings
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(getDefaultAddProperty(HOST, "").setInputFromFileMandatory(true));
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(getDefaultAddProperty(PORT, "Default"));
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(getDefaultAddProperty(USER, "").setInputFromFileMandatory(true));
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(getDefaultAddProperty(PASSWORD, "").setInputFromFileMandatory(true));
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(getDefaultAddProperty(SECURITY, "None"));
		
		host = WebPlugin.getInstance().getFlowerWebProperties().getProperty(HOST);
		String port = WebPlugin.getInstance().getFlowerWebProperties().getProperty(PORT);
		user = WebPlugin.getInstance().getFlowerWebProperties().getProperty(USER);
		password = WebPlugin.getInstance().getFlowerWebProperties().getProperty(PASSWORD);
		String security = WebPlugin.getInstance().getFlowerWebProperties().getProperty(SECURITY);
		
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
				WebPlugin.getInstance().getFlowerWebProperties().getProperty(SERVER_URL_GLOBAL), 
				WebPlugin.getInstance().getFlowerWebProperties().getProperty(SERVER_URL),
				FrameworkProperties.getProperty("flower.server.app.context"));
	}
	
	public String getServerUrlForOrganization(String organizationUrl) {
		if (!initialized)
			return null;
		return MessageFormat.format(
				WebPlugin.getInstance().getFlowerWebProperties().getProperty(SERVER_URL_FOR_ORGANIZATION), 
				WebPlugin.getInstance().getFlowerWebProperties().getProperty(SERVER_URL),
				FrameworkProperties.getProperty("flower.server.app.context"),
				organizationUrl);
	}
}
