DROP KEYSPACE test;

CREATE KEYSPACE test
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};

USE test;

CREATE TABLE repository ( 
	
	repoId timeuuid,
	path text,
	// resources info is in the default design for this repo
	 
	PRIMARY KEY (repoId) 
);

CREATE TABLE design (
	
	designId timeuuid,
	repoId timeuuid,
	path text,
	resources map <timeuuid, text>,
	resourcesAlias map <text, timeuuid>,
	
	PRIMARY KEY (designId)
);

CREATE TABLE resource (
	
	resourceId timeuuid,
	designId timeuuid,
	path text,
	
	PRIMARY KEY (designId, resourceId)	
);

CREATE TABLE diagram (
	
	designId timeuuid,
	resourceId timeuuid,
	diagramId timeuuid,
	
	smth text,
	
	PRIMARY KEY (designId, resourceId, diagramId)	
);

CREATE TABLE node (
	
	designId timeuuid,
	resourceId timeuuid,
	nodeId timeuuid,
	
	parentId timeuuid,
	name text,
	
	diagrammableElementResourceId timeuuid,
	diagrammableElementId timeuuid,
	
	PRIMARY KEY (designId, resourceId, nodeId)
);

CREATE INDEX nodes_by_parent ON node (parentId);

CREATE TABLE codeSyncElement (
	
	designId timeuuid,
	resourceId timeuuid,
	cseId timeuuid,
	
	parentId timeuuid,
	name text,
	
	PRIMARY KEY (designId, resourceId, cseId)	
);

CREATE INDEX cse_by_parent ON codeSyncElement (parentId);

