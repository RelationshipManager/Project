package com.example.zhang.relationshipManager.models;

import java.util.ArrayList;


public class RelationshipManager {
    private static RelationshipManager sRelationshipManager;

    public static RelationshipManager getInstance(){
        if (sRelationshipManager == null)
            sRelationshipManager = new RelationshipManager();
        return sRelationshipManager;
    }

    public void getRelationship(Contact contact1,Contact contact2){

    }

}
