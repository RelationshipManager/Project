package com.example.zhang.relationshipManager.models;

/**
 * Created by zhang on 2017-06-08.
 */

public class Person {
    private int id;
    private String name;
    int[] mRelationshipIds;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
        this.mRelationshipIds = new int[0];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
