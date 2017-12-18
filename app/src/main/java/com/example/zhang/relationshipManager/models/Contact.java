package com.example.zhang.relationshipManager.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 29110 on 2017/12/11.
 */

public class Contact {
    //性别
    public static final int SEX_MALE = 0; //男
    public static final int SEX_FEMALE = 1; //女
    public static final int SEX_SECRET = 2; //保密
    //默认值
    public static final int DEFAULT_ID = -1;
    public static final int DEFAULT_AGE = -1;
    public static final int DEFAULT_SEX = SEX_SECRET;
    public static final String DEFAULT_NAME = "";
    public static final String DEFAULT_NOTES = "";
    public static final String DEFAULT_PHONE_NUM = "";
    private int mId;
    private int mAge;
    private int mSex;
    private String mName;
    private String mNotes;
    private String mPhoneNumber;
    private Map<String, String> mOtherContacts;

    public Contact() {
        //初始化
        mId = DEFAULT_ID;
        mAge = DEFAULT_AGE;
        mSex = DEFAULT_SEX;
        mName = DEFAULT_NAME;
        mNotes = DEFAULT_NOTES;
        mPhoneNumber = DEFAULT_PHONE_NUM;
    }

    //判断传入的联系人是否和本实例匹配，这里传入的联系人中设置为非默认值的属性会被当成需要匹配的对象。
    public boolean isMatch(Contact contact){
        boolean result = true;
        if (contact.mId != DEFAULT_ID && contact.mId != mId)
            result = false;
        if (contact.mAge != DEFAULT_AGE && contact.mAge != mAge)
            result = false;
        if (contact.mSex != DEFAULT_SEX && contact.mSex != mSex)
            result = false;
        if (!contact.mName.equals(DEFAULT_NAME) && !contact.mName.equals(mName))
            result = false;
        return result;
    }

    //获取一个副本，副本复制了所有属性，除了关系的数组
    public Contact copy(){
        Contact contact = new Contact();
        contact.copyFromContact(this);
        return contact;
    }

    //根据传入的contact设置本实例的属性
    public void copyFromContact(Contact contact){
        mId = contact.mId;
        mName = contact.mName;
        mSex = contact.mSex;
        mAge = contact.mAge;
        mPhoneNumber = contact.mPhoneNumber;
        mNotes = contact.mNotes;
        if (contact.mOtherContacts != null){
            mOtherContacts = new HashMap<>(contact.mOtherContacts);
        }
    }

    //更新联系人信息到数据库
    public void update(){
        ContactManager.getInstance(null).updateContact(this);
    }

    //一堆的setter和getter
    public void setId(int id) {
        this.mId = id;
    }

    public void setAge(int age) {
        this.mAge = age;
    }

    public Contact setName(String name) {
        this.mName = name;
        return this;
    }

    public void setSex(int sex) {
        //保证性别是特定的几种
        if (sex != SEX_FEMALE && sex != SEX_MALE)
            sex = SEX_SECRET;
        this.mSex = sex;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public void setOther_contact(Map<String, String> other_contact) {
        this.mOtherContacts = other_contact;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getAge() {
        return mAge;
    }

    public int getSex() {
        return mSex;
    }

    public String getNotes() {
        return mNotes;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public Map<String, String> getOtherContacts() {
        //需要时再初始化
        if (mOtherContacts == null)
            mOtherContacts = new HashMap<>();
        return mOtherContacts;
    }

}
