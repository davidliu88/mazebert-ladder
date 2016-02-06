package com.mazebert.entities;

import java.util.Date;

public class Player {
    private long id;
    private String key;
    private String name;
    private int level;
    private long experience;
    private Date lastUpdate;
    private int rank;
    private String email;
    private int supporterLevel;
    private int relics;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSupporterLevel(int supporterLevel) {
        this.supporterLevel = supporterLevel;
    }

    public int getSupporterLevel() {
        return supporterLevel;
    }

    public void setRelics(int relics) {
        this.relics = relics;
    }

    public int getRelics() {
        return relics;
    }
}
