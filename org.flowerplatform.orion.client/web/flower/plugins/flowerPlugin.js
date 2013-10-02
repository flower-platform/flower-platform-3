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
	     uriTemplate: "http://localhost:8080/org.flowerplatform.web.app/MainWeb.html"
	});
	     
	provider.registerServiceProvider("orion.edit.editor", {}, {
		id: "orion.flower",
	    name: "Flower Platform Editor",
	    uriTemplate: "{OrionHome}/flower/flowerContent.html#{Location},contentProvider=orion.flower.content"
	});
	     
	provider.registerServiceProvider("orion.navigate.openWith", {}, {
		editor: "orion.flower",
	    contentType: ["image/gif", "image/jpeg", "image/ico", "image/png","image/tiff"]
	});
		
	provider.registerService("orion.page.link", null, {
		name: "Flower Editor",		
		uriTemplate: "{OrionHome}/flower/flowerContent.html#{Location},contentProvider=orion.flower.content"
	});	
	
	provider.connect();
});