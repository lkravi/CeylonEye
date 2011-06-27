package org.aa.social;

import org.neo4j.graphdb.RelationshipType;

public enum social_rel implements RelationshipType {
	USER,
	HOME_TOWN,
	LIVES_IN,
	WORKS_IN
	
}
