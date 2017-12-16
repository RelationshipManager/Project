package com.example.zhang.relationshipManager.models;

public class RelationshipManager extends DatabaseHelper{
    private static RelationshipManager sRelationshipManager;

    public static RelationshipManager getInstance() {
        if (sRelationshipManager == null)
            sRelationshipManager = new RelationshipManager();
        return sRelationshipManager;
    }

    public void getRelationship(Contact contact1, Contact contact2) {

    }

}
