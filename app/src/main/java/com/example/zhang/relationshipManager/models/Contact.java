package com.example.zhang.relationshipManager.models;

import java.util.ArrayList;

/**
 * Created by 29110 on 2017/12/11.
 */

public class Contact {
    private int age;
    private int id;
    private int imageId;
    private String name;
    private String sex;
    private String notes;
    private String phone_number;
    private String[] other_contact;
    private ArrayList<Relationship> relationships;

    public Contact() {

    }

    public Contact setAge(int age) {
        this.age = age;
        return this;
    }

    public Contact setId(int id) {
        this.id = id;
        return this;
    }

    public Contact setImageId(int imageId) {
        this.imageId = imageId;
        return this;
    }

    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    public Contact setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public Contact setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public Contact setPhone_number(String phone_number) {
        this.phone_number = phone_number;
        return this;
    }

    public Contact setOther_contact(String[] other_contact) {
        this.other_contact = other_contact;
        return this;
    }

    public Contact setRelationships(ArrayList<Relationship> relationships) {
        this.relationships = relationships;
        return this;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public String getNotes() {
        return notes;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String[] getOther_contact() {
        return other_contact;
    }

    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }
}
