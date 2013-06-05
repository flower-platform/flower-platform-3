package org.flowerplatform.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.color.HighlightingCompositeConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;

/**
 * Contains simple utility methods used to log audit messages.
 * 
 * <p>
 * The methods that perform the same tasks but have different signatures, 
 * are copy/paste of one another on purpose / for performance reasons. 
 * 
 * @author Cristi
 */
public class LogUtil {
	
	public static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

	public static class FlowerHighlightingCompositeConverter extends HighlightingCompositeConverter {

		@Override
		protected String getForegroundColorCode(ILoggingEvent event) {
			if (event.getLevel().toInt() == Level.TRACE_INT) {
				return ANSIConstants.CYAN_FG;
			} else {
				return super.getForegroundColorCode(event);				
			}
		}
		
	}
	
	public static class FlowerHighlightingCompositeConverterDefaultYellow extends HighlightingCompositeConverter {

		@Override
		protected String getForegroundColorCode(ILoggingEvent event) {
			String value = super.getForegroundColorCode(event);				
			if (ANSIConstants.DEFAULT_FG.equals(value)) {
				return ANSIConstants.YELLOW_FG;
			} else {
				return value;
			}
		}
		
	}	
	
	private static IAuditAppender auditAppender;
	
	public static IAuditAppender getAuditAppender() {
		return auditAppender;
	}

	/**
	 * @author Mariana
	 */
	public static void setAuditAppender(IAuditAppender appender) {
		auditAppender = appender;
	}
	
	/**
	 * Append an audit message, using the {@link #auditAppender}. Also measures the duration since the
	 * moment the <code>auditDetails</code>.
	 * 
	 * @author Mariana
	 */
	public static void audit(AuditDetails auditDetails) {
		if (auditAppender != null) {
			auditDetails.measureDuration();
			auditAppender.append(auditDetails);
		}
	}
}
