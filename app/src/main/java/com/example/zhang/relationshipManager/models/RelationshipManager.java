package com.example.zhang.relationshipManager.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import java.util.ArrayList;

public class RelationshipManager extends DatabaseHelper{
    //保存的单例
    private static RelationshipManager sRelationshipManager;
    //保存的ContactManager实例
    private Neo4jManager mNeo4jManager;
    //关系类型
    private SparseArray<RsType> mRsTypeMap;

    public static RelationshipManager getInstance(Context context) {
        if (sRelationshipManager == null){
            sRelationshipManager = new RelationshipManager(context);
        }
        return sRelationshipManager;
    }

    public ArrayList<String> getAllRoles(){
        ArrayList<String> allRoles = new ArrayList<>();
        for (int i = 0; i < mRsTypeMap.size(); i++) {
            RsType rt = mRsTypeMap.valueAt(i);
            if (!allRoles.contains(rt.getStartRole()))
                allRoles.add(rt.getStartRole());
            if (!allRoles.contains(rt.getEndRole()))
                allRoles.add(rt.getEndRole());
        }
        return allRoles;
    }

    //获取联系人的所有关系
    public ArrayList<Relationship> getRelationships(Contact contact) {
        ArrayList<Relationship> relationships = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String contactId = String.valueOf(contact.getId());
        Cursor cursor = db.query(RS, null,
                "("+RS_START_CONTACT_ID+"=? or "+RS_END_CONTACT_ID+"=?)",
                new String[]{contactId,contactId},null,null,null);
        if (cursor.moveToFirst()){
            do {
                ContactManager cm = ContactManager.getInstance(null);
                Contact startContact = cm.getContactById(cursor.getInt(cursor.getColumnIndex(RS_START_CONTACT_ID)));
                Contact endContact = cm.getContactById(cursor.getInt(cursor.getColumnIndex(RS_END_CONTACT_ID)));
                RsType rsType = mRsTypeMap.get(cursor.getInt(cursor.getColumnIndex(RS_RT_ID)));
                relationships.add(new Relationship(startContact, endContact, rsType));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return relationships;
    }

    //如果关系类型符合，则添加关系并返回true；如果找不到对应关系类型，则返回false
    public boolean addRelationship(Relationship relationship){
        SQLiteDatabase db = getWritableDatabase();
        RsType rsType = relationship.getRsType();
        //遍历找到对应的关系类型
        for (int i = 0; i < mRsTypeMap.size(); i++){
            RsType rsType2 = mRsTypeMap.valueAt(i);
            if (rsType.getEndRole().equals(rsType2.getEndRole()) && rsType.getStartRole().equals(rsType2.getStartRole())){
                ContentValues values = getContentValues(relationship.getStartContact().getId(),relationship.getEndContact().getId(),rsType2.getId());
                db.insert(RS, null, values);
                mNeo4jManager.addRelationship(relationship.getStartContact(),relationship.getEndContact(),rsType2);
                return true;
            }else if (rsType.getEndRole().equals(rsType2.getStartRole()) && rsType.getStartRole().equals(rsType2.getEndRole())){
                ContentValues values = getContentValues(relationship.getEndContact().getId(),relationship.getStartContact().getId(),rsType2.getId());
                db.insert(RS, null, values);
                mNeo4jManager.addRelationship(relationship.getEndContact(),relationship.getStartContact(),rsType2);
                return true;
            }
        }
        return false;
    }

    public boolean removeRelationship(Relationship relationship){
        SQLiteDatabase db = getWritableDatabase();
        String startId = String.valueOf(relationship.getStartContact().getId());
        String endId = String.valueOf(relationship.getEndContact().getId());
        boolean result = db.delete(RS,RS_START_CONTACT_ID+ "=? and "+RS_END_CONTACT_ID+"=?",new String[]{startId, endId}) > 0;
        if (result)
            mNeo4jManager.removeRelationship(relationship);

        return result;
    }

    public boolean removeRelationships(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        String id = String.valueOf(contact.getId());
        boolean result = db.delete(RS,RS_END_CONTACT_ID+ "=? or "+RS_START_CONTACT_ID+"=?",new String[]{id, id}) > 0;
        if (result)
            mNeo4jManager.removeRelationships(contact);

        return result;
    }


    //构造函数
    private RelationshipManager(Context context) {
        super(context);
        mRsTypeMap = new SparseArray<>();
        mNeo4jManager = Neo4jManager.getInstance(context);
        readAllRsType();
    }

    private ContentValues getContentValues(int startContactId, int endContactId, int rsTypeId){
        ContentValues values = new ContentValues();
        values.put(RS_START_CONTACT_ID, startContactId);
        values.put(RS_END_CONTACT_ID, endContactId);
        values.put(RS_RT_ID, rsTypeId);
        return values;
    }

    private void readAllRsType(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(RT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                RsType rsType = new RsType();
                rsType.setId(cursor.getInt(cursor.getColumnIndex(RT_ID)));
                rsType.setRelationshipType(cursor.getInt(cursor.getColumnIndex(RT_CLASS_TYPE)));
                rsType.setStartRole(cursor.getString(cursor.getColumnIndex(RT_START_ROLE)));
                rsType.setEndRole(cursor.getString(cursor.getColumnIndex(RT_END_ROLE)));
                mRsTypeMap.put(rsType.getId(), rsType);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
