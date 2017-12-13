package com.example.zhang.relationshipManager.models;

import android.content.Context;

import java.util.Map;

/**
 * Created by 29110 on 2017/12/11.
 */

public class ContactManager {
    private Map<Contact,Integer> contacts;
    static private ContactManager sContactManager;

    static public ContactManager getInstance(){
        if (sContactManager == null)
            sContactManager = new ContactManager();
        return sContactManager;
    }

    public void addContact(Contact contact){

    }

    public Contact getContact(Contact contact){
        return contact;
    }

    public Contact modifyContact(Contact conA,Contact conB){
        return conA;
    }

}
