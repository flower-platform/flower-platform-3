package com.crispico.flower.mp.codesync.code.java;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.crispico.flower.mp.codesync.code.AstModelElementAdapter;
import com.crispico.flower.mp.codesync.merge.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Mapped to {@link ASTNode}.
 * 
 * @author Mariana
 */
public abstract class JavaAbstractAstNodeModelAdapter extends AstModelElementAdapter {

	public JavaAbstractAstNodeModelAdapter() {
		super();
		common = new CodeSyncElementFeatureProvider();
	}
	
	@Override
	public boolean hasChildren(Object modelElement) {
		return getChildren(modelElement).size() > 0;
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		return leftOrRightMap.remove(getMatchKey(element));
	}
	
	/**
	 * Must handle all the containment features provided by <code>common</code>.
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			return getChildren(element);
		}
		
		// handle modifiers here to avoid using the same code in multiple adapters
		if (AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers().equals(feature)) {
			if (element instanceof BodyDeclaration) {
				return ((BodyDeclaration) element).modifiers();
			}
			if (element instanceof SingleVariableDeclaration) {
				return ((SingleVariableDeclaration) element).modifiers();
			}
			return Collections.emptyList();
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Must handle all the features provided by <code>common</code>, except for containment features.
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation().equals(feature)) {
			return getJavaDoc(element);
		}
		return null;
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation().equals(feature)) {
			setJavaDoc(element, value);
		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		if (AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers().equals(feature)) {
			if (!(element instanceof BodyDeclaration || element instanceof SingleVariableDeclaration)) {
				return null;
			} else {
				IExtendedModifier extendedModifier = null;
				
				if (correspondingChild instanceof com.crispico.flower.mp.model.astcache.code.Modifier) {
					ASTNode parent = (ASTNode) element;
					AST ast = parent.getAST();
					com.crispico.flower.mp.model.astcache.code.Modifier modifier = 
							(com.crispico.flower.mp.model.astcache.code.Modifier) correspondingChild;
					
					extendedModifier = ast.newModifier(Modifier.ModifierKeyword.fromFlagValue(modifier.getType()));
					ChildListPropertyDescriptor descriptor = parent instanceof BodyDeclaration ? 
							((BodyDeclaration) parent).getModifiersProperty() :
							SingleVariableDeclaration.MODIFIERS2_PROPERTY;
					if (parent instanceof BodyDeclaration) {
						((BodyDeclaration) parent).modifiers().add(extendedModifier);
					} else {
						((SingleVariableDeclaration) parent).modifiers().add(extendedModifier);
					}
				}
				
				if (correspondingChild instanceof com.crispico.flower.mp.model.astcache.code.Annotation) {
					ASTNode parent = (ASTNode) element;
					AST ast = parent.getAST();
					com.crispico.flower.mp.model.astcache.code.Annotation annotation = 
							(com.crispico.flower.mp.model.astcache.code.Annotation) correspondingChild;
					if (annotation.getValues().size() == 0) {
						MarkerAnnotation markerAnnotation = ast.newMarkerAnnotation();
						extendedModifier = markerAnnotation;
					}
					if (annotation.getValues().size() == 1) {
						SingleMemberAnnotation singleMemberAnnotation = ast.newSingleMemberAnnotation();
						extendedModifier = singleMemberAnnotation;
					} else {
						NormalAnnotation normalAnnotation = ast.newNormalAnnotation();
						extendedModifier = normalAnnotation;
					}
					ChildListPropertyDescriptor descriptor = parent instanceof BodyDeclaration ? 
							((BodyDeclaration) parent).getModifiersProperty() :
							SingleVariableDeclaration.MODIFIERS2_PROPERTY;
					if (parent instanceof BodyDeclaration) {
						((BodyDeclaration) parent).modifiers().add(extendedModifier);
					} else {
						((SingleVariableDeclaration) parent).modifiers().add(extendedModifier);
					}
				}
				return extendedModifier;
			}
		}
		
		return null;
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		((ASTNode) child).delete();
	}

	@Override
	abstract public List<?> getChildren(Object modelElement);

	@Override
	public String getLabel(Object modelElement) {
		return (String) getMatchKey(modelElement);
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}
	
	@Override
	public boolean save(Object element) {
		// nothing to do, the changes to the AST will be saved when the file is saved
		return false;
	}
	
	@Override
	public boolean discard(Object element) {
		// nothing to do, the changes to the AST will be discarded when the file is discarded
		return false;
	}

	protected Object getJavaDoc(Object element) {
		if (element instanceof BodyDeclaration) {
			BodyDeclaration node = (BodyDeclaration) element;
			if (node.getJavadoc() != null) {
				String docComment = new String();
				for (Object o : node.getJavadoc().tags()) {
					TagElement tag = (TagElement) o;
					String tagName = tag.getTagName();
					if (tagName != null) {
						docComment += tag.getTagName();
					}
					for (Object o2 : tag.fragments()) {
						TextElement text = (TextElement) o2;
						docComment += text.getText();
					}
					docComment += "\n";
				}
				docComment = docComment.trim();
				return docComment;
			}
		}
		return null;
	}
	
	protected void setJavaDoc(Object element, Object docComment) {
		if (element instanceof BodyDeclaration) {
			BodyDeclaration node = (BodyDeclaration) element;
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
			parser.setSource(("/** " + docComment + "*/ int x;").toCharArray());
			TypeDeclaration type = (TypeDeclaration) parser.createAST(null);
			BodyDeclaration x = (BodyDeclaration) type.bodyDeclarations().get(0);
			Javadoc javadoc = x.getJavadoc();
			node.setJavadoc((Javadoc) ASTNode.copySubtree(node.getAST(), javadoc));
		}
	}
	
	/**
	 * Creates an {@link Expression} from the given string, owned by the given AST. 
	 */
	protected Expression getExpressionFromString(AST ast, String expression) {
		if (expression == null) {
			return null;
		}
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_EXPRESSION);
		parser.setSource(expression.toCharArray());
		ASTNode node = parser.createAST(null);
		return (Expression) ASTNode.copySubtree(ast, node);
	}
	
	protected String getStringFromExpression(Expression expression) {
		if (expression == null) {
			return null;
		}
		return expression.toString();
	}
	
	/**
	 * Creates a {@link Type} from the given name, owned by the given AST.
	 */
	protected Type getTypeFromString(AST ast, String name) {
		if (name == null) {
			return ast.newPrimitiveType(PrimitiveType.VOID);
		}
		PrimitiveType.Code primitiveTypeCode = PrimitiveType.toCode(name);
		if (primitiveTypeCode != null) {
			return ast.newPrimitiveType(primitiveTypeCode);
		}
		
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource((name + " a;").toCharArray());
		
		Block block = (Block) parser.createAST(null);
		VariableDeclarationStatement declaration = (VariableDeclarationStatement) block.statements().get(0);
		return (Type) ASTNode.copySubtree(ast, declaration.getType());
	}
	
	protected String getStringFromType(Type type) {
		if (type == null) {
			return null;
		}
		return type.toString();
	}
	
}
