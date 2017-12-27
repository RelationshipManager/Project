package com.example.zhang.relationshipManager.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Neo4jManager extends DatabaseHelper {
    private static final String LOCAL_USER_ID = "local_user_id";
    private static Neo4jManager sNeo4jManager;

    //获取单例
    static public Neo4jManager getInstance(Context context) {
        if (sNeo4jManager == null){
            sNeo4jManager = new Neo4jManager(context);
        }
        return sNeo4jManager;
    }

    void addContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        // TODO: 2017/12/27 这里需要知道用户的网络节点id
        String prop = LOCAL_USER_ID + ":"  /*这里要获取用户的网络节点id*/ + "," +
                CONTACT_ID + ":" + contact.getId() + "," +
                CONTACT_PHONE_NUM + ":" + contact.getPhoneNumber() + "," +
                CONTACT_NAME + ":" + contact.getName() + "," +
                CONTACT_AGE + ":" + contact.getAge() + "," +
                CONTACT_SEX + ":" + contact.getSex() + "," +
                CONTACT_NOTE + ":" + contact.getNotes();
        String cypher = "create(:VirtualContact{"+ prop +"})";
        ContentValues values = new ContentValues();
        values.put(CL_OPERATOR, cypher);
        db.insert(CL, null, values);
    }

    void updateContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        String prop = CONTACT_NAME + "=" + contact.getName() + "," +
                CONTACT_AGE + "=" + contact.getAge() + "," +
                CONTACT_PHONE_NUM + "=" + contact.getPhoneNumber() + "," +
                CONTACT_SEX + "=" + contact.getSex() + "," +
                CONTACT_NOTE + "=" + contact.getNotes();
        // TODO: 2017/12/27 这里需要知道用户的网络节点id
        String cypher = "match(target:VirtualContact) where target." + LOCAL_USER_ID + " = " +
                /*就是这里*/ " and target." + CONTACT_ID + "=" + contact.getId() +
                " set "+ prop;
        ContentValues values = new ContentValues();
        values.put(CL_OPERATOR, cypher);
        db.insert(CL, null, values);
    }

    void removeContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        // TODO: 2017/12/27 这里需要知道用户的网络节点id
        String cypher = "match(target:VirtualContact) where target." + LOCAL_USER_ID + " = " +
                /*就是这里*/ " and target." + CONTACT_ID + "=" + contact.getId() +
                "delete target";
        ContentValues values = new ContentValues();
        values.put(CL_OPERATOR, cypher);
        db.insert(CL, null, values);
    }

    void addRelationship(Contact startContact, Contact endContact, RsType rsType){
        String rsLabel = "Unknown";
        switch (rsType.getRelationshipType()){
            case RsType.COLLEAGUES:
                rsLabel = "Colleagues";
                break;
            case RsType.FRIENDS:
                rsLabel = "Friends";
                break;
        }

        if (!rsLabel.equals("Unknown")){
            SQLiteDatabase db = getWritableDatabase();
            // TODO: 2017/12/27 这里需要知道用户的网络节点id
            String cypher = "match(startContact:VirtualContact),(endContact:VirtualContact)"+
                    " where startContact." + LOCAL_USER_ID + " = "+/*就是这里*/
                    " and endContact." + LOCAL_USER_ID + " = "+/*就是这里*/
                    " and startContact." + CONTACT_ID + "=" + startContact.getId()+
                    " and endContact." + CONTACT_ID + "=" + endContact.getId()+
                    " create(startContact)-[:" + rsLabel + "]->(endContact)";
            ContentValues values = new ContentValues();
            values.put(CL_OPERATOR, cypher);
            db.insert(CL, null, values);
        }
    }

    void removeRelatonship(Relationship relationship){
        String rsLabel = "Unknown";
        switch (relationship.getRsType().getRelationshipType()){
            case RsType.COLLEAGUES:
                rsLabel = "Colleagues";
                break;
            case RsType.FRIENDS:
                rsLabel = "Friends";
                break;
        }

        if (!rsLabel.equals("Unknown")){
            SQLiteDatabase db = getWritableDatabase();
            // TODO: 2017/12/27 这里需要知道用户的网络节点id
            String cypher = "match(startContact:VirtualContact)-[target:" + rsLabel + "]->(endContact:VirtualContact)" +
                    " where startContact." + LOCAL_USER_ID + " = "+/*就是这里*/
                    " and endContact." + LOCAL_USER_ID + " = "+/*就是这里*/
                    " and startContact." + CONTACT_ID + "=" + relationship.getStartContact().getId()+
                    " and endContact." + CONTACT_ID + "=" + relationship.getEndContact().getId()+
                    "delete target";
            ContentValues values = new ContentValues();
            values.put(CL_OPERATOR, cypher);
            db.insert(CL, null, values);
        }
    }

    void removeRelationships(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        // TODO: 2017/12/27 这里需要知道用户的网络节点id
        String cypher = "match(:VirtualContact)-[target:]-(contact:VirtualContact)" +
                " and contact." + LOCAL_USER_ID + " = "+/*就是这里*/
                " and contact." + CONTACT_ID + "=" + contact.getId()+
                "delete target";
        ContentValues values = new ContentValues();
        values.put(CL_OPERATOR, cypher);
        db.insert(CL, null, values);
    }


    //构造函数
    private Neo4jManager(Context context) {
        super(context);
    }


}
