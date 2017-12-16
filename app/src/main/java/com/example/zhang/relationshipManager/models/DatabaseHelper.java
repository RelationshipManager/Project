package com.example.zhang.relationshipManager.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 10040 on 2017/12/15.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    //数据库版本
    static private final int DB_VERSION = 1;
    //数据库名
    static private final String DB_NAME = "uml_project2";
    //建表语句(person,relationship_type,relationship）
    static private final String CREATE_TABLE =
            "create table contact(" +
                    "contact_id integer primary key autoincrement," +
                    "name text," +
                    "age integer," +
                    "sex integer," +
                    "phone_num text," +
                    "other_contacts text," +
                    "note text)";
    static private final String CREATE_TABLE2 =
            "create table relationship_type(" +
                    "rs_type_id integer primary key autoincrement," +
                    "start_role text," +
                    "end_role text," +
                    "class_type integer)";
    static private final String CREATE_TABLE3 =
            "create table relationship(" +
                    "relationship_id integer primary key autoincrement," +
                    "start_contact_id integer," +
                    "end_contact_id integer," +
                    "relationship_type_id integer)";
    static private final String INSERT_TYPES =
            "insert into relationship_type(start_role,end_role,class_type) values(?,?,?)";
    private final String[][] mTypeData = new String[][]{
            {"朋友", "朋友", String.valueOf(RsType.FRIENDS)},
            {"上级", "下级", String.valueOf(RsType.COLLEAGUES)},
            {"同事", "同事", String.valueOf(RsType.COLLEAGUES)}};

    DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE2);
        sqLiteDatabase.execSQL(CREATE_TABLE3);
        for (String[] typeData : mTypeData) {
            sqLiteDatabase.execSQL(INSERT_TYPES, typeData);
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
