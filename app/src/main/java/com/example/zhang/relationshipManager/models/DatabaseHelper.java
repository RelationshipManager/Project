package com.example.zhang.relationshipManager.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    //数据库版本
    static private final int DB_VERSION = 4;
    //数据库名
    static private final String DB_NAME = "relationship_manager";
    //表名
    static final String CONTACT = "contact";
    static final String RT = "relationship_type";
    static final String RS = "relationship";
    static final String CL = "cypher_log";
    //表字段名
    static final String CONTACT_ID = "contact_id";
    static final String CONTACT_NAME = "name";
    static final String CONTACT_AGE = "age";
    static final String CONTACT_SEX = "sex";
    static final String CONTACT_PHONE_NUM = "phone_num";
    static final String CONTACT_OTHER_CONTACTS = "other_contacts";
    static final String CONTACT_NOTE = "note";
    static final String RT_ID = "rs_type_id";
    static final String RT_START_ROLE = "start_role";
    static final String RT_END_ROLE = "end_role";
    static final String RT_CLASS_TYPE = "class_type";
    static final String RS_ID = "relationship_id";
    static final String RS_START_CONTACT_ID = "start_contact_id";
    static final String RS_END_CONTACT_ID = "end_contact_id";
    static final String RS_RT_ID = "class_type";
    static final String CL_OPERATOR = "cypher_operator";

    DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //建表语句(person,relationship_type,relationship）
        String CREATE_TABLE =
                "create table contact(" +
                        "contact_id integer primary key autoincrement," +
                        "name text," +
                        "age integer," +
                        "sex integer," +
                        "phone_num text," +
                        "other_contacts text," +
                        "note text)";
        String CREATE_TABLE2 =
                "create table relationship_type(" +
                        "rs_type_id integer primary key autoincrement," +
                        "start_role text," +
                        "end_role text," +
                        "class_type integer)";
        String CREATE_TABLE3 =
                "create table relationship(" +
                        "relationship_id integer primary key autoincrement," +
                        "start_contact_id integer," +
                        "end_contact_id integer," +
                        "relationship_type_id integer)";
        String CREATE_TABLE4 =
                "create table cypher_log(" +
                        "cypher_operator text)";
        String INSERT_TYPES =
                "insert into relationship_type(start_role,end_role,class_type) values(?,?,?)";
        String[][] mTypeData = new String[][]{
                {"朋友", "朋友", String.valueOf(RsType.FRIENDS)},
                {"上级", "下级", String.valueOf(RsType.COLLEAGUES)},
                {"同事", "同事", String.valueOf(RsType.COLLEAGUES)}};

        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE2);
        sqLiteDatabase.execSQL(CREATE_TABLE3);
        sqLiteDatabase.execSQL(CREATE_TABLE4);
        for (String[] typeData : mTypeData) {
            sqLiteDatabase.execSQL(INSERT_TYPES, typeData);
        }
        //添加本机用户
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, "我");
        values.put(CONTACT_AGE, Contact.DEFAULT_AGE);
        values.put(CONTACT_SEX, Contact.DEFAULT_SEX);
        values.put(CONTACT_PHONE_NUM, Contact.DEFAULT_PHONE_NUM);
        values.put(CONTACT_NOTE, Contact.DEFAULT_NOTES);
        values.put(CONTACT_OTHER_CONTACTS, "");
        sqLiteDatabase.insert(CONTACT,null,values);
    }

    //暂时用不到
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table contact");
        sqLiteDatabase.execSQL("drop table relationship");
        sqLiteDatabase.execSQL("drop table relationship_type");
        if (oldVersion == 3){
            sqLiteDatabase.execSQL("drop table cypher_log");
        }
        onCreate(sqLiteDatabase);
    }
}
