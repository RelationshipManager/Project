package com.example.zhang.relationshipManager.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by 10040 on 2017/6/10.
 */

public class PersonManager extends DatabaseHelper {
    //保存的实例
    static private PersonManager sPersonManager;
    static private RelationshipManager sRelationshipManager;

    private PersonManager(Context context) {
        super(context);
    }

    public static PersonManager getInstance(Context context) {
        if (sPersonManager == null) {
            sPersonManager = new PersonManager(context);
            sRelationshipManager = RelationshipManager.getInstance(context);
            sPersonManager.addFirstPerson();
        }
        return sPersonManager;
    }

    private void addFirstPerson() {
        if (sRelationshipManager.getPersonsByLevel(0).isEmpty()) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", "本机用户");
            values.put("level", 0);
            db.insert("person", null, values);
        }
    }

    public boolean addPerson(String name) {
        SQLiteDatabase db = getWritableDatabase();
        //临时写来用来防止重名的
        Cursor cursor = db.query("person", new String[]{"id"}, "name=?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("level", -20);
            db.insert("person", null, values);
            cursor.close();
            return true;
        }
    }
    // @// TODO: 2017-06-12 操作完成之后通知前台更新
    public void updatePerson(int person_ID, String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        SQLiteDatabase db = getWritableDatabase();
        db.update("person", values, "id=?", new String[]{String.valueOf(person_ID)});
    }

    public boolean removePerson(String id){
        SQLiteDatabase db = getWritableDatabase();
        //db.beginTransaction();
        boolean result= db.delete("person","id = ?",new String[]{id}) > 0;
        if(result){
            result=db.delete("relationship","source_person_id=? or target_person_id=?",new String[]{id,id})>0;
        }
        //db.endTransaction();
        return result;
    }

    public Person getPersonById(int id) {
        String name;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("person", new String[]{"name"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
        } else {
            return null;
        }
        cursor.close();
        return new Person(id, name);
    }

    public Person getPersonByName(String name) {
        int id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("person", new String[]{"id"}, "name=?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
        } else {
            return null;
        }
        cursor.close();
        return new Person(id, name);
    }

    public ArrayList<Person> getAllPerson() {
        ArrayList<Person> personList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("person", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                // 不显示 本机用户
                if (id > 1)
                    personList.add(new Person(id, name));
            } while (cursor.moveToNext());
        } else {
            return personList;
        }
        cursor.close();
        return personList;
    }

}
