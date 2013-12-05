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
package org.flowerplatform.codesync.code.javascript.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.regex.CodeSyncRegexAction;
import org.flowerplatform.codesync.regex.RegexService;
import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.common.regex.RegexUtil;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Mariana Gheorghe
 */
public class Parser {

	public static final String JS_FILE 				= "javaScriptFile";
	public static final String HTML_FILE 			= "htmlFile";
	
	
	//////////////////////////////////
	// HTML
	//////////////////////////////////
	
	public static final String HTML_CHILDREN_INSERT_POINT		= "htmlChildrenInsertPoint";
	public static final String HTML_CHILDREN_INSERT_POINT_REGEX	= "<!-- children-insert-point (.*?) -->";
	
	public static final String HTML_TABLE						= "table";
	public static final String HTML_TABLE_REGEX					= "<!-- template table -->\\s*<table .*?id=\\s*\"(.*?)\">\\s*<tr .*?id=\\s*\"(.*?)\"";
	
	public static final String HTML_TABLE_END					= "htmlTableEnd";
	public static final String HTML_TABLE_END_REGEX				= "</table>";
	
	public static final String HTML_TABLE_HEADER_ENTRY 			= "tableHeaderEntry";
	public static final String HTML_TABLE_HEADER_ENTRY_REGEX 	= "<th>(.*?)</th>";
	
	public static final String HTML_TABLE_ITEM					= "tableItem";
	public static final String HTML_TABLE_ITEM_REGEX 			= "<!-- template tableItem -->";
	public static final String HTML_TABLE_ITEM_URL				= "tableItemUrl";
	public static final String HTML_TABLE_ITEM_URL_REGEX 		= "<td><a .*?href=\"#(.+)/<.*?"; 
	
	public static final String HTML_TABLE_ITEM_ENTRY 			= "tableItemEntry";
	public static final String HTML_TABLE_ITEM_ENTRY_REGEX		= "<td>.*?<%= (.*?) %>.*?</td>";
	
	public static final String HTML_FORM						= "form";
	public static final String HTML_FORM_REGEX					= "<!-- template form -->.*?<table";
	
	public static final String HTML_FORM_END					= "htmlFormEnd";
	public static final String HTML_FORM_END_REGEX				= "</table>";
	public static final String HTML_FORM_ID_SUFFIX_REGEX		= "type=\"button\"\\s*?id=\"edit-(.*?)\"";
	
	public static final String HTML_FORM_ITEM					= "formItem";
	public static final String HTML_FORM_ITEM_REGEX 			= "<tr.*?<td>(.*?):</td>.*?<td .*?<%= (.*?) %>.*?</tr>";
	
	//////////////////////////////////
	// JS
	//////////////////////////////////
	
	public static final String JS_CHILDREN_INSERT_POINT			= "jsChildrenInsertPoint";
	public static final String JS_CHILDREN_INSERT_POINT_REGEX	= "// children-insert-point (\\S*)";
	
	public static final String JS_BACKBONE_CLASS 				= "backboneClass";
	public static final String JS_BACKBONE_CLASS_REGEX			= "// template backboneClass";
	public static final String JS_BACKBONE_SUPER_CLASS			= "jsBackboneClassSuperClass";
	public static final String JS_BACKBONE_SUPER_CLASS_REGEX	= "return\\s*+(.*?).extend\\s*\\(";
	
	public static final String JS_REQUIRE_ENTRY					= "requireEntry";
	public static final String JS_REQUIRE_ENTRY_CATEGORY		= "Require";
	public static final String JS_REQUIRE_ENTRY_REGEX			= "var\\s*?(\\S*?)\\s*?=\\s*?require\\('(.*?)'\\)\\s*?;";
	
	public static final String JS_BACKBONE_CLASS_MEMBER			= "classMember";
	
	public static final String JS_OPERATION						= "javaScriptOperation";
	public static final String JS_OPERATION_CATEGORY			= "Operation";
	public static final String JS_OPERATION_REGEX				= "(\\S*?)\\s*?:\\s*?function\\s*?\\((.*?)\\)\\s*?\\{";
	
	public static final String JS_ATTRIBUTE						= "javaScriptAttribute";
	public static final String JS_ATTRIBUTE_CATEGORY			= "Attribute";
	public static final String JS_ATTRIBUTE_REGEX				= "(\\w+)\\s*+:\\s*([\\S&&[^,/]]+)";
	
