package com.mazebert.plugins.random;

import com.mazebert.entities.CardType;
import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.entities.Version;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.QuestGateway;
import com.mazebert.plugins.time.CurrentDatePlugin;

import java.util.*;

public class DailyQuestGenerator {
    private final QuestGateway questGateway;
    private final FoilCardGateway foilCardGateway;
    private final PlayerGateway playerGateway;
    private final CurrentDatePlugin currentDatePlugin;
    private final RandomNumberGenerator randomNumberGenerator;

    private static final int ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
    private static final int MAX_QUESTS = 3;

    public DailyQuestGenerator(QuestGateway questGateway,
                               FoilCardGateway foilCardGateway,
                               PlayerGateway playerGateway, CurrentDatePlugin currentDatePlugin,
                               RandomNumberGenerator randomNumberGenerator) {
        this.questGateway = questGateway;
        this.foilCardGateway = foilCardGateway;
        this.playerGateway = playerGateway;
        this.currentDatePlugin = currentDatePlugin;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public Quest tryToGenerateDailyQuest(Player player, Version appVersion, TimeZone timeZone) {
        List<Long> dailyQuestIds = questGateway.findDailyQuestIds(player.getId());
        if (isQuestGenerationPossible(player, dailyQuestIds, timeZone)) {
            return generateDailyQuest(player, dailyQuestIds, appVersion);
        } else {
            return null;
        }
    }

    public Quest tryToReplaceDailyQuest(Player player, Version appVersion, TimeZone timeZone, List<Long> dailyQuestIds, long questIdToReplace) {
        if (isQuestReplacementPossible(player, dailyQuestIds, timeZone)) {
            return replaceDailyQuest(player, dailyQuestIds, appVersion, questIdToReplace);
        } else {
            return null;
        }
    }

    public boolean isQuestReplacementPossible(Player player, List<Long> dailyQuestIds, TimeZone timeZone) {
        if (dailyQuestIds.size() < MAX_QUESTS ) {
            return false;
        }

        return isQuestGenerationPossible(player, timeZone);
    }

    private boolean isQuestGenerationPossible(Player player, List<Long> dailyQuestIds, TimeZone timeZone) {
        if (dailyQuestIds.size() >= MAX_QUESTS) {
            return false;
        }

        return isQuestGenerationPossible(player, timeZone);
    }

    private boolean isQuestGenerationPossible(Player player, TimeZone timeZone) {
        Date now = currentDatePlugin.getCurrentDate();
        Date lastQuestCreation = player.getLastQuestCreation();
        if (lastQuestCreation != null && now.getTime() - lastQuestCreation.getTime() < ONE_DAY_MILLIS) {
            return !isQuestAlreadyGeneratedToday(now, lastQuestCreation, timeZone);
        }

        return true;
    }

    private boolean isQuestAlreadyGeneratedToday(Date now, Date lastQuestCreation, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);

        calendar.setTime(now);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.setTime(lastQuestCreation);
        int questCreationDay = calendar.get(Calendar.DAY_OF_WEEK);

        return today == questCreationDay;
    }

    private Quest generateDailyQuest(Player player, List<Long> dailyQuestIds, Version appVersion) {
        Quest quest = findNewRandomDailyQuest(player, dailyQuestIds, appVersion);
        if (quest != null) {
            questGateway.addDailyQuest(player, quest, currentDatePlugin.getCurrentDate());
            updateLastQuestCreation(player);
        }
        return quest;
    }

    private Quest replaceDailyQuest(Player player, List<Long> dailyQuestIds, Version appVersion, long questIdToReplace) {
        Quest quest = findNewRandomDailyQuest(player, dailyQuestIds, appVersion);
        if (quest != null) {
            questGateway.replaceDailyQuest(player.getId(), questIdToReplace, quest.getId(), currentDatePlugin.getCurrentDate());
            updateLastQuestCreation(player);
        }
        return quest;
    }

    private void updateLastQuestCreation(Player player) {
        player.setLastQuestCreation(currentDatePlugin.getCurrentDate());
        playerGateway.updatePlayer(player);
    }

    private Quest findNewRandomDailyQuest(Player player, List<Long> dailyQuestIds, Version appVersion) {
        List<Quest> allQuests = questGateway.findAllQuests();
        List<Quest> potentialQuests = new ArrayList<>();

        for (Quest quest : allQuests) {
            if (isQuestPossibleForDaily(quest, player, dailyQuestIds, appVersion)) {
                potentialQuests.add(quest);
            }
        }

        if (potentialQuests.size() > 0) {
            return potentialQuests.get(randomNumberGenerator.randomInteger(0, potentialQuests.size() - 1));
        }

        return null;
    }

    private boolean isQuestPossibleForDaily(Quest quest, Player player, List<Long> dailyQuestIds, Version appVersion) {
        if (quest.isHidden()) {
            return false;
        }

        if (appVersion.compareTo(new Version(quest.getSinceVersion())) < 0) {
            return false;
        }

        if (dailyQuestIds.contains(quest.getId())) {
            return false;
        }

        return checkConditionsForSpecialDailyQuests(quest, player);
    }

    private boolean checkConditionsForSpecialDailyQuests(Quest quest, Player player) {
        if (quest.getId() == 20) {
            return foilCardGateway.isFoilCardOwnedByPlayer(player.getId(), 58, CardType.ITEM);
        }

        return true;
    }
}
