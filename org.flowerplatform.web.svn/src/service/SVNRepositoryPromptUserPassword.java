package service;

import java.util.HashMap;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.svn.remote.OpenSvnCredentialsWindowClientCommand;
import org.flowerplatform.web.svn.remote.SvnService;
import org.tigris.subversion.svnclientadapter.ISVNPromptUserPassword;

public class SVNRepositoryPromptUserPassword implements ISVNPromptUserPassword{

	private String username;
	private String password;
	private boolean allowedSave;
	private boolean rtnCode;
	
	@Override
	public String askQuestion(String arg0, String arg1, boolean arg2,
			boolean arg3) {
		return null;
	}

	@Override
	public int askTrustSSLServer(String arg0, boolean arg1) {
		return ISVNPromptUserPassword.AcceptPermanently;
	}

	@Override
	public boolean askYesNo(String arg0, String arg1, boolean arg2) {
		return false;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public int getSSHPort() {
		return 0;
	}

	@Override
	public String getSSHPrivateKeyPassphrase() {
		return null;
	}

	@Override
	public String getSSHPrivateKeyPath() {
		return null;
	}

	@Override
	public String getSSLClientCertPassword() {
		return null;
	}

	@Override
	public String getSSLClientCertPath() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean prompt(String promptRealm, String promptUsername, boolean promptMaySave) {
		allowedSave = false;
		rtnCode = false;	
		
		CommunicationChannel cc = (CommunicationChannel)CommunicationPlugin.tlCurrentChannel.get();
		ServiceInvocationContext context = new ServiceInvocationContext(cc);
		InvokeServiceMethodServerCommand command = SvnService.tlCommand.get();
		
		FlowerWebPrincipal principal = (FlowerWebPrincipal) cc.getPrincipal();
		if (principal == null) {
			return rtnCode;
		}
		if (principal.getUserSvnRepositories().get(promptRealm) == null || 
			principal.getUserSvnRepositories().get(promptRealm).get(0) == null ||  // user
			promptUsername == null) {
				context.getCommunicationChannel().appendOrSendCommand(
					new OpenSvnCredentialsWindowClientCommand(promptRealm, promptUsername, command));
			
		} else {
			username = principal.getUserSvnRepositories().get(promptRealm).get(0);
			password = principal.getUserSvnRepositories().get(promptRealm).get(1);
			rtnCode = true;	
			
		}
		return rtnCode;
	}

	@Override
	public boolean promptSSH(String arg0, String arg1, int arg2, boolean arg3) {
		return false;
	}

	@Override
	public boolean promptSSL(String arg0, boolean arg1) {
		return false;
	}

	@Override
	public boolean promptUser(String arg0, String arg1, boolean arg2) {
		return false;
	}

	@Override
	public boolean userAllowedSave() {
		return allowedSave;
	}
}
