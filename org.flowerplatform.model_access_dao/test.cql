//CREATE KEYSPACE test
//WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};

USE test;

DROP TABLE cse;
DROP TABLE relation;
DROP TABLE node;

CREATE TABLE cse ( 
	
	name text,
	type text,
	featureValues map <text, text>,
	
	cseId text,
	repoId text,
	
	parentId text,
	
	PRIMARY KEY (repoId, cseId)
);

CREATE TABLE cse_child ( 
	
	childName text,
	childType text,
	childFeatureValues map <text, text>,
	
	childId text,
	
	parentId text,
	repoId text,
	
	PRIMARY KEY (repoId, parentId, childId)
);

CREATE TABLE relation ( 
	
	sourceId text,
	targetId text,
	
	relationId text,
	repoId text,
	
	PRIMARY KEY (repoId, relationId) 
);

CREATE TABLE node ( 
	
	name text,
	
	nodeId text,
	repoId text,

	parentId text, 
	childIndex text,

	PRIMARY KEY (repoId, nodeId) 
);



//
//CREATE INDEX cse_by_parent ON cse (parentId);
//
//INSERT INTO cse (name, cseId, repoId, ddId, resourceId, parentId, childIndex)
//VALUES ('src', 'folder1', 'repo1', 'dd1', 'cse', ' ', 0);
//
//INSERT INTO cse (name, cseId, repoId, ddId, resourceId, parentId, childIndex)
//VALUES ('MyClass', 'cls1', 'repo1', 'dd1', 'cse', 'folder1', 0);
//
//INSERT INTO cse (name, cseId, repoId, ddId, resourceId, parentId, childIndex)
//VALUES ('MyOtherClass', 'cls2', 'repo1', 'dd1', 'cse', 'folder1', 1);
//
//SELECT * FROM cse;
//
//INSERT INTO cse (name, cseId, repoId, ddId, resourceId, parentId, childIndex)
//VALUES ('MyOtherClass', 'cls2', 'repo1', 'dd1', 'cse', 'folder1', 3);
//
//SELECT * FROM cse;

//SELECT * FROM cse WHERE token(repoId) > token('rep') AND ddId > 'd' AND parentId = 'folder1' LIMIT 10000 ALLOW FILTERING;

//SELECT * FROM cse WHERE parentId = 'folder1';

//SELECT * FROM cse WHERE parentid = 'folder1';
//
//SELECT * FROM cse WHERE repoId = 'repo1' AND ddId = 'dd1' AND resourceId = 'cse' AND cseId = 'cls1';