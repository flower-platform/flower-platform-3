package org.flowerplatform.common.log;

/**
 * @author Cristi
 */
public class DualDelegatingAuditAppender implements IAuditAppender {

	protected IAuditAppender appender1;
	
	protected IAuditAppender appender2;
	
	public DualDelegatingAuditAppender(IAuditAppender appender1,
			IAuditAppender appender2) {
		super();
		this.appender1 = appender1;
		this.appender2 = appender2;
	}

	public void append(AuditDetails auditDetails) {
		if (appender1 != null) {
			appender1.append(auditDetails);
		}
		if (appender2 != null) {
			appender2.append(auditDetails);
		}
	}

}
