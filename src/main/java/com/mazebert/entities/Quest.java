package com.mazebert.entities;

public class Quest {
    public long id;
    public int reward;
    public boolean isHidden;
    public String sinceVersion;
    public int requiredAmount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getSinceVersion() {
        return sinceVersion;
    }

    public void setSinceVersion(String sinceVersion) {
        this.sinceVersion = sinceVersion;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(int requiredAmount) {
        this.requiredAmount = requiredAmount;
    }
}
