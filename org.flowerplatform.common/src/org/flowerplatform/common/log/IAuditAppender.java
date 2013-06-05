package org.flowerplatform.common.log;

/**
 * Used to log audit messages.
 * 
 * @see LogUtil#audit()
 * 
 * @author Mariana
 */
public interface IAuditAppender {

	public void append(AuditDetails auditDetails);
	
}
