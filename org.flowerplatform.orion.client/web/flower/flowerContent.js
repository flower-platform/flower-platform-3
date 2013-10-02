define(['i18n!orion/navigate/nls/messages', 'require', 'orion/webui/littlelib', 'orion/bootstrap', 'orion/status', 'orion/progress', 'orion/commandRegistry', 'orion/fileClient', 'orion/operationsClient',
	        'orion/searchClient', 'orion/globalCommands', 'orion/URITemplate', 'orion/PageUtil', 'orion/PageLinks', 'orion/selection', 'orion/contentTypes', 'orion/fileCommands', 'orion/extensionCommands',
	        'orion/explorers/explorer-table', 'orion/explorers/navigatorRenderer', 'orion/fileUtils', 'orion/keyBinding', 'orion/navoutliner', 'orion/URL-shim'], 
			function(messages, require, lib, mBootstrap, mStatus, mProgress, mCommandRegistry, mFileClient, mOperationsClient, mSearchClient, 
			mGlobalCommands, URITemplate, PageUtil, PageLinks, mSelection, mContentTypes, mFileCommands, mExtensionCommands, mExplorerTable, mNavigatorRenderer, mFileUtils, 
			KeyBinding, mNavOutliner) {

	mBootstrap.startup().then(function(core) {
		var serviceRegistry = core.serviceRegistry;
		var preferences = core.preferences;
		
		var selection = new mSelection.Selection(serviceRegistry);
		var operationsClient = new mOperationsClient.OperationsClient(serviceRegistry);
		new mStatus.StatusReportingService(serviceRegistry, operationsClient, "statusPane", "notifications", "notificationArea"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		
		var commandRegistry = new mCommandRegistry.CommandRegistry({selection: selection});
		var progressService = new mProgress.ProgressService(serviceRegistry, operationsClient, commandRegistry);
		var fileClient = new mFileClient.FileClient(serviceRegistry);
		var searcher = new mSearchClient.Searcher({serviceRegistry: serviceRegistry, commandService: commandRegistry, fileService: fileClient});

		var fileMetadata;
		var orionHome = PageLinks.getOrionHome();
			
		/**
		 * Utility method for saving file contents to a specified location
		 */
		function saveFileContents(fileClient, targetMetadata, contents, afterSave) {
			var etag = targetMetadata.ETag;
			var args = { "ETag" : etag }; //$NON-NLS-0$
			progressService.progress(fileClient.write(targetMetadata.Location, contents, args), "Saving file " + targetMetadata.Location).then(
				function(result) {
					if (afterSave) {
						afterSave();
					}
				},
				/* error handling */
				function(error) {
					// expected error - HTTP 412 Precondition Failed 
					// occurs when file is out of sync with the server
					if (error.status === 412) {
						var forceSave = window.confirm(messages["Resource is out of sync with the server. Do you want to save it anyway?"]);
						if (forceSave) {
							// repeat save operation, but without ETag 
							progressService.progress(fileClient.write(targetMetadata.Location, contents), "Saving file " + targetMetadata.Location).then(
								function(result) {
										targetMetadata.ETag = result.ETag;
										if (afterSave) {
											afterSave();
										}
								}
							);
						}
					}
					// unknown error
					else {
						error.log = true;
					}
				}
			);
		}
		
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
						
						function makeIFrame() {
							var parent = lib.node("delegatedContent"); //$NON-NLS-0$
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
						}
						
						// TODO should we have the plugin specify whether it needs a Location?
						// If there is metadata, we want to fill in the location object with the name.
						if (locationObject.Location && locationObject.Location.length > 0) {
							progressService.progress(fileClient.read(locationObject.Location, true), "Getting content of " + locationObject.Location).then(
								function(metadata) {
									if (metadata) {
										// store info used for iframe and saving
										locationObject.Name = metadata.Name;
										fileMetadata = metadata;
										mGlobalCommands.setPageTarget(
											{task: info.name, location: metadata.Location, target: metadata, serviceRegistry: serviceRegistry, 
												commandService: commandRegistry, searchService: searcher, fileService: fileClient});
										makeIFrame();
									}
					
								},  
								// TODO couldn't read metadata, try to make iframe anyway.
								function() {
									mGlobalCommands.setPageTarget({task: info.name, title: info.name});
									makeIFrame();
								});
						} else {
							mGlobalCommands.setPageTarget({task: info.name, title: info.name});
							makeIFrame();
						}
						break;
					}
				}
			}
			if (!foundContent) {
				var parent = lib.node("delegatedContent"); //$NON-NLS-0$
				lib.empty(parent);
				var message = document.createElement("div"); //$NON-NLS-0$
				message.appendChild(document.createTextNode(messages["Plugin content could not be found"]));
				parent.appendChild(message);
			}
		}
		
		// Listen for events from our internal iframe.  This should eventually belong as part of the plugin registry.
		// This mechanism should become generalized into a "shell services" API for plugin iframes to contact the outer context.
		window.addEventListener("message", function(event) { //$NON-NLS-0$
			// For potentially dangerous actions, such as save, we will force the content to be from our domain (internal
			// save hook), which we know has given the user the change to look at the data before save.
			if (orionHome && fileMetadata && event.source.parent === window && event.origin === new URL(window.location).origin ) {
				if (typeof event.data === "string") { //$NON-NLS-0$
				var data = JSON.parse(event.data);
					if (data.shellService) {
						if (data.sourceLocation) {
							saveFileContents(fileClient, fileMetadata, {sourceLocation: data.sourceLocation}, function() {
								if (window.confirm(messages["Content has been saved.  Click OK to go to the navigator, Cancel to keep editing."])) {
									// go to the navigator
									window.location.href = orionHome + "/navigate/table.html#" + fileMetadata.Parents[0].ChildrenLocation; //$NON-NLS-0$
								} else {
									loadContent();
								}
							});
						}
					}
				}
			}
		}, false);
		
		mGlobalCommands.generateBanner("orion-delegatedContent", serviceRegistry, commandRegistry, preferences, searcher); //$NON-NLS-0$
		window.addEventListener("hashchange", function() { loadContent(); }, false); //$NON-NLS-0$
		loadContent();
				
		// global commands
		mGlobalCommands.setPageCommandExclusions(["eclipse.openWith", "orion.navigateFromMetadata"]); //$NON-NLS-1$ //$NON-NLS-0$
		
		var treeRoot = {
			children:[]
		};
				
		var contentTypeService = new mContentTypes.ContentTypeService(serviceRegistry);
		
		var explorer = new mExplorerTable.FileExplorer({
				serviceRegistry: serviceRegistry, 
				treeRoot: treeRoot, 
				selection: selection, 
				fileClient: fileClient, 
				parentId: "explorer-tree", //$NON-NLS-0$
				dragAndDrop: mFileCommands.uploadFile,
				rendererFactory: function(explorer) {
					var renderer = new mNavigatorRenderer.NavigatorRenderer({
						checkbox: false,						
						cachePrefix: "Navigator"}, explorer, commandRegistry, contentTypeService);  //$NON-NLS-0$
					renderer.oneColumn = true;
					return renderer;
				}});
		function setResource(resource) {
			window.location = new URITemplate("#{,resource,params*}").expand({ //$NON-NLS-0$
				resource: resource
			});
		}
		// On scope up, change the href of the window.location to navigate to the parent page.
		// TODO reuse eclipse.upFolder
		explorer.scopeUp = function() {
			var root = this.treeRoot, parents = root && root.Parents;
			if (parents) {
				var resource;
				if (parents.length === 0) {
					resource = fileClient.fileServiceRootURL(root.Location);
				} else {
					resource = parents[0].ChildrenLocation;
				}
				setResource(resource);
			}
		}.bind(explorer);
		explorer.addEventListener("rootMoved", function(event) { //$NON-NLS-0$
			setResource(event.newInput);
		});

		function refresh() {
			var pageParams = PageUtil.matchResourceParameters();
			// TODO working around https://bugs.eclipse.org/bugs/show_bug.cgi?id=373450
			explorer.loadResourceList("", false).then(function() {
				mGlobalCommands.setPageTarget({task: "Navigator", target: explorer.treeRoot,
					serviceRegistry: serviceRegistry, searchService: searcher, fileService: fileClient, commandService: commandRegistry});
				mFileCommands.updateNavTools(serviceRegistry, commandRegistry, explorer, "pageActions", "selectionTools", explorer.treeRoot, true); //$NON-NLS-1$ //$NON-NLS-0$
			});
		}
		refresh();
	
		// commands shared by navigators
		mFileCommands.createFileCommands(serviceRegistry, commandRegistry, explorer, fileClient); 
		
		// define the command contributions - where things appear, first the groups
		commandRegistry.addCommandGroup("pageActions", "orion.new", 1000, messages["New"], null, null, "core-sprite-addcontent", null, "dropdownSelection"); //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.addCommandGroup("pageActions", "orion.gitGroup", 200); //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.addCommandGroup("selectionTools", "orion.selectionGroup", 500, messages["Actions"], null, null, "core-sprite-gear", null, "dropdownSelection"); //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.addCommandGroup("selectionTools", "orion.importExportGroup", 100, null, "orion.selectionGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.addCommandGroup("selectionTools", "orion.newResources", 101, null, "orion.selectionGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		// commands that don't appear but have keybindings
		commandRegistry.registerCommandContribution("pageActions", "eclipse.copySelections", 1, null, true, new KeyBinding.KeyBinding('c', true)); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("pageActions", "eclipse.pasteSelections", 1, null, true, new KeyBinding.KeyBinding('v', true)); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		
		// commands appearing in nav tool bar
		commandRegistry.registerCommandContribution("pageActions", "eclipse.openResource", 500); //$NON-NLS-1$ //$NON-NLS-0$
		
		// new file and new folder in the nav bar (in a group)
		commandRegistry.registerCommandContribution("pageActions", "eclipse.newFile", 1, "orion.new"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("pageActions", "eclipse.newFolder", 2, "orion.new", false, null, new mCommandRegistry.URLBinding("newFolder", "name")); //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("navActions", "eclipse.upFolder", 3, null, false, new KeyBinding.KeyBinding(38, false, false, true)); //$NON-NLS-1$ //$NON-NLS-0$
		// new project creation in the toolbar (in a group)
		commandRegistry.registerCommandContribution("pageActions", "orion.new.project", 1, "orion.new"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("pageActions", "orion.new.linkProject", 2, "orion.new"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
	
		// selection based command contributions in nav toolbar
		var binding;
		binding = new KeyBinding.KeyBinding(113); // F2
		binding.domScope = "explorer-tree"; //$NON-NLS-0$
		binding.scopeName = messages["Navigator"];
		commandRegistry.registerCommandContribution("selectionTools", "eclipse.renameResource", 2, "orion.selectionGroup", false, binding); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "eclipse.copyFile", 3, "orion.selectionGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "eclipse.moveFile", 4, "orion.selectionGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		binding = new KeyBinding.KeyBinding(46); // Delete
		binding.domScope = "explorer-tree"; //$NON-NLS-0$
		binding.scopeName = messages["Navigator"];
		commandRegistry.registerCommandContribution("selectionTools", "eclipse.deleteFile", 5, "orion.selectionGroup", false, binding); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "eclipse.compareWithEachOther", 6, "orion.selectionGroup");  //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "eclipse.compareWith", 7, "orion.selectionGroup");  //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "orion.importZipURL", 1, "orion.selectionGroup/orion.importExportGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "orion.import", 2, "orion.selectionGroup/orion.importExportGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "eclipse.downloadFile", 3, "orion.selectionGroup/orion.importExportGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "orion.importSFTP", 4, "orion.selectionGroup/orion.importExportGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		commandRegistry.registerCommandContribution("selectionTools", "eclipse.exportSFTPCommand", 5, "orion.selectionGroup/orion.importExportGroup"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
			
		mExtensionCommands.createAndPlaceFileCommandsExtension(serviceRegistry, commandRegistry, "selectionTools", 0, "orion.selectionGroup", true).then(function() { //$NON-NLS-1$ //$NON-NLS-0$
			// If no selection, we have to build against the treeRoot
			// This should be in the file explorer.
			mFileCommands.updateNavTools(serviceRegistry, commandRegistry, explorer, "pageActions", "selectionTools", explorer.treeRoot, true); //$NON-NLS-1$ //$NON-NLS-0$
			explorer.updateCommands();
			explorer.addEventListener("rootChanged", function(event) {
				var folderNavToolbar = lib.node("navActions");
				if (folderNavToolbar) {
					commandRegistry.destroy(folderNavToolbar);
					commandRegistry.renderCommands(folderNavToolbar.id, folderNavToolbar, event.root, null, "tool"); //$NON-NLS-0$
				} else {
					throw new Error("could not find toolbar navActions"); //$NON-NLS-0$
				}
			});
			// Must happen after the above call, so that all the open with commands are registered when we create our navigation links.
			new mNavOutliner.NavigationOutliner({parent: "fileSystems", commandService: commandRegistry, serviceRegistry: serviceRegistry}); //$NON-NLS-0$
		});

		window.addEventListener("hashchange", function() {refresh();}, false); //$NON-NLS-0$
	});
});