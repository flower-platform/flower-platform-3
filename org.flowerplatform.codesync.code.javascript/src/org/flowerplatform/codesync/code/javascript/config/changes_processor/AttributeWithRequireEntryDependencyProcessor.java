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
public class AttributeWithRequireEntryDependencyProcessor extends RequireEntryDependencyProcessor {

	private final static Logger logger = LoggerFactory.getLogger(AttributeWithRequireEntryDependencyProcessor.class);

	public AttributeWithRequireEntryDependencyProcessor(String prefix) {
		super(prefix);
	}

	@Override
	protected void updateSource(Relation relation, CodeSyncElement sourceAttribute, CodeSyncElement targetClass) {
		// set the attribute value
		String className = (String) CodeSyncOperationsService.getInstance().getFeatureValue(targetClass, FeatureAccessExtension.CODE_SYNC_NAME);
		if (logger.isDebugEnabled()) {
			logger.debug("For attribute = {}, setting default value = {}", sourceAttribute, className);
		}
		CodeSyncOperationsService.getInstance().setFeatureValue(sourceAttribute, JavaScriptDescriptors.FEATURE_DEFAULT_VALUE, className);
		
		// look for a require entry; if not found, create one
		boolean foundCorrespondingRequiresEntry = false; 
		CodeSyncElement parentClassOfSourceAttribute = (CodeSyncElement) sourceAttribute.eContainer(); // TODO CS/JS: access parent through CSOS as well; children as well
		for (CodeSyncElement child : parentClassOfSourceAttribute.getChildren()) {
			if (!JavaScriptDescriptors.TYPE_REQUIRE_ENTRY.equals(child.getType())) {
				continue;
			}
			if (className.equals(CodeSyncOperationsService.getInstance().getFeatureValue(child, FeatureAccessExtension.CODE_SYNC_NAME))) {
				foundCorrespondingRequiresEntry = true;
				break;
			}
		}
		if (!foundCorrespondingRequiresEntry) {
			CodeSyncElement requireEntry = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_REQUIRE_ENTRY);
			updateRequireEntry(requireEntry, targetClass);
			CodeSyncOperationsService.getInstance().add(parentClassOfSourceAttribute, requireEntry);
		}
	}

}