	public static final String JS_EVENTS_ATTRIBUTE				= "eventsAttribute";
	public static final String JS_EVENTS_ATTRIBUTE_CATEGORY 	= "Events";
	public static final String JS_EVENTS_ATTRIBUTE_REGEX		= "(events)\\s*:\\s*\\{";
	
	public static final String JS_EVENTS_ATTRIBUTE_ENTRY		= "eventsAttributeEntry";
	public static final String JS_EVENTS_ATTRIBUTE_ENTRY_REGEX	= "\"(\\S+?)\\s*(\\S*?)\"\\s*:\\s*\"(\\S+?)\"";
	
	public static final String JS_ROUTES_ATTRIBUTE				= "routesAttribute";
	public static final String JS_ROUTES_ATTRIBUTE_CATEGORY		= "Routes";
	public static final String JS_ROUTES_ATTRIBUTE_REGEX		= "(routes)\\s*:\\s*\\{";
	
	public static final String JS_ROUTES_ATTRIBUTE_ENTRY		= "routesAttributeEntry";
	public static final String JS_ROUTES_ATTRIBUTE_ENTRY_REGEX	= "\"(\\S+?)\"\\s*:\\s*\"(\\S+?)\"";
		
	public static final String NAME = "name";
	
	public static RegexEngineState[] statesStack = new RegexEngineState[100];
	public static RegexEngineState currentState;

	protected String input;
	
	protected RegExAstNode jsDoc;
	
