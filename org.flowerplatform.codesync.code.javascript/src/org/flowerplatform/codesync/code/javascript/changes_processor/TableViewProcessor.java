package org.flowerplatform.codesync.code.javascript.changes_processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.operation_extension.FeatureAccessExtension;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.editor.model.changes_processor.Changes;
import org.flowerplatform.editor.model.changes_processor.IChangesProcessor;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

public class TableViewProcessor implements IChangesProcessor {

	@Override
	public void processChanges(Map<String, Object> context, EObject object, Changes changes) {
		if (changes.getAddedToContainer() == null) {
			return;
		}
		CodeSyncElement tableView = (CodeSyncElement) object;
		CodeSyncElement attribute;
		
		attribute = CodeSyncOperationsService.getInstance().create("javaScriptAttribute");
		CodeSyncOperationsService.getInstance().setFeatureValue(attribute, FeatureAccessExtension.CODE_SYNC_NAME, "tpl");
		CodeSyncOperationsService.getInstance().add(tableView, attribute);		
	}

}
