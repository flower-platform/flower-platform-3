/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.EntityPackage;
import org.flowerplatform.web.entity.FavoriteItem;
import org.flowerplatform.web.entity.GroupUser;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.OrganizationUser;
import org.flowerplatform.web.entity.PerspectiveUserEntry;
import org.flowerplatform.web.entity.SVNCommentEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.dao.Dao;
import org.hibernate.Session;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>User</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getLogin <em>Login</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getHashedPassword <em>Hashed Password</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getEmail <em>Email</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#isActivated <em>Activated</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getActivationCode <em>Activation Code</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getGroupUsers <em>Group Users</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getOrganizationUsers <em>Organization Users</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getPerspectiveUserEntries <em>Perspective User Entries</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getLastPerspective <em>Last Perspective</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getSvnComments <em>Svn Comments</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.UserImpl#getFavoriteItems <em>Favorite Items</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UserImpl extends NamedEntityImpl implements User {
	/**
	 * The default value of the '{@link #getLogin() <em>Login</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogin()
	 * @generated
	 * @ordered
	 */
	protected static final String LOGIN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLogin() <em>Login</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogin()
	 * @generated
	 * @ordered
	 */
	protected String login = LOGIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getHashedPassword() <em>Hashed Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHashedPassword()
	 * @generated
	 * @ordered
	 */
	protected static final String HASHED_PASSWORD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHashedPassword() <em>Hashed Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHashedPassword()
	 * @generated
	 * @ordered
	 */
	protected String hashedPassword = HASHED_PASSWORD_EDEFAULT;

	/**
	 * The default value of the '{@link #getEmail() <em>Email</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmail()
	 * @generated
	 * @ordered
	 */
	protected static final String EMAIL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEmail() <em>Email</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmail()
	 * @generated
	 * @ordered
	 */
	protected String email = EMAIL_EDEFAULT;

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
	 * The default value of the '{@link #getActivationCode() <em>Activation Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivationCode()
	 * @generated
	 * @ordered
	 */
	protected static final String ACTIVATION_CODE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getActivationCode() <em>Activation Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivationCode()
	 * @generated
	 * @ordered
	 */
	protected String activationCode = ACTIVATION_CODE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGroupUsers() <em>Group Users</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupUsers()
	 * @generated
	 * @ordered
	 */
	protected EList<GroupUser> groupUsers;

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
	 * The cached value of the '{@link #getPerspectiveUserEntries() <em>Perspective User Entries</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPerspectiveUserEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<PerspectiveUserEntry> perspectiveUserEntries;

	/**
	 * The default value of the '{@link #getLastPerspective() <em>Last Perspective</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastPerspective()
	 * @generated
	 * @ordered
	 */
	protected static final String LAST_PERSPECTIVE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLastPerspective() <em>Last Perspective</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastPerspective()
	 * @generated
	 * @ordered
	 */
	protected String lastPerspective = LAST_PERSPECTIVE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSvnComments() <em>Svn Comments</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSvnComments()
	 * @generated
	 * @ordered
	 */
	protected EList<SVNCommentEntity> svnComments;

	/**
	 * The cached value of the '{@link #getFavoriteItems() <em>Favorite Items</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFavoriteItems()
	 * @generated
	 * @ordered
	 */
	protected EList<FavoriteItem> favoriteItems;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UserImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EntityPackage.Literals.USER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLogin(String newLogin) {
		String oldLogin = login;
		login = newLogin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.USER__LOGIN, oldLogin, login));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHashedPassword(String newHashedPassword) {
		String oldHashedPassword = hashedPassword;
		hashedPassword = newHashedPassword;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.USER__HASHED_PASSWORD, oldHashedPassword, hashedPassword));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEmail(String newEmail) {
		String oldEmail = email;
		email = newEmail;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.USER__EMAIL, oldEmail, email));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.USER__ACTIVATED, oldActivated, activated));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getActivationCode() {
		return activationCode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActivationCode(String newActivationCode) {
		String oldActivationCode = activationCode;
		activationCode = newActivationCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.USER__ACTIVATION_CODE, oldActivationCode, activationCode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GroupUser> getGroupUsers() {
		if (groupUsers == null) {
			groupUsers = new EObjectResolvingEList<GroupUser>(GroupUser.class, this, EntityPackage.USER__GROUP_USERS);
		}
		return groupUsers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OrganizationUser> getOrganizationUsers() {
		if (organizationUsers == null) {
			organizationUsers = new EObjectResolvingEList<OrganizationUser>(OrganizationUser.class, this, EntityPackage.USER__ORGANIZATION_USERS);
		}
		return organizationUsers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PerspectiveUserEntry> getPerspectiveUserEntries() {
		if (perspectiveUserEntries == null) {
			perspectiveUserEntries = new EObjectResolvingEList<PerspectiveUserEntry>(PerspectiveUserEntry.class, this, EntityPackage.USER__PERSPECTIVE_USER_ENTRIES);
		}
		return perspectiveUserEntries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLastPerspective() {
		return lastPerspective;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastPerspective(String newLastPerspective) {
		String oldLastPerspective = lastPerspective;
		lastPerspective = newLastPerspective;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.USER__LAST_PERSPECTIVE, oldLastPerspective, lastPerspective));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SVNCommentEntity> getSvnComments() {
		if (svnComments == null) {
			svnComments = new EObjectResolvingEList<SVNCommentEntity>(SVNCommentEntity.class, this, EntityPackage.USER__SVN_COMMENTS);
		}
		return svnComments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FavoriteItem> getFavoriteItems() {
		if (favoriteItems == null) {
			favoriteItems = new EObjectResolvingEList<FavoriteItem>(FavoriteItem.class, this, EntityPackage.USER__FAVORITE_ITEMS);
		}
		return favoriteItems;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FavoriteItem getFavoriteItem(Object item, int category) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAdmin() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean contains(ISecurityEntity securityEntity) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EntityPackage.USER__LOGIN:
				return getLogin();
			case EntityPackage.USER__HASHED_PASSWORD:
				return getHashedPassword();
			case EntityPackage.USER__EMAIL:
				return getEmail();
			case EntityPackage.USER__ACTIVATED:
				return isActivated();
			case EntityPackage.USER__ACTIVATION_CODE:
				return getActivationCode();
			case EntityPackage.USER__GROUP_USERS:
				return getGroupUsers();
			case EntityPackage.USER__ORGANIZATION_USERS:
				return getOrganizationUsers();
			case EntityPackage.USER__PERSPECTIVE_USER_ENTRIES:
				return getPerspectiveUserEntries();
			case EntityPackage.USER__LAST_PERSPECTIVE:
				return getLastPerspective();
			case EntityPackage.USER__SVN_COMMENTS:
				return getSvnComments();
			case EntityPackage.USER__FAVORITE_ITEMS:
				return getFavoriteItems();
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
			case EntityPackage.USER__LOGIN:
				setLogin((String)newValue);
				return;
			case EntityPackage.USER__HASHED_PASSWORD:
				setHashedPassword((String)newValue);
				return;
			case EntityPackage.USER__EMAIL:
				setEmail((String)newValue);
				return;
			case EntityPackage.USER__ACTIVATED:
				setActivated((Boolean)newValue);
				return;
			case EntityPackage.USER__ACTIVATION_CODE:
				setActivationCode((String)newValue);
				return;
			case EntityPackage.USER__GROUP_USERS:
				getGroupUsers().clear();
				getGroupUsers().addAll((Collection<? extends GroupUser>)newValue);
				return;
			case EntityPackage.USER__ORGANIZATION_USERS:
				getOrganizationUsers().clear();
				getOrganizationUsers().addAll((Collection<? extends OrganizationUser>)newValue);
				return;
			case EntityPackage.USER__PERSPECTIVE_USER_ENTRIES:
				getPerspectiveUserEntries().clear();
				getPerspectiveUserEntries().addAll((Collection<? extends PerspectiveUserEntry>)newValue);
				return;
			case EntityPackage.USER__LAST_PERSPECTIVE:
				setLastPerspective((String)newValue);
				return;
			case EntityPackage.USER__SVN_COMMENTS:
				getSvnComments().clear();
				getSvnComments().addAll((Collection<? extends SVNCommentEntity>)newValue);
				return;
			case EntityPackage.USER__FAVORITE_ITEMS:
				getFavoriteItems().clear();
				getFavoriteItems().addAll((Collection<? extends FavoriteItem>)newValue);
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
			case EntityPackage.USER__LOGIN:
				setLogin(LOGIN_EDEFAULT);
				return;
			case EntityPackage.USER__HASHED_PASSWORD:
				setHashedPassword(HASHED_PASSWORD_EDEFAULT);
				return;
			case EntityPackage.USER__EMAIL:
				setEmail(EMAIL_EDEFAULT);
				return;
			case EntityPackage.USER__ACTIVATED:
				setActivated(ACTIVATED_EDEFAULT);
				return;
			case EntityPackage.USER__ACTIVATION_CODE:
				setActivationCode(ACTIVATION_CODE_EDEFAULT);
				return;
			case EntityPackage.USER__GROUP_USERS:
				getGroupUsers().clear();
				return;
			case EntityPackage.USER__ORGANIZATION_USERS:
				getOrganizationUsers().clear();
				return;
			case EntityPackage.USER__PERSPECTIVE_USER_ENTRIES:
				getPerspectiveUserEntries().clear();
				return;
			case EntityPackage.USER__LAST_PERSPECTIVE:
				setLastPerspective(LAST_PERSPECTIVE_EDEFAULT);
				return;
			case EntityPackage.USER__SVN_COMMENTS:
				getSvnComments().clear();
				return;
			case EntityPackage.USER__FAVORITE_ITEMS:
				getFavoriteItems().clear();
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
			case EntityPackage.USER__LOGIN:
				return LOGIN_EDEFAULT == null ? login != null : !LOGIN_EDEFAULT.equals(login);
			case EntityPackage.USER__HASHED_PASSWORD:
				return HASHED_PASSWORD_EDEFAULT == null ? hashedPassword != null : !HASHED_PASSWORD_EDEFAULT.equals(hashedPassword);
			case EntityPackage.USER__EMAIL:
				return EMAIL_EDEFAULT == null ? email != null : !EMAIL_EDEFAULT.equals(email);
			case EntityPackage.USER__ACTIVATED:
				return activated != ACTIVATED_EDEFAULT;
			case EntityPackage.USER__ACTIVATION_CODE:
				return ACTIVATION_CODE_EDEFAULT == null ? activationCode != null : !ACTIVATION_CODE_EDEFAULT.equals(activationCode);
			case EntityPackage.USER__GROUP_USERS:
				return groupUsers != null && !groupUsers.isEmpty();
			case EntityPackage.USER__ORGANIZATION_USERS:
				return organizationUsers != null && !organizationUsers.isEmpty();
			case EntityPackage.USER__PERSPECTIVE_USER_ENTRIES:
				return perspectiveUserEntries != null && !perspectiveUserEntries.isEmpty();
			case EntityPackage.USER__LAST_PERSPECTIVE:
				return LAST_PERSPECTIVE_EDEFAULT == null ? lastPerspective != null : !LAST_PERSPECTIVE_EDEFAULT.equals(lastPerspective);
			case EntityPackage.USER__SVN_COMMENTS:
				return svnComments != null && !svnComments.isEmpty();
			case EntityPackage.USER__FAVORITE_ITEMS:
				return favoriteItems != null && !favoriteItems.isEmpty();
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
		result.append(" (login: ");
		result.append(login);
		result.append(", hashedPassword: ");
		result.append(hashedPassword);
		result.append(", email: ");
		result.append(email);
		result.append(", activated: ");
		result.append(activated);
		result.append(", activationCode: ");
		result.append(activationCode);
		result.append(", lastPerspective: ");
		result.append(lastPerspective);
		result.append(')');
		return result.toString();
	}

} //UserImpl
