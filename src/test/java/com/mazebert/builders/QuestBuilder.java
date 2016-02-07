package com.mazebert.builders;

import com.mazebert.entities.Quest;
import org.jusecase.builders.Builder;

public class QuestBuilder implements Builder<Quest> {
    private Quest quest = new Quest();

    @Override
    public Quest build() {
        return quest;
    }
}
