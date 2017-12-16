package com.example.zhang.relationshipManager.models;

/**
 * Created by 10040 on 2017/12/16.
 */

public class RsType {
    public static final int FRIENDS = 0;//朋友关系
    public static final int COLLEAGUES = 1;//同事关系
    private int mId;
    private String mStartRole;
    private String mEndRole;
    private int mRelationshipType;

    public RsType() {
        mId = 1;
        mStartRole = "朋友";
        mEndRole = "朋友";
        mRelationshipType = FRIENDS;
    }

    public RsType(int id, String startRole, String endRole, int relationshipType) {
        mId = id;
        mStartRole = startRole;
        mEndRole = endRole;
        mRelationshipType = relationshipType;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getStartRole() {
        return mStartRole;
    }

    public void setStartRole(String startRole) {
        mStartRole = startRole;
    }

    public String getEndRole() {
        return mEndRole;
    }

    public void setEndRole(String endRole) {
        mEndRole = endRole;
    }

    public int getRelationshipType() {
        return mRelationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        mRelationshipType = relationshipType;
    }
}
