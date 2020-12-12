package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class ProductEvent implements DatabaseReference.CompletionListener {

    private int type;
    private int count;
    private int cal;
    private String name;
    private String start;

    public ProductEvent() {

    }
    public ProductEvent(int type, int count, int cal, String name, String start) {
        this.type = type;
        this.count = count;
        this.cal = cal;
        this.name = name;
        this.start = start;
    }

    public int getType() {
        return type;
    }
    public int getCount() {
        return count;
    }
    public int getCal() {
        return cal;
    }
    public String getFullName() {
        return name + " ("+cal+" cal)";
    }
    public String getName() {
        return name;
    }
    public String getStart() {
        return start;
    }

    public void setType(int type) {
        this.type = type;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setCal(int cal) {
        this.cal = cal;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStart(String start) {
        this.start = start;
    }


    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

    }
}
