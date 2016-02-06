CREATE TABLE IF NOT EXISTS `BlackMarket` (
  `id` int(11) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `BlackMarketOffer` (
`id` int(11) NOT NULL,
  `cardId` int(11) NOT NULL,
  `cardType` int(11) NOT NULL,
  `expirationDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `BonusTime` (
`id` int(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `mapId` int(11) NOT NULL,
  `bonusTime00` int(11) NOT NULL DEFAULT '0',
  `bonusTime01` int(11) NOT NULL DEFAULT '0',
  `bonusTime02` int(11) NOT NULL DEFAULT '0',
  `bonusTime03` int(11) NOT NULL DEFAULT '0',
  `bonusTime10` int(11) NOT NULL DEFAULT '0',
  `bonusTime11` int(11) NOT NULL DEFAULT '0',
  `bonusTime12` int(11) NOT NULL DEFAULT '0',
  `bonusTime13` int(11) NOT NULL DEFAULT '0',
  `bonusTime20` int(11) NOT NULL DEFAULT '0',
  `bonusTime21` int(11) NOT NULL DEFAULT '0',
  `bonusTime22` int(11) NOT NULL DEFAULT '0',
  `bonusTime23` int(11) NOT NULL DEFAULT '0',
  `bonusTimeMax` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `BonusTime_0_9_2` (
`id` int(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `mapId` int(11) NOT NULL,
  `bonusTime00` int(11) NOT NULL DEFAULT '0',
  `bonusTime01` int(11) NOT NULL DEFAULT '0',
  `bonusTime02` int(11) NOT NULL DEFAULT '0',
  `bonusTime03` int(11) NOT NULL DEFAULT '0',
  `bonusTime10` int(11) NOT NULL DEFAULT '0',
  `bonusTime11` int(11) NOT NULL DEFAULT '0',
  `bonusTime12` int(11) NOT NULL DEFAULT '0',
  `bonusTime13` int(11) NOT NULL DEFAULT '0',
  `bonusTime20` int(11) NOT NULL DEFAULT '0',
  `bonusTime21` int(11) NOT NULL DEFAULT '0',
  `bonusTime22` int(11) NOT NULL DEFAULT '0',
  `bonusTime23` int(11) NOT NULL DEFAULT '0',
  `bonusTimeMax` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `BonusTime_0_9_3` (
`id` int(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `mapId` int(11) NOT NULL,
  `bonusTime00` int(11) NOT NULL DEFAULT '0',
  `bonusTime01` int(11) NOT NULL DEFAULT '0',
  `bonusTime02` int(11) NOT NULL DEFAULT '0',
  `bonusTime03` int(11) NOT NULL DEFAULT '0',
  `bonusTime10` int(11) NOT NULL DEFAULT '0',
  `bonusTime11` int(11) NOT NULL DEFAULT '0',
  `bonusTime12` int(11) NOT NULL DEFAULT '0',
  `bonusTime13` int(11) NOT NULL DEFAULT '0',
  `bonusTime20` int(11) NOT NULL DEFAULT '0',
  `bonusTime21` int(11) NOT NULL DEFAULT '0',
  `bonusTime22` int(11) NOT NULL DEFAULT '0',
  `bonusTime23` int(11) NOT NULL DEFAULT '0',
  `bonusTimeMax` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `BonusTime_0_10_0` (
`id` int(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `mapId` int(11) NOT NULL,
  `bonusTime00` int(11) NOT NULL DEFAULT '0',
  `bonusTime01` int(11) NOT NULL DEFAULT '0',
  `bonusTime02` int(11) NOT NULL DEFAULT '0',
  `bonusTime03` int(11) NOT NULL DEFAULT '0',
  `bonusTime10` int(11) NOT NULL DEFAULT '0',
  `bonusTime11` int(11) NOT NULL DEFAULT '0',
  `bonusTime12` int(11) NOT NULL DEFAULT '0',
  `bonusTime13` int(11) NOT NULL DEFAULT '0',
  `bonusTime20` int(11) NOT NULL DEFAULT '0',
  `bonusTime21` int(11) NOT NULL DEFAULT '0',
  `bonusTime22` int(11) NOT NULL DEFAULT '0',
  `bonusTime23` int(11) NOT NULL DEFAULT '0',
  `bonusTimeMax` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `BonusTime_1_0_0` (
`id` int(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `mapId` int(11) NOT NULL,
  `bonusTime00` int(11) NOT NULL DEFAULT '0',
  `bonusTime01` int(11) NOT NULL DEFAULT '0',
  `bonusTime02` int(11) NOT NULL DEFAULT '0',
  `bonusTime03` int(11) NOT NULL DEFAULT '0',
  `bonusTime10` int(11) NOT NULL DEFAULT '0',
  `bonusTime11` int(11) NOT NULL DEFAULT '0',
  `bonusTime12` int(11) NOT NULL DEFAULT '0',
  `bonusTime13` int(11) NOT NULL DEFAULT '0',
  `bonusTime20` int(11) NOT NULL DEFAULT '0',
  `bonusTime21` int(11) NOT NULL DEFAULT '0',
  `bonusTime22` int(11) NOT NULL DEFAULT '0',
  `bonusTime23` int(11) NOT NULL DEFAULT '0',
  `bonusTimeMax` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `BonusTime_1_1_0` (
`id` int(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `mapId` int(11) NOT NULL,
  `bonusTime00` int(11) NOT NULL DEFAULT '0',
  `bonusTime01` int(11) NOT NULL DEFAULT '0',
  `bonusTime02` int(11) NOT NULL DEFAULT '0',
  `bonusTime03` int(11) NOT NULL DEFAULT '0',
  `bonusTime10` int(11) NOT NULL DEFAULT '0',
  `bonusTime11` int(11) NOT NULL DEFAULT '0',
  `bonusTime12` int(11) NOT NULL DEFAULT '0',
  `bonusTime13` int(11) NOT NULL DEFAULT '0',
  `bonusTime20` int(11) NOT NULL DEFAULT '0',
  `bonusTime21` int(11) NOT NULL DEFAULT '0',
  `bonusTime22` int(11) NOT NULL DEFAULT '0',
  `bonusTime23` int(11) NOT NULL DEFAULT '0',
  `bonusTimeMax` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `BonusTime_1_2_0` (
`id` int(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `mapId` int(11) NOT NULL,
  `bonusTime00` int(11) NOT NULL DEFAULT '0',
  `bonusTime01` int(11) NOT NULL DEFAULT '0',
  `bonusTime02` int(11) NOT NULL DEFAULT '0',
  `bonusTime03` int(11) NOT NULL DEFAULT '0',
  `bonusTime10` int(11) NOT NULL DEFAULT '0',
  `bonusTime11` int(11) NOT NULL DEFAULT '0',
  `bonusTime12` int(11) NOT NULL DEFAULT '0',
  `bonusTime13` int(11) NOT NULL DEFAULT '0',
  `bonusTime20` int(11) NOT NULL DEFAULT '0',
  `bonusTime21` int(11) NOT NULL DEFAULT '0',
  `bonusTime22` int(11) NOT NULL DEFAULT '0',
  `bonusTime23` int(11) NOT NULL DEFAULT '0',
  `bonusTimeMax` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `Hero` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_bin NOT NULL,
  `rarity` int(4) NOT NULL,
  `sinceVersion` varchar(5) COLLATE utf8_bin NOT NULL,
  `isForgeable` int(1) NOT NULL,
  `isBlackMarketOffer` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `Item` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_bin NOT NULL,
  `rarity` int(4) NOT NULL,
  `sinceVersion` varchar(5) COLLATE utf8_bin NOT NULL,
  `isForgeable` int(1) NOT NULL,
  `isBlackMarketOffer` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `Player` (
`id` int(11) NOT NULL,
  `savekey` varchar(6) COLLATE utf8_bin NOT NULL,
  `level` int(11) NOT NULL,
  `experience` bigint(20) NOT NULL,
  `name` varchar(32) COLLATE utf8_bin NOT NULL,
  `isCheater` tinyint(1) NOT NULL DEFAULT '0',
  `lastUpdate` datetime DEFAULT NULL,
  `relics` int(11) NOT NULL DEFAULT '0',
  `boosters` int(11) NOT NULL DEFAULT '0',
  `lastQuestCreation` datetime DEFAULT NULL,
  `email` text COLLATE utf8_bin,
  `supporterLevel` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `PlayerDailyQuest` (
  `playerId` int(11) NOT NULL,
  `questId` int(11) NOT NULL,
  `creationDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `PlayerFoilCard` (
  `playerId` int(11) NOT NULL,
  `cardId` int(11) NOT NULL,
  `cardType` int(11) NOT NULL,
  `amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `PlayerHiddenQuest` (
  `playerId` int(11) NOT NULL,
  `questId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `PlayerPurchasedBlackMarketOffer` (
  `playerId` int(11) NOT NULL,
  `offerId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `PlayerPurchasedProduct` (
  `playerId` int(11) NOT NULL,
  `productId` varchar(128) COLLATE utf8_bin NOT NULL,
  `store` varchar(64) COLLATE utf8_bin NOT NULL,
  `data` text COLLATE utf8_bin NOT NULL,
  `signature` text COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `Potion` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_bin NOT NULL,
  `rarity` int(4) NOT NULL,
  `sinceVersion` varchar(5) COLLATE utf8_bin NOT NULL,
  `isForgeable` int(1) NOT NULL,
  `isBlackMarketOffer` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `Quest` (
  `id` int(11) NOT NULL,
  `title` varchar(128) COLLATE utf8_bin NOT NULL,
  `description` varchar(256) COLLATE utf8_bin NOT NULL,
  `requiredAmount` int(11) NOT NULL,
  `reward` int(11) NOT NULL,
  `isHidden` int(1) NOT NULL,
  `sinceVersion` varchar(16) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `Tower` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_bin NOT NULL,
  `rarity` int(4) NOT NULL,
  `sinceVersion` varchar(5) COLLATE utf8_bin NOT NULL,
  `isForgeable` int(1) NOT NULL,
  `isBlackMarketOffer` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `VersionInfo` (
  `store` varchar(64) COLLATE utf8_bin NOT NULL,
  `version` varchar(14) COLLATE utf8_bin NOT NULL,
  `url` text COLLATE utf8_bin NOT NULL,
  `details` text COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


ALTER TABLE `BlackMarket`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `BlackMarketOffer`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `BonusTime`
 ADD PRIMARY KEY (`id`), ADD KEY `playerId` (`playerId`), ADD KEY `mapId` (`mapId`);

ALTER TABLE `BonusTime_0_9_2`
 ADD PRIMARY KEY (`id`), ADD KEY `playerId` (`playerId`), ADD KEY `mapId` (`mapId`);

ALTER TABLE `BonusTime_0_9_3`
 ADD PRIMARY KEY (`id`), ADD KEY `playerId` (`playerId`), ADD KEY `mapId` (`mapId`);

ALTER TABLE `BonusTime_0_10_0`
 ADD PRIMARY KEY (`id`), ADD KEY `playerId` (`playerId`), ADD KEY `mapId` (`mapId`);

ALTER TABLE `BonusTime_1_0_0`
 ADD PRIMARY KEY (`id`), ADD KEY `playerId` (`playerId`), ADD KEY `mapId` (`mapId`);

ALTER TABLE `BonusTime_1_1_0`
 ADD PRIMARY KEY (`id`), ADD KEY `playerId` (`playerId`), ADD KEY `mapId` (`mapId`);

ALTER TABLE `BonusTime_1_2_0`
 ADD PRIMARY KEY (`id`), ADD KEY `playerId` (`playerId`), ADD KEY `mapId` (`mapId`);

ALTER TABLE `Hero`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `Item`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `Player`
 ADD PRIMARY KEY (`id`), ADD KEY `savekey` (`savekey`);

ALTER TABLE `PlayerDailyQuest`
 ADD PRIMARY KEY (`playerId`,`questId`);

ALTER TABLE `PlayerFoilCard`
 ADD PRIMARY KEY (`playerId`,`cardId`,`cardType`);

ALTER TABLE `PlayerHiddenQuest`
 ADD PRIMARY KEY (`playerId`,`questId`);

ALTER TABLE `PlayerPurchasedBlackMarketOffer`
 ADD PRIMARY KEY (`playerId`,`offerId`);

ALTER TABLE `PlayerPurchasedProduct`
 ADD PRIMARY KEY (`playerId`,`productId`);

ALTER TABLE `Potion`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `Quest`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `Tower`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `VersionInfo`
 ADD PRIMARY KEY (`store`);


ALTER TABLE `BlackMarketOffer`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `BonusTime`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `BonusTime_0_9_2`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `BonusTime_0_9_3`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `BonusTime_0_10_0`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `BonusTime_1_0_0`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `BonusTime_1_1_0`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `BonusTime_1_2_0`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `Player`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
