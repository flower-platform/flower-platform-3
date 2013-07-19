package org.flowerplatform.editor.model.java;

import org.eclipse.emf.ecore.EObject;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public class JavaClassTitleProcessor extends JavaClassChildProcessor {

	@Override
	protected String getLabel(EObject object) {
		return (String) CodeSyncPlugin.getInstance().getFeatureValue((CodeSyncElement) object, 
				CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
	}

	@Override
	protected String getImageForVisibility(int type) {
		return "images/obj16/SyncClass.gif";
	}

}
