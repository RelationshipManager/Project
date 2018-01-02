package com.example.zhang.relationshipManager.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Map;


public class ContactManager extends DatabaseHelper{
    //保存的单例
    static private ContactManager sContactManager;
    //保存的RelationshipManager实例
    private Neo4jManager mNeo4jManager;
    //联系人数组
    private SparseArray<Contact> mContactMap;

    //获取单例
    static public ContactManager getInstance(Context context) {
        if (sContactManager == null){
            sContactManager = new ContactManager(context);
        }
        return sContactManager;
    }

    //获取用户
    public Contact getUser(){
        return mContactMap.valueAt(0);
    }

    //根据联系人id获取联系人
    public Contact getContactById(int id) {
        Contact contact = mContactMap.get(id);
        if (contact == null){
            contact = getContactByIdFromDB(id);
            if (contact != null)
                mContactMap.put(id, contact);
        }
        return contact;
    }

    //获取所有联系人
    public ArrayList<Contact> getAllContacts(){
        ArrayList<Contact> contacts = new ArrayList<>();
        for(int i = 0; i < mContactMap.size(); i++)
            contacts.add(mContactMap.valueAt(i));
        return contacts;
    }

    //根据联系人姓名获取联系人,暂时只查找内存中的联系人
    public ArrayList<Contact> getContactsByName(String name){
        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < mContactMap.size(); i++){
            Contact c = mContactMap.valueAt(i);
            if (c.getName().equals(name))
                contacts.add(c);
        }
        return contacts;
    }

    //根据联系人的各种信息匹配符合条件的联系人，暂时只查找内存中的联系人
    public ArrayList<Contact> getContact(Contact contact) {
        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < mContactMap.size(); i++){
            Contact c = mContactMap.valueAt(i);
            if (c.isMatch(contact))
                contacts.add(c);
        }
        return contacts;
    }

    //添加联系人,返回添加的联系人
    public Contact addContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        //构造联系人的值
        ContentValues values = getContentValues(contact);
        //执行插入
        db.insert(CONTACT, null, values);

        //获取插入的id，用来构造contact
        int id = 0;
        Cursor cursor = db.rawQuery("select last_insert_rowid() from " + CONTACT, null);
        if (cursor.moveToFirst()) id = cursor.getInt(0);
        cursor.close();
        
        Contact newContact = contact.copy();
        newContact.setId(id);
        mContactMap.put(id, newContact);

        //更新添加记录
        mNeo4jManager.addContact(newContact);
        return newContact;
    }


    //把联系人的信息写入数据库
    public void updateContact(Contact contact){
        Contact c = mContactMap.get(contact.getId());
        if (c != null){
            SQLiteDatabase db = getWritableDatabase();
            //构造联系人的值
            ContentValues values = getContentValues(contact);
            db.update(CONTACT, values, CONTACT_ID + "=?", new String[]{String.valueOf(contact.getId())});
            if (contact != c)
                c.copyFromContact(contact);
            //更新操作记录
            mNeo4jManager.updateContact(c);
        }
    }

    public boolean removeContact(Contact contactToRemove){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String id = String.valueOf(contactToRemove.getId());
        db.delete(RS,RS_END_CONTACT_ID+ "=? or "+RS_START_CONTACT_ID+"=?",new String[]{id, id});
        boolean result = db.delete(CONTACT,CONTACT_ID+ "=?",new String[]{id}) > 0;
        if(result){
            // 更新内存中数据
            mContactMap.delete(Integer.valueOf(id));
            db.setTransactionSuccessful();
            //更新删除记录
            mNeo4jManager.removeRelationships(contactToRemove);
            mNeo4jManager.removeContact(contactToRemove);
        }
        db.endTransaction();
        return result;
    }

    //构造函数
    private ContactManager(Context context) {
        super(context);
        mContactMap = new SparseArray<>();
        mNeo4jManager = Neo4jManager.getInstance(context);
        readAllContacts();
    }

    //从数据库中读取所有联系人
    private void readAllContacts(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(CONTACT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                setContact(contact,cursor);
                mContactMap.put(contact.getId(), contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    //从数据库中读取联系人
    private Contact getContactByIdFromDB(int id){
        SQLiteDatabase db = getReadableDatabase();
        Contact contact = new Contact();
        Cursor cursor = db.query(CONTACT, null, CONTACT + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            setContact(contact, cursor);
        } else {
            return null;
        }
        cursor.close();
        return contact;
    }

    //从游标中读取联系人
    private void setContact(Contact contact, Cursor cursor){
        contact.setId(cursor.getInt(cursor.getColumnIndex(CONTACT_ID)));
        contact.setName(cursor.getString(cursor.getColumnIndex(CONTACT_NAME)));
        contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUM)));
        contact.setAge(cursor.getInt(cursor.getColumnIndex(CONTACT_AGE)));
        contact.setSex(cursor.getInt(cursor.getColumnIndex(CONTACT_SEX)));
        //contact.setOther_contact(cursor.getString(cursor.getColumnIndex("other_contacts")));
        contact.setNotes(cursor.getString(cursor.getColumnIndex(CONTACT_NOTE)));
    }
    
    //根据联系人信息构造ContentValues
    private ContentValues getContentValues(Contact contact){
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contact.getName());
        values.put(CONTACT_AGE, contact.getAge());
        values.put(CONTACT_SEX, contact.getSex());
        values.put(CONTACT_PHONE_NUM, contact.getPhoneNumber());
        StringBuilder otherContacts = new StringBuilder();
        Map<String, String> otherContactsList = contact.getOtherContacts();
        if (otherContactsList != null)
            for (String key :
                    otherContactsList.keySet()) {
                otherContacts.append(";").append(key).append(":").append(otherContactsList.get(key));
            }
        values.put(CONTACT_OTHER_CONTACTS, otherContacts.toString());
        return values;
    }

    //此接口比较危险，等有需要再使用
    //根据传入的联系人来更新联系人，根据oldContact匹配符合条件的联系人，然后更新为newContact，返回更新的联系人列表。
    /*public ArrayList<Contact> modifyContact(Contact oldContact, Contact newContact) {
        SQLiteDatabase db = getWritableDatabase();
        //构造联系人的值
        ContentValues values = getContentValues(newContact);
        //获取满足条件的联系人
        ArrayList<Contact> contacts = getContact(oldContact);
        for (Contact c :
                contacts) {
            //执行数据库更新
            db.update(CONTACT, values, CONTACT_ID + "=?", new String[]{String.valueOf(c.getId())});
            //更新内存中的联系人
            if (c != newContact){
                int id = c.getId();
                c.copyFromContact(newContact);
                c.setId(id);
            }
        }
        return contacts;
    }*/
}
