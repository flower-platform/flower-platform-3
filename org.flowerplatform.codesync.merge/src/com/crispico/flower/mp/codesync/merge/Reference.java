/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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