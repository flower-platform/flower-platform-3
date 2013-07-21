package org.flowerplatform.editor.mindmap;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.editor.mindmap.processor.MindMapChildrenChangeProcessor;
import org.flowerplatform.editor.mindmap.processor.MindMapFolderProcessor;
import org.flowerplatform.editor.mindmap.processor.MindMapHeadlineProcessor;
import org.flowerplatform.editor.mindmap.processor.MindMapPageProcessor;
import org.flowerplatform.editor.mindmap.processor.MindMapParagraphProcessor;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.wiki.WikiPlugin;

/**
 * @author Cristina Constantinescu
 */
public class MindMapModelPlugin extends AbstractFlowerJavaPlugin {

	protected static MindMapModelPlugin INSTANCE;
	
	public static MindMapModelPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;		
		
		MindMapChildrenChangeProcessor mmChildrenChangeProcessor = new MindMapChildrenChangeProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.FOLDER_CATEGORY, mmChildrenChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.PAGE_CATEGORY, mmChildrenChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_1_CATEGORY, mmChildrenChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_2_CATEGORY, mmChildrenChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_3_CATEGORY, mmChildrenChangeProcessor);
//		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_4_CATEGORY, mmChildrenChangeProcessor);
//		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_5_CATEGORY, mmChildrenChangeProcessor);
//		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_6_CATEGORY, mmChildrenChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.PARAGRAPH_CATEGORY, mmChildrenChangeProcessor);
		
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.FOLDER_CATEGORY, new MindMapFolderProcessor());
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.PAGE_CATEGORY, new MindMapPageProcessor());
		
		MindMapHeadlineProcessor mmHeadingChangeProcessor = new MindMapHeadlineProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_1_CATEGORY, mmHeadingChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_2_CATEGORY, mmHeadingChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_3_CATEGORY, mmHeadingChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_4_CATEGORY, mmHeadingChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_5_CATEGORY, mmHeadingChangeProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.HEADLINE_LEVEL_6_CATEGORY, mmHeadingChangeProcessor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor(WikiPlugin.PARAGRAPH_CATEGORY, new MindMapParagraphProcessor());
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {		
	}
	
}
