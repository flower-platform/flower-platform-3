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
package org.flowerplatform.blazeds.custom_serialization;

/**
 * A wrapper class around a <code>TransferableObject</code>. Java references
 * between TransferableObjects are replaced (during serialization towards Flex)
 * with ReferenceHolders.
 * 
 * @author Cristi
 */
public class ReferenceHolder {

	private Object referenceId;

	public Object getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Object referenceId) {
		this.referenceId = referenceId;
	}

	public String toString() {
		return "ReferenceHolder(referenceId: " + referenceId + ")";
	}
	
}