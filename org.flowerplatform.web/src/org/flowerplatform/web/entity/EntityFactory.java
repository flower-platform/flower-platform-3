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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.web.entity.EntityPackage
 * @generated
 */
public interface EntityFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EntityFactory eINSTANCE = org.flowerplatform.web.entity.impl.EntityFactoryImpl.init();

	/**
	 * @author Mariana
	 */
	Object create(String className);
	
	/**
	 * Returns a new object of class '<em>Organization</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Organization</em>'.
	 * @generated
	 */
	Organization createOrganization();

	/**
	 * Returns a new object of class '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group</em>'.
	 * @generated
	 */
	Group createGroup();

	/**
	 * Returns a new object of class '<em>User</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>User</em>'.
	 * @generated
	 */
	User createUser();

	/**
	 * Returns a new object of class '<em>Organization User</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Organization User</em>'.
	 * @generated
	 */
	OrganizationUser createOrganizationUser();

	/**
	 * Returns a new object of class '<em>Group User</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group User</em>'.
	 * @generated
	 */
	GroupUser createGroupUser();

	/**
	 * Returns a new object of class '<em>Permission Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Permission Entity</em>'.
	 * @generated
	 */
	PermissionEntity createPermissionEntity();

	/**
	 * Returns a new object of class '<em>SVN Repository URL Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>SVN Repository URL Entity</em>'.
	 * @generated
	 */
	SVNRepositoryURLEntity createSVNRepositoryURLEntity();

	/**
	 * Returns a new object of class '<em>SVN Comment Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>SVN Comment Entity</em>'.
	 * @generated
	 */
	SVNCommentEntity createSVNCommentEntity();

	/**
	 * Returns a new object of class '<em>Perspective User Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Perspective User Entry</em>'.
	 * @generated
	 */
	PerspectiveUserEntry createPerspectiveUserEntry();

	/**
	 * Returns a new object of class '<em>Favorite Item</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Favorite Item</em>'.
	 * @generated
	 */
	FavoriteItem createFavoriteItem();

	/**
	 * Returns a new object of class '<em>Recent Resource</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Recent Resource</em>'.
	 * @generated
	 */
	RecentResource createRecentResource();

	/**
	 * Returns a new object of class '<em>Audit Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Audit Entry</em>'.
	 * @generated
	 */
	AuditEntry createAuditEntry();

	/**
	 * Returns a new object of class '<em>DB Version</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>DB Version</em>'.
	 * @generated
	 */
	DBVersion createDBVersion();

	/**
	 * Returns a new object of class '<em>Working Directory</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Working Directory</em>'.
	 * @generated
	 */
	WorkingDirectory createWorkingDirectory();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EntityPackage getEntityPackage();

} //EntityFactory