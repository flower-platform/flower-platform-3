define(['i18n!orion/navigate/nls/messages', 'require', 'orion/webui/littlelib', 'orion/bootstrap', 'orion/status', 'orion/progress', 'orion/commandRegistry', 'orion/fileClient', 'orion/operationsClient',
	        'orion/searchClient', 'orion/globalCommands', 'orion/URITemplate', 'orion/PageUtil', 'orion/PageLinks', 'orion/selection', 'orion/contentTypes', 'orion/fileCommands', 'orion/extensionCommands',
	        'orion/explorers/explorer-table', 'orion/explorers/navigatorRenderer', 'orion/fileUtils', 'orion/keyBinding', 'orion/outliner', 'orion/inputManager', 
	        'orion/folderView', 'orion/editorView', 'orion/blameAnnotations', 'orion/problems', 'orion/EventTarget', 'orion/sidebar', 'orion/i18nUtil', 'orion/URL-shim'], 
			function(messages, require, lib, mBootstrap, mStatus, mProgress, mCommandRegistry, mFileClient, mOperationsClient, mSearchClient, 
			mGlobalCommands, URITemplate, PageUtil, PageLinks, mSelection, mContentTypes, mFileCommands, mExtensionCommands, mExplorerTable, mNavigatorRenderer, mFileUtils, 
			KeyBinding, mOutliner, mInputManager, mFolderView, mEditorView, mBlameAnnotation, mProblems, EventTarget, Sidebar, i18nUtil) {

	mBootstrap.startup().then(function(core) {
		var serviceRegistry = core.serviceRegistry;
		var preferences = core.preferences;
		
		var selection = new mSelection.Selection(serviceRegistry);
		var operationsClient = new mOperationsClient.OperationsClient(serviceRegistry);
		var statusReportingService = new mStatus.StatusReportingService(serviceRegistry, operationsClient, "statusPane", "notifications", "notificationArea"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		
		var commandRegistry = new mCommandRegistry.CommandRegistry({selection: selection});
		var progressService = new mProgress.ProgressService(serviceRegistry, operationsClient, commandRegistry);
		var fileClient = new mFileClient.FileClient(serviceRegistry);
		var searcher = new mSearchClient.Searcher({serviceRegistry: serviceRegistry, commandService: commandRegistry, fileService: fileClient});
		var outlineService = new mOutliner.OutlineService({serviceRegistry: serviceRegistry, preferences: preferences});
		contentTypeRegistry = new mContentTypes.ContentTypeService(serviceRegistry);
		
		new mBlameAnnotation.BlameService(serviceRegistry);
		new mProblems.ProblemService(serviceRegistry);
		
		var sidebarDomNode = lib.node("sidebar"), //$NON-NLS-0$
		sidebarToolbar = lib.node("sidebarToolbar"), //$NON-NLS-0$
		editorDomNode = lib.node("editor"); //$NON-NLS-0$

		var editor, inputManager, folderView, editorView;
		function renderToolbars(metadata) {
			var toolbar = lib.node("pageActions"); //$NON-NLS-0$
			if (toolbar) {
				if (metadata) {
					// now add any "orion.navigate.command" commands that should be shown in non-nav pages.
					mExtensionCommands.createAndPlaceFileCommandsExtension(serviceRegistry, commandRegistry, toolbar.id, 500).then(function() {
						commandRegistry.destroy(toolbar);
						commandRegistry.renderCommands(toolbar.id, toolbar, metadata, editor, "button"); //$NON-NLS-0$
					});
				} else {
					commandRegistry.destroy(toolbar);
				}
			}
			var rightToolbar = lib.node("pageNavigationActions"); //$NON-NLS-0$
			if (rightToolbar) {
				commandRegistry.destroy(rightToolbar);
				if (metadata) {
					commandRegistry.renderCommands(rightToolbar.id, rightToolbar, metadata, editor, "button"); //$NON-NLS-0$
				}
			}
			var settingsToolbar = lib.node("settingsActions"); //$NON-NLS-0$
			if (settingsToolbar) {
				commandRegistry.destroy(settingsToolbar);
				if (metadata) {
					commandRegistry.renderCommands(settingsToolbar.id, settingsToolbar, metadata, editor, "button"); //$NON-NLS-0$
				}
			}
		}
	
		var sidebarNavBreadcrumb = function(/**HTMLAnchorElement*/ segment, folderLocation, folder) {
			// Link to this page (edit page)
			segment.href = new URITemplate("#{,resource,params*}").expand({ //$NON-NLS-0$
				resource: inputManager.getInput() || "", //$NON-NLS-0$
				params: {
					navigate: folder ? folder.ChildrenLocation : fileClient.fileServiceRootURL(folderLocation || "")  //$NON-NLS-0$
				}
			});
		};
	
		inputManager = new mInputManager.InputManager({
			serviceRegistry: serviceRegistry,
			fileClient: fileClient,
			progressService: progressService,
			selection: selection,
			contentTypeRegistry: contentTypeRegistry
		});
		editorView = new mEditorView.EditorView({
			parent: editorDomNode,
			renderToolbars: renderToolbars,
			fileService: fileClient,
			progressService: progressService,
			serviceRegistry: serviceRegistry,
			statusService: statusReportingService,
			inputManager: inputManager,
			preferences: preferences,
			readonly: false,
			searcher: searcher,
			commandRegistry: commandRegistry,
			contentTypeRegistry: contentTypeRegistry
		});
		editor = editorView.editor;
		inputManager.editor = editor;
	
		loadContent();
		
		inputManager.addEventListener("InputChanged", function(evt) { //$NON-NLS-0$
			var metadata = evt.metadata;
			renderToolbars(metadata);
			if (evt.input === null || evt.input === undefined) {
				return;
			}
			changeSelection(metadata);				
		});
	
		// Sidebar
		function SidebarNavInputManager() {
			EventTarget.attach(this);
		}
		SidebarNavInputManager.prototype.processHash = function() {
			var newParams = PageUtil.matchResourceParameters(location.hash), navigate = newParams.navigate;
			if (typeof navigate === "string" || !newParams.resource) { //$NON-NLS-0$
				var input = navigate || "";
				this.dispatchEvent({type: "InputChanged", input: input}); //$NON-NLS-0$
			}
		};
		var sidebarNavInputManager = new SidebarNavInputManager();
		var sidebar = new Sidebar({
			commandRegistry: commandRegistry,
			contentTypeRegistry: contentTypeRegistry,
			editorInputManager: inputManager,
			fileClient: fileClient,
			outlineService: outlineService,
			parent: sidebarDomNode,
			progressService: progressService,
			selection: selection,
			serviceRegistry: serviceRegistry,
			sidebarNavInputManager: sidebarNavInputManager,
			toolbar: sidebarToolbar
		});
		sidebar.show();
		sidebarNavInputManager.addEventListener("rootChanged", function(evt) { //$NON-NLS-0$
			var root = evt.root;
			// update the navigate param, if it's present, or if this was a user action
			var pageParams = PageUtil.matchResourceParameters(location.hash);
			if (evt.force || Object.prototype.hasOwnProperty.call(pageParams, "navigate")) {//$NON-NLS-0$
				var params = {};
				params.resource = pageParams.resource || ""; //$NON-NLS-0$
				params.params = { navigate: root.Path };
				window.location = new URITemplate("#{,resource,params*}").expand(params); //$NON-NLS-0$
			}
			if (!pageParams.resource) {
				// No primary resource (editor file), so target the folder being navigated in the sidebar.
				mGlobalCommands.setPageTarget({
					task: "Coding", //$NON-NLS-0$
					name: root.Name,
					target: root,
					makeBreadcrumbLink: sidebarNavBreadcrumb,
					serviceRegistry: serviceRegistry,
					commandService: commandRegistry,
					searchService: searcher,
					fileService: fileClient
				});
			}
		});
		sidebarNavInputManager.addEventListener("editorInputMoved", function(evt) { //$NON-NLS-0$
			var newInput = evt.newInput, parent = evt.parent;
			var params = {};
			// If we don't know where the file went, go to "no editor"
			params.resource = newInput || "";
			params.params = {
				navigate: parent
			};
			window.location = new URITemplate("#{,resource,params*}").expand(params); //$NON-NLS-0$
		});
	
		editor.addEventListener("DirtyChanged", function(evt) { //$NON-NLS-0$
			mGlobalCommands.setDirtyIndicator(editor.isDirty());
		});
	
		selection.addEventListener("selectionChanged", function(event) { //$NON-NLS-0$
			inputManager.setInput(event.selection);
		});
		window.addEventListener("hashchange", function() { //$NON-NLS-0$
			inputManager.setInput(window.location.hash);
			// inform the sidebar
			sidebarNavInputManager.processHash(window.location.hash);
		});
		inputManager.setInput(window.location.hash);
		sidebarNavInputManager.processHash(window.location.hash);
	
		//mGlobalCommands.setPageCommandExclusions(["orion.editFromMetadata"]); //$NON-NLS-0$
		mGlobalCommands.generateBanner("orion-delegatedContent", serviceRegistry, commandRegistry, preferences, searcher, editor, editor, window.location.hash !== ""); //$NON-NLS-0$
	
		window.onbeforeunload = function() {
			if (editor.isDirty()) {
				 return messages.unsavedChanges;
			}
		};
			
		var orionHome = PageLinks.getOrionHome();
		
		function loadContent() {
			var foundContent = false;
			var params = PageUtil.matchResourceParameters(window.location.href);
			var nonHash = window.location.href.split('#')[0]; //$NON-NLS-0$
			// TODO: should not be necessary, see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=373450
			var locationObject = {OrionHome: orionHome, Location: params.resource};
			if (params.contentProvider) {
				// Note that the shape of the "orion.page.content" extension is not in any shape or form that could be considered final.
				// We've included it to enable experimentation. Please provide feedback on IRC or bugzilla.
		
				// The shape of the extension is:
				// info - information about the extension (object)
				//		required attribute: name - the name to be used in the page title and orion page heading
				//		required attribute: id - the id of the content contribution
				//		required attribute: uriTemplate - a uriTemplate that expands to the URL of the content to be placed in a content iframe
				//		optional attribute: saveToken - if specified, this token (or array of tokens) should be used to find a content URL provided inside a save URL
				//		optional attribute: saveTokenTerminator - if specified this terminator (or array of terminators) should be used to find the 
				//			end of a content URL provided in a save URL
				var contentProviders = serviceRegistry.getServiceReferences("orion.page.content"); //$NON-NLS-0$
				for (var i=0; i<contentProviders.length; i++) {
					// Exclude any navigation commands themselves, since we are the navigator.
					var id = contentProviders[i].getProperty("id"); //$NON-NLS-0$
					if (id === params.contentProvider) {
						var impl = serviceRegistry.getService(contentProviders[i]);
						var info = {};
						var propertyNames = contentProviders[i].getPropertyKeys();
						for (var j = 0; j < propertyNames.length; j++) {
							info[propertyNames[j]] = contentProviders[i].getProperty(propertyNames[j]);
						}
						foundContent = true;
						locationObject.ExitURL = orionHome+"/content/exit.html"; //$NON-NLS-0$
						if (info.saveToken) {
							// we need to set up a SaveURL for the iframe to use.
							locationObject.SaveURL = orionHome+"/content/saveHook.html#" + params.resource + ",contentProvider=" + params.contentProvider + ","; //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
						}
												
						var parent = lib.node("flowerEditor"); //$NON-NLS-0$
						var uriTemplate = new URITemplate(info.uriTemplate);
						var href = uriTemplate.expand(locationObject);
						var iframe = document.createElement("iframe"); //$NON-NLS-0$
						iframe.id = id; //$NON-NLS-0$
						iframe.type = "text/html"; //$NON-NLS-0$
						iframe.x = "100"; //$NON-NLS-0$
						iframe.width = "100%"; //$NON-NLS-0$
						iframe.height = "100%"; //$NON-NLS-0$
						iframe.frameborder= 0; //$NON-NLS-0$
						iframe.src = href; //$NON-NLS-0$
						lib.empty(parent);
						parent.appendChild(iframe); 
																		
						break;
					}
				}
			}
			if (!foundContent) {
				var parent = lib.node("flowerEditor"); //$NON-NLS-0$
				lib.empty(parent);
				var message = document.createElement("div"); //$NON-NLS-0$
				message.appendChild(document.createTextNode(messages["Plugin content could not be found"]));
				parent.appendChild(message);
			}
		}
		
        function getFlowerPlatformApp() {
        	var iframe = document.getElementById('orion.flower.content');  
        	var contDoc = iframe.contentDocument || iframe.contentWindow.document;
        	        	
        	return contDoc.getElementById("FlexHostApp");        	
        } 
        
        function changeSelection(metadata) {
        	if (metadata && metadata.Directory) {
				return;
			}			
			getFlowerPlatformApp().handleLink("openResourcesFromOrion=" + metadata.Location);		
        }
	});
});