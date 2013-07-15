package org.flowerplatform.editor.model.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana
 */
public class JavaAttributeProcessor implements IDiagrammableElementFeatureChangesProcessor {

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		
		if (featureChanges == null) {
			// full content
			processFeatureChange(object, null, associatedViewOnOpenDiagram, viewDetails);
		} else {
			// TODO diff
		}
		
		if (!viewDetails.isEmpty()) {
			ViewDetailsUpdate update = new ViewDetailsUpdate();
			update.setViewId(associatedViewOnOpenDiagram.eResource().getURIFragment(associatedViewOnOpenDiagram));
			update.setViewDetails(viewDetails);
			
			DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, true).
				getViewDetailsUpdates().add(update);
		}
	}
	
	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> viewDetails) {
		if (object == null) {
			return;
		}
		if (featureChange == null) {
			// TODO
		}
		
		String visibility = "";
		CodeSyncElement cse = (CodeSyncElement) object;
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) 
				CodeSyncPlugin.getInstance().getFeatureValue(cse, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		if (modifiers != null) {
			for (ExtendedModifier modifier : modifiers) {
				if (modifier instanceof Modifier) {
					switch (((Modifier) modifier).getType()) {
					case 1:
						visibility = "+";
						break;
					case 2:
						visibility = "-";
						break;
					default:
						break;
					}
				}
			}
		}
		
		String name = (String) CodeSyncPlugin.getInstance().getFeatureValue((CodeSyncElement) object, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		String type = (String) CodeSyncPlugin.getInstance().getFeatureValue(cse, AstCacheCodePackage.eINSTANCE.getTypedElement_Type());
		viewDetails.put("label", String.format("%s%s:%s", visibility, name, type));
		
		// TODO test
		viewDetails.put("iconUrls", new String[] {"images/obj16/SyncProperty_protected.gif", "images/ovr16/Synchronized_All_Generated.gif"});
	}

}
