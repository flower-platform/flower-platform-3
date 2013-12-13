/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao2.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.flowerplatform.model_access_dao2.model.ModelPackage;
import org.flowerplatform.model_access_dao2.model.ResourceInfo;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.impl.ResourceInfoImpl#getRepoId <em>Repo Id</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.impl.ResourceInfoImpl#getDesignId <em>Design Id</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.impl.ResourceInfoImpl#getResourceId <em>Resource Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceInfoImpl extends EObjectImpl implements ResourceInfo {
	/**
	 * The default value of the '{@link #getRepoId() <em>Repo Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepoId()
	 * @generated
	 * @ordered
	 */
	protected static final String REPO_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRepoId() <em>Repo Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepoId()
	 * @generated
	 * @ordered
	 */
	protected String repoId = REPO_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getDesignId() <em>Design Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesignId()
	 * @generated
	 * @ordered
	 */
	protected static final String DESIGN_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDesignId() <em>Design Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesignId()
	 * @generated
	 * @ordered
	 */
	protected String designId = DESIGN_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getResourceId() <em>Resource Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceId()
	 * @generated
	 * @ordered
	 */
	protected static final String RESOURCE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResourceId() <em>Resource Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceId()
	 * @generated
	 * @ordered
	 */
	protected String resourceId = RESOURCE_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResourceInfoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.RESOURCE_INFO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRepoId() {
		return repoId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRepoId(String newRepoId) {
		String oldRepoId = repoId;
		repoId = newRepoId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.RESOURCE_INFO__REPO_ID, oldRepoId, repoId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDesignId() {
		return designId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDesignId(String newDesignId) {
		String oldDesignId = designId;
		designId = newDesignId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.RESOURCE_INFO__DESIGN_ID, oldDesignId, designId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResourceId(String newResourceId) {
		String oldResourceId = resourceId;
		resourceId = newResourceId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.RESOURCE_INFO__RESOURCE_ID, oldResourceId, resourceId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.RESOURCE_INFO__REPO_ID:
				return getRepoId();
			case ModelPackage.RESOURCE_INFO__DESIGN_ID:
				return getDesignId();
			case ModelPackage.RESOURCE_INFO__RESOURCE_ID:
				return getResourceId();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.RESOURCE_INFO__REPO_ID:
				setRepoId((String)newValue);
				return;
			case ModelPackage.RESOURCE_INFO__DESIGN_ID:
				setDesignId((String)newValue);
				return;
			case ModelPackage.RESOURCE_INFO__RESOURCE_ID:
				setResourceId((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.RESOURCE_INFO__REPO_ID:
				setRepoId(REPO_ID_EDEFAULT);
				return;
			case ModelPackage.RESOURCE_INFO__DESIGN_ID:
				setDesignId(DESIGN_ID_EDEFAULT);
				return;
			case ModelPackage.RESOURCE_INFO__RESOURCE_ID:
				setResourceId(RESOURCE_ID_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.RESOURCE_INFO__REPO_ID:
				return REPO_ID_EDEFAULT == null ? repoId != null : !REPO_ID_EDEFAULT.equals(repoId);
			case ModelPackage.RESOURCE_INFO__DESIGN_ID:
				return DESIGN_ID_EDEFAULT == null ? designId != null : !DESIGN_ID_EDEFAULT.equals(designId);
			case ModelPackage.RESOURCE_INFO__RESOURCE_ID:
				return RESOURCE_ID_EDEFAULT == null ? resourceId != null : !RESOURCE_ID_EDEFAULT.equals(resourceId);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (repoId: ");
		result.append(repoId);
		result.append(", designId: ");
		result.append(designId);
		result.append(", resourceId: ");
		result.append(resourceId);
		result.append(')');
		return result.toString();
	}

} //ResourceInfoImpl
