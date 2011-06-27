package org.aa.geo;

import org.neo4j.graphdb.RelationshipType;

public enum geo_rel implements RelationshipType {
	PRIMARY_NODE, 
	SECONDARY_NODE
}
