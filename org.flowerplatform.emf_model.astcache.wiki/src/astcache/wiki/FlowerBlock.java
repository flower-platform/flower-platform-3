/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package astcache.wiki;

import com.crispico.flower.mp.model.codesync.AstCacheElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Flower Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link astcache.wiki.FlowerBlock#getContent <em>Content</em>}</li>
 *   <li>{@link astcache.wiki.FlowerBlock#getLineStart <em>Line Start</em>}</li>
 *   <li>{@link astcache.wiki.FlowerBlock#getLineEnd <em>Line End</em>}</li>
 *   <li>{@link astcache.wiki.FlowerBlock#isConflict <em>Conflict</em>}</li>
 * </ul>
 * </p>
 *
 * @see astcache.wiki.WikiPackage#getFlowerBlock()
 * @model
 * @generated
 */
public interface FlowerBlock extends AstCacheElement {
	/**
	 * Returns the value of the '<em><b>Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content</em>' attribute.
	 * @see #setContent(String)
	 * @see astcache.wiki.WikiPackage#getFlowerBlock_Content()
	 * @model transient="true"
	 * @generated
	 */
	String getContent();

	/**
	 * Sets the value of the '{@link astcache.wiki.FlowerBlock#getContent <em>Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content</em>' attribute.
	 * @see #getContent()
	 * @generated
	 */
	void setContent(String value);

	/**
	 * Returns the value of the '<em><b>Line Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Line Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Line Start</em>' attribute.
	 * @see #setLineStart(int)
	 * @see astcache.wiki.WikiPackage#getFlowerBlock_LineStart()
	 * @model transient="true"
	 * @generated
	 */
	int getLineStart();

	/**
	 * Sets the value of the '{@link astcache.wiki.FlowerBlock#getLineStart <em>Line Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Start</em>' attribute.
	 * @see #getLineStart()
	 * @generated
	 */
	void setLineStart(int value);

	/**
	 * Returns the value of the '<em><b>Line End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Line End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Line End</em>' attribute.
	 * @see #setLineEnd(int)
	 * @see astcache.wiki.WikiPackage#getFlowerBlock_LineEnd()
	 * @model transient="true"
	 * @generated
	 */
	int getLineEnd();

	/**
	 * Sets the value of the '{@link astcache.wiki.FlowerBlock#getLineEnd <em>Line End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line End</em>' attribute.
	 * @see #getLineEnd()
	 * @generated
	 */
	void setLineEnd(int value);

	/**
	 * Returns the value of the '<em><b>Conflict</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conflict</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conflict</em>' attribute.
	 * @see #setConflict(boolean)
	 * @see astcache.wiki.WikiPackage#getFlowerBlock_Conflict()
	 * @model transient="true"
	 * @generated
	 */
	boolean isConflict();

	/**
	 * Sets the value of the '{@link astcache.wiki.FlowerBlock#isConflict <em>Conflict</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Conflict</em>' attribute.
	 * @see #isConflict()
	 * @generated
	 */
	void setConflict(boolean value);

} // FlowerBlock
