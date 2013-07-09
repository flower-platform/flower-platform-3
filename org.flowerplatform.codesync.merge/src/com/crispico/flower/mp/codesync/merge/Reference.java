package com.crispico.flower.mp.codesync.merge;

import org.eclipse.emf.ecore.EObject;

/**
 * @flowerModelElementId _E6kicC4UEeCzKcFA-Du_zw
 */
public class Reference {

	protected EObject referencedObject;
	
	public Reference(EObject referencedObject) {
		super();
		this.referencedObject = referencedObject;
	}

	public String getXmiId() {
		return referencedObject != null ? referencedObject.eResource().getURIFragment(referencedObject) : null;
	}
		
	public EObject getReferencedObject() {
		return referencedObject;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) || (obj instanceof Reference && ModelMerge.safeEquals(((Reference) obj).getXmiId(), getXmiId()));
	}

	@Override
	public String toString() {
		return String.format("Ref: %s with xmi:id %s", ModelMerge.getFullyQualifiedName(referencedObject), getXmiId());
	}
	
}
