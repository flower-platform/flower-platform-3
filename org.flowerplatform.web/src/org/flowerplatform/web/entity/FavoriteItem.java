/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Favorite Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.FavoriteItem#getUser <em>User</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.FavoriteItem#getResource <em>Resource</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.FavoriteItem#getOrganization <em>Organization</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.FavoriteItem#getCategory <em>Category</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getFavoriteItem()
 * @model
 * @generated
 */
public interface FavoriteItem extends Entity {
	/**
	 * Returns the value of the '<em><b>User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User</em>' reference.
	 * @see #setUser(User)
	 * @see org.flowerplatform.web.entity.EntityPackage#getFavoriteItem_User()
	 * @model
	 * @generated
	 */
	User getUser();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.FavoriteItem#getUser <em>User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User</em>' reference.
	 * @see #getUser()
	 * @generated
	 */
	void setUser(User value);

	/**
	 * Returns the value of the '<em><b>Resource</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource</em>' reference.
	 * @see #setResource(RecentResource)
	 * @see org.flowerplatform.web.entity.EntityPackage#getFavoriteItem_Resource()
	 * @model
	 * @generated
	 */
	RecentResource getResource();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.FavoriteItem#getResource <em>Resource</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource</em>' reference.
	 * @see #getResource()
	 * @generated
	 */
	void setResource(RecentResource value);

	/**
	 * Returns the value of the '<em><b>Organization</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Organization</em>' reference.
	 * @see #setOrganization(Organization)
	 * @see org.flowerplatform.web.entity.EntityPackage#getFavoriteItem_Organization()
	 * @model
	 * @generated
	 */
	Organization getOrganization();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.FavoriteItem#getOrganization <em>Organization</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Organization</em>' reference.
	 * @see #getOrganization()
	 * @generated
	 */
	void setOrganization(Organization value);

	/**
	 * Returns the value of the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' attribute.
	 * @see #setCategory(int)
	 * @see org.flowerplatform.web.entity.EntityPackage#getFavoriteItem_Category()
	 * @model
	 * @generated
	 */
	int getCategory();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.FavoriteItem#getCategory <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category</em>' attribute.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Object getItem();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setItem(Object item);

} // FavoriteItem
