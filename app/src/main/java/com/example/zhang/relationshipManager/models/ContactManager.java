package com.example.zhang.relationshipManager.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 29110 on 2017/12/11.
 */

public class ContactManager {
    private Map<Integer, Contact> contacts;
    static private ContactManager sContactManager;

    static public ContactManager getInstance(){
        if (sContactManager == null)
            sContactManager = new ContactManager();
        return sContactManager;
    }

    private ContactManager(){
        contacts = new HashMap<>();
    }

    public void addContact(Contact contact){

    }

    public Contact getContact(Contact contact){
        return contact;
    }

    public Contact modifyContact(Contact conA,Contact conB){
        return conA;
    }

    // @todo Interface needed    Used by ContactAdapter
    public ArrayList<Contact> getContactList(){
        // @todo test UI for ContactListFragment
        contacts.put(1,new Contact().setName("A"));
        contacts.put(2,new Contact().setName("B"));
        contacts.put(3,new Contact().setName("C"));

        return new ArrayList<Contact>(contacts.values());
    }

}
