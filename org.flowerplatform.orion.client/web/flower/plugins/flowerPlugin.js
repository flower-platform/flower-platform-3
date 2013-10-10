define([
	'orion/plugin'
	], function(PluginProvider) {
	
	var headers = {
		name: "Flower Platform Editor Plugin",
		version: "1.0",
		description: "This plugin provides editor ..."
	};

	var provider = new PluginProvider(headers);
	provider.registerServiceProvider("orion.page.content", {}, {
	     id: "orion.flower.content",
	     name: "Flower",	   
	     uriTemplate: "/flowerplatform/main.jsp"
	});
	     
	provider.registerService("orion.core.contenttype", {}, {
		contentTypes:
			// Text types
			[{	id: "flower/diagram",
				name: "Diagram",
				extension: ["notation"]				
			}]
		});
	
	provider.registerServiceProvider("orion.edit.editor", {}, {
		id: "orion.flower",
	    name: "Flower Platform Editor",
	    uriTemplate: "{OrionHome}/flowerplatform/flowerContent.html#{Location},contentProvider=orion.flower.content"
	});
	     		
	provider.registerService("orion.navigate.openWith", {}, {
		editor: "orion.flower",
		contentType: ["text/plain", "text/html", "text/css", "application/javascript", "application/json", "application/xml", "text/x-java-source", "flower/diagram"]});
	
	provider.registerService("orion.navigate.openWith.default", {}, {
		editor: "orion.flower"});
	
	provider.registerService("orion.page.link", null, {
		name: "Flower Editor",
		uriTemplate: "{OrionHome}/flowerplatform/flowerContent.html#{Location},contentProvider=orion.flower.content"
	});	
	
	
	provider.connect();
});