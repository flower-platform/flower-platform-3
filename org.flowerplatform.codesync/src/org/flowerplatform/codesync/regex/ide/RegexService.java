/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.regex.ide;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.codesync.regex.ide.remote.RegexIndexDto;
import org.flowerplatform.codesync.regex.ide.remote.RegexMatchDto;
import org.flowerplatform.codesync.regex.ide.remote.RegexSubMatchDto;
import org.flowerplatform.codesync.regex.ide.remote.command.RegexCommand;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.common.regex.RegexWithAction;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.regex.MacroRegex;
import org.flowerplatform.emf_model.regex.ParserRegex;
import org.flowerplatform.emf_model.regex.RegexFactory;
import org.flowerplatform.emf_model.regex.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Cristina Constantinescu
 */
public class RegexService {

	private Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	private String REGEX_LOCATION = CommonPlugin.getInstance().getWorkspaceRoot() + "/regex";
	
	private Map<String, String> regexes = new HashMap<String, String>();
	private RegexConfiguration config = new RegexConfiguration();
	
	public static RegexService getInstance() {
		return (RegexService) CommunicationPlugin.getInstance().getServiceRegistry().getService("regexService");
	}
	
	public RegexService() {
		regexes.put("jsChildrenInsertPoint", "// children-insert-point (\\S*)");
		regexes.put("backboneClass", "// template backboneClass");
		regexes.put("multiline commment", "/\\*.*?\\*/");
		regexes.put("single line comment", "//[^\\r^\\n]*\\r?\\n?");
		regexes.put("closing bracket", "[\\}\\]\\)]");
		regexes.put("opening bracket", "[\\{\\[\\(]");
		regexes.put("jsBackboneClassSuperClass", "return\\s*+(.*?).extend\\s*\\(");
		regexes.put("requireEntry", "var\\s*?(\\S*?)\\s*?=\\s*?require\\('(.*?)'\\)\\s*?;");
		regexes.put("javaScriptOperation", "(\\S*?)\\s*?:\\s*?function\\s*?\\((.*?)\\)\\s*?\\{");
		regexes.put("javaScriptAttribute", "(\\w+)\\s*+:\\s*([\\S&&[^,/]]+)");
		
		
		for (Entry<String, String> entry : regexes.entrySet()) {
			config.add(new RegexWithAction.IfFindThisSkip(entry.getKey(), entry.getValue()));
		}
		config.compile(Pattern.DOTALL);
	}
	
	private RegexIndexDto getRegexIndexDtoFromIndex(String str, int index) {
		RegexIndexDto dto = new RegexIndexDto();
		String prefix = str.substring(0, index);
		
		Matcher m = Pattern.compile("(\r\n)|(\n)|(\r)").matcher(prefix);
		int lines = 0;
		int lineEndIndex = -1;
		while (m.find()) {			
		    lines++;
		    lineEndIndex = m.end();
		}
		dto.setNbLine(lines);		
		dto.setNbCharacter(lineEndIndex == -1 ? index : index - lineEndIndex);
		
		return dto;
	}
	
	private String getNameWithoutExtension(File file) {	
		return file.getName().substring(0, file.getName().lastIndexOf("."));
	}
	
	private String getNameWithExtension(String name) {
		if (name.endsWith(".regex")) {
			return name;
		}
		return name + ".regex";
	}
	
	public void renameConfig(CommunicationChannel channel, String oldConfig, String newConfig) {			
		File configFile = new File(REGEX_LOCATION, getNameWithExtension(oldConfig));
		Path path = Paths.get(configFile.getAbsolutePath());
		
		try {
			Files.move(path, path.resolveSibling(getNameWithExtension(newConfig)));
			
			requestRefresh(channel, RegexCommand.REFRESH_CONFIGS);
			
		} catch (IOException e) {
			logger.error("Error while renaming config file!", e);
		}			
	}
	
	public void changeMacroName(CommunicationChannel channel, String config, MacroRegex macro, String newName) {			
		Root root = getRoot(config);
		
		MacroRegex macroToChange = null;
		for (MacroRegex macroRegex : root.getMacroRegex()) {
			if (macroRegex.getName().equals(newName)) {
				channel.appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								CodeSyncPlugin.getInstance().getMessage("regex.macro.exists", newName), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return;
			}
			if (macroRegex.getName().equals(macro.getName())) {
				macroToChange = macroRegex;
			}
		}				
		macroToChange.setName(newName);
		
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(channel, RegexCommand.REFRESH_MACROS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}		
	}
	
	public void changeMacroRegex(CommunicationChannel channel, String config, MacroRegex macro, String newRegex) {			
		Root root = getRoot(config);
		
		for (MacroRegex macroRegex : root.getMacroRegex()) {			
			if (macroRegex.getName().equals(macro.getName())) {
				macroRegex.setRegex(newRegex);
				break;
			}
		}		
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(channel, RegexCommand.REFRESH_MACROS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}		
	}
	
