/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.notation;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.flowerplatform.emf_model.notation.NotationFactory
 * @model kind="package"
 * @generated
 */
public interface NotationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "notation";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.flower-platform.com/xmi/notation_1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "notation";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	NotationPackage eINSTANCE = org.flowerplatform.emf_model.notation.impl.NotationPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.notation.impl.NotationElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.notation.impl.NotationElementImpl
	 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getNotationElement()
	 * @generated
	 */
	int NOTATION_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTATION_ELEMENT__ID = 0;

	/**
	 * The feature id for the '<em><b>Id Before Removal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTATION_ELEMENT__ID_BEFORE_REMOVAL = 1;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTATION_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.notation.impl.ViewImpl <em>View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.notation.impl.ViewImpl
	 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getView()
	 * @generated
	 */
	int VIEW = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__ID = NOTATION_ELEMENT__ID;

	/**
	 * The feature id for the '<em><b>Id Before Removal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__ID_BEFORE_REMOVAL = NOTATION_ELEMENT__ID_BEFORE_REMOVAL;

	/**
	 * The feature id for the '<em><b>View Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__VIEW_TYPE = NOTATION_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Persistent Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__PERSISTENT_CHILDREN = NOTATION_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>View Details</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__VIEW_DETAILS = NOTATION_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__DIAGRAMMABLE_ELEMENT = NOTATION_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_FEATURE_COUNT = NOTATION_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.notation.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.notation.impl.NodeImpl
	 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__ID = VIEW__ID;

	/**
	 * The feature id for the '<em><b>Id Before Removal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__ID_BEFORE_REMOVAL = VIEW__ID_BEFORE_REMOVAL;

	/**
	 * The feature id for the '<em><b>View Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__VIEW_TYPE = VIEW__VIEW_TYPE;

	/**
	 * The feature id for the '<em><b>Persistent Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__PERSISTENT_CHILDREN = VIEW__PERSISTENT_CHILDREN;

	/**
	 * The feature id for the '<em><b>View Details</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__VIEW_DETAILS = VIEW__VIEW_DETAILS;

	/**
	 * The feature id for the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__DIAGRAMMABLE_ELEMENT = VIEW__DIAGRAMMABLE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Layout Constraint</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__LAYOUT_CONSTRAINT = VIEW_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = VIEW_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.notation.impl.DiagramImpl <em>Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.notation.impl.DiagramImpl
	 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getDiagram()
	 * @generated
	 */
	int DIAGRAM = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__ID = VIEW__ID;

	/**
	 * The feature id for the '<em><b>Id Before Removal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__ID_BEFORE_REMOVAL = VIEW__ID_BEFORE_REMOVAL;

	/**
	 * The feature id for the '<em><b>View Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__VIEW_TYPE = VIEW__VIEW_TYPE;

	/**
	 * The feature id for the '<em><b>Persistent Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__PERSISTENT_CHILDREN = VIEW__PERSISTENT_CHILDREN;

	/**
	 * The feature id for the '<em><b>View Details</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__VIEW_DETAILS = VIEW__VIEW_DETAILS;

	/**
	 * The feature id for the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__DIAGRAMMABLE_ELEMENT = VIEW__DIAGRAMMABLE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__NAME = VIEW_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_FEATURE_COUNT = VIEW_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.notation.impl.LayoutConstraintImpl <em>Layout Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.notation.impl.LayoutConstraintImpl
	 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getLayoutConstraint()
	 * @generated
	 */
	int LAYOUT_CONSTRAINT = 4;

	/**
	 * The number of structural features of the '<em>Layout Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYOUT_CONSTRAINT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.notation.impl.LocationImpl <em>Location</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.notation.impl.LocationImpl
	 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__ID = NOTATION_ELEMENT__ID;

	/**
	 * The feature id for the '<em><b>Id Before Removal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__ID_BEFORE_REMOVAL = NOTATION_ELEMENT__ID_BEFORE_REMOVAL;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__X = NOTATION_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__Y = NOTATION_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_FEATURE_COUNT = NOTATION_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.notation.impl.BoundsImpl <em>Bounds</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.notation.impl.BoundsImpl
	 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getBounds()
	 * @generated
	 */
	int BOUNDS = 6;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDS__ID = LOCATION__ID;

	/**
	 * The feature id for the '<em><b>Id Before Removal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDS__ID_BEFORE_REMOVAL = LOCATION__ID_BEFORE_REMOVAL;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDS__X = LOCATION__X;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDS__Y = LOCATION__Y;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDS__WIDTH = LOCATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDS__HEIGHT = LOCATION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Bounds</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDS_FEATURE_COUNT = LOCATION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.notation.impl.NoteImpl <em>Note</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.notation.impl.NoteImpl
	 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getNote()
	 * @generated
	 */
	int NOTE = 7;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Id Before Removal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__ID_BEFORE_REMOVAL = NODE__ID_BEFORE_REMOVAL;

	/**
	 * The feature id for the '<em><b>View Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__VIEW_TYPE = NODE__VIEW_TYPE;

	/**
	 * The feature id for the '<em><b>Persistent Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__PERSISTENT_CHILDREN = NODE__PERSISTENT_CHILDREN;

	/**
	 * The feature id for the '<em><b>View Details</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__VIEW_DETAILS = NODE__VIEW_DETAILS;

	/**
	 * The feature id for the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__DIAGRAMMABLE_ELEMENT = NODE__DIAGRAMMABLE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Layout Constraint</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__LAYOUT_CONSTRAINT = NODE__LAYOUT_CONSTRAINT;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__TEXT = NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Note</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_FEATURE_COUNT = NODE_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.notation.NotationElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.flowerplatform.emf_model.notation.NotationElement
	 * @generated
	 */
	EClass getNotationElement();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.NotationElement#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.flowerplatform.emf_model.notation.NotationElement#getId()
	 * @see #getNotationElement()
	 * @generated
	 */
	EAttribute getNotationElement_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.NotationElement#getIdBeforeRemoval <em>Id Before Removal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id Before Removal</em>'.
	 * @see org.flowerplatform.emf_model.notation.NotationElement#getIdBeforeRemoval()
	 * @see #getNotationElement()
	 * @generated
	 */
	EAttribute getNotationElement_IdBeforeRemoval();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.notation.View <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>View</em>'.
	 * @see org.flowerplatform.emf_model.notation.View
	 * @generated
	 */
	EClass getView();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.View#getViewType <em>View Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>View Type</em>'.
	 * @see org.flowerplatform.emf_model.notation.View#getViewType()
	 * @see #getView()
	 * @generated
	 */
	EAttribute getView_ViewType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.emf_model.notation.View#getPersistentChildren <em>Persistent Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Persistent Children</em>'.
	 * @see org.flowerplatform.emf_model.notation.View#getPersistentChildren()
	 * @see #getView()
	 * @generated
	 */
	EReference getView_PersistentChildren();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.View#getViewDetails <em>View Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>View Details</em>'.
	 * @see org.flowerplatform.emf_model.notation.View#getViewDetails()
	 * @see #getView()
	 * @generated
	 */
	EAttribute getView_ViewDetails();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.emf_model.notation.View#getDiagrammableElement <em>Diagrammable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Diagrammable Element</em>'.
	 * @see org.flowerplatform.emf_model.notation.View#getDiagrammableElement()
	 * @see #getView()
	 * @generated
	 */
	EReference getView_DiagrammableElement();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.notation.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see org.flowerplatform.emf_model.notation.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the containment reference '{@link org.flowerplatform.emf_model.notation.Node#getLayoutConstraint <em>Layout Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Layout Constraint</em>'.
	 * @see org.flowerplatform.emf_model.notation.Node#getLayoutConstraint()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_LayoutConstraint();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.notation.Diagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram</em>'.
	 * @see org.flowerplatform.emf_model.notation.Diagram
	 * @generated
	 */
	EClass getDiagram();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.Diagram#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.emf_model.notation.Diagram#getName()
	 * @see #getDiagram()
	 * @generated
	 */
	EAttribute getDiagram_Name();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.notation.LayoutConstraint <em>Layout Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Layout Constraint</em>'.
	 * @see org.flowerplatform.emf_model.notation.LayoutConstraint
	 * @generated
	 */
	EClass getLayoutConstraint();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.notation.Location <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Location</em>'.
	 * @see org.flowerplatform.emf_model.notation.Location
	 * @generated
	 */
	EClass getLocation();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.Location#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see org.flowerplatform.emf_model.notation.Location#getX()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_X();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.Location#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see org.flowerplatform.emf_model.notation.Location#getY()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Y();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.notation.Bounds <em>Bounds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bounds</em>'.
	 * @see org.flowerplatform.emf_model.notation.Bounds
	 * @generated
	 */
	EClass getBounds();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.Bounds#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see org.flowerplatform.emf_model.notation.Bounds#getWidth()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Width();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.Bounds#getHeight <em>Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see org.flowerplatform.emf_model.notation.Bounds#getHeight()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Height();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.notation.Note <em>Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Note</em>'.
	 * @see org.flowerplatform.emf_model.notation.Note
	 * @generated
	 */
	EClass getNote();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.notation.Note#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.flowerplatform.emf_model.notation.Note#getText()
	 * @see #getNote()
	 * @generated
	 */
	EAttribute getNote_Text();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	NotationFactory getNotationFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.notation.impl.NotationElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.notation.impl.NotationElementImpl
		 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getNotationElement()
		 * @generated
		 */
		EClass NOTATION_ELEMENT = eINSTANCE.getNotationElement();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTATION_ELEMENT__ID = eINSTANCE.getNotationElement_Id();

		/**
		 * The meta object literal for the '<em><b>Id Before Removal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTATION_ELEMENT__ID_BEFORE_REMOVAL = eINSTANCE.getNotationElement_IdBeforeRemoval();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.notation.impl.ViewImpl <em>View</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.notation.impl.ViewImpl
		 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getView()
		 * @generated
		 */
		EClass VIEW = eINSTANCE.getView();

		/**
		 * The meta object literal for the '<em><b>View Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIEW__VIEW_TYPE = eINSTANCE.getView_ViewType();

		/**
		 * The meta object literal for the '<em><b>Persistent Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIEW__PERSISTENT_CHILDREN = eINSTANCE.getView_PersistentChildren();

		/**
		 * The meta object literal for the '<em><b>View Details</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIEW__VIEW_DETAILS = eINSTANCE.getView_ViewDetails();

		/**
		 * The meta object literal for the '<em><b>Diagrammable Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIEW__DIAGRAMMABLE_ELEMENT = eINSTANCE.getView_DiagrammableElement();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.notation.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.notation.impl.NodeImpl
		 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Layout Constraint</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__LAYOUT_CONSTRAINT = eINSTANCE.getNode_LayoutConstraint();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.notation.impl.DiagramImpl <em>Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.notation.impl.DiagramImpl
		 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getDiagram()
		 * @generated
		 */
		EClass DIAGRAM = eINSTANCE.getDiagram();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM__NAME = eINSTANCE.getDiagram_Name();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.notation.impl.LayoutConstraintImpl <em>Layout Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.notation.impl.LayoutConstraintImpl
		 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getLayoutConstraint()
		 * @generated
		 */
		EClass LAYOUT_CONSTRAINT = eINSTANCE.getLayoutConstraint();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.notation.impl.LocationImpl <em>Location</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.notation.impl.LocationImpl
		 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getLocation()
		 * @generated
		 */
		EClass LOCATION = eINSTANCE.getLocation();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__X = eINSTANCE.getLocation_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__Y = eINSTANCE.getLocation_Y();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.notation.impl.BoundsImpl <em>Bounds</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.notation.impl.BoundsImpl
		 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getBounds()
		 * @generated
		 */
		EClass BOUNDS = eINSTANCE.getBounds();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BOUNDS__WIDTH = eINSTANCE.getBounds_Width();

		/**
		 * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BOUNDS__HEIGHT = eINSTANCE.getBounds_Height();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.notation.impl.NoteImpl <em>Note</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.notation.impl.NoteImpl
		 * @see org.flowerplatform.emf_model.notation.impl.NotationPackageImpl#getNote()
		 * @generated
		 */
		EClass NOTE = eINSTANCE.getNote();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTE__TEXT = eINSTANCE.getNote_Text();

	}

} //NotationPackage
