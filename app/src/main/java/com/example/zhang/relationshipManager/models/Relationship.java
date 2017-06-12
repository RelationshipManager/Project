package com.example.zhang.relationshipManager.models;

import java.io.Serializable;

/**
 * Created by 10040 on 2017/6/11.
 */

public class Relationship implements Serializable {
    private Person mSourcePerson;
    private Person mTargetPerson;
    private String mSourceRole;
    private String mTargetRole;

    public Relationship(Person sourcePerson, Person targetPerson,String sourceRole, String targetRole) {
        mSourcePerson = sourcePerson;
        mTargetPerson = targetPerson;
        mSourceRole=sourceRole;
        mTargetRole = targetRole;
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

    public String getSourceRole() {
        return mSourceRole;
    }

    public void setSourceRole(String sourceRole) {
        mSourceRole = sourceRole;
    }

    public String getTargetRole() {
        return mTargetRole;
    }

    public void setTargetRole(String relationshipType) {
        mTargetRole = relationshipType;
    }

}