	public void changeParserName(CommunicationChannel channel, String config, ParserRegex parser, String newName) {			
		Root root = getRoot(config);
		
		MacroRegex macroToChange = null;
		for (ParserRegex parserRegex : root.getParserRegex()) {
			if (parserRegex.getName().equals(newName)) {
				channel.appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								CodeSyncPlugin.getInstance().getMessage("regex.parser.exists", newName), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return;
			}
			if (parserRegex.getName().equals(parser.getName())) {
				macroToChange = parserRegex;
			}
		}				
		macroToChange.setName(newName);
		
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(channel, RegexCommand.REFRESH_PARSERS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}		
	}
	
	public void changeParserRegex(CommunicationChannel channel, String config, ParserRegex parser, String newRegex) {			
		Root root = getRoot(config);
		
		for (ParserRegex parserRegex : root.getParserRegex()) {			
			if (parserRegex.getName().equals(parser.getName())) {
				parserRegex.setRegex(newRegex);
				break;
			}
		}		
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(channel, RegexCommand.REFRESH_PARSERS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}		
	}
	
	public void changeParserAction(CommunicationChannel channel, String config, ParserRegex parser, String newAction) {			
		Root root = getRoot(config);
		
		for (ParserRegex parserRegex : root.getParserRegex()) {			
			if (parserRegex.getName().equals(parser.getName())) {
				parserRegex.setAction(newAction);
				break;
			}
		}		
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(channel, RegexCommand.REFRESH_PARSERS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}		
	}
	
	private void requestRefresh(CommunicationChannel channel, String operation) {
		RegexCommand cmd = new RegexCommand();
		cmd.setOperation(operation);
		
		channel.appendOrSendCommand(cmd);		
	}
	
	private Root getRoot(String config) {
		File configFile = new File(REGEX_LOCATION, getNameWithExtension(config));
						
		URI resourceURI = EditorModelPlugin.getInstance().getModelAccessController().getURIFromFile(configFile);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(resourceURI, true);
		
		return (Root) resource.getContents().get(0);	
	}

	private MacroRegex nextMacro(ParserRegex parser, EList<MacroRegex> macros) {
		for (MacroRegex macro : macros) {
			if (parser.getFullRegex().contains(macro.getName())) {				
				return macro;
			}								
		}
		return null;
	}
	
