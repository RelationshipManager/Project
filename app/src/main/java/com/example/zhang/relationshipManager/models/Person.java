package com.example.zhang.relationshipManager.models;

import java.io.Serializable;

/**
 * Created by zhang on 2017-06-08.
 */

public class Person implements Serializable{
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
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
