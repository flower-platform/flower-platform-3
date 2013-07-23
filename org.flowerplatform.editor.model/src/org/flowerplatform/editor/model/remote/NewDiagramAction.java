package org.flowerplatform.editor.model.remote;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.emf_model.notation.Diagram;

/**
 * @author Mariana Gheorghe
 */
public abstract class NewDiagramAction extends AbstractServerCommand {

	public String parentPath;
	public String name;
	
	@Override
	public void executeCommand() {
		File file = new File(CommonPlugin.getInstance().getWorkspaceRoot(), parentPath);
		// go to parent dir if actions was executed on a file
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		
		File diagram = new File(file, getNextDiagram(file, name));
		try {
			diagram.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		URI resourceURI = URI.createFileURI(diagram.getAbsolutePath());
		Resource resource = new ResourceSetImpl().createResource(resourceURI);
		resource.getContents().clear();
		resource.getContents().add(createDiagram(file));
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_XML_VERSION, "1.1");
		try {
			resource.save(options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		DiagramEditorStatefulService service = (DiagramEditorStatefulService) 
				CommunicationPlugin.getInstance().getServiceRegistry().getService(getServiceId());
		service.subscribeClientForcefully(getCommunicationChannel(), CommonPlugin.getInstance().getWorkspaceRoot().toURI().relativize(diagram.toURI()).toString());
	}
	
	protected String getNextDiagram(File parent, String name) {
		int i = 0;
		boolean exists = true;
		StringBuilder builder = null;
		while (exists) {
			i++;
			builder = new StringBuilder(name);
			builder.insert(builder.indexOf("."), i);
			if (!new File(parent, builder.toString()).exists()) {
				exists = false;
			}
		}
		return builder.toString();
	}

	abstract protected Diagram createDiagram(File file);
	abstract protected String getServiceId();
}
