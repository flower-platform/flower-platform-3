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
