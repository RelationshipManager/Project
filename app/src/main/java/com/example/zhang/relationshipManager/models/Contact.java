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

    public Contact(){

    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String[] getOther_contact() {
        return other_contact;
    }

    public void setOther_contact(String[] other_contact) {
        this.other_contact = other_contact;
    }

    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(ArrayList<Relationship> relationships) {
        this.relationships = relationships;
    }
}
