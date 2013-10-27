package org.flowerplatform.codesync.code.javascript.config.changes_processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.changes_processor.Changes;
import org.flowerplatform.editor.model.changes_processor.IChangesProcessor;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristian Spiescu
 */
public abstract class AbstractDependencyProcessor implements IChangesProcessor {
	
	@Override
	public void processChanges(Map<String, Object> context, EObject object, Changes changes) {
		if (changes.getRemovedFromContainer() != null) {
			// relation deleted; don't do anything
			return;
		}
		
		for (FeatureChange featureChange : changes.getFeatureChanges()) {
			if (CodeSyncPackage.eINSTANCE.getRelation_Target().equals(featureChange.getFeature())) {
				// either freshly added, or target changed
				Relation relation = (Relation) object;
				// TODO CS/JS: on create and change relation end: we need to verify that the source/targets
				// are cf. descriptors; maybe have extensions that do additional checks (and validation messages?)

				updateSource(relation, relation.getSource(), relation.getTarget());
			}
		}
	}
	
	protected abstract void updateSource(Relation relation, CodeSyncElement source, CodeSyncElement target);

}
