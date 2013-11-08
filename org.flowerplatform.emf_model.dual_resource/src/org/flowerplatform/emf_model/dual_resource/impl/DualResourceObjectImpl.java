/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.dual_resource.impl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.flowerplatform.emf_model.dual_resource.DualResourceObject;
import org.flowerplatform.emf_model.dual_resource.DualResourcePackage;
import org.flowerplatform.emf_model.dual_resource.DualResourceURIHandler;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DualResourceObjectImpl extends EObjectImpl implements DualResourceObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DualResourceObjectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DualResourcePackage.Literals.DUAL_RESOURCE_OBJECT;
	}

	/**
	 * @generated NOT
	 */
	@Override
	public EObject eResolveProxy(InternalEObject proxy) {
		URI uri = proxy.eProxyURI();
		if (DualResourceURIHandler.isDualResourceURI(uri)) {
			URI baseUri = eResource().getURI();
			uri = DualResourceURIHandler.createDualResourceURI(baseUri, uri);
			proxy.eSetProxyURI(uri);
		}
		return super.eResolveProxy(proxy);
	}

} //DualResourceObjectImpl
