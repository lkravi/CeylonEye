package org.aa.admin;

import org.neo4j.graphdb.RelationshipType;

public enum admin_rel implements RelationshipType {
	SOCIAL,
	GEO, 
	PROVINCE,
	DISTRICT, 
	LOGGED_USERS,	
	LOGGED_USER
}
