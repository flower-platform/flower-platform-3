package org.flowerplatform.codesync.code.javascript.config.changes_processor;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.code.javascript.config.Utils;
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

	/**
	 * @author Cristina Constantinescu
	 */
	protected List<String> ignoreTypesFromDependencyPath;
	
	/**
	 * @author Cristina Constantinescu
	 */
	protected boolean ignoreTargetNameFromDependencyPath;
	
	
	public RequireEntryDependencyProcessor(String prefix) {
		this(prefix, false, null);
	}

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public RequireEntryDependencyProcessor(String prefix, boolean ignoreTargetNameFromDependencyPath, String[] ignoreTypesFromDependencyPath) {
		super();
		this.prefix = prefix;
		if (ignoreTypesFromDependencyPath != null) {
			this.ignoreTypesFromDependencyPath = Arrays.asList(ignoreTypesFromDependencyPath);
		}
		this.ignoreTargetNameFromDependencyPath = ignoreTargetNameFromDependencyPath;
	}

	@Override
	protected void updateSource(Relation relation, CodeSyncElement source, CodeSyncElement target) {
		updateRequireEntry(source, target);
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	protected void updateRequireEntry(CodeSyncElement requireEntry, CodeSyncElement targetClass) {
		String className = (String) CodeSyncOperationsService.getInstance().getFeatureValue(targetClass, JavaScriptDescriptors.FEATURE_NAME);
		String path = Utils.getQualifiedPath(targetClass, ignoreTargetNameFromDependencyPath, ignoreTypesFromDependencyPath);
		if (prefix != null) {
			path = prefix + path;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Updating requireEntry({}, {})", className, path);
		}
		CodeSyncOperationsService.getInstance().setFeatureValue(requireEntry, JavaScriptDescriptors.FEATURE_VAR_NAME, className);
		CodeSyncOperationsService.getInstance().setFeatureValue(requireEntry, JavaScriptDescriptors.FEATURE_DEPENDENCY_PATH, path);
	}


}
