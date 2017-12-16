package com.example.zhang.relationshipManager.models;


public class Relationship {
    private Contact mStartContact;
    private Contact mEndContact;
    private RsType mRsType;

    public Relationship() {
        mStartContact = new Contact();
        mEndContact = new Contact();
        mRsType = new RsType();
    }

    public Relationship(Contact startContact, Contact endContact, RsType rsType) {
        mStartContact = startContact;
        mEndContact = endContact;
        mRsType = rsType;
    }

    //获取联系人在关系中的角色
    public String getRole(Contact contact) {
        if (contact == mStartContact)
            return mRsType.getStartRole();
        else if (contact == mEndContact)
            return mRsType.getEndRole();
        else
            return "";
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

    public RsType getRsType() {
        return mRsType;
    }

    public void setRsType(RsType rsType) {
        mRsType = rsType;
    }
}
