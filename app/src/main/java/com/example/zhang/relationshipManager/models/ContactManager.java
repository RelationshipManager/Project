package com.example.zhang.relationshipManager.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 29110 on 2017/12/11.
 */

public class ContactManager {
    private Map<Integer, Contact> contactMap;
    static private ContactManager sContactManager;

    static public ContactManager getInstance() {
        if (sContactManager == null)
            sContactManager = new ContactManager();
        return sContactManager;
    }

    private ContactManager() {
        contactMap = new HashMap<>();
    }

    public void addContact(Contact contact) {

    }

    // @todo Interface needed    Used by ContactAdapter
    public Contact getContactById(int id) {
        return contactMap.get(id);
    }

    public Contact getContact(Contact contact) {
        return contact;
    }

    public Contact modifyContact(Contact conA, Contact conB) {
        return conA;
    }

    // @todo Interface needed    Used by ContactInfoActivity
    public ArrayList<Contact> getContactList() {
        // @todo test UI for ContactListFragment
        contactMap.put(1, new Contact().setName("A"));
        contactMap.put(2, new Contact().setName("B"));
        contactMap.put(3, new Contact().setName("C"));

        return new ArrayList<Contact>(contactMap.values());
    }

}
