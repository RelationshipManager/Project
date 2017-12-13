package com.example.zhang.relationshipManager.models;


public class Relationship {
    public static final int FRIENDS = 0;//朋友关系
    public static final int COLLEAGUES = 1;//同事关系
    private Contact mStartContact;
    private Contact mEndContact;
    private String mStartRole;
    private String mEndRole;
    private int mRelationshipType;

    public Relationship(){
        mStartContact = new Contact();
        mEndContact = new Contact();
        mStartRole = "";
        mEndRole = "";
        mRelationshipType = 0;
    }

    public Contact getStartContact() {
        return mStartContact;
    }

    public void setStartContact(Contact startContact) {
        mStartContact = startContact;
    }

    public Contact getEndContact() {
        return mEndContact;
    }

    public void setEndContact(Contact endContact) {
        mEndContact = endContact;
    }

    public String getRole(Contact contact) {
        if (contact == mStartContact)
            return mStartRole;
        else if (contact == mEndContact)
            return mEndRole;
        else
            return "";
    }

    public int getRelationshipType() {
        return mRelationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        mRelationshipType = relationshipType;
    }
}
