/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SVN Repository URL Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.SVNRepositoryURLEntity#getOrganization <em>Organization</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getSVNRepositoryURLEntity()
 * @model
 * @generated
 */
public interface SVNRepositoryURLEntity extends NamedEntity {
	/**
	 * Returns the value of the '<em><b>Organization</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.Organization#getSvnRepositoryURLs <em>Svn Repository UR Ls</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Organization</em>' reference.
	 * @see #setOrganization(Organization)
	 * @see org.flowerplatform.web.entity.EntityPackage#getSVNRepositoryURLEntity_Organization()
	 * @see org.flowerplatform.web.entity.Organization#getSvnRepositoryURLs
	 * @model opposite="svnRepositoryURLs"
	 * @generated
	 */
	Organization getOrganization();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.SVNRepositoryURLEntity#getOrganization <em>Organization</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Organization</em>' reference.
	 * @see #getOrganization()
	 * @generated
	 */
	void setOrganization(Organization value);

} // SVNRepositoryURLEntity
