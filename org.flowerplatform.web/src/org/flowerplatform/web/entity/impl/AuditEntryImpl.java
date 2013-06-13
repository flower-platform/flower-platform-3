/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.impl;

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.flowerplatform.web.entity.AuditEntry;
import org.flowerplatform.web.entity.EntityPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Audit Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.impl.AuditEntryImpl#getTimestamp <em>Timestamp</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.AuditEntryImpl#getUsername <em>Username</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.AuditEntryImpl#getIpAddress <em>Ip Address</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.AuditEntryImpl#getAuditCategory <em>Audit Category</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.AuditEntryImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.AuditEntryImpl#getParam0 <em>Param0</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.AuditEntryImpl#getParam1 <em>Param1</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.AuditEntryImpl#getParam2 <em>Param2</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AuditEntryImpl extends EntityImpl implements AuditEntry {
	/**
	 * The default value of the '{@link #getTimestamp() <em>Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimestamp()
	 * @generated
	 * @ordered
	 */
	protected static final Date TIMESTAMP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTimestamp() <em>Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimestamp()
	 * @generated
	 * @ordered
	 */
	protected Date timestamp = TIMESTAMP_EDEFAULT;

	/**
	 * The default value of the '{@link #getUsername() <em>Username</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUsername()
	 * @generated
	 * @ordered
	 */
	protected static final String USERNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUsername() <em>Username</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUsername()
	 * @generated
	 * @ordered
	 */
	protected String username = USERNAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getIpAddress() <em>Ip Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIpAddress()
	 * @generated
	 * @ordered
	 */
	protected static final String IP_ADDRESS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIpAddress() <em>Ip Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIpAddress()
	 * @generated
	 * @ordered
	 */
	protected String ipAddress = IP_ADDRESS_EDEFAULT;

	/**
	 * The default value of the '{@link #getAuditCategory() <em>Audit Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuditCategory()
	 * @generated
	 * @ordered
	 */
	protected static final String AUDIT_CATEGORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAuditCategory() <em>Audit Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuditCategory()
	 * @generated
	 * @ordered
	 */
	protected String auditCategory = AUDIT_CATEGORY_EDEFAULT;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final long DURATION_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected long duration = DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getParam0() <em>Param0</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParam0()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAM0_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParam0() <em>Param0</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParam0()
	 * @generated
	 * @ordered
	 */
	protected String param0 = PARAM0_EDEFAULT;

	/**
	 * The default value of the '{@link #getParam1() <em>Param1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParam1()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAM1_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParam1() <em>Param1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParam1()
	 * @generated
	 * @ordered
	 */
	protected String param1 = PARAM1_EDEFAULT;

	/**
	 * The default value of the '{@link #getParam2() <em>Param2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParam2()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAM2_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParam2() <em>Param2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParam2()
	 * @generated
	 * @ordered
	 */
	protected String param2 = PARAM2_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AuditEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EntityPackage.Literals.AUDIT_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimestamp(Date newTimestamp) {
		Date oldTimestamp = timestamp;
		timestamp = newTimestamp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.AUDIT_ENTRY__TIMESTAMP, oldTimestamp, timestamp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUsername(String newUsername) {
		String oldUsername = username;
		username = newUsername;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.AUDIT_ENTRY__USERNAME, oldUsername, username));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIpAddress(String newIpAddress) {
		String oldIpAddress = ipAddress;
		ipAddress = newIpAddress;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.AUDIT_ENTRY__IP_ADDRESS, oldIpAddress, ipAddress));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAuditCategory() {
		return auditCategory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAuditCategory(String newAuditCategory) {
		String oldAuditCategory = auditCategory;
		auditCategory = newAuditCategory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.AUDIT_ENTRY__AUDIT_CATEGORY, oldAuditCategory, auditCategory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDuration(long newDuration) {
		long oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.AUDIT_ENTRY__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParam0() {
		return param0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParam0(String newParam0) {
		String oldParam0 = param0;
		param0 = newParam0;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.AUDIT_ENTRY__PARAM0, oldParam0, param0));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParam1() {
		return param1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParam1(String newParam1) {
		String oldParam1 = param1;
		param1 = newParam1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.AUDIT_ENTRY__PARAM1, oldParam1, param1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParam2() {
		return param2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParam2(String newParam2) {
		String oldParam2 = param2;
		param2 = newParam2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.AUDIT_ENTRY__PARAM2, oldParam2, param2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EntityPackage.AUDIT_ENTRY__TIMESTAMP:
				return getTimestamp();
			case EntityPackage.AUDIT_ENTRY__USERNAME:
				return getUsername();
			case EntityPackage.AUDIT_ENTRY__IP_ADDRESS:
				return getIpAddress();
			case EntityPackage.AUDIT_ENTRY__AUDIT_CATEGORY:
				return getAuditCategory();
			case EntityPackage.AUDIT_ENTRY__DURATION:
				return getDuration();
			case EntityPackage.AUDIT_ENTRY__PARAM0:
				return getParam0();
			case EntityPackage.AUDIT_ENTRY__PARAM1:
				return getParam1();
			case EntityPackage.AUDIT_ENTRY__PARAM2:
				return getParam2();
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
			case EntityPackage.AUDIT_ENTRY__TIMESTAMP:
				setTimestamp((Date)newValue);
				return;
			case EntityPackage.AUDIT_ENTRY__USERNAME:
				setUsername((String)newValue);
				return;
			case EntityPackage.AUDIT_ENTRY__IP_ADDRESS:
				setIpAddress((String)newValue);
				return;
			case EntityPackage.AUDIT_ENTRY__AUDIT_CATEGORY:
				setAuditCategory((String)newValue);
				return;
			case EntityPackage.AUDIT_ENTRY__DURATION:
				setDuration((Long)newValue);
				return;
			case EntityPackage.AUDIT_ENTRY__PARAM0:
				setParam0((String)newValue);
				return;
			case EntityPackage.AUDIT_ENTRY__PARAM1:
				setParam1((String)newValue);
				return;
			case EntityPackage.AUDIT_ENTRY__PARAM2:
				setParam2((String)newValue);
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
			case EntityPackage.AUDIT_ENTRY__TIMESTAMP:
				setTimestamp(TIMESTAMP_EDEFAULT);
				return;
			case EntityPackage.AUDIT_ENTRY__USERNAME:
				setUsername(USERNAME_EDEFAULT);
				return;
			case EntityPackage.AUDIT_ENTRY__IP_ADDRESS:
				setIpAddress(IP_ADDRESS_EDEFAULT);
				return;
			case EntityPackage.AUDIT_ENTRY__AUDIT_CATEGORY:
				setAuditCategory(AUDIT_CATEGORY_EDEFAULT);
				return;
			case EntityPackage.AUDIT_ENTRY__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case EntityPackage.AUDIT_ENTRY__PARAM0:
				setParam0(PARAM0_EDEFAULT);
				return;
			case EntityPackage.AUDIT_ENTRY__PARAM1:
				setParam1(PARAM1_EDEFAULT);
				return;
			case EntityPackage.AUDIT_ENTRY__PARAM2:
				setParam2(PARAM2_EDEFAULT);
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
			case EntityPackage.AUDIT_ENTRY__TIMESTAMP:
				return TIMESTAMP_EDEFAULT == null ? timestamp != null : !TIMESTAMP_EDEFAULT.equals(timestamp);
			case EntityPackage.AUDIT_ENTRY__USERNAME:
				return USERNAME_EDEFAULT == null ? username != null : !USERNAME_EDEFAULT.equals(username);
			case EntityPackage.AUDIT_ENTRY__IP_ADDRESS:
				return IP_ADDRESS_EDEFAULT == null ? ipAddress != null : !IP_ADDRESS_EDEFAULT.equals(ipAddress);
			case EntityPackage.AUDIT_ENTRY__AUDIT_CATEGORY:
				return AUDIT_CATEGORY_EDEFAULT == null ? auditCategory != null : !AUDIT_CATEGORY_EDEFAULT.equals(auditCategory);
			case EntityPackage.AUDIT_ENTRY__DURATION:
				return duration != DURATION_EDEFAULT;
			case EntityPackage.AUDIT_ENTRY__PARAM0:
				return PARAM0_EDEFAULT == null ? param0 != null : !PARAM0_EDEFAULT.equals(param0);
			case EntityPackage.AUDIT_ENTRY__PARAM1:
				return PARAM1_EDEFAULT == null ? param1 != null : !PARAM1_EDEFAULT.equals(param1);
			case EntityPackage.AUDIT_ENTRY__PARAM2:
				return PARAM2_EDEFAULT == null ? param2 != null : !PARAM2_EDEFAULT.equals(param2);
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
		result.append(" (timestamp: ");
		result.append(timestamp);
		result.append(", username: ");
		result.append(username);
		result.append(", ipAddress: ");
		result.append(ipAddress);
		result.append(", auditCategory: ");
		result.append(auditCategory);
		result.append(", duration: ");
		result.append(duration);
		result.append(", param0: ");
		result.append(param0);
		result.append(", param1: ");
		result.append(param1);
		result.append(", param2: ");
		result.append(param2);
		result.append(')');
		return result.toString();
	}

} //AuditEntryImpl
