package com.example.simpleusedsqllitedb.database.entity;

import android.app.usage.UsageStats;

import kotlin.jvm.internal.PropertyReference0Impl;

public class Contact {
    public static final String TABLE_NAME = "contact";
    public static final String COLUMN_NAME = "column_name";
    public static final String COLUMN_ID = "contact_id";
    public static final String COLUMN_EMAIL = "contact_email";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            + "(" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
            + "TEXT," + COLUMN_EMAIL + "DATETIME DEFAULT CURRENT TIMESTAMP" + ")";
    private String name;
    private String email;
    private int id;

    public Contact(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
