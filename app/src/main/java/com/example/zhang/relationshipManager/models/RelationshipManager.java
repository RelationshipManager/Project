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
    private static ContactManager sContactManager;
    //关系类型
    private SparseArray<RsType> mRsTypeMap;

    public static RelationshipManager getInstance(Context context) {
        if (sRelationshipManager == null){
            sRelationshipManager = new RelationshipManager(context);
            sContactManager = ContactManager.getInstance(context);
        }
        return sRelationshipManager;
    }

    private RelationshipManager(Context context) {
        super(context);
        mRsTypeMap = new SparseArray<>();
        readAllRsType();
    }

    public ArrayList<Relationship> getRelationships(Contact contact) {
        ArrayList<Relationship> relationships = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String contactId = String.valueOf(contact.getId());
        Cursor cursor = db.query("relationship", null,
                "(start_contact_id=? or end_contact_id=?)",
                new String[]{contactId,contactId},null,null,null);
        if (cursor.moveToFirst()){
            do {
                Contact startContact = sContactManager.getContactById(cursor.getInt(cursor.getColumnIndex("start_contact_id")));
                Contact endContact = sContactManager.getContactById(cursor.getInt(cursor.getColumnIndex("end_contact_id")));
                RsType rsType = mRsTypeMap.get(cursor.getInt(cursor.getColumnIndex("relationship_type_id")));
                relationships.add(new Relationship(startContact, endContact, rsType));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return relationships;
    }

    public void addRelationship(Relationship relationship){
        SQLiteDatabase db = getWritableDatabase();
        RsType rsType = relationship.getRsType();
        //遍历找到对应的关系类型
        for (int i = 0; i < mRsTypeMap.size(); i++){
            RsType rsType2 = mRsTypeMap.valueAt(i);
            if (rsType.getEndRole().equals(rsType2.getEndRole()) && rsType.getStartRole().equals(rsType2.getStartRole())){
                ContentValues values = getContentValues(relationship.getStartContact().getId(),relationship.getEndContact().getId(),rsType.getId());
                db.insert("relationship", null, values);
                break;
            }else if (rsType.getEndRole().equals(rsType2.getStartRole()) && rsType.getStartRole().equals(rsType2.getEndRole())){
                ContentValues values = getContentValues(relationship.getEndContact().getId(),relationship.getStartContact().getId(),rsType.getId());
                db.insert("relationship", null, values);
                break;
            }
        }
    }

    private ContentValues getContentValues(int startContactId, int endContactId, int rsTypeId){
        ContentValues values = new ContentValues();
        values.put("start_contact_id", startContactId);
        values.put("end_contact_id", startContactId);
        values.put("relationship_type_id", startContactId);
        return values;
    }

    private void readAllRsType(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("relationship_type", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                RsType rsType = new RsType();
                rsType.setId(cursor.getInt(cursor.getColumnIndex("rs_type_id")));
                rsType.setRelationshipType(cursor.getInt(cursor.getColumnIndex("class_type")));
                rsType.setStartRole(cursor.getString(cursor.getColumnIndex("start_role")));
                rsType.setEndRole(cursor.getString(cursor.getColumnIndex("end_role")));
                mRsTypeMap.put(rsType.getId(), rsType);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

}
