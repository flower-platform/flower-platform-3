package org.flowerplatform.codesync.code.javascript.config.changes_processor;

import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.config.extension.FeatureAccessExtension;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristian Spiescu
 */
public class InheritanceProcessor extends AttributeWithRequireEntryDependencyProcessor {

	private final static Logger logger = LoggerFactory.getLogger(AttributeWithRequireEntryDependencyProcessor.class);

	public InheritanceProcessor() {
		super(null);
	}

	@Override
	protected void updateSource(Relation relation, CodeSyncElement sourceClass, CodeSyncElement targetClass) {
		// set the attribute value
		String className = (String) CodeSyncOperationsService.getInstance().getFeatureValue(targetClass, FeatureAccessExtension.CODE_SYNC_NAME);
		if (logger.isDebugEnabled()) {
			logger.debug("For class = {}, setting superClass = {}", sourceClass, className);
		}
		CodeSyncOperationsService.getInstance().setFeatureValue(sourceClass, JavaScriptDescriptors.FEATURE_SUPER_CLASS, className);
		addRequireEntryIfNeeded(sourceClass, targetClass, className);
	}

}
