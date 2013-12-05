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
package org.flowerplatform.codesync.regex;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.flowerplatform.codesync.regex.remote.MacroRegexDto;
import org.flowerplatform.codesync.regex.remote.ParserRegexDto;
import org.flowerplatform.codesync.regex.remote.RegexActionDto;
import org.flowerplatform.codesync.regex.remote.RegexIndexDto;
import org.flowerplatform.codesync.regex.remote.RegexMatchDto;
import org.flowerplatform.codesync.regex.remote.RegexSubMatchDto;
import org.flowerplatform.codesync.regex.remote.command.RegexCommand;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.regex.RegexAction;
import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
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
	
	// TODO CC: Temporary location
	private String REGEX_LOCATION = CommonPlugin.getInstance().getWorkspaceRoot() + "/regex";
	
	private static final String REGEX_CONFIGURATION_NAME = "NewRegexConfiguration.regex";
	private static final String MACRO_REGEX_NAME = "NewMacroRegex";
	private static final String PARSER_REGEX_NAME = "NewParserRegex";
	
	private static final String REGEX_EXTENSION = ".regex";
	
	private Map<String, RegexAction> actions;
	
	private Map<String, List<RegexConfiguration>> fileExtensionToRegexConfigurations = new HashMap<String, List<RegexConfiguration>>();
	
	private Pattern newLinePattern;
	
	private Pattern macroPattern;
	
	public static RegexService getInstance() {
		return (RegexService) CommunicationPlugin.getInstance().getServiceRegistry().getService("regexService");
	}
	
	public Map<String, RegexAction> getActions() {
		return actions;
	}
	
	public List<RegexConfiguration> getCompiledRegexConfigurations(File file) {
		String fileExtension = getFileExtension(file);
		if (fileExtension == null) {
			return null;
		}
		if (!fileExtensionToRegexConfigurations.containsKey(fileExtension)) {
			// get all regex configurations files
			File[] regexConfigFiles = new File(REGEX_LOCATION).listFiles(new FilenameFilter() {			
				@Override
				public boolean accept(File dir, String name) {				
					return name.endsWith(REGEX_EXTENSION);
				}
			});
			for (File regexConfigFile : regexConfigFiles) {				
				Root root = getRoot(getFileNameWithoutExtension(regexConfigFile));
				
				if (root.getExtensions().contains(fileExtension)) {
					String config = getFileNameWithoutExtension(regexConfigFile);
					// create new regex configuration and compile it
					RegexConfiguration regexConfig = new RegexConfiguration();
					for (ParserRegex parserRegex : root.getParserRegexes()) {
						regexConfig.add(new DelegatingRegexWithAction().setConfig(config).setParserRegex(parserRegex));
					}
					regexConfig.compile(Pattern.DOTALL);
					
					// put compiled regex configuration in map
					fileExtensionToRegexConfigurations.put(fileExtension, new ArrayList<RegexConfiguration>());
					fileExtensionToRegexConfigurations.get(fileExtension).add(regexConfig);					
				}
			}			
		}		
		return fileExtensionToRegexConfigurations.get(fileExtension);
	}

	public void addRegexAction(RegexAction action) {
		if (actions == null) {
			actions = new HashMap<String, RegexAction>();
		}
		actions.put(action.getName(), action);
	}
	
	public void clearRegexActionsAndCompiledRegexConfigurations() {
		actions.clear();		
		fileExtensionToRegexConfigurations.clear();
	}
		
	private RegexIndexDto getRegexIndexDtoFromIndex(String str, int index) {
		RegexIndexDto dto = new RegexIndexDto();
		String prefix = str.substring(0, index);
		
		if (newLinePattern == null) { // first time
			newLinePattern = Pattern.compile("(\r\n)|(\n)|(\r)");
		}
		
		Matcher m = newLinePattern.matcher(prefix); // search for new line separator
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
	
	/**
	 * @return The next available name for a new regex configuration file. <br>
	 * E.g. NewRegexConfiguration.regex, NewRegexConfiguration1.regex ...
	 */
	private String getRegexConfigurationFileName() {
		File parent = new File(REGEX_LOCATION);
		
		// before changing name, verify if the current one exists
		if (!new File(parent, REGEX_CONFIGURATION_NAME).exists()) {
			return REGEX_CONFIGURATION_NAME;
		}
		int i = 0;
		boolean exists = true;
		StringBuilder builder = null;
		while (exists) {
			i++;
			builder = new StringBuilder(REGEX_CONFIGURATION_NAME);
			builder.insert(builder.indexOf("."), i);
			if (!new File(parent, builder.toString()).exists()) {
				exists = false;
			}
		}
		return builder.toString();
	}
		
	/**
	 * @return The next available name for a new <code>MacroRegex</code>. <br>
	 * E.g. NewMacroRegex123, NewMacroRegex027 ...
	 */
	private String getMacroRegexName() {		
		return MACRO_REGEX_NAME + RandomStringUtils.randomNumeric(3);				
	}
	
	/**
	 * @return The next available name for a new <code>ParserRegex</code>. <br>
	 * E.g. NewParserRegex123, NewParserRegex027 ...
	 */
	private String getParserRegexName() {		
		return PARSER_REGEX_NAME + RandomStringUtils.randomNumeric(3);				
	}
	
	private String getFileNameWithoutExtension(File file) {	
		return file.getName().substring(0, file.getName().lastIndexOf("."));
	}
	
	private String getFileExtension(File file) {
		int lastIndexOfDot = file.getName().lastIndexOf(".");
		if (lastIndexOfDot == -1) {
			return null;
		}
		return file.getName().substring(lastIndexOfDot + 1);
	}
	
	private String getFileNameWithRegexExtension(String name) {
		if (name.endsWith(REGEX_EXTENSION)) {
			return name;
		}
		return name + REGEX_EXTENSION;
	}
		
	private MacroRegexDto convertMacroRegexToDto(MacroRegex macro) {
		MacroRegexDto dto = new MacroRegexDto();
		dto.setId(macro.eResource().getURIFragment(macro));
		dto.setName(macro.getName());
		dto.setRegex(macro.getRegex());		
		return dto;
	}
	
	private RegexActionDto convertRegexActionToDto(RegexAction action) {
		RegexActionDto dto = new RegexActionDto();		
		dto.setName(action.getName());
		dto.setDescription(action.getDescription());		
		return dto;
	}
	
	private ParserRegexDto convertParserRegexToDto(ParserRegex parser) {
		ParserRegexDto dto = new ParserRegexDto();
		dto.setId(parser.eResource().getURIFragment(parser));
		dto.setName(parser.getName());
		dto.setRegex(parser.getRegex());	
		
		if (parser.getAction() != null) {
			dto.setAction(convertRegexActionToDto(actions.get(parser.getAction())));
		}
		return dto;
	}
	
	private Map<Object, Object> getSaveOptions() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_XML_VERSION, "1.1");
		return options;
	}
		
	private void deleteCompiledRegexConfigurations(File configFile) {
		deleteCompiledRegexConfigurations(getFileNameWithoutExtension(configFile));
	}
	
	private void deleteCompiledRegexConfigurations(String configName) {
		Root root = getRoot(configName);
		
		for (String extension : root.getExtensions()) {
			fileExtensionToRegexConfigurations.remove(extension);
		}
	}
	
	public void changeRegexConfigName(CommunicationChannel channel, String oldConfig, String newConfig) {
		try {
			File configFile = new File(REGEX_LOCATION, getFileNameWithRegexExtension(oldConfig));
			Path path = Paths.get(configFile.getAbsolutePath());
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(configFile);
			// change configuration file name
			Files.move(path, path.resolveSibling(getFileNameWithRegexExtension(newConfig)));
			// notify client about changes
			requestRefresh(channel, RegexCommand.REFRESH_CONFIGS);						
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e);
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
	}
	
	public void changeRegexConfigExtensions(CommunicationChannel channel, String config, String extensions) {			
		try {
			File configFile = new File(REGEX_LOCATION, getFileNameWithRegexExtension(config));
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(configFile);
			// set new extensions
			Root root = getRoot(config);			
			root.getExtensions().clear();
			for (String extension : StringUtils.splitByWholeSeparator(extensions, ",")) {
				root.getExtensions().add(extension.replaceAll("\\s", ""));
			}	
			// save configuration
			root.eResource().save(getSaveOptions());
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e);
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
	}
		
	public void changeMacroRegexName(CommunicationChannel channel, String config, MacroRegexDto macroRegex, String newName) {			
		try {
			Root root = getRoot(config);
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(config);
			// set new value
			MacroRegex configMacroRegexToModify = null;
			for (MacroRegex configMacroRegex : root.getMacroRegexes()) {
				if (configMacroRegex.getName().equals(newName) && !configMacroRegex.getName().equals(macroRegex.getName())) { 
					// newName already taken -> notify client
					channel.appendOrSendCommand(
							new DisplaySimpleMessageClientCommand(
									CommonPlugin.getInstance().getMessage("error"), 
									CodeSyncPlugin.getInstance().getMessage("regex.macro.exists", newName), 
									DisplaySimpleMessageClientCommand.ICON_ERROR));
					return;
				}
				if (configMacroRegex.getName().equals(macroRegex.getName())) { // found macroRegex in configuration
					configMacroRegexToModify = configMacroRegex;
				}
			}				
			// change macro name
			configMacroRegexToModify.setName(newName);
			// save configuration
			root.eResource().save(getSaveOptions());
			// notify client to refresh macrRegexes list
			requestRefresh(channel, RegexCommand.REFRESH_MACROS);
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e);
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
	}
	
	public void changeMacroRegexRegex(CommunicationChannel channel, String config, MacroRegexDto macroRegex, String newRegex) {			
		try {
			Root root = getRoot(config);
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(config);
			// set new value
			for (MacroRegex configMacroRegex : root.getMacroRegexes()) {			
				if (configMacroRegex.getName().equals(macroRegex.getName())) { // found macroRegex in configuration
					configMacroRegex.setRegex(newRegex);
					break;
				}
			}	
			// save configuration
			root.eResource().save(getSaveOptions());
			// notify client to refresh macroRegexes list
			requestRefresh(channel, RegexCommand.REFRESH_MACROS);
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e);
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
	}
	
	public void changeParserRegexName(CommunicationChannel channel, String config, ParserRegexDto parserRegex, String newName) {			
		try {
			Root root = getRoot(config);
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(config);
			// set new value
			MacroRegex configParserRegexToModify = null;
			for (ParserRegex configParserRegex : root.getParserRegexes()) {
				if (configParserRegex.getName().equals(newName) && !configParserRegex.getName().equals(parserRegex.getName())) { 
					// newName already taken -> notify client
					channel.appendOrSendCommand(
							new DisplaySimpleMessageClientCommand(
									CommonPlugin.getInstance().getMessage("error"), 
									CodeSyncPlugin.getInstance().getMessage("regex.parser.exists", newName), 
									DisplaySimpleMessageClientCommand.ICON_ERROR));
					return;
				}
				if (configParserRegex.getName().equals(parserRegex.getName())) { // found parserRegex in configuration
					configParserRegexToModify = configParserRegex;
				}
			}				
			configParserRegexToModify.setName(newName);
			// save configuration
			root.eResource().save(getSaveOptions());
			// notify client to refresh parserRegexes list
			requestRefresh(channel, RegexCommand.REFRESH_PARSERS);
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e);
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
	}
	
	public void changeParserRegexRegex(CommunicationChannel channel, String config, ParserRegexDto parserRegex, String newRegex) {			
		try {
			Root root = getRoot(config);
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(config);
			// set new value
			for (ParserRegex configParserRegex : root.getParserRegexes()) {
				if (configParserRegex.getName().equals(parserRegex.getName())) {
					configParserRegex.setRegex(newRegex);
					break;
				}
			}		
			// save configuration
			root.eResource().save(getSaveOptions());
			// notify client to refresh parserRegexes list
			requestRefresh(channel, RegexCommand.REFRESH_PARSERS);
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e);
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
	}
	
	public void changeParserRegexAction(CommunicationChannel channel, String config, ParserRegexDto parserRegex, String newAction) {			
		try {
			Root root = getRoot(config);
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(config);
			// set new value
			for (ParserRegex configParserRegex : root.getParserRegexes()) {			
				if (configParserRegex.getName().equals(parserRegex.getName())) {
					configParserRegex.setAction(newAction);
					break;
				}
			}		
			// save configuration
			root.eResource().save(getSaveOptions());
			// notify client to refresh parserRegexes list
			requestRefresh(channel, RegexCommand.REFRESH_PARSERS);
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e);
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.modify.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
	}
	
	private void requestRefresh(CommunicationChannel channel, String operation) {
		RegexCommand cmd = new RegexCommand();
		cmd.setOperation(operation);
		
		channel.appendOrSendCommand(cmd);		
	}
	
	public Root getRoot(String config) {
		// get Root node for given config
		File configFile = new File(REGEX_LOCATION, getFileNameWithRegexExtension(config));
						
		URI resourceURI = EditorModelPlugin.getInstance().getModelAccessController().getURIFromFile(configFile);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(resourceURI, true);
		
		return (Root) resource.getContents().get(0);	
	}

	/**
	 * @return <code>regexWithMacros</code> without macros.
	 */
	public String getFullRegex(String config, String regexWithMacros) {
		Root root = getRoot(config);	
		
		return getFullRegexInternal(root, regexWithMacros);
	}
	
	/**
	 * Recursive method to replace all macros with their corresponding regular expressions.
	 * <p>
	 * Note: A macro regular expression can contain other macros.
	 */
	private String getFullRegexInternal(Root root, String regexWithMacros) {		
		String fullRegex = regexWithMacros;
		if (macroPattern == null) {
			macroPattern = Pattern.compile("%(.*?)%");
		}
		Matcher matcher = macroPattern.matcher(regexWithMacros);
		while (matcher.find()) { // found %...%			
			// search ... in list and replace it with macro's regular expression
			for (MacroRegex macroRegex : root.getMacroRegexes()) {
				if (macroRegex.getName().equals(matcher.group().substring(1, matcher.group().length() - 1))) {					
					fullRegex = fullRegex.replace(matcher.group(), getFullRegexInternal(root, macroRegex.getRegex()));
					break;
				}
			}			
		}
		return fullRegex;
	}
	
	@RemoteInvocation
	public List<RegexMatchDto> run(ServiceInvocationContext context, String editorResourcePath, String config) {		
		try {
			List<RegexMatchDto> regexMatchDtos = new ArrayList<RegexMatchDto>();
			
			File file = (File) EditorPlugin.getInstance().getFileAccessController().getFile(editorResourcePath);
			String fileContent = FileUtils.readFileToString(file);
			String fileExtension = getFileExtension(file);
										
			Root root = getRoot(config);
			
			if (!root.getExtensions().contains(fileExtension)) {				
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								CodeSyncPlugin.getInstance().getMessage("regex.run.extensionNotInConfig"),								
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return null;
			}
			// compile a new regexConfiguration for this configuration
			// this configuration uses CodeSyncRegexAction.IfFindThisSkip as RegexAction for all its parserRegexes
			RegexConfiguration regexConfig = new RegexConfiguration();
			for (ParserRegex parserRegex : root.getParserRegexes()) {
				regexConfig.add(
						new DelegatingRegexWithAction()
						.setConfig(config).setParserRegex(parserRegex).setRegexAction(new CodeSyncRegexAction.IfFindThisSkip()));
			}
			regexConfig.compile(Pattern.DOTALL);
						
			int currentIndex = 1;				
			RegexProcessingSession session = regexConfig.startSession(fileContent);				
			while (session.find()) {
				RegexMatchDto dto = new RegexMatchDto(
						String.valueOf(currentIndex++), 
						getRegexIndexDtoFromIndex(fileContent, session.getMatcher().start()), 
						getRegexIndexDtoFromIndex(fileContent, session.getMatcher().end()));
					
				dto.setParserRegex(
						new ParserRegexDto(
								session.getCurrentRegex().getName(), 
								((DelegatingRegexWithAction) session.getCurrentRegex()).getRegexWithMacros()));
					
				if (session.getCurrentSubMatchesForCurrentRegex() != null) { // has subMatches -> add them too
					List<RegexSubMatchDto> regexSubMatchDtos = new ArrayList<RegexSubMatchDto>();
					for (int i = 0; i < session.getCurrentSubMatchesForCurrentRegex().length; i++) {
						int index = session.getCurrentMatchGroupIndex() + i + 1;
						regexSubMatchDtos.add(new RegexSubMatchDto(
								session.getCurrentSubMatchesForCurrentRegex()[i], 
								getRegexIndexDtoFromIndex(fileContent, session.getMatcher().start(index)), 
								getRegexIndexDtoFromIndex(fileContent, session.getMatcher().end(index))));
					}
					dto.setSubMatches(regexSubMatchDtos);
				}
				regexMatchDtos.add(dto);
			}
			
			return regexMatchDtos;
		} catch (Exception e) {
			logger.debug(CodeSyncPlugin.getInstance().getMessage("regex.run.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.run.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
		return null;
	}
			
	@RemoteInvocation
	public List<String> getRegexConfigs() {		
		File[] files = new File(REGEX_LOCATION).listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {				
				return name.endsWith(REGEX_EXTENSION);
			}
		});
	
		if (files.length == 0) {
			return null;
		}
		
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {			
			names.add(getFileNameWithoutExtension(files[i]));
		}
		
		return names;
	}
	
	@RemoteInvocation
	public String addRegexConfig(ServiceInvocationContext context) {	
		File configFile;
		try {		
			configFile = new File(REGEX_LOCATION, getRegexConfigurationFileName());
			
			URI resourceURI = EditorModelPlugin.getInstance().getModelAccessController().getURIFromFile(configFile);
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.createResource(resourceURI);
						
			resource.getContents().clear();
			resource.getContents().add(RegexFactory.eINSTANCE.createRoot());
						
			for (Resource r : resourceSet.getResources()) {
				r.save(getSaveOptions());
			}
			return getFileNameWithoutExtension(configFile);
			
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.add.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.add.error"),	e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}	
		return null;
	}
	
	@RemoteInvocation
	public String removeRegexConfig(ServiceInvocationContext context, String config) {			
		try {			
			File configFile = new File(REGEX_LOCATION, getFileNameWithRegexExtension(config));
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(configFile);
			
			Files.delete(Paths.get(configFile.getAbsolutePath()));
			
			return config;
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.config.remove.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"),
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.config.remove.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));			
		}		
		return null;
	}
	
	@RemoteInvocation
	public List<MacroRegexDto> getMacroRegexes(ServiceInvocationContext context, String config) {
		Root root = getRoot(config);
			
		List<MacroRegexDto> macroDtos = new ArrayList<MacroRegexDto>();
		for (MacroRegex macro : root.getMacroRegexes()) {
			macroDtos.add(convertMacroRegexToDto(macro));
		}
		return macroDtos;
	}
	
	@RemoteInvocation
	public MacroRegexDto addMacroRegex(ServiceInvocationContext context, String config) {
		try {
			MacroRegex macro = RegexFactory.eINSTANCE.createMacroRegex();
			macro.setName(getMacroRegexName());
			macro.setRegex("");
			
			Root root = getRoot(config);			
			root.getMacroRegexes().add(macro);
			
			root.eResource().save(getSaveOptions());

			return convertMacroRegexToDto(macro);
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.macro.add.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.macro.add.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
		}
		return null;
	}
	
	@RemoteInvocation
	public MacroRegexDto removeMacroRegex(ServiceInvocationContext context, String config, MacroRegexDto macro) {
		try {
			Root root = getRoot(config);
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(config);
						
			for (MacroRegex macroRegex : root.getMacroRegexes()) {
				if (macroRegex.getName().equals(macro.getName())) {
					root.getMacroRegexes().remove(macroRegex);
					break;
				}
			}	
			root.eResource().save(getSaveOptions());	
			return macro;
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.macro.remove.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.macro.remove.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
		}	
		return null;
	}
	
	@RemoteInvocation
	public List<ParserRegexDto> getParserRegexes(ServiceInvocationContext context, String config) {
		Root root = getRoot(config);
		
		ECollections.sort(root.getMacroRegexes(), new Comparator<MacroRegex>() {

			@Override
			public int compare(MacroRegex o1, MacroRegex o2) {				
				return o1.getName().compareTo(o2.getName());
			}			
		});		
		
		List<ParserRegexDto> dtos = new ArrayList<ParserRegexDto>();
		for (ParserRegex parser : root.getParserRegexes()) {	
			dtos.add(convertParserRegexToDto(parser));
		}		
		return dtos;
	}
		
	@RemoteInvocation
	public ParserRegexDto addParserRegex(ServiceInvocationContext context, String config) {
		try {
			ParserRegex parser = RegexFactory.eINSTANCE.createParserRegex();
			parser.setName(getParserRegexName());
			parser.setRegex(""); // empty
			
			Root root = getRoot(config);		
			root.getParserRegexes().add(parser);
			
			root.eResource().save(getSaveOptions());
			
			return convertParserRegexToDto(parser);
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.parser.add.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.parser.add.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
		return null;
	}
	
	@RemoteInvocation
	public void removeParserRegex(ServiceInvocationContext context, String config, List<String> names) {
		try {
			Root root = getRoot(config);
			// configuration changed -> delete compiled regex configurations from map
			deleteCompiledRegexConfigurations(config);	
			
			for (String name : names) {
				for (MacroRegex parserRegex : root.getParserRegexes()) {
					if (parserRegex.getName().equals(name)) {
						root.getParserRegexes().remove(parserRegex);
						break;
					}
				}
			}	
			root.eResource().save(getSaveOptions());
			
			requestRefresh(context.getCommunicationChannel(), RegexCommand.REFRESH_PARSERS);		
		} catch (Exception e) {
			logger.error(CodeSyncPlugin.getInstance().getMessage("regex.parser.remove.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							String.format("%s\n%s", CodeSyncPlugin.getInstance().getMessage("regex.parser.remove.error"), e.getMessage()),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}		
	}
	
	@RemoteInvocation
	public List<RegexActionDto> getRegexActions(ServiceInvocationContext context) {	
		List<RegexActionDto> dtos = new ArrayList<RegexActionDto>();
		
		for (RegexAction regexAction : actions.values()) {
			dtos.add(new RegexActionDto(regexAction.getName(), regexAction.getDescription()));
		}		
		return dtos;
	}
	
}
