package com.mazebert.entities;

public abstract class Card {
    private long id;
    private String sinceVersion;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSinceVersion(String sinceVersion) {
        this.sinceVersion = sinceVersion;
    }

    public String getSinceVersion() {
        return sinceVersion;
    }


    public abstract int getType();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
