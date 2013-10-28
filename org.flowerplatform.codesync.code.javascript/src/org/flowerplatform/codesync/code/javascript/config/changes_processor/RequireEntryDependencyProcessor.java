package org.flowerplatform.codesync.code.javascript.config.changes_processor;

import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.code.javascript.config.Utils;
import org.flowerplatform.codesync.config.extension.FeatureAccessExtension;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristian Spiescu
 */
public class RequireEntryDependencyProcessor extends AbstractDependencyProcessor {

	private final static Logger logger = LoggerFactory.getLogger(RequireEntryDependencyProcessor.class);

	protected String prefix;

	public RequireEntryDependencyProcessor(String prefix) {
		super();
		this.prefix = prefix;
	}

	@Override
	protected void updateSource(Relation relation, CodeSyncElement source,
			CodeSyncElement target) {
		updateRequireEntry(source, target);
	}
	
	protected void updateRequireEntry(CodeSyncElement requireEntry, CodeSyncElement targetClass) {
		String className = (String) CodeSyncOperationsService.getInstance().getFeatureValue(targetClass, FeatureAccessExtension.CODE_SYNC_NAME);
		String path = Utils.getQualifiedPath(targetClass);
		if (prefix != null) {
			path = prefix + path;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Updating requireEntry({}, {})", className, path);
		}
		CodeSyncOperationsService.getInstance().setFeatureValue(requireEntry, FeatureAccessExtension.CODE_SYNC_NAME, className);
		CodeSyncOperationsService.getInstance().setFeatureValue(requireEntry, JavaScriptDescriptors.FEATURE_DEPENDENCY_PATH, path);
	}


}
