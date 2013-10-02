function getURL() /* : String */ {
	// Using the string property and not the window.location object.
	try {
		var url = window.location.href; // Obtaining url, if exceptions then skip to next. 
		if (url.length != 0) // Must have some characters or skip to next. If null then skip to next.
			return url;
	} catch(err) { }

	try {
		var url = window.document.URL;
		if (url.length != 0)
			return url;
	} catch(err) { }
	
	// document.location seems to be deprecated to window.location
	try {
		var url = document.location.href; 
		if (url.length != 0) 
			return url;
	} catch(err) { }

	alert("Could not obtain URL !");
}