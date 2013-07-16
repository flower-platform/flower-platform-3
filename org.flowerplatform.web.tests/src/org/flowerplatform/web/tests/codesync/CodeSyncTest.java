package org.flowerplatform.web.tests.codesync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.model.java.JavaDragOnDiagramHandler;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.security.service.OrganizationService;
import org.flowerplatform.web.temp.GeneralService;
import org.flowerplatform.web.tests.TestUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.Match.MatchType;
import com.crispico.flower.mp.codesync.base.action.ActionResult;
import com.crispico.flower.mp.codesync.base.action.ActionSynchronize;
import com.crispico.flower.mp.codesync.base.action.DiffAction;
import com.crispico.flower.mp.codesync.base.action.MatchActionAddLeftToRight;
import com.crispico.flower.mp.codesync.base.action.MatchActionAddRightToLeft;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveLeft;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveLeftRight;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveRight;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.code.java.CodeSyncCodeJavaPlugin;
import com.crispico.flower.mp.codesync.merge.CodeSyncMergePlugin;
import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.Attribute;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.Operation;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.astcache.code.TypedElement;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.FeatureChange;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

/**
 * @author Mariana
 */
public class CodeSyncTest {

	public static final String PROJECT = "codesync";
	public static final String INITIAL = "initial";
	public static final String CACHE_DELETED = "cache_deleted";
	public static final String MODIFIED_NO_CONFLICTS = "modified_no_conflicts";
	public static final String MODIFIED_CONFLICTS = "modified_conflicts";
	public static final String MODIFIED_NO_CONFLICTS_PERFORM_SYNC = "modified_no_conflicts_perform_sync";
	
	public static final String DIR = TestUtil.ECLIPSE_DEPENDENT_FILES_DIR + "/" + PROJECT + "/";
	
	public static final String LINK = "link-to-project/";
	
	public static final String SOURCE_FILE = "Test.java";
	public static final String MODEL_FILE = "CSE.notation";
	
	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();
	
	@BeforeClass
	public static void setUpBeforeClass() {
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(CodeSyncEditorStatefulService.SERVICE_ID, new CodeSyncEditorStatefulService());
		Class cls = JavaDragOnDiagramHandler.class; // force activation of code.java plugin.
		TestUtil.copyFilesAndCreateProject(new ServiceInvocationContext(communicationChannel), DIR + TestUtil.INITIAL_TO_BE_COPIED, PROJECT);
	}
	
	@Before
	public void before() {
		IProject project = getProject();
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		service.cancelSelectedActions(project.getFullPath().toString());
	}
	
	@Test
	public void testMatchWhenSync() {
		CodeSyncCodePlugin.getInstance().addSrcDir(INITIAL);
		String fullyQualifiedName = "/" + PROJECT + "/" + INITIAL + "/" + SOURCE_FILE;
		CodeSyncCodePlugin.getInstance().CSE_MAPPING_FILE_LOCATION = LINK + INITIAL + "/CSE.notation";
		CodeSyncCodePlugin.getInstance().ACE_FILE_LOCATION = LINK + INITIAL + "/ACE.notation";
		
		IProject project = getProject();
		
		IFile file = getFile(fullyQualifiedName);
//		CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(file.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, file, CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel, false);
		
		Match match = getMatch();
		
		assertEquals(1, match.getSubMatches().size());
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		service.synchronize(new StatefulServiceInvocationContext(communicationChannel), project.getFullPath().toString());
		service.applySelectedActions(new StatefulServiceInvocationContext(communicationChannel), project.getFullPath().toString());
		service.cancelSelectedActions(project.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, getFile(fullyQualifiedName), CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel, false);
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),			// src
					new Pair(MatchType._3MATCH, 1),				// Test.java
						new Pair(MatchType._3MATCH, 2),				// @Deprecated public class Test
						
