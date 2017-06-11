package com.example.zhang.relationshipManager.models;

/**
 * Created by 10040 on 2017/6/11.
 */

public class Relationship {
    private Person mSourcePerson;
    private Person mTargetPerson;
    private String mRelationshipType;

    public Relationship(Person sourcePerson, Person targetPerson, String relationshipType) {
        mSourcePerson = sourcePerson;
        mTargetPerson = targetPerson;
        mRelationshipType = relationshipType;
    }

    public Person getSourcePerson() {
        return mSourcePerson;
    }

    public void setSourcePerson(Person sourcePerson) {
        mSourcePerson = sourcePerson;
    }

    public Person getTargetPerson() {
        return mTargetPerson;
    }

    public void setTargetPerson(Person targetPerson) {
        mTargetPerson = targetPerson;
    }

    public String getRelationshipType() {
        return mRelationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        mRelationshipType = relationshipType;
    }

}
