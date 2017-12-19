package com.example.zhang.relationshipManager.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Map;


public class ContactManager extends DatabaseHelper{
    //保存的单例
    static private ContactManager sContactManager;
    //保存的RelationshipManager实例
    static private RelationshipManager sRelationshipManager;
    //联系人数组
    private SparseArray<Contact> mContactMap;

    //获取单例
    static public ContactManager getInstance(Context context) {
        if (sContactManager == null){
            sContactManager = new ContactManager(context);
            sRelationshipManager = RelationshipManager.getInstance(context);
        }
        return sContactManager;
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
        db.insert("contact", null, values);
        //获取插入的id，用来构造contact
        int id = 0;
        Cursor cursor = db.rawQuery("select last_insert_rowid() from contact", null);
        if (cursor.moveToFirst()) id = cursor.getInt(0);
        cursor.close();
        
        Contact newContact = contact.copy();
        newContact.setId(id);
        mContactMap.put(id, newContact);
        return newContact;
    }

    //根据传入的联系人来更新联系人，根据oldContact匹配符合条件的联系人，然后更新为newContact，返回更新的联系人列表。
    public ArrayList<Contact> modifyContact(Contact oldContact, Contact newContact) {
        SQLiteDatabase db = getWritableDatabase();
        //构造联系人的值
        ContentValues values = getContentValues(newContact);
        //获取满足条件的联系人
        ArrayList<Contact> contacts = getContact(oldContact);
        for (Contact c :
                contacts) {
            //执行数据库更新
            db.update("contact", values, "contact_id=?", new String[]{String.valueOf(c.getId())});
            //更新内存中的联系人
            if (c != newContact){
                int id = c.getId();
                c.copyFromContact(newContact);
                c.setId(id);
            }
        }
        return contacts;
    }

    //把联系人的信息写入数据库
    public void updateContact(Contact contact){
        if (contact == mContactMap.get(contact.getId())){
            SQLiteDatabase db = getWritableDatabase();
            //构造联系人的值
            ContentValues values = getContentValues(contact);
            db.update("contact", values, "contact_id=?", new String[]{String.valueOf(contact.getId())});
        }
    }

    public boolean removeContact(Contact contactToRemove){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String id = String.valueOf(contactToRemove.getId());
        db.delete("relationship","start_contact_id=? or end_contact_id=?",new String[]{id,id});
        boolean result= db.delete("contact","contact_id=?",new String[]{id}) > 0;
        if(result){
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        return result;
    }

    //构造函数
    private ContactManager(Context context) {
        super(context);
        mContactMap = new SparseArray<>();
        readAllContacts();
    }

    //从数据库中读取所有联系人
    private void readAllContacts(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("contact", null, null, null, null, null, null);
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
        Cursor cursor = db.query("person", null, "contact_id=?", new String[]{String.valueOf(id)}, null, null, null);
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
        contact.setId(cursor.getInt(cursor.getColumnIndex("contact_id")));
        contact.setName(cursor.getString(cursor.getColumnIndex("name")));
        contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone_num")));
        contact.setAge(cursor.getInt(cursor.getColumnIndex("age")));
        contact.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
        //contact.setOther_contact(cursor.getString(cursor.getColumnIndex("other_contacts")));
        contact.setNotes(cursor.getString(cursor.getColumnIndex("note")));
    }
    
    //根据联系人信息构造ContentValues
    private ContentValues getContentValues(Contact contact){
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("age", contact.getAge());
        values.put("sex", contact.getSex());
        values.put("phone_num", contact.getPhoneNumber());
        StringBuilder otherContacts = new StringBuilder();
        Map<String, String> otherContactsList = contact.getOtherContacts();
        if (otherContactsList != null)
            for (String key :
                    otherContactsList.keySet()) {
                otherContacts.append(";").append(key).append(":").append(otherContactsList.get(key));
            }
        values.put("other_contacts", otherContacts.toString());
        return values;
    }
}
