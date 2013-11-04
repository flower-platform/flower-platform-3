package org.flowerplatform.codesync.code.javascript.config.changes_processor;

import java.util.List;

import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.config.extension.NamedElementFeatureAccessExtension;
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
	
	/**
	 * @author Cristina Constantinescu
	 */
	public AttributeWithRequireEntryDependencyProcessor(String prefix,	boolean ignoreTargetNameFromDependencyPath, String[] ignoreTypesFromDependencyPath) {
		super(prefix, ignoreTargetNameFromDependencyPath, ignoreTypesFromDependencyPath);		
	}

	@Override
	protected void updateSource(Relation relation, CodeSyncElement sourceAttribute, CodeSyncElement targetClass) {
		// set the attribute value
		String className = (String) CodeSyncOperationsService.getInstance().getFeatureValue(targetClass, JavaScriptDescriptors.FEATURE_NAME);
		if (logger.isDebugEnabled()) {
			logger.debug("For attribute = {}, setting default value = {}", sourceAttribute, className);
		}
		CodeSyncOperationsService.getInstance().setFeatureValue(sourceAttribute, JavaScriptDescriptors.FEATURE_DEFAULT_VALUE, className);
		CodeSyncElement parentClassOfSourceAttribute = (CodeSyncElement) sourceAttribute.eContainer(); // TODO CS/JS: access parent through CSOS as well; children as well
		addRequireEntryIfNeeded(parentClassOfSourceAttribute, targetClass, className);	
		
		addHtmlIdSuffixValueIfNeeded((CodeSyncElement) sourceAttribute.eContainer(), className);
	}
	
	protected void addRequireEntryIfNeeded(CodeSyncElement hostForRequireEntry, CodeSyncElement targetClass, String className) {
		// look for a require entry; if not found, create one
		boolean foundCorrespondingRequiresEntry = CodeSyncOperationsService.getInstance().hasChildWithKeyFeatureValue(
				hostForRequireEntry, JavaScriptDescriptors.TYPE_REQUIRE_ENTRY, className); 
		if (!foundCorrespondingRequiresEntry) {
			CodeSyncElement requireEntry = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_REQUIRE_ENTRY);
			updateRequireEntry(requireEntry, targetClass);
			CodeSyncOperationsService.getInstance().add(hostForRequireEntry, requireEntry);
		}		
	}	

}
