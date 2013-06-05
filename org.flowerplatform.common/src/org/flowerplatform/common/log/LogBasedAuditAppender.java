package org.flowerplatform.common.log;

import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * @author Cristi
 */
public class LogBasedAuditAppender implements IAuditAppender {

	private static Marker auditMarker = MarkerFactory.getMarker("AUDIT");
	
	protected void addEntriesInMDC(AuditDetails auditDetails) {
		if (auditDetails.getParam0() != null) {
			MDC.put("param0", auditDetails.getParam0().toString());
		}
		if (auditDetails.getParam1() != null) {
			MDC.put("param1", auditDetails.getParam1().toString());
		}
		if (auditDetails.getParam2() != null) {
			MDC.put("param2", auditDetails.getParam2().toString());
		}
	}
	
	protected void removeEntriesFromMDC(AuditDetails auditDetails) {
		if (auditDetails.getParam0() != null) {
			MDC.remove("param0");
		}
		if (auditDetails.getParam1() != null) {
			MDC.remove("param1");
		}
		if (auditDetails.getParam2() != null) {
			MDC.remove("param2");
		}
	}
	
	public void append(AuditDetails auditDetails) {
		if (auditDetails.getLogger().isInfoEnabled()) {
			addEntriesInMDC(auditDetails);
			auditDetails.getLogger().info(auditMarker, auditDetails.getAuditCategory());
			removeEntriesFromMDC(auditDetails);
		}		
	}

}
