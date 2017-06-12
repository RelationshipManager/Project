package com.example.zhang.relationshipManager.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 10040 on 2017/6/11.
 */

public class RelationshipManager extends DatabaseHelper {
    //保存的实例
    static private RelationshipManager sRelationshipManager;
    //PersonManager实例
    static private PersonManager sPersonManager;

    public static RelationshipManager getInstance(Context context) {
        if (sRelationshipManager == null) {
            sRelationshipManager = new RelationshipManager(context.getApplicationContext());
            sPersonManager = PersonManager.getInstance(context);
        }
        return sRelationshipManager;
    }

    private RelationshipManager(Context context) {
        super(context);
    }

    private boolean updatePersonLevel(Person oldPerson, int level) {
        if (oldPerson == null)
            return false;
        ContentValues values = new ContentValues();
        values.put("level", level);
        SQLiteDatabase db = getWritableDatabase();
        db.update("person", values, "id=?", new String[]{String.valueOf(oldPerson.getId())});
        return true;
    }

    public int getLevelOfPerson(Person person) {
        if (person == null)
            return -1;
        int level = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("person", new String[]{"level"}, "id=?", new String[]{String.valueOf(person.getId())}, null, null, null);
        if (cursor.moveToFirst()) {
            level = cursor.getInt(cursor.getColumnIndex("level"));
        }
        cursor.close();
        return level;
    }

    public int getMaxLevel() {
        int level = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select max(level) as max_level from person where level<>?", new String[]{"-20"});
        if (cursor.moveToFirst()) {
            level = cursor.getInt(cursor.getColumnIndex("max_level"));
        }
        cursor.close();
        return level;
    }

    public int getMinLevel() {
        int level = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select min(level) as min_level from person where level<>?", new String[]{"-20"});
        if (cursor.moveToFirst()) {
            level = cursor.getInt(cursor.getColumnIndex("min_level"));
        }
        cursor.close();
        return level;
    }

    public ArrayList<Person> getPersonsByLevel(int level) {
        ArrayList<Person> personList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("person", new String[]{"id", "name"}, "level=?", new String[]{String.valueOf(level)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                personList.add(new Person(id, name));
            } while (cursor.moveToNext());
        } else {
            return personList;
        }
        cursor.close();
        return personList;
    }

    public ArrayList<Relationship> getRelationshipsOfPerson(Person person) {
        ArrayList<Relationship> relationshipsList = new ArrayList<>();
        Person target_person;
        String type_name;
        SQLiteDatabase db = getReadableDatabase();
        String id = String.valueOf(person.getId());
        Cursor cursor = db.query("relationship", new String[]{"target_person_id", "relationship_id"}, "source_person_id=?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                target_person = sPersonManager.getPersonById(cursor.getInt(cursor.getColumnIndex("target_person_id")));
                String type_id = String.valueOf(cursor.getInt(cursor.getColumnIndex("relationship_id")));
                Cursor cursor1 = db.query("relationship_type", new String[]{"target_type"}, "id=?", new String[]{type_id}, null, null, null);
                if (cursor1.moveToFirst()) {
                    type_name = cursor1.getString(cursor1.getColumnIndex("target_type"));
                } else
                    type_name = "无";
                cursor1.close();
                relationshipsList.add(new Relationship(person, target_person, type_name));
            } while (cursor.moveToNext());
        }
        cursor.close();

