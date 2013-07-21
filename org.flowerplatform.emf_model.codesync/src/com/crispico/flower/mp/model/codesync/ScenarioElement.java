/**
 */
package com.crispico.flower.mp.model.codesync;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getNumber <em>Number</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getInteraction <em>Interaction</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getComment <em>Comment</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getScenarioElement()
 * @model
 * @generated
 */
public interface ScenarioElement extends CodeSyncElement {
	/**
	 * Returns the value of the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number</em>' attribute.
	 * @see #setNumber(String)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getScenarioElement_Number()
	 * @model
	 * @generated
	 */
	String getNumber();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getNumber <em>Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number</em>' attribute.
	 * @see #getNumber()
	 * @generated
	 */
	void setNumber(String value);

	/**
	 * Returns the value of the '<em><b>Interaction</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interaction</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interaction</em>' reference.
	 * @see #setInteraction(CodeSyncElement)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getScenarioElement_Interaction()
	 * @model
	 * @generated
	 */
	CodeSyncElement getInteraction();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getInteraction <em>Interaction</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interaction</em>' reference.
	 * @see #getInteraction()
	 * @generated
	 */
	void setInteraction(CodeSyncElement value);

	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getScenarioElement_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

} // ScenarioElement
