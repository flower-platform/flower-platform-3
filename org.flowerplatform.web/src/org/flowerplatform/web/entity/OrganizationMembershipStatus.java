/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Organization Membership Status</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.web.entity.EntityPackage#getOrganizationMembershipStatus()
 * @model
 * @generated
 */
public enum OrganizationMembershipStatus implements Enumerator {
	/**
	 * The '<em><b>MEMBER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MEMBER_VALUE
	 * @generated
	 * @ordered
	 */
	MEMBER(0, "MEMBER", "MEMBER"),

	/**
	 * The '<em><b>ADMIN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ADMIN_VALUE
	 * @generated
	 * @ordered
	 */
	ADMIN(1, "ADMIN", "ADMIN"),

	/**
	 * The '<em><b>PENDING MEMBERSHIP APPROVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PENDING_MEMBERSHIP_APPROVAL_VALUE
	 * @generated
	 * @ordered
	 */
	PENDING_MEMBERSHIP_APPROVAL(2, "PENDING_MEMBERSHIP_APPROVAL", "PENDING_MEMBERSHIP_APPROVAL");

	/**
	 * The '<em><b>MEMBER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MEMBER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MEMBER
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MEMBER_VALUE = 0;

	/**
	 * The '<em><b>ADMIN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ADMIN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ADMIN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ADMIN_VALUE = 1;

	/**
	 * The '<em><b>PENDING MEMBERSHIP APPROVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PENDING MEMBERSHIP APPROVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PENDING_MEMBERSHIP_APPROVAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PENDING_MEMBERSHIP_APPROVAL_VALUE = 2;

	/**
	 * An array of all the '<em><b>Organization Membership Status</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final OrganizationMembershipStatus[] VALUES_ARRAY =
		new OrganizationMembershipStatus[] {
			MEMBER,
			ADMIN,
			PENDING_MEMBERSHIP_APPROVAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Organization Membership Status</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<OrganizationMembershipStatus> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Organization Membership Status</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OrganizationMembershipStatus get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OrganizationMembershipStatus result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Organization Membership Status</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OrganizationMembershipStatus getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OrganizationMembershipStatus result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Organization Membership Status</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OrganizationMembershipStatus get(int value) {
		switch (value) {
			case MEMBER_VALUE: return MEMBER;
			case ADMIN_VALUE: return ADMIN;
			case PENDING_MEMBERSHIP_APPROVAL_VALUE: return PENDING_MEMBERSHIP_APPROVAL;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private OrganizationMembershipStatus(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //OrganizationMembershipStatus