        cursor = db.query("relationship", new String[]{"source_person_id", "relationship_id"}, "target_person_id=?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                target_person = sPersonManager.getPersonById(cursor.getInt(cursor.getColumnIndex("source_person_id")));
                String type_id = String.valueOf(cursor.getInt(cursor.getColumnIndex("relationship_id")));
                Cursor cursor1 = db.query("relationship_type", new String[]{"source_type"}, "id=?", new String[]{type_id}, null, null, null);
                if (cursor1.moveToFirst()) {
                    type_name = cursor1.getString(cursor1.getColumnIndex("source_type"));
                } else
                    type_name = "无";
                cursor1.close();
                relationshipsList.add(new Relationship(person, target_person, type_name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return relationshipsList;
    }

    public boolean addRelationship(Person source, Person target, String sourceType, String targetType) {
        if (source == null || target == null) {
            Log.d("addRelationship", "source==null||target==null");
            return false;
        }

        int sourceLevel = getLevelOfPerson(source);
        if (-20 == sourceLevel) {
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        //检查唯一性
        cursor = db.query("relationship", null, "source_person_id=? and target_person_id=?",
                new String[]{String.valueOf(source.getId()), String.valueOf(target.getId())}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        cursor.close();

        int relationshipId;
        int levelDiff;
        int targetLevel = getLevelOfPerson(target);
        cursor = db.query("relationship_type", new String[]{"id", "level_diff"},
                "source_type=? and target_type=?", new String[]{sourceType, targetType}, null, null, null);
        if (cursor.moveToFirst()) {
            relationshipId = cursor.getInt(cursor.getColumnIndex("id"));
            levelDiff = cursor.getInt(cursor.getColumnIndex("level_diff"));
            if (-20 != targetLevel && targetLevel != sourceLevel - levelDiff) {
                cursor.close();
                return false;
            }
            ContentValues values = new ContentValues();
            values.put("source_person_id", source.getId());
            values.put("target_person_id", target.getId());
            values.put("relationship_id", relationshipId);
            db.insert("relationship", null, values);
        } else {
            cursor.close();
            cursor = db.query("relationship_type", new String[]{"id", "level_diff"},
                    "source_type=? and target_type=?", new String[]{sourceType, targetType}, null, null, null);
            if (cursor.moveToFirst()) {
                relationshipId = cursor.getInt(cursor.getColumnIndex("id"));
                levelDiff = -cursor.getInt(cursor.getColumnIndex("level_diff"));
                if (-20 != targetLevel && targetLevel != sourceLevel - levelDiff) {
                    cursor.close();
                    return false;
                }
                ContentValues values = new ContentValues();
                values.put("source_person_id", target.getId());
                values.put("target_person_id", source.getId());
                values.put("relationship_id", relationshipId);
                db.insert("relationship", null, values);

            } else {
                cursor.close();
                return false;
            }
        }
        cursor.close();
        updatePersonLevel(target, sourceLevel - levelDiff);
        return true;
    }

    public boolean updateRelationship(Person source, Person target, String source_type, String target_type) {
        if (source == null || target == null) {
            Log.d("updateRelationship", "source==null||target==null");
            return false;
        }

        int relationshipId;
        int levelDiff;
        int sourceLevel = getLevelOfPerson(source);
        int targetLevel = getLevelOfPerson(target);
        String source_id = String.valueOf(source.getId());
        String target_id = String.valueOf(target.getId());
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("relationship_type", new String[]{"id"},
                "source_type=? and target_type=?", new String[]{source_type, target_type}, null, null, null);
        if (cursor.moveToFirst()) {
            relationshipId = cursor.getInt(cursor.getColumnIndex("id"));
            levelDiff = cursor.getInt(cursor.getColumnIndex("level_diff"));
            if (-20 != targetLevel && targetLevel != sourceLevel - levelDiff) {
                cursor.close();
                return false;
            }
            ContentValues values = new ContentValues();
            values.put("relationship_id", relationshipId);
            db.update("relationship", values, "source_person_id=? and target_person_id=?", new String[]{source_id, target_id});
        } else {
            cursor.close();
            cursor = db.query("relationship_type", new String[]{"id"},
                    "source_type=? and target_type=?", new String[]{target_type, source_type}, null, null, null);
            if (cursor.moveToFirst()) {
                relationshipId = cursor.getInt(cursor.getColumnIndex("id"));
                levelDiff = -cursor.getInt(cursor.getColumnIndex("level_diff"));
                if (-20 != targetLevel && targetLevel != sourceLevel - levelDiff) {
                    cursor.close();
                    return false;
                }
                ContentValues values = new ContentValues();
                values.put("relationship_id", relationshipId);
                db.update("relationship", values, "source_person_id=? and target_person_id=?", new String[]{target_id, source_id});
            } else {
                cursor.close();
                return false;
            }
        }
        cursor.close();
        updatePersonLevel(target, sourceLevel - levelDiff);
        return true;
    }
}