	public RegExAstNode parse(File file) {
		try {
			input = FileUtils.readFileToString(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		RegExAstNode root = createRegExAstNode(file.getPath().endsWith(".js") ? JS_FILE : HTML_FILE, NAME, false, 0, 0);
		addParameter(root, NAME, file.getName().substring(0, file.getName().indexOf(".")), 0, 0);
		
		
		List<RegexConfiguration> regexConfigs = RegexService.getInstance().getCompiledRegexConfigurations(file);

		RegexProcessingSession session = regexConfigs.get(0).startSession(input);
		
		enterState(session, file.getPath().endsWith(".js") ? JS_FILE : HTML_FILE, root, 0);
		
		try {
			while (session.find()) {}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
//		print("", root);
		
		// adding to resource to avoid UNDEFINED values during sync
		// see EObjectModelAdapter.getValueFeatureValue()
		Resource resource = new ResourceImpl();
		resource.getContents().add(root);
		
		return root;
	}
	
	public void configureHtmlRegexActions() {
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				currentState.node.getChildrenInsertPoints().put(session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex()));
			}			
		}
		.setName(HTML_CHILDREN_INSERT_POINT)
		.setDescription(HTML_CHILDREN_INSERT_POINT_REGEX)
		);			
			
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {
			
			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(HTML_FILE)) {
					currentState.node.setType(HTML_TABLE);
					addParameter(currentState.node, "tableId", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(currentState.node, "headerRowId", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
					currentState.node.setOffset(session.getMatcher().start(session.getCurrentMatchGroupIndex()));
					enterState(session, HTML_TABLE, currentState.node, 1);
				}
			}
		}
		.setName(HTML_TABLE)
		.setDescription(HTML_TABLE_REGEX)
		);		
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				currentState.node.getChildrenInsertPoints().put(session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex()));
			}			
		}
		.setName(HTML_CHILDREN_INSERT_POINT)
		.setDescription(HTML_CHILDREN_INSERT_POINT_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(HTML_FILE)) {
					currentState.node.setType(HTML_TABLE);
					addParameter(currentState.node, "tableId", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(currentState.node, "headerRowId", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
					currentState.node.setOffset(session.getMatcher().start(session.getCurrentMatchGroupIndex()));
					enterState(session, HTML_TABLE, currentState.node, 1);
				}
			}			
		}
		.setName(HTML_TABLE)
		.setDescription(HTML_TABLE_REGEX)
		);
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (HTML_TABLE.equals(currentState.node.getType()) || HTML_FORM.equals(currentState.node.getType())) {
					exitState(session);
				}
			}			
		}
		.setName(HTML_TABLE_END)
		.setDescription(HTML_TABLE_END_REGEX)
		);

		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (HTML_TABLE.equals(currentState.node.getType())) {
					RegExAstNode entry = createRegExAstNode(HTML_TABLE_HEADER_ENTRY, "title", false, session.getMatcher().start(session.getCurrentMatchGroupIndex()), session.getMatcher().end(session.getCurrentMatchGroupIndex()));
					entry.setType(HTML_TABLE_HEADER_ENTRY);
					entry.setNextSiblingInsertPoint(session.getMatcher().end(session.getCurrentMatchGroupIndex()));
					addParameter(entry, "title", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					currentState.node.getChildren().add(entry);
				}
			}			
		}
		.setName(HTML_TABLE_HEADER_ENTRY)
		.setDescription(HTML_TABLE_HEADER_ENTRY_REGEX)
		);

		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(HTML_FILE)) {
					currentState.node.setType(HTML_TABLE_ITEM);
					currentState.node.setOffset(session.getMatcher().start(session.getCurrentMatchGroupIndex()));
					enterState(session, HTML_TABLE_ITEM, currentState.node, 1);
				}
			}			
		}
		.setName(HTML_TABLE_ITEM)
		.setDescription(HTML_TABLE_ITEM_REGEX)
		);
	
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (HTML_TABLE_ITEM.equals(currentState.node.getType())) {
					addParameter(currentState.node, "itemUrl", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					exitState(session);
				}
			}			
		}
		.setName(HTML_TABLE_ITEM_URL)
		.setDescription(HTML_TABLE_ITEM_URL_REGEX)
		);
	
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (HTML_TABLE_ITEM.equals(currentState.node.getType())) {
					RegExAstNode entry = createRegExAstNode(HTML_TABLE_ITEM_ENTRY, "valueExpression", false, session.getMatcher().start(session.getCurrentMatchGroupIndex()), session.getMatcher().end(session.getCurrentMatchGroupIndex()));
					entry.setType(HTML_TABLE_ITEM_ENTRY);
					entry.setNextSiblingInsertPoint(session.getMatcher().end(session.getCurrentMatchGroupIndex()));
					addParameter(entry, "valueExpression", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					currentState.node.getChildren().add(entry);
				}
			}			
		}
		.setName(HTML_TABLE_ITEM_ENTRY)
		.setDescription(HTML_TABLE_ITEM_ENTRY_REGEX)
		);

		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(HTML_FILE)) {
					currentState.node.setType(HTML_FORM);
					currentState.node.setOffset(session.getMatcher().start(session.getCurrentMatchGroupIndex()));
					enterState(session, HTML_FORM, currentState.node, 1);
				}
			}			
		}
		.setName(HTML_FORM)
		.setDescription(HTML_FORM_REGEX)
		);
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				addParameter(currentState.node, "idSuffix", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
			}			
		}
		.setName(HTML_FORM)
		.setDescription(HTML_FORM_ID_SUFFIX_REGEX)
		);
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (HTML_FORM.equals(currentState.node.getType())) {
					RegExAstNode entry = createRegExAstNode(HTML_FORM_ITEM, "title", false, session.getMatcher().start(session.getCurrentMatchGroupIndex()), session.getMatcher().end(session.getCurrentMatchGroupIndex()));
					entry.setType(HTML_FORM_ITEM);
					entry.setNextSiblingInsertPoint(session.getMatcher().end(session.getCurrentMatchGroupIndex()));
					addParameter(entry, "title", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(entry, "valueExpression", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
					currentState.node.getChildren().add(entry);
				}
			}			
		}
		.setName(HTML_FORM_ITEM)
		.setDescription(HTML_FORM_ITEM_REGEX)
		);		
	}
	
	public void configureJSRegexActions() {
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				currentState.node.getChildrenInsertPoints().put(session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex()));
			}			
		}
		.setName(JS_CHILDREN_INSERT_POINT)
		.setDescription(JS_CHILDREN_INSERT_POINT_REGEX)
		);	
	
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(JS_FILE)) {
					currentState.node.setType(JS_BACKBONE_CLASS);
					attachJsDoc(currentState.node);					
					enterState(session, JS_BACKBONE_CLASS, currentState.node, 1);
				}				
			}			
		}
		.setName(JS_BACKBONE_CLASS)
		.setDescription(JS_BACKBONE_CLASS_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(
				new CodeSyncRegexAction.IfFindThisSkip()
				.setName("multiline commment")
				.setDescription(RegexUtil.MULTI_LINE_COMMENT)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(
				new CodeSyncRegexAction.IfFindThisSkip()
				.setName("single line comment")
				.setDescription(RegexUtil.SINGLE_LINE_COMMENT)
		);	

		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				exitState(session);			
			}			
		}
		.setName("closing bracket")
		.setDescription("[\\}\\]\\)]")
		);	

		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				session.currentNestingLevel++;	
			}			
		}
		.setName("opening bracket")
		.setDescription("[\\{\\[\\(]")
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (JS_BACKBONE_CLASS.equals(currentState.node.getType())) {
					addParameter(currentState.node, "superClass", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
				}	
			}			
		}
		.setName(JS_BACKBONE_SUPER_CLASS)
		.setDescription(JS_BACKBONE_SUPER_CLASS_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (JS_BACKBONE_CLASS.equals(currentState.node.getType())) {
					RegExAstNode requireEntry = addToCategory(JS_REQUIRE_ENTRY_CATEGORY, currentState.node, "varName", JS_REQUIRE_ENTRY, session.getMatcher().start(session.getCurrentMatchGroupIndex()), session.getMatcher().end(session.getCurrentMatchGroupIndex()));
					requireEntry.setType(JS_REQUIRE_ENTRY);
					requireEntry.setNextSiblingInsertPoint(session.getMatcher().end(session.getCurrentMatchGroupIndex()));
					addParameter(requireEntry, "varName", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(requireEntry, "dependencyPath", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
				
					attachJsDoc(requireEntry);
				}
			}			
		}
		.setName(JS_REQUIRE_ENTRY)
		.setDescription(JS_REQUIRE_ENTRY_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				RegExAstNode function = addToCategory(JS_OPERATION_CATEGORY, currentState.node, NAME, JS_OPERATION, session.getMatcher().start(), 0);
				function.setType(JS_OPERATION);
				addParameter(function, NAME, session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
				addParameter(function, "parameters", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
				
				attachJsDoc(function);
				
				enterState(session, JS_OPERATION, function, 1);
			}			
		}
		.setName(JS_OPERATION)
		.setDescription(JS_OPERATION_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				RegExAstNode events = addToCategory(JS_ATTRIBUTE_CATEGORY, currentState.node, NAME, JS_EVENTS_ATTRIBUTE, session.getMatcher().start(), session.getMatcher().end());
				addParameter(events, "name", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
				events.setType(JS_EVENTS_ATTRIBUTE);
				enterState(session, JS_EVENTS_ATTRIBUTE, currentState.node, 1);
				
				attachJsDoc(events);
			}			
		}
		.setName(JS_EVENTS_ATTRIBUTE)
		.setDescription(JS_EVENTS_ATTRIBUTE_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				RegExAstNode routes = addToCategory(JS_ATTRIBUTE_CATEGORY, currentState.node, NAME, JS_ROUTES_ATTRIBUTE, session.getMatcher().start(), session.getMatcher().end());
				addParameter(routes, "name", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
				routes.setType(JS_ROUTES_ATTRIBUTE);
				enterState(session, JS_ROUTES_ATTRIBUTE, currentState.node, 1);
				
				attachJsDoc(routes);
			}			
		}
		.setName(JS_ROUTES_ATTRIBUTE)
		.setDescription(JS_ROUTES_ATTRIBUTE_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				RegExAstNode attr = addToCategory(JS_ATTRIBUTE_CATEGORY, currentState.node, NAME, JS_ATTRIBUTE, session.getMatcher().start(), session.getMatcher().end());
				attr.setType(JS_ATTRIBUTE);
				attr.setNextSiblingInsertPoint(attr.getOffset() + attr.getLength());
				addParameter(attr, NAME, session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
				addParameter(attr, "defaultValue", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
				
				attachJsDoc(attr);
				
//				enterState(session, JS_ATTRIBUTE, attr, 1);
			}			
		}
		.setName(JS_ATTRIBUTE)
		.setDescription(JS_ATTRIBUTE_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(JS_EVENTS_ATTRIBUTE)) {
					RegExAstNode event = addToCategory(JS_EVENTS_ATTRIBUTE_CATEGORY, currentState.node, "event", JS_EVENTS_ATTRIBUTE_ENTRY, session.getMatcher().start(), session.getMatcher().end());
					event.setType(JS_EVENTS_ATTRIBUTE_ENTRY);
					addParameter(event, "event", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(event, "selector", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
					addParameter(event, "handler", session.getCurrentSubMatchesForCurrentRegex()[2], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 3), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 3));
				}
			}			
		}
		.setName(JS_EVENTS_ATTRIBUTE_ENTRY)
		.setDescription(JS_EVENTS_ATTRIBUTE_ENTRY_REGEX)
		);	
		
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new CodeSyncRegexAction() {

			@Override
			public void executeAction(RegexProcessingSession session) {
				if (currentState.category.equals(JS_ROUTES_ATTRIBUTE)) {
					RegExAstNode route = addToCategory(JS_ROUTES_ATTRIBUTE_CATEGORY, currentState.node, "path", JS_ROUTES_ATTRIBUTE_ENTRY, session.getMatcher().start(), session.getMatcher().end());
					route.setType(JS_ROUTES_ATTRIBUTE_ENTRY);
					addParameter(route, "path", session.getCurrentSubMatchesForCurrentRegex()[0], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 1), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 1));
					addParameter(route, "function", session.getCurrentSubMatchesForCurrentRegex()[1], session.getMatcher().start(session.getCurrentMatchGroupIndex() + 2), session.getMatcher().end(session.getCurrentMatchGroupIndex() + 2));
				}
			}			
		}
		.setName(JS_ROUTES_ATTRIBUTE_ENTRY)
		.setDescription(JS_ROUTES_ATTRIBUTE_ENTRY_REGEX)
		);

	}
	
	protected static void enterState(RegexProcessingSession session, String state, RegExAstNode node, int increment) {
		session.currentNestingLevel += increment;
		currentState = new RegexEngineState(state, node);
		statesStack[session.currentNestingLevel] = currentState;
	}
	
	protected static void exitState(RegexProcessingSession session) {
		if (session.currentNestingLevel > 0) {
			if (statesStack[session.currentNestingLevel--] != null) {
				currentState.node.setLength(session.getMatcher().end() - currentState.node.getOffset());
				currentState.node.setNextSiblingInsertPoint(session.getMatcher().end());
				statesStack[session.currentNestingLevel + 1] = null;
				int index = session.currentNestingLevel;
				while (statesStack[index] == null) {
					index--;
				}
				currentState = statesStack[index];
			}
		}
	}
	
	protected static RegExAstNode createRegExAstNode(String type, String keyParameter, boolean isCategoryNode, int start, int end) {
		RegExAstNode node = RegExAstFactory.eINSTANCE.createRegExAstNode();
		node.setType(type);
		node.setKeyParameter(keyParameter);
		node.setOffset(start);
		node.setLength(end - start);
		return node;
	}
	
	protected RegExAstNode addToCategory(String category, RegExAstNode parent, String keyParameter, String type, int start, int end) {
		RegExAstNode node = createRegExAstNode(type, keyParameter, false, start, end);
//		getCategory(parent, category).getChildren().add(node);
		parent.getChildren().add(node);
		return node;
	}
	
	protected void addParameter(RegExAstNode node, String name, String value, int start, int end) {
		RegExAstNodeParameter parameter = RegExAstFactory.eINSTANCE.createRegExAstNodeParameter();
		parameter.setName(name);
		parameter.setValue(value);
		parameter.setValue(value);
		parameter.setOffset(start);
		parameter.setLength(end - start);
		node.getParameters().add(parameter);
	}
	
	private String getParameterValue(RegExAstNode node, String name) {
		for (RegExAstNodeParameter parameter : node.getParameters()) {
			if (parameter.getName().equals(name)) {
				return parameter.getValue();
			}
		}
		return "";
	}
	
	protected void attachJsDoc(RegExAstNode node) {
		if (jsDoc != null) {
			node.getChildren().add(jsDoc);
			jsDoc = null;
		}
	}
	
	private void print(String indent, RegExAstNode node) {
		String label = new String(node.getType() + " ");
		label += getParameterValue(node, node.getKeyParameter());
		if (node.getOffset() != 0 || node.getLength() != 0) {
			label += " (" + node.getOffset() + ", " + node.getLength() + ") ";
		}
		label += ": ";
		for (RegExAstNodeParameter parameter : node.getParameters()) {
			if (parameter.getOffset() != 0 || parameter.getLength() != 0) {
				label += String.format("(%s = %s) (%s, %s), ", 
						parameter.getName(),
						parameter.getValue().replaceAll("\n", ""),
						parameter.getOffset(),
						parameter.getLength());
			} else {
				label += String.format("(%s = %s), ",
						parameter.getName(),
						parameter.getValue().replace("\n", ""));
			}
		}
		label = label.substring(0, label.length() - 2);
		System.out.println(indent + label);
		
		for (RegExAstNode child : node.getChildren()) {
			print(indent + "	", child);
		}
	}
	
}
