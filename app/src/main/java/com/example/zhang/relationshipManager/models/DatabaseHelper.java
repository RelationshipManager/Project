package com.example.zhang.relationshipManager.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 10040 on 2017/6/11.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    //数据库名
    static private final String DB_NAME = "uml_project2";
    //建表语句(person,relationship_type,relationship）
    static private final String CREATE_TABLE =
            "create table person(" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "level integer default -20)";
    static private final String CREATE_TABLE2 =
            "create table relationship_type(" +
                    "id integer primary key autoincrement," +
                    "source_type text," +
                    "target_type text," +
                    "level_diff integer)";
    static private final String CREATE_TABLE3 =
            "create table relationship(" +
                    "source_person_id integer," +
                    "target_person_id integer," +
                    "relationship_id integer)";
    static private final String INSERT_TYPES =
            "insert into relationship_type(source_type,target_type,level_diff) values(?,?,?)";
    static private final String[][] TYPEDATA = new String[][]{
            {"父亲", "儿子", "1"}, {"父亲", "女儿", "1"}, {"母亲", "儿子", "1"}, {"母亲", "女儿", "1"}, {"丈夫", "妻子", "0"}};

    DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE2);
        sqLiteDatabase.execSQL(CREATE_TABLE3);
        for (String[] typedata : TYPEDATA) {
            sqLiteDatabase.execSQL(INSERT_TYPES, typedata);
        }
    }

    //暂时用不到
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table person");
        sqLiteDatabase.execSQL("drop table relationship");
        sqLiteDatabase.execSQL("drop table relationship_type");
        onCreate(sqLiteDatabase);
    }
}
