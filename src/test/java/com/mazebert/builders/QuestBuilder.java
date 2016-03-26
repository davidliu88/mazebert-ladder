package com.mazebert.builders;

import com.mazebert.entities.Quest;
import org.jusecase.builders.Builder;

public class QuestBuilder implements Builder<Quest> {
    private Quest quest = new Quest();

    public QuestBuilder daily() {
        return this.withIsHidden(false);
    }

    public QuestBuilder hidden() {
        return this.withIsHidden(true);
    }

    public QuestBuilder rollStrikesWithBowlingBall() {
        return this
                .daily()
                .withId(20)
                .withSinceVersion("1.2.0");
    }

    public QuestBuilder headhunter() {
        return this
                .daily()
                .withId(1)
                .withSinceVersion("1.0.0");
    }

    public QuestBuilder completeTwistedPaths() {
        return this
                .hidden()
                .withId(11)
                .withReward(100)
                .withSinceVersion("1.0.0");
    }

    public QuestBuilder withId(long value) {
        quest.setId(value);
        return this;
    }

    public QuestBuilder withIsHidden(boolean value) {
        quest.setHidden(value);
        return this;
    }

    public QuestBuilder withSinceVersion(String value) {
        quest.setSinceVersion(value);
        return this;
    }

    public QuestBuilder withReward(int value) {
        quest.setReward(value);
        return this;
    }

    @Override
    public Quest build() {
        return quest;
    }
}
