CREATE TABLE term(
	termId INTEGER PRIMARY KEY,
	termValue VARCHAR(255) NOT NULL,
	termFrequency INTEGER NOT NULL,
	UNIQUE (termValue)
);

CREATE TABLE field_top_syntag_term(
	fieldId INTEGER NOT NULL,
	termId INTEGER NOT NULL,
	score FLOAT NOT NULL,
	PRIMARY KEY (fieldId, termId)
);

CREATE TABLE term_matrix(
	termId1 INTEGER NOT NULL,
	termId2 INTEGER NOT NULL,
	cooc INTEGER NOT NULL,
	syntag FLOAT, 
	PRIMARY KEY (termId1, termId2)
);

CREATE TABLE top_related_term(
	termId INTEGER PRIMARY KEY,
	topTermId INTEGER,
	syntag FLOAT
);

CREATE TABLE entity(
	entityId INTEGER PRIMARY KEY,
	entityName VARCHAR(255) NOT NULL,
	UNIQUE (entityName)
);

CREATE TABLE entity_field(
	fieldId INTEGER NOT NULL PRIMARY KEY,
	fieldType INTEGER NOT NULL,
	entityId INTEGER NOT NULL
);

CREATE TABLE field_term(
	fieldId INTEGER NOT NULL,
	termId INTEGER NOT NULL,
	PRIMARY KEY (fieldId, termId)
);