	private Map<Object, Object> getSaveOptions() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_XML_VERSION, "1.1");
		return options;
	}
	
	@RemoteInvocation
	public List<Object> run(String editorResourcePath) {		
		try {
			File file = (File) EditorPlugin.getInstance().getFileAccessController().getFile(editorResourcePath);
			String cs = FileUtils.readFileToString(file);
			
			List<ParserRegex> regexDtos = new ArrayList<ParserRegex>();
			for (Entry<String, String> entry : regexes.entrySet()) {
				ParserRegex regex = RegexFactory.eINSTANCE.createParserRegex();
				regex.setName(entry.getKey());
				regex.setFullRegex(entry.getValue());
				regexDtos.add(regex);
			}
			
			List<RegexMatchDto> regexMatchDtos = new ArrayList<RegexMatchDto>();
			RegexProcessingSession session = config.startSession(cs);
			int currentIndex = 1;
			while (session.find()) {
				RegexMatchDto dto = new RegexMatchDto(
						String.valueOf(currentIndex++), 
						getRegexIndexDtoFromIndex(cs, session.getMatcher().start()), 
						getRegexIndexDtoFromIndex(cs, session.getMatcher().end()));
				ParserRegex regex = RegexFactory.eINSTANCE.createParserRegex();
				regex.setName(session.getCurrentRegex().getHumanReadableRegexMeaning());
				regex.setFullRegex(session.getCurrentRegex().getRegex());
				dto.setParserRegex(regex);
				
				if (session.getCurrentSubMatchesForCurrentRegex() != null) {
					List<RegexSubMatchDto> regexSubMatchDtos = new ArrayList<RegexSubMatchDto>();
					for (int i = 0; i < session.getCurrentSubMatchesForCurrentRegex().length; i++) {
						int index = session.getCurrentMatchGroupIndex() + i + 1;
						regexSubMatchDtos.add(new RegexSubMatchDto(
								session.getCurrentSubMatchesForCurrentRegex()[i], 
								getRegexIndexDtoFromIndex(cs, session.getMatcher().start(index)), 
								getRegexIndexDtoFromIndex(cs, session.getMatcher().end(index))));
					}
					dto.setSubMatches(regexSubMatchDtos);
				}
				regexMatchDtos.add(dto);
			}
			
			List<Object> result = new ArrayList<Object>();
			result.add(regexDtos);
			result.add(regexMatchDtos);
			result.add( RegexFactory.eINSTANCE.createMacroRegex());
			return result;
		} catch (Exception e) {
			logger.error("error while getting data for regex IDE!", e);
		}		
		return null;
	}
			
	@RemoteInvocation
	public List<String> getConfigs() {		
		File[] files = new File(REGEX_LOCATION).listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {				
				return name.endsWith(".regex");
			}
		});
	
		if (files.length == 0) {
			return null;
		}
		
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {			
			names.add(getNameWithoutExtension(files[i]));
		}
		
		return names;
	}
	
	@RemoteInvocation
	public void addConfig(ServiceInvocationContext context, String config) {	
		File configFile;
		try {		
			// TODO CC: where we save regex config files ???
			configFile = new File(REGEX_LOCATION, getNameWithExtension(config));
			if (configFile.exists()) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								CodeSyncPlugin.getInstance().getMessage("regex.config.exists", config), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return;
			}
			
			URI resourceURI = EditorModelPlugin.getInstance().getModelAccessController().getURIFromFile(configFile);
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.createResource(resourceURI);
						
			resource.getContents().clear();
			resource.getContents().add(RegexFactory.eINSTANCE.createRoot());
						
			for (Resource r : resourceSet.getResources()) {
				r.save(getSaveOptions());
			}
			
			requestRefresh(context.getCommunicationChannel(), RegexCommand.REFRESH_CONFIGS);
			
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.add.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							CodeSyncPlugin.getInstance().getMessage("regex.config.add.error"),
							e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
	}
	
	@RemoteInvocation
	public void removeConfig(ServiceInvocationContext context, String config) {			
		try {			
			File configFile = new File(REGEX_LOCATION, getNameWithExtension(config));
			Files.delete(Paths.get(configFile.getAbsolutePath()));
			
			requestRefresh(context.getCommunicationChannel(), RegexCommand.REFRESH_CONFIGS);
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.remove.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							CodeSyncPlugin.getInstance().getMessage("regex.config.remove.error"),
							e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));			
		}		
	}
	
	@RemoteInvocation
	public List<MacroRegex> getMacros(ServiceInvocationContext context, String config) {
		Root root = getRoot(config);
					
		return root.getMacroRegex();
	}
	
	@RemoteInvocation
	public void addMacro(ServiceInvocationContext context, String config, String name, String regex) {
		MacroRegex macro = RegexFactory.eINSTANCE.createMacroRegex();
		macro.setName(name);
		macro.setRegex(regex);
		
		Root root = getRoot(config);
			
		for (MacroRegex macroRegex : root.getMacroRegex()) {
			if (macroRegex.getName().equals(name)) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								CodeSyncPlugin.getInstance().getMessage("regex.macro.exists", name), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return;
			}
		}
		
		root.getMacroRegex().add(macro);
		
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(context.getCommunicationChannel(), RegexCommand.REFRESH_MACROS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}
	}
	
	@RemoteInvocation
	public void removeMacro(ServiceInvocationContext context, String config, String name) {
		Root root = getRoot(config);
				
		for (MacroRegex macroRegex : root.getMacroRegex()) {
			if (macroRegex.getName().equals(name)) {
				root.getMacroRegex().remove(macroRegex);
				break;
			}
		}
			
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(context.getCommunicationChannel(), RegexCommand.REFRESH_MACROS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}		
	}
	
	@RemoteInvocation
	public List<ParserRegex> getParserRegexes(ServiceInvocationContext context, String config) {
		Root root = getRoot(config);
		
		ECollections.sort(root.getMacroRegex(), new Comparator<MacroRegex>() {

			@Override
			public int compare(MacroRegex o1, MacroRegex o2) {				
				return o1.getName().compareTo(o2.getName());
			}			
		});		
		
		for (ParserRegex parser : root.getParserRegex()) {	
			parser.setFullRegex(parser.getRegex());
			MacroRegex macro = null;
			do {
				if (macro != null) {
					parser.setFullRegex(parser.getFullRegex().replaceAll(macro.getName(), macro.getRegex()));
				}
				macro = nextMacro(parser, root.getMacroRegex());
			} while (macro != null);	
		}
		return root.getParserRegex();
	}
		
	@RemoteInvocation
	public void addParserRegex(ServiceInvocationContext context, String config, String name, String regex, String action) {
		ParserRegex parser = RegexFactory.eINSTANCE.createParserRegex();
		parser.setName(name);
		parser.setRegex(regex);
		parser.setAction(action);
		
		Root root = getRoot(config);
				
		for (ParserRegex parserRegex : root.getParserRegex()) {
			if (parserRegex.getName().equals(name)) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								CodeSyncPlugin.getInstance().getMessage("regex.parser.exists", name), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return;
			}
		}
		
		root.getParserRegex().add(parser);
		
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(context.getCommunicationChannel(), RegexCommand.REFRESH_PARSERS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}
	}
	
	@RemoteInvocation
	public void removeParserRegex(ServiceInvocationContext context, String config, List<String> names) {
		Root root = getRoot(config);
				
		for (String name : names) {
			for (MacroRegex parserRegex : root.getParserRegex()) {
				if (parserRegex.getName().equals(name)) {
					root.getParserRegex().remove(parserRegex);
					break;
				}
			}
		}
		
		try {
			root.eResource().save(getSaveOptions());
			requestRefresh(context.getCommunicationChannel(), RegexCommand.REFRESH_PARSERS);
		} catch (IOException e) {
			logger.error("Cannot save resource!", e);
		}		
	}
}
