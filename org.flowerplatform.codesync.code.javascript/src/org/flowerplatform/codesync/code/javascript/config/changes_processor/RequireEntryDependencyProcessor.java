package org.flowerplatform.codesync.code.javascript.config.changes_processor;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.code.javascript.config.Utils;
import org.flowerplatform.codesync.config.extension.NamedElementFeatureAccessExtension;
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
		
		String className = (String) CodeSyncOperationsService.getInstance().getFeatureValue(target, JavaScriptDescriptors.FEATURE_NAME);		
		addHtmlIdSuffixValueIfNeeded((CodeSyncElement) source.eContainer(), className);
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

	/**
	 * @author Cristina Constantinescu
	 */
	protected void addHtmlIdSuffixValueIfNeeded(CodeSyncElement hostForRequireEntry, String name) {	
		if (hostForRequireEntry == null) {
			return;
		}
		// get last upper case index (name="CompanyForm")
		int lastUpperCaseIndex = 0;
		char[] chars = name.toCharArray();
		for (int i = 0; i < chars.length; i++) {
		    if (Character.isUpperCase(chars[i])) {
		    	lastUpperCaseIndex = i;
		    }
		}
		
		// find child with name "htmlIdSuffix" and set its default value (value="company")
		List<CodeSyncElement> children = hostForRequireEntry.getChildren();
		for (CodeSyncElement child : children) {
			if ("htmlIdSuffix".equals(CodeSyncOperationsService.getInstance().getFeatureValue(child, NamedElementFeatureAccessExtension.NAME))) {
				CodeSyncOperationsService.getInstance().setFeatureValue(child, "defaultValue", "'" + name.substring(0, lastUpperCaseIndex).toLowerCase() + "'");
				break;
			}
		}
	}
	
}