							new Pair(MatchType._3MATCH, 3),				// @OneToMany(mappedBy="test") public int test(String st)
								new Pair(MatchType._3MATCH, 4),				// public
								new Pair(MatchType._3MATCH, 4),				// @OneToMany
									new Pair(MatchType._3MATCH, 5),				// mappedBy = test
								new Pair(MatchType._3MATCH, 4),				// String st
							
							new Pair(MatchType._3MATCH, 3),				// @OverrideAnnotationOf(mappedBy="test") public static Test getTest()
								new Pair(MatchType._3MATCH, 4),				// public	
								new Pair(MatchType._3MATCH, 4),				// @OverrideAnnotationOf
									new Pair(MatchType._3MATCH, 5),				// x+y
								new Pair(MatchType._3MATCH, 4),				// static
								
							new Pair(MatchType._3MATCH, 3),				// private int y
								new Pair(MatchType._3MATCH, 4),				// private
								
							new Pair(MatchType._3MATCH, 3), 				// private int x
								new Pair(MatchType._3MATCH, 4),				// private
								
							new Pair(MatchType._3MATCH, 3),				// @Deprecated
							new Pair(MatchType._3MATCH, 3),				// public
							new Pair(MatchType._3MATCH, 3),					// ITest
				};
		testMatchTree(typeList, true);
	}
	
	@Test
	public void testMatchCacheDeleted() {
		CodeSyncCodePlugin.getInstance().addSrcDir(CACHE_DELETED);
		String fullyQualifiedName = "/" + PROJECT + "/" + CACHE_DELETED + "/" + SOURCE_FILE;
		CodeSyncCodePlugin.getInstance().CSE_MAPPING_FILE_LOCATION = LINK + CACHE_DELETED + "/CSE.notation";
		CodeSyncCodePlugin.getInstance().ACE_FILE_LOCATION = LINK + CACHE_DELETED + "/ACE.notation";
		
		IFile file = getFile(fullyQualifiedName);
//		CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(file.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(getProject(), file, CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel, false);
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),				// src
					new Pair(MatchType._3MATCH, 1),				// Test.java
						new Pair(MatchType._3MATCH, 2),				// @Deprecated public class Test
						
							new Pair(MatchType._3MATCH, 3),				// @OneToMany(mappedBy="test") public int test(String st)
								new Pair(MatchType._3MATCH, 4),				// public
								new Pair(MatchType._3MATCH, 4),				// @OneToMany
									new Pair(MatchType._3MATCH, 5),				// mappedBy = test
								new Pair(MatchType._3MATCH, 4),				// String st
							
							new Pair(MatchType._3MATCH, 3),				// @OverrideAnnotationOf(mappedBy="test") public static Test getTest()
								new Pair(MatchType._3MATCH, 4),				// public
								new Pair(MatchType._3MATCH, 4),				// @OverrideAnnotationOf
									new Pair(MatchType._3MATCH, 5),				// x+y
								new Pair(MatchType._3MATCH, 4),				// static
								
							new Pair(MatchType._3MATCH, 3),				// private int y
								new Pair(MatchType._3MATCH, 4),				// private
								
							new Pair(MatchType._3MATCH, 3), 				// private int x
								new Pair(MatchType._3MATCH, 4),				// private
								
							new Pair(MatchType._3MATCH, 3),				// @Deprecated
							new Pair(MatchType._3MATCH, 3),				// public
							new Pair(MatchType._3MATCH, 3),					// ITest
				};
		testMatchTree(typeList, true);
	}
	
	@Test
	public void testMatchNoConflicts() {
		CodeSyncCodePlugin.getInstance().addSrcDir(MODIFIED_NO_CONFLICTS);
		String fullyQualifiedName = "/" + PROJECT + "/" + MODIFIED_NO_CONFLICTS + "/" + SOURCE_FILE;
		CodeSyncCodePlugin.getInstance().CSE_MAPPING_FILE_LOCATION = LINK + MODIFIED_NO_CONFLICTS + "/CSE.notation";
		CodeSyncCodePlugin.getInstance().ACE_FILE_LOCATION = LINK + MODIFIED_NO_CONFLICTS + "/ACE.notation";

		IFile file = getFile(fullyQualifiedName);
//		CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(file.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(getProject(), file, CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel, false);
		
//		// create FeatureChanges to simulate model modifications
//		IProject project = getProject(PROJECT);
//		CodeSyncElement srcDir = CodeSyncCodePlugin.getInstance().getSrcDir(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project), MODIFIED_NO_CONFLICTS);
//		FeatureChange featureChange = null;
//		
//		// change superCls, superInterfaces
//		CodeSyncElement Test = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test"});
//		com.crispico.flower.mp.model.astcache.code.Class cls = (com.crispico.flower.mp.model.astcache.code.Class) Test.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(Test, AstCacheCodePackage.eINSTANCE.getClass_SuperClasses(), featureChange);
//		featureChange.setOldValue(cls.getSuperClasses());
//		List<String> superClasses = Collections.singletonList("SuperClassFromModel");
//		featureChange.setNewValue(superClasses);
////		CodeSyncCodePlugin.getInstance().getUtils().setSuperClasses(cls, superClasses);
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(Test, AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces(), featureChange);
//		featureChange.setOldValue(cls.getSuperInterfaces());
//		List<String> superInterfaces = new ArrayList<String>();
//		superInterfaces.add("IFromModel");
//		superInterfaces.add("ITest");
//		featureChange.setNewValue(superInterfaces);
////		CodeSyncCodePlugin.getInstance().getUtils().setSuperInterfaces(cls, superInterfaces);
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(Test, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//		featureChange.setOldValue(cls.getModifiers());
//		Modifier modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		List<ExtendedModifier> modifiers = new BasicEList<ExtendedModifier>();
//		modifier.setType(1); // public
//		modifiers.add(modifier);
//		Annotation a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//		a.setName("Deprecated");
//		AnnotationValue value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("_");
//		value.setValue("test");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		modifiers.add(a);
//		featureChange.setNewValue(modifiers);
////		CodeSyncCodePlugin.getInstance().getUtils().setModifiers(cls, modifiers);
//		
//		// add class
//		CodeSyncElement InternalCls = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
//		InternalCls.setAdded(true);
//		InternalCls.setName("InternalClassFromModel");
//		InternalCls.setType("Class");
//		com.crispico.flower.mp.model.astcache.code.Class internalCls = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createClass();
//		InternalCls.setAstCacheElement(internalCls);
//		CodeSyncCodePlugin.getInstance().getUtils().addChild(Test, InternalCls);
//		CodeSyncCodePlugin.getInstance().getUtils().addToResource(project, internalCls);
//		
//		// change typed element type
//		CodeSyncElement x = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "x"});
//		TypedElement attr = (TypedElement) x.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(x, AstCacheCodePackage.eINSTANCE.getTypedElement_Type(), featureChange);
//		featureChange.setOldValue(attr.getType());
//		featureChange.setNewValue("Test");
////		attr.setType("Test");
//		
//		// change modifiers + annotations
//		CodeSyncElement test = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "test(String)"});
//		Operation op = (Operation) test.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(test, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//		featureChange.setOldValue(op.getModifiers());
//		modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		modifiers = new BasicEList<ExtendedModifier>();
//		modifier.setType(2); // private
//		modifiers.add(modifier);
//		a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//		a.setName("OneToMany");
//		value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("mappedBy");
//		value.setValue("\"modified_by_model\"");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("orphanRemoval");
//		value.setValue("true");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		modifiers.add(a);
//		featureChange.setNewValue(modifiers);
////		CodeSyncCodePlugin.getInstance().getUtils().setModifiers(op, modifiers);
//		
//		// change parameters
//		CodeSyncElement getTest = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "getTest()"});
//		op = (Operation) getTest.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(getTest, AstCacheCodePackage.eINSTANCE.getOperation_Parameters(), featureChange);
//		featureChange.setOldValue(op.getParameters());
//		Parameter param = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createParameter();
//		param.setName("a");
//		param.setType("int");
//		List<Parameter> params = new BasicEList<Parameter>();
//		params.add(param);
//		featureChange.setNewValue(params);
////		CodeSyncCodePlugin.getInstance().getUtils().setParams(op, params);
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(getTest, AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation(), featureChange);
//		featureChange.setOldValue(op.getDocumentation());
//		String doc = "modified from model\n@author test";
//		featureChange.setNewValue(doc);
////		op.setDocumentation(doc);
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(getTest, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//		featureChange.setOldValue(op.getModifiers());
//		modifiers = new BasicEList<ExtendedModifier>();
//		modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		modifier.setType(1); // public
//		modifiers.add(modifier);
//		a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//		a.setName("OverrideAnnotationOf");
//		value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("value1");
//		value.setValue("true");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("value2");
//		value.setValue("false");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		modifiers.add(a);
//		featureChange.setNewValue(modifiers);
////		CodeSyncCodePlugin.getInstance().getUtils().setModifiers(op, modifiers);
//		
//		// add element
//		CodeSyncElement newCSE = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
//		newCSE.setAdded(true);
//		newCSE.setName("t");
//		newCSE.setType("Attribute");
//		Attribute t = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAttribute();
//		t.setDocumentation("doc from model @author test");
//		t.setType("int");
//		modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		modifier.setType(1);
//		CodeSyncCodePlugin.getInstance().getUtils().setModifiers(t, Collections.singletonList((ExtendedModifier) modifier));
//		newCSE.setAstCacheElement(t);
//		CodeSyncCodePlugin.getInstance().getUtils().addChild(Test, newCSE);
//		CodeSyncCodePlugin.getInstance().getUtils().addToResource(project, t);
//		
//		// remove element
//		CodeSyncElement y = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "y"});
//		y.setDeleted(true);
//		
//		CodeSyncMergePlugin.getInstance().saveResource(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project));
//		CodeSyncMergePlugin.getInstance().discardResource(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project));
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		service.unsubscribeAllClientsForcefully(project.getFullPath().toString(), false);
//		CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, getFile(fullyQualifiedName), CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel);
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),					// src
					new Pair(MatchType._3MATCH, 1),					// Test.java
						new Pair(MatchType._3MATCH, 2),					// @Deprecated public class Test
						
							new Pair(MatchType._3MATCH, 3),					// @OneToMany(mappedBy="test") public int test(String st) {
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// removed public from model
								new Pair(MatchType._3MATCH, 4),					// @OneToMany
									new Pair(MatchType._3MATCH, 5),					// mappedBy
									new Pair(MatchType._1MATCH_LEFT, 5),				// orphanRemoval
								new Pair(MatchType._1MATCH_LEFT, 4),				// added private to model
								new Pair(MatchType._1MATCH_RIGHT, 4),				// added static to source
								new Pair(MatchType._3MATCH, 4),					// String st
									new Pair(MatchType._1MATCH_RIGHT, 5),				// final (added to source)
								
							new Pair(MatchType._3MATCH, 3),					// @OverrideAnnotationOf(x+y) public static Test getTest() {
								new Pair(MatchType._3MATCH, 4),					// public
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// @OverrideAnnotationOf(x+y) (removed from model)
									new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 5),		// x+y
								new Pair(MatchType._1MATCH_ANCESTOR, 4),			// removed static from model and source
								new Pair(MatchType._1MATCH_LEFT, 4),				// @overrideAnnotationOf(valu1=true, value2=false) (added to model)
									new Pair(MatchType._1MATCH_LEFT, 5),				// value1 (added to model)
									new Pair(MatchType._1MATCH_LEFT, 5),				// value2 (added to model)
								new Pair(MatchType._1MATCH_LEFT, 4),				// added param to model
								
							new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 3),		// private int y (removed from model)
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// private
								
							new Pair(MatchType._3MATCH, 3),					// private Test x <> private int x
								new Pair(MatchType._3MATCH, 4),					// private
								
							new Pair(MatchType._1MATCH_LEFT, 3),				// private int t (added to model)
								new Pair(MatchType._1MATCH_LEFT, 4),				// private
								
							new Pair(MatchType._1MATCH_LEFT, 3),				// class InternalClassFromModel
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public enum ActionType
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public Object diffAction
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// public
								new Pair(MatchType._1MATCH_RIGHT, 4),				// private ActionType(Object action)
									new Pair(MatchType._1MATCH_RIGHT, 5),				// Object action
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// private
								new Pair(MatchType._1MATCH_RIGHT, 4),				// ACTION_TYPE_COPY_LEFT_RIGHT(new Test())
									new Pair(MatchType._1MATCH_RIGHT, 5),				// new Test()
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// ACTION_TYPE_COPY_RIGHT_LEFT(new InternalClsFromSource());
									new Pair(MatchType._1MATCH_RIGHT, 5),				// new InternalClsFromSource()
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public class InternalClsFromSource
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public int x
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// public
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public @interface AnnotationTest
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// boolean value1() default true
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// boolean value2() default false
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// private int z (added to source)
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// private
								
							new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 3),		// @Deprecated (removed from model)
							new Pair(MatchType._3MATCH, 3),					// public
							new Pair(MatchType._1MATCH_LEFT, 3),				// @Deprecated(test) (added to model)
								new Pair(MatchType._1MATCH_LEFT, 4),				// test
							new Pair(MatchType._2MATCH_ANCESTOR_LEFT, 3),		// ITest
							new Pair(MatchType._1MATCH_LEFT, 3),				// IFromModel
							new Pair(MatchType._1MATCH_RIGHT, 3),				// IFromSource
		};
		Match match = testMatchTree(typeList, false);
		assertFalse("Conflicts not expected!", match.isChildrenConflict());
	}
	
	@Test
	public void testMatchNoConflictsAndPerformSync() {
		CodeSyncCodePlugin.getInstance().addSrcDir(MODIFIED_NO_CONFLICTS_PERFORM_SYNC);
		String fullyQualifiedName = "/" + PROJECT + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/" + SOURCE_FILE;
		String cseLocation = getProject().getFullPath().toString() + "/" + LINK + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/CSE.notation";
		String aceLocation = getProject().getFullPath().toString() + "/" + LINK + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/ACE.notation";
		CodeSyncCodePlugin.getInstance().CSE_MAPPING_FILE_LOCATION = LINK + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/CSE.notation";
		CodeSyncCodePlugin.getInstance().ACE_FILE_LOCATION = LINK + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/ACE.notation";

		IProject project = getProject();
		
		IFile file = getFile(fullyQualifiedName);
//		CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(file.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, file, CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel, false);
		
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		service.synchronize(new StatefulServiceInvocationContext(communicationChannel), project.getFullPath().toString());
		service.applySelectedActions(new StatefulServiceInvocationContext(communicationChannel), project.getFullPath().toString());
		
		String expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/" + SOURCE_FILE);
		String actual = TestUtil.readFile(TestUtil.getWorkspaceResourceAbsolutePath(file.getFullPath().toString()));
		assertEquals("Source not in sync", expected, actual);
		
		expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/CSE.notation");
		actual = TestUtil.readFile(TestUtil.getWorkspaceResourceAbsolutePath(cseLocation));
		assertEquals("CSE not in sync", expected, actual);
		
		expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/ACE.notation");
		actual = TestUtil.readFile(TestUtil.getWorkspaceResourceAbsolutePath(aceLocation));
		assertEquals("ACE not in sync", expected, actual);
		
//		Resource expectedCSE = CodeSyncMergePlugin.getInstance().getResource(null, new File(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/CSE.notation"));
//		Resource actualCSE = CodeSyncMergePlugin.getInstance().getResource(null, project.getFile(CodeSyncCodePlugin.getInstance().CSE_MAPPING_FILE_LOCATION));
//		
//		assertTrue(CodeSyncCodePlugin.getInstance().getUtils().testEquality(expectedCSE, actualCSE, MODIFIED_NO_CONFLICTS_PERFORM_SYNC));
	}
	
	@Test
	public void testMatchConflicts() {
		CodeSyncCodePlugin.getInstance().addSrcDir(MODIFIED_CONFLICTS);
		String fullyQualifiedName = "/" + PROJECT + "/" + MODIFIED_CONFLICTS + "/" + SOURCE_FILE;
		CodeSyncCodePlugin.getInstance().CSE_MAPPING_FILE_LOCATION = LINK + MODIFIED_CONFLICTS + "/CSE.notation";
		CodeSyncCodePlugin.getInstance().ACE_FILE_LOCATION = LINK + MODIFIED_CONFLICTS + "/ACE.notation";

		IFile file = getFile(fullyQualifiedName);
//		CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(file.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(getProject(), file, CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel, false);
		
//		// create FeatureChanges to simulate model modifications
//		IProject project = getProject(PROJECT);
//		CodeSyncElement srcDir = CodeSyncCodePlugin.getInstance().getSrcDir(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project), MODIFIED_CONFLICTS);
//		FeatureChange featureChange = null;
//		
//		// change super class
//		CodeSyncElement Test = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test"});
//		com.crispico.flower.mp.model.astcache.code.Class cls = (com.crispico.flower.mp.model.astcache.code.Class) Test.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(Test, AstCacheCodePackage.eINSTANCE.getClass_SuperClasses(), featureChange);
//		featureChange.setOldValue(cls.getSuperClasses());
//		List<String> superClasses = Collections.singletonList("SuperClassFromModel");
//		featureChange.setNewValue(superClasses);
//		// change typed element type
//		CodeSyncElement x = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "x"});
//		TypedElement attr = (TypedElement) x.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(x, AstCacheCodePackage.eINSTANCE.getTypedElement_Type(), featureChange);
//		featureChange.setOldValue(attr.getType());
//		featureChange.setNewValue("Test");
//		// change typed element type
//		CodeSyncElement y = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "y"});
//		TypedElement attry = (TypedElement) y.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(y, AstCacheCodePackage.eINSTANCE.getTypedElement_Type(), featureChange);
//		featureChange.setOldValue(attry.getType());
//		featureChange.setNewValue("Test");
//		// change modifiers + annotations
//		CodeSyncElement test = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "test(String)"});
//		Operation op = (Operation) test.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(test, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//		featureChange.setOldValue(op.getModifiers());
//		Modifier modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		List<ExtendedModifier> modifiers = new BasicEList<ExtendedModifier>();
//		modifier.setType(1); // public
//		modifiers.add(modifier);
//		Annotation a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//		a.setName("OneToMany");
//		AnnotationValue value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("mappedBy");
//		value.setValue("\"modified_by_model\"");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		modifiers.add(a);
//		featureChange.setNewValue(modifiers);
//		
//		CodeSyncMergePlugin.getInstance().saveResource(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project));
//		CodeSyncMergePlugin.getInstance().discardResource(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project));
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		service.unsubscribeAllClientsForcefully(project.getFullPath().toString(), false);
//		CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, getFile(fullyQualifiedName), CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel);
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),				// src
					new Pair(MatchType._3MATCH, 1),				// Test.java
						new Pair(MatchType._3MATCH, 2),				// @Deprecated public class Test
						
							new Pair(MatchType._3MATCH, 3),				// @OneToMany(mappedBy="test") public int test(String st)
								new Pair(MatchType._3MATCH, 4),				// public
								new Pair(MatchType._3MATCH, 4),				// @OneToMany
									new Pair(MatchType._3MATCH, 5),				// mappedBy = test
								new Pair(MatchType._3MATCH, 4),				// String st
							
							new Pair(MatchType._3MATCH, 3),				// @OverrideAnnotationOf(mappedBy="test") public static Test getTest()
								new Pair(MatchType._3MATCH, 4),				// public
								new Pair(MatchType._3MATCH, 4),				// @OverrideAnnotationOf
									new Pair(MatchType._3MATCH, 5),				// x+y
								new Pair(MatchType._3MATCH, 4),				// static
								
							new Pair(MatchType._3MATCH, 3),				// private int y
								new Pair(MatchType._3MATCH, 4),				// private
								
							new Pair(MatchType._3MATCH, 3), 				// private int x
								new Pair(MatchType._3MATCH, 4),				// private
								
							new Pair(MatchType._3MATCH, 3),				// @Deprecated
							new Pair(MatchType._3MATCH, 3),				// public
							new Pair(MatchType._3MATCH, 3)				// ITest
				};
		boolean[] conflicts = {
				false,
					false,
						true,			// superClass changed on model and source
						
							false,
								false,
								false,
									true,	// annotation value changed on model and source
								false,
							
						false,
							false,
								false,
							false,
							false,
							
						true,			// type changed on model and source
							false,
							
						false,			
							false,
							
						false,
						false,
							false
			};
		assertTrue("Conflicts expected!", getMatch().isChildrenConflict());
		testMatchTree(typeList, false);
		testConflicts(conflicts);
	}
	
	@Test
	public void testValueAsString() {
		assertTrue(CodeSyncCodePlugin.getInstance().testValueAsString());
	}
	
	/////////////////////////////
	// Utils
	/////////////////////////////
	
	private IProject getProject() {
		String absolutePath = CommonPlugin.getInstance().getWorkspaceRoot().getAbsolutePath() + "/org/ws_trunk/" + PROJECT;
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(absolutePath));
		return resource.getProject();
	}
	
	private IFile getFile(String path) {
		String absolutePath = CommonPlugin.getInstance().getWorkspaceRoot().getAbsolutePath() + "/org/ws_trunk/" + path;
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(absolutePath));
		return (IFile) resource;
	}
	
	private Pair[] typeList;
	private boolean[] conflicts;
	private int i;
	
	private Match getMatch() {
		IProject project = getProject();
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		CodeSyncEditableResource er = (CodeSyncEditableResource) service.getEditableResource(project.getFullPath().toString());
		assertNotNull("Editable resource for project " + project + " was not created", er);
		return er.getMatch();
	}
	
	private void testConflicts(boolean[] conflicts) {
		Match match = getMatch();
		i = 0;
		this.conflicts = conflicts;
		checkTree_conflict(match);
	}
	
	private Match testMatchTree(Pair[] typeList, boolean checkNoDiffs) {
		Match match = getMatch();
		assertNotNull("Match was not created", match);
		i = 0;
		this.typeList = typeList;
		checkTree_type(match, checkNoDiffs, 0);
		assertEquals(typeList.length, i + 1);
		return match;
	}
	
	private void checkTree_type(Match parentMatch, boolean checkNoDiffs, int level) {
		checkMatch_type(parentMatch, level);
		if (checkNoDiffs) {
			assertEquals("No diffs expected", 0, parentMatch.getDiffs().size());
		}
		for (Match subMatch : parentMatch.getSubMatches()) {
			i++;
			checkTree_type(subMatch, checkNoDiffs, level + 1);
		}
	}
	
	private void checkMatch_type(Match match, int level) {
		assertEquals("Wrong match at index " + i, typeList[i].type, match.getMatchType());
		assertEquals("Wrong level at index " + i, typeList[i].level, level);
	}
	
	private void checkTree_conflict(Match parentMatch) {
		checkMatch_conflict(parentMatch);
		for (Match subMatch : parentMatch.getSubMatches()) {
			i++;
			checkTree_conflict(subMatch);
		}
	}
	
	private void checkMatch_conflict(Match match) {
		assertEquals("Wrong conflict state at index " + i, conflicts[i], match.isConflict());
	}
	
	class Pair {
		public MatchType type;
		public int level;
		
		public Pair(MatchType type, int level) {
			this.type = type;
			this.level = level;
		}
	}
}
