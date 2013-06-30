/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.flowerplatform.web.entity.EntityPackage;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.OrganizationUser;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.entity.User;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Organization</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getURL <em>URL</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getLogoURL <em>Logo URL</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getIconURL <em>Icon URL</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#isActivated <em>Activated</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getGroups <em>Groups</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getOrganizationUsers <em>Organization Users</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getSvnRepositoryURLs <em>Svn Repository UR Ls</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getProjectsCount <em>Projects Count</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getFilesCount <em>Files Count</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getModelsCount <em>Models Count</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getDiagramsCount <em>Diagrams Count</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.OrganizationImpl#getWorkingDirectories <em>Working Directories</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OrganizationImpl extends NamedEntityImpl implements Organization {
	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getURL() <em>URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getURL()
	 * @generated
	 * @ordered
	 */
	protected static final String URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getURL() <em>URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getURL()
	 * @generated
	 * @ordered
	 */
	protected String url = URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getLogoURL() <em>Logo URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogoURL()
	 * @generated
	 * @ordered
	 */
	protected static final String LOGO_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLogoURL() <em>Logo URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogoURL()
	 * @generated
	 * @ordered
	 */
	protected String logoURL = LOGO_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getIconURL() <em>Icon URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIconURL()
	 * @generated
	 * @ordered
	 */
	protected static final String ICON_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIconURL() <em>Icon URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIconURL()
	 * @generated
	 * @ordered
	 */
	protected String iconURL = ICON_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #isActivated() <em>Activated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isActivated()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ACTIVATED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isActivated() <em>Activated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isActivated()
	 * @generated
	 * @ordered
	 */
	protected boolean activated = ACTIVATED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGroups() <em>Groups</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<Group> groups;

	/**
	 * The cached value of the '{@link #getOrganizationUsers() <em>Organization Users</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrganizationUsers()
	 * @generated
	 * @ordered
	 */
	protected EList<OrganizationUser> organizationUsers;

	/**
	 * The cached value of the '{@link #getSvnRepositoryURLs() <em>Svn Repository UR Ls</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSvnRepositoryURLs()
	 * @generated
	 * @ordered
	 */
	protected EList<SVNRepositoryURLEntity> svnRepositoryURLs;

	/**
	 * The default value of the '{@link #getProjectsCount() <em>Projects Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProjectsCount()
	 * @generated
	 * @ordered
	 */
	protected static final int PROJECTS_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getProjectsCount() <em>Projects Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProjectsCount()
	 * @generated
	 * @ordered
	 */
	protected int projectsCount = PROJECTS_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #getFilesCount() <em>Files Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilesCount()
	 * @generated
	 * @ordered
	 */
	protected static final int FILES_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFilesCount() <em>Files Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilesCount()
	 * @generated
	 * @ordered
	 */
	protected int filesCount = FILES_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #getModelsCount() <em>Models Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelsCount()
	 * @generated
	 * @ordered
	 */
	protected static final int MODELS_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getModelsCount() <em>Models Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelsCount()
	 * @generated
	 * @ordered
	 */
	protected int modelsCount = MODELS_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDiagramsCount() <em>Diagrams Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagramsCount()
	 * @generated
	 * @ordered
	 */
	protected static final int DIAGRAMS_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDiagramsCount() <em>Diagrams Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagramsCount()
	 * @generated
	 * @ordered
	 */
	protected int diagramsCount = DIAGRAMS_COUNT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getWorkingDirectories() <em>Working Directories</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWorkingDirectories()
	 * @generated
	 * @ordered
	 */
	protected EList<WorkingDirectory> workingDirectories;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OrganizationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EntityPackage.Literals.ORGANIZATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getURL() {
		return url;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setURL(String newURL) {
		String oldURL = url;
		url = newURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__URL, oldURL, url));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLogoURL() {
		return logoURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLogoURL(String newLogoURL) {
		String oldLogoURL = logoURL;
		logoURL = newLogoURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__LOGO_URL, oldLogoURL, logoURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIconURL() {
		return iconURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIconURL(String newIconURL) {
		String oldIconURL = iconURL;
		iconURL = newIconURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__ICON_URL, oldIconURL, iconURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActivated(boolean newActivated) {
		boolean oldActivated = activated;
		activated = newActivated;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__ACTIVATED, oldActivated, activated));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Group> getGroups() {
		if (groups == null) {
			groups = new EObjectWithInverseResolvingEList<Group>(Group.class, this, EntityPackage.ORGANIZATION__GROUPS, EntityPackage.GROUP__ORGANIZATION);
		}
		return groups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OrganizationUser> getOrganizationUsers() {
		if (organizationUsers == null) {
			organizationUsers = new EObjectWithInverseResolvingEList<OrganizationUser>(OrganizationUser.class, this, EntityPackage.ORGANIZATION__ORGANIZATION_USERS, EntityPackage.ORGANIZATION_USER__ORGANIZATION);
		}
		return organizationUsers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SVNRepositoryURLEntity> getSvnRepositoryURLs() {
		if (svnRepositoryURLs == null) {
			svnRepositoryURLs = new EObjectWithInverseResolvingEList<SVNRepositoryURLEntity>(SVNRepositoryURLEntity.class, this, EntityPackage.ORGANIZATION__SVN_REPOSITORY_UR_LS, EntityPackage.SVN_REPOSITORY_URL_ENTITY__ORGANIZATION);
		}
		return svnRepositoryURLs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getProjectsCount() {
		return projectsCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProjectsCount(int newProjectsCount) {
		int oldProjectsCount = projectsCount;
		projectsCount = newProjectsCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__PROJECTS_COUNT, oldProjectsCount, projectsCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getFilesCount() {
		return filesCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFilesCount(int newFilesCount) {
		int oldFilesCount = filesCount;
		filesCount = newFilesCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__FILES_COUNT, oldFilesCount, filesCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getModelsCount() {
		return modelsCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelsCount(int newModelsCount) {
		int oldModelsCount = modelsCount;
		modelsCount = newModelsCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__MODELS_COUNT, oldModelsCount, modelsCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDiagramsCount() {
		return diagramsCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagramsCount(int newDiagramsCount) {
		int oldDiagramsCount = diagramsCount;
		diagramsCount = newDiagramsCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.ORGANIZATION__DIAGRAMS_COUNT, oldDiagramsCount, diagramsCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<WorkingDirectory> getWorkingDirectories() {
		if (workingDirectories == null) {
			workingDirectories = new EObjectWithInverseResolvingEList<WorkingDirectory>(WorkingDirectory.class, this, EntityPackage.ORGANIZATION__WORKING_DIRECTORIES, EntityPackage.WORKING_DIRECTORY__ORGANIZATION);
		}
		return workingDirectories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 * @author Mariana
	 */
	public boolean contains(final ISecurityEntity securityEntity) {
		boolean includes = false;
		
		if (securityEntity instanceof User) {
			User user = (User) securityEntity;
			for (OrganizationUser ou : getOrganizationUsers()) {
				if (user.equals(ou.getUser())) {
					includes = true;
					break;
				}
			}
		} else if (securityEntity instanceof Group) {
			includes = getGroups().contains(securityEntity);
		} else if (securityEntity instanceof Organization) {
			includes = OrganizationImpl.this.equals(securityEntity);
		}
		return includes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EntityPackage.ORGANIZATION__GROUPS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getGroups()).basicAdd(otherEnd, msgs);
			case EntityPackage.ORGANIZATION__ORGANIZATION_USERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getOrganizationUsers()).basicAdd(otherEnd, msgs);
			case EntityPackage.ORGANIZATION__SVN_REPOSITORY_UR_LS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSvnRepositoryURLs()).basicAdd(otherEnd, msgs);
			case EntityPackage.ORGANIZATION__WORKING_DIRECTORIES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getWorkingDirectories()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EntityPackage.ORGANIZATION__GROUPS:
				return ((InternalEList<?>)getGroups()).basicRemove(otherEnd, msgs);
			case EntityPackage.ORGANIZATION__ORGANIZATION_USERS:
				return ((InternalEList<?>)getOrganizationUsers()).basicRemove(otherEnd, msgs);
			case EntityPackage.ORGANIZATION__SVN_REPOSITORY_UR_LS:
				return ((InternalEList<?>)getSvnRepositoryURLs()).basicRemove(otherEnd, msgs);
			case EntityPackage.ORGANIZATION__WORKING_DIRECTORIES:
				return ((InternalEList<?>)getWorkingDirectories()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EntityPackage.ORGANIZATION__LABEL:
				return getLabel();
			case EntityPackage.ORGANIZATION__URL:
				return getURL();
			case EntityPackage.ORGANIZATION__LOGO_URL:
				return getLogoURL();
			case EntityPackage.ORGANIZATION__ICON_URL:
				return getIconURL();
			case EntityPackage.ORGANIZATION__ACTIVATED:
				return isActivated();
			case EntityPackage.ORGANIZATION__GROUPS:
				return getGroups();
			case EntityPackage.ORGANIZATION__ORGANIZATION_USERS:
				return getOrganizationUsers();
			case EntityPackage.ORGANIZATION__SVN_REPOSITORY_UR_LS:
				return getSvnRepositoryURLs();
			case EntityPackage.ORGANIZATION__PROJECTS_COUNT:
				return getProjectsCount();
			case EntityPackage.ORGANIZATION__FILES_COUNT:
				return getFilesCount();
			case EntityPackage.ORGANIZATION__MODELS_COUNT:
				return getModelsCount();
			case EntityPackage.ORGANIZATION__DIAGRAMS_COUNT:
				return getDiagramsCount();
			case EntityPackage.ORGANIZATION__WORKING_DIRECTORIES:
				return getWorkingDirectories();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EntityPackage.ORGANIZATION__LABEL:
				setLabel((String)newValue);
				return;
			case EntityPackage.ORGANIZATION__URL:
				setURL((String)newValue);
				return;
			case EntityPackage.ORGANIZATION__LOGO_URL:
				setLogoURL((String)newValue);
				return;
			case EntityPackage.ORGANIZATION__ICON_URL:
				setIconURL((String)newValue);
				return;
			case EntityPackage.ORGANIZATION__ACTIVATED:
				setActivated((Boolean)newValue);
				return;
			case EntityPackage.ORGANIZATION__GROUPS:
				getGroups().clear();
				getGroups().addAll((Collection<? extends Group>)newValue);
				return;
			case EntityPackage.ORGANIZATION__ORGANIZATION_USERS:
				getOrganizationUsers().clear();
				getOrganizationUsers().addAll((Collection<? extends OrganizationUser>)newValue);
				return;
			case EntityPackage.ORGANIZATION__SVN_REPOSITORY_UR_LS:
				getSvnRepositoryURLs().clear();
				getSvnRepositoryURLs().addAll((Collection<? extends SVNRepositoryURLEntity>)newValue);
				return;
			case EntityPackage.ORGANIZATION__PROJECTS_COUNT:
				setProjectsCount((Integer)newValue);
				return;
			case EntityPackage.ORGANIZATION__FILES_COUNT:
				setFilesCount((Integer)newValue);
				return;
			case EntityPackage.ORGANIZATION__MODELS_COUNT:
				setModelsCount((Integer)newValue);
				return;
			case EntityPackage.ORGANIZATION__DIAGRAMS_COUNT:
				setDiagramsCount((Integer)newValue);
				return;
			case EntityPackage.ORGANIZATION__WORKING_DIRECTORIES:
				getWorkingDirectories().clear();
				getWorkingDirectories().addAll((Collection<? extends WorkingDirectory>)newValue);
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
			case EntityPackage.ORGANIZATION__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__URL:
				setURL(URL_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__LOGO_URL:
				setLogoURL(LOGO_URL_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__ICON_URL:
				setIconURL(ICON_URL_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__ACTIVATED:
				setActivated(ACTIVATED_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__GROUPS:
				getGroups().clear();
				return;
			case EntityPackage.ORGANIZATION__ORGANIZATION_USERS:
				getOrganizationUsers().clear();
				return;
			case EntityPackage.ORGANIZATION__SVN_REPOSITORY_UR_LS:
				getSvnRepositoryURLs().clear();
				return;
			case EntityPackage.ORGANIZATION__PROJECTS_COUNT:
				setProjectsCount(PROJECTS_COUNT_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__FILES_COUNT:
				setFilesCount(FILES_COUNT_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__MODELS_COUNT:
				setModelsCount(MODELS_COUNT_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__DIAGRAMS_COUNT:
				setDiagramsCount(DIAGRAMS_COUNT_EDEFAULT);
				return;
			case EntityPackage.ORGANIZATION__WORKING_DIRECTORIES:
				getWorkingDirectories().clear();
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
			case EntityPackage.ORGANIZATION__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case EntityPackage.ORGANIZATION__URL:
				return URL_EDEFAULT == null ? url != null : !URL_EDEFAULT.equals(url);
			case EntityPackage.ORGANIZATION__LOGO_URL:
				return LOGO_URL_EDEFAULT == null ? logoURL != null : !LOGO_URL_EDEFAULT.equals(logoURL);
			case EntityPackage.ORGANIZATION__ICON_URL:
				return ICON_URL_EDEFAULT == null ? iconURL != null : !ICON_URL_EDEFAULT.equals(iconURL);
			case EntityPackage.ORGANIZATION__ACTIVATED:
				return activated != ACTIVATED_EDEFAULT;
			case EntityPackage.ORGANIZATION__GROUPS:
				return groups != null && !groups.isEmpty();
			case EntityPackage.ORGANIZATION__ORGANIZATION_USERS:
				return organizationUsers != null && !organizationUsers.isEmpty();
			case EntityPackage.ORGANIZATION__SVN_REPOSITORY_UR_LS:
				return svnRepositoryURLs != null && !svnRepositoryURLs.isEmpty();
			case EntityPackage.ORGANIZATION__PROJECTS_COUNT:
				return projectsCount != PROJECTS_COUNT_EDEFAULT;
			case EntityPackage.ORGANIZATION__FILES_COUNT:
				return filesCount != FILES_COUNT_EDEFAULT;
			case EntityPackage.ORGANIZATION__MODELS_COUNT:
				return modelsCount != MODELS_COUNT_EDEFAULT;
			case EntityPackage.ORGANIZATION__DIAGRAMS_COUNT:
				return diagramsCount != DIAGRAMS_COUNT_EDEFAULT;
			case EntityPackage.ORGANIZATION__WORKING_DIRECTORIES:
				return workingDirectories != null && !workingDirectories.isEmpty();
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
		result.append(" (label: ");
		result.append(label);
		result.append(", URL: ");
		result.append(url);
		result.append(", logoURL: ");
		result.append(logoURL);
		result.append(", iconURL: ");
		result.append(iconURL);
		result.append(", activated: ");
		result.append(activated);
		result.append(", projectsCount: ");
		result.append(projectsCount);
		result.append(", filesCount: ");
		result.append(filesCount);
		result.append(", modelsCount: ");
		result.append(modelsCount);
		result.append(", diagramsCount: ");
		result.append(diagramsCount);
		result.append(')');
		return result.toString();
	}

} //OrganizationImpl
