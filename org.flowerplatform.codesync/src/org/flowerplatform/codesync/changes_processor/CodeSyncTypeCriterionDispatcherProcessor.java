package org.flowerplatform.codesync.changes_processor;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.editor.model.changes_processor.AbstractCriterionDispatcherProcessor;
import org.flowerplatform.editor.model.changes_processor.Changes;
import org.flowerplatform.editor.model.changes_processor.IChangesProcessor;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristian Spiescu
 */
public class CodeSyncTypeCriterionDispatcherProcessor extends AbstractCriterionDispatcherProcessor<String> {

	@Override
	protected List<IChangesProcessor> getProcessorsThatMeetCriterion(Map<String, Object> context, EObject object, Changes changes) {
		if (object instanceof CodeSyncElement) {
			return processors.get(((CodeSyncElement) object).getType());
		} else if (object instanceof Relation) {
			return processors.get(((Relation) object).getType());
		} else {
			return null;
		}
	}

}
