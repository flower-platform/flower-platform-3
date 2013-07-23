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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Organization</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getLabel <em>Label</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getURL <em>URL</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getLogoURL <em>Logo URL</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getIconURL <em>Icon URL</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#isActivated <em>Activated</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getGroups <em>Groups</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getOrganizationUsers <em>Organization Users</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getSvnRepositoryURLs <em>Svn Repository UR Ls</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getProjectsCount <em>Projects Count</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getFilesCount <em>Files Count</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getModelsCount <em>Models Count</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getDiagramsCount <em>Diagrams Count</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Organization#getWorkingDirectories <em>Working Directories</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization()
 * @model
 * @generated
 */
public interface Organization extends NamedEntity, ISecurityEntity {
	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_Label()
	 * @model
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>URL</em>' attribute.
	 * @see #setURL(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_URL()
	 * @model
	 * @generated
	 */
	String getURL();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#getURL <em>URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>URL</em>' attribute.
	 * @see #getURL()
	 * @generated
	 */
	void setURL(String value);

	/**
	 * Returns the value of the '<em><b>Logo URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Logo URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Logo URL</em>' attribute.
	 * @see #setLogoURL(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_LogoURL()
	 * @model
	 * @generated
	 */
	String getLogoURL();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#getLogoURL <em>Logo URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Logo URL</em>' attribute.
	 * @see #getLogoURL()
	 * @generated
	 */
	void setLogoURL(String value);

	/**
	 * Returns the value of the '<em><b>Icon URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Icon URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Icon URL</em>' attribute.
	 * @see #setIconURL(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_IconURL()
	 * @model
	 * @generated
	 */
	String getIconURL();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#getIconURL <em>Icon URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Icon URL</em>' attribute.
	 * @see #getIconURL()
	 * @generated
	 */
	void setIconURL(String value);

	/**
	 * Returns the value of the '<em><b>Activated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activated</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activated</em>' attribute.
	 * @see #setActivated(boolean)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_Activated()
	 * @model
	 * @generated
	 */
	boolean isActivated();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#isActivated <em>Activated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Activated</em>' attribute.
	 * @see #isActivated()
	 * @generated
	 */
	void setActivated(boolean value);

	/**
	 * Returns the value of the '<em><b>Groups</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.Group}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.Group#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Groups</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Groups</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_Groups()
	 * @see org.flowerplatform.web.entity.Group#getOrganization
	 * @model opposite="organization"
	 * @generated
	 */
	EList<Group> getGroups();

	/**
	 * Returns the value of the '<em><b>Organization Users</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.OrganizationUser}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.OrganizationUser#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization Users</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Organization Users</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_OrganizationUsers()
	 * @see org.flowerplatform.web.entity.OrganizationUser#getOrganization
	 * @model opposite="organization"
	 * @generated
	 */
	EList<OrganizationUser> getOrganizationUsers();

	/**
	 * Returns the value of the '<em><b>Svn Repository UR Ls</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.SVNRepositoryURLEntity}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.SVNRepositoryURLEntity#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Svn Repository UR Ls</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Svn Repository UR Ls</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_SvnRepositoryURLs()
	 * @see org.flowerplatform.web.entity.SVNRepositoryURLEntity#getOrganization
	 * @model opposite="organization"
	 * @generated
	 */
	EList<SVNRepositoryURLEntity> getSvnRepositoryURLs();

	/**
	 * Returns the value of the '<em><b>Projects Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Projects Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Projects Count</em>' attribute.
	 * @see #setProjectsCount(int)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_ProjectsCount()
	 * @model
	 * @generated
	 */
	int getProjectsCount();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#getProjectsCount <em>Projects Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Projects Count</em>' attribute.
	 * @see #getProjectsCount()
	 * @generated
	 */
	void setProjectsCount(int value);

	/**
	 * Returns the value of the '<em><b>Files Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Files Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Files Count</em>' attribute.
	 * @see #setFilesCount(int)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_FilesCount()
	 * @model
	 * @generated
	 */
	int getFilesCount();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#getFilesCount <em>Files Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Files Count</em>' attribute.
	 * @see #getFilesCount()
	 * @generated
	 */
	void setFilesCount(int value);

	/**
	 * Returns the value of the '<em><b>Models Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Models Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Models Count</em>' attribute.
	 * @see #setModelsCount(int)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_ModelsCount()
	 * @model
	 * @generated
	 */
	int getModelsCount();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#getModelsCount <em>Models Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Models Count</em>' attribute.
	 * @see #getModelsCount()
	 * @generated
	 */
	void setModelsCount(int value);

	/**
	 * Returns the value of the '<em><b>Diagrams Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagrams Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagrams Count</em>' attribute.
	 * @see #setDiagramsCount(int)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_DiagramsCount()
	 * @model
	 * @generated
	 */
	int getDiagramsCount();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Organization#getDiagramsCount <em>Diagrams Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagrams Count</em>' attribute.
	 * @see #getDiagramsCount()
	 * @generated
	 */
	void setDiagramsCount(int value);

	/**
	 * Returns the value of the '<em><b>Working Directories</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.WorkingDirectory}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.WorkingDirectory#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Working Directories</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Working Directories</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganization_WorkingDirectories()
	 * @see org.flowerplatform.web.entity.WorkingDirectory#getOrganization
	 * @model opposite="organization"
	 * @generated
	 */
	EList<WorkingDirectory> getWorkingDirectories();

} // Organization