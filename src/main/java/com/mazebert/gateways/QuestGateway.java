package com.mazebert.gateways;

import java.util.List;

public interface QuestGateway {
    List<Long> findCompletedHiddenQuestIds(long playerId);
}
