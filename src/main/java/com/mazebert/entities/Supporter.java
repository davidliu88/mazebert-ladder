package com.mazebert.entities;

public class Supporter {
    private long id;
    private String name;
    private int supporterLevel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSupporterLevel() {
        return supporterLevel;
    }

    public void setSupporterLevel(int supporterLevel) {
        this.supporterLevel = supporterLevel;
    }
}
