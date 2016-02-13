-- Schema

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

-- Fixed data

-- Black Market settings
INSERT INTO BlackMarket (id, price) VALUES
(1, 250);

-- Quests
INSERT INTO Quest (id, title, description, requiredAmount, reward, isHidden, sinceVersion) VALUES
(1, 'Headhunter', 'Kill 7 <color rgb="0xffff00">Challenges</color>!', 7, 40, 0, '1.0.0'),
(2, 'Dark Planet', 'Win a game with <color rgb="0x444444">Darkness</color> and <color rgb="0x71864c">Nature</color> towers only.', 1, 40, 0, '1.0.0'),
(3, 'Green City', 'Win a game with <color rgb="0x71864c">Nature</color> and <color rgb="0x868686">Metropolis</color> towers only.', 1, 40, 0, '1.0.0'),
(4, 'Claim the Moor', 'Defeat all enemies in the Blood Moor!', 1, 100, 1, '1.0.0'),
(5, 'Wind Lord', 'Defeat all enemies in the Shattered Plains!', 1, 100, 1, '1.0.0'),
(6, 'Credits Watcher', 'AWESOME! You watched the ENTIRE credits!', 1, 20, 1, '1.0.0'),
(7, 'Black Mirrors', 'Win a game with <color rgb="0x444444">Darkness</color> and <color rgb="0x868686">Metropolis</color> towers only.', 1, 40, 0, '1.0.0'),
(8, 'Embrace the Dark', 'Win a game with <color rgb="0x444444">Darkness</color> towers only.', 1, 60, 1, '1.0.0'),
(9, 'Nature''s Wrath', 'Win a game with <color rgb="0x71864c">Nature</color> towers only.', 1, 60, 1, '1.0.0'),
(10, 'Urbanize!', 'Win a game with <color rgb="0x868686">Metropolis</color> towers only.', 1, 60, 1, '1.0.0'),
(11, 'Twisted Mind', 'Defeat all enemies in the Twisted Paths!', 1, 100, 1, '1.0.0'),
(12, 'Golden Deckmaster!', 'Defeat all enemies in the Golden Land!', 1, 250, 1, '1.0.0'),
(13, 'Survivor', 'Survive 800 seconds in the bonus round!', 800, 60, 0, '1.0.0'),
(14, 'American Dream', 'Collect  1M gold!', 1000000, 60, 0, '1.0.0'),
(15, 'The wisdom of towers', 'Gain 400 tower levels!', 400, 40, 0, '1.0.0'),
(16, 'Black Market Visitor', 'Hey! You found the black market!', 1, 20, 1, '1.1.0'),
(17, 'Innkeeper''s regular', 'Thank you, come again! :-)', 1, 20, 1, '1.1.0'),
(18, 'Black Market Customer', 'A pleasure, friend. Come again!', 1, 100, 1, '1.1.0'),
(19, 'Perfect game', 'Good lord! You just bowled a perfect game!', 1, 250, 1, '1.2.0'),
(20, 'Strike Machine', 'Play bowling and roll 5 strikes!', 5, 40, 0, '1.2.0'),
(21, 'Dust, everywhere!', 'You just created your first
Card Dust.', 1, 20, 1, '1.3.0'),
(22, 'About time!', 'No longer swipe until
your fingers bleed.', 1, 20, 1, '1.3.0'),
(23, 'Cheers!', 'From now on you win every drinking contest.', 1, 20, 1, '1.3.0'),
(24, 'Deck Shuffler', 'Transmute 200 cards!', 200, 40, 0, '1.3.0'),
(25, 'Nibble, nibble, gnaw', 'Let <color rgb="0xa800ff">Knusperhexe</color> eat 700 creeps.', 700, 40, 0, '1.3.0'),
(26, 'It''s so fluffy!!!', 'Let <color rgb="0xa800ff">Balu</color> be cuddled 200 times.', 200, 40, 0, '1.3.0'),
(27, 'Monkey Mania!', 'Let <color rgb="0xa800ff">Muli</color> have 200 hangovers.', 200, 40, 0, '1.3.0');

-- Towers
INSERT INTO Tower (id, name, rarity, sinceVersion, isForgeable, isBlackMarketOffer) VALUES
(1, 'Beaver', 1, '0.1', 1, 0),
(2, 'Dandelion', 1, '0.1', 1, 0),
(3, 'Baby Rabbit', 1, '0.1', 1, 0),
(4, 'Poisonous Frog', 2, '0.1', 1, 0),
(5, 'Herb Witch', 2, '0.1', 1, 0),
(6, 'Wolf', 2, '0.1', 1, 0),
(7, 'Huli the Monkey', 3, '0.1', 1, 0),
(8, 'Bear Hunter', 3, '0.2', 1, 0),
(9, 'Holgar the Horrible', 3, '0.3', 1, 0),
(10, 'Ganesha', 4, '0.1', 1, 0),
(11, 'Balu the Bear', 4, '0.3', 1, 0),
(12, 'Manitou', 4, '0.3', 1, 0),
(39, 'Kiwi Egg', 5, '1.3', 1, 0),
(40, 'Kiwi', 5, '1.3', 0, 0),
(13, 'Hitman', 1, '0.2', 1, 0),
(14, 'Scientist', 1, '0.3', 1, 0),
(15, 'Pocket Thief', 1, '0.4', 1, 0),
(16, 'Electric Chair', 2, '0.1', 1, 0),
(17, 'Elvis Imitator', 2, '0.4', 1, 0),
(18, 'Irish Pub', 2, '0.9', 1, 0),
(19, 'Money Bin', 3, '0.4', 1, 0),
(20, 'Mr. Iron', 3, '0.9', 1, 0),
(21, 'Scarface', 3, '0.9', 1, 0),
(22, 'Muli the Evil Twin', 4, '0.7', 1, 0),
(23, 'Blofeld Laser Satellite', 4, '0.4', 1, 0),
(24, 'Black Widow', 4, '0.5', 1, 0),
(41, 'Stonecutter''s Temple', 4, '1.3', 1, 0),
(25, 'Miss Jilly', 1, '0.9', 1, 0),
(33, 'Twisted Novice Wizard', 1, '1.0', 1, 0),
(34, 'Small Spider', 1, '1.0', 1, 0),
(26, 'Scarecrow', 2, '0.6', 1, 0),
(27, 'Shadow', 2, '0.7', 1, 0),
(36, 'Solara, The Fire Elemental', 2, '1.0', 1, 0),
(28, 'Gib, the Frozen Daemon', 2, '0.8', 1, 0),
(32, 'Acolyte of Greed', 3, '1.0', 1, 0),
(38, 'The Ripper', 3, '1.1', 1, 0),
(29, 'Knusperhexe', 4, '0.6', 1, 0),
(30, 'The Dark Forge', 4, '0.7', 1, 0),
(37, 'Abyss King', 4, '1.0', 1, 0),
(35, 'Blood Demon', 5, '1.0', 1, 0),
(31, 'Horadric Cube', 4, '0.3', 0, 0);

-- Items
INSERT INTO Item (id, name, rarity, sinceVersion, isForgeable, isBlackMarketOffer) VALUES
(1, 'Wooden Staff', 1, '0.2', 1, 0),
(2, 'Leather Boots', 1, '0.2', 1, 0),
(3, 'Well done T-Bone Steak', 1, '0.2', 1, 0),
(4, 'Baby Sword', 1, '0.2', 1, 0),
(5, 'School Book', 1, '0.2', 1, 0),
(6, 'Wet Towel', 1, '0.2', 1, 0),
(7, 'Pumpkin', 1, '0.6', 1, 0),
(8, 'Medium T-Bone Steak', 2, '0.2', 1, 0),
(9, 'Handbag', 2, '0.2', 1, 0),
(10, 'Gold coins', 1, '0.2', 1, 0),
(11, 'Ring of greed', 1, '0.2', 1, 0),
(12, 'Longbow', 2, '0.3', 1, 0),
(13, 'Monster Teeth', 2, '0.3', 1, 0),
(14, 'Magic Mushroom', 2, '0.4', 1, 0),
(15, 'Lucky Pants', 2, '0.2', 1, 0),
(16, 'Painting of Solea', 2, '0.7', 1, 0),
(17, 'Meat Mallet', 2, '0.7', 1, 0),
(51, 'Wolfskin Cloak', 2, '0.10', 1, 0),
(18, 'Rare T-Bone Steak', 3, '0.2', 1, 0),
(19, 'Seven-leagues boots', 3, '0.2', 1, 0),
(20, 'Fistful of Steel', 3, '0.4', 1, 0),
(21, 'Herb Witch''s Cauldron', 3, '0.4', 1, 0),
(22, 'Key of Wisdom', 3, '0.5', 1, 0),
(23, 'Viking Helmet', 3, '0.9', 1, 0),
(24, 'Irish Pub''s Barrel', 3, '0.9', 1, 0),
(25, 'Excalibur', 4, '0.2', 1, 0),
(26, 'Helm of Hades', 4, '0.3', 1, 0),
(27, 'Messerschmidt''s Reaver', 4, '0.5', 1, 0),
(28, 'Dungeon Door', 4, '0.6', 1, 0),
(29, 'Scepter of Time', 4, '0.8', 1, 0),
(30, 'Wedding Ring of Yin', 4, '0.9', 1, 0),
(31, 'Wedding Ring of Yang', 4, '0.9', 1, 0),
(32, 'Norls Steel', 2, '0.7', 1, 0),
(33, 'Norls Guardian', 1, '0.7', 1, 0),
(34, 'Frozen Water', 1, '0.8', 1, 0),
(35, 'Frozen Heart', 3, '0.8', 1, 0),
(36, 'Frozen Candle', 3, '0.8', 1, 0),
(37, 'Frozen Book', 2, '0.8', 1, 0),
(38, 'Dried Cactus', 2, '0.8', 1, 0),
(39, 'Rotten Toadstool', 1, '0.8', 1, 0),
(40, 'Mummy Bandages', 4, '0.8', 1, 0),
(41, 'Stressful Wristwatch', 1, '0.9', 1, 0),
(42, 'Last Train of the Day', 2, '0.9', 1, 0),
(43, 'Unrelenting Force', 3, '0.9', 1, 0),
(59, 'Plasma Blade', 4, '1.3', 1, 0),
(60, 'GT1, the little robot', 4, '1.3', 1, 0),
(44, 'Dark Baby sword', 1, '0.7', 1, 0),
(45, 'Dark gold coins', 1, '0.7', 1, 0),
(46, 'Dark ring of greed', 1, '0.7', 1, 0),
(47, 'Dark Meat Mallet', 2, '0.7', 1, 0),
(48, 'Dark Witch''s Cauldron', 3, '0.7', 1, 0),
(49, 'Dark Fistful of Steel', 3, '0.7', 1, 0),
(50, 'Blade of Darkness', 4, '0.7', 1, 0),
(52, 'Blood Demon''s Blade', 5, '1.0', 0, 0),
(53, 'Seelenreisser', 5, '1.0', 1, 0),
(54, 'Unlucky Pants', 5, '1.1', 0, 1),
(55, 'Skull of Darkness', 5, '1.1', 0, 1),
(56, 'Spectral Daggers', 5, '1.1', 0, 1),
(57, 'Cape of the Specter', 5, '1.1', 0, 1),
(58, 'The Dude', 5, '1.2', 0, 1),
(63, 'Mjoelnir', 5, '1.3', 0, 1),
(61, 'A Note from the Developer', 4, '1.3', 0, 0),
(62, 'A Note from the Developer', 1, '1.3', 0, 0);

-- Potions
INSERT INTO Potion (id, name, rarity, sinceVersion, isForgeable, isBlackMarketOffer) VALUES
(1, 'Small Potion of Strength', 1, '0.3', 1, 0),
(2, 'Small Potion of Speed', 1, '0.3', 1, 0),
(3, 'Small Potion of Crit', 1, '0.3', 1, 0),
(4, 'Mead Bottle', 1, '0.3', 1, 0),
(5, 'Potion of Strength', 2, '0.3', 1, 0),
(6, 'Potion of Speed', 2, '0.3', 1, 0),
(7, 'Potion of Crit', 2, '0.3', 1, 0),
(8, 'Water of Life', 2, '0.3', 1, 0),
(9, 'Great Potion of Strength', 3, '0.3', 1, 0),
(10, 'Great Potion of Speed', 3, '0.3', 1, 0),
(11, 'Great Potion of Crit', 3, '0.3', 1, 0),
(12, 'Great Water of Life', 3, '0.3', 1, 0),
(13, 'Tears of the Gods', 4, '0.4', 1, 0),
(14, 'Nillos'' Elixir of Cunning', 4, '0.4', 1, 0),
(15, 'Painkiller', 4, '0.5', 1, 0),
(16, 'Soul Flask', 4, '0.6', 1, 0),
(17, 'Essence of Wisdom', 4, '0.8', 1, 0),
(20, 'Card Dust', 4, '1.3', 1, 0),
(21, 'Card Dust', 4, '1.3', 1, 0),
(22, 'Card Dust', 4, '1.3', 1, 0),
(23, 'Card Dust', 4, '1.3', 1, 0),
(18, 'Essence of Luck', 5, '1.0', 1, 0),
(19, 'Angelic Elixir', 5, '1.0', 0, 0),
(24, 'A Drink from the Developer', 4, '1.3', 0, 0);

-- Heroes
INSERT INTO Hero (id, name, rarity, sinceVersion, isForgeable, isBlackMarketOffer) VALUES
(1, 'Sir Littlefinger', 1, '1.0', 1, 0),
(2, 'Shadow Mane', 1, '1.0', 1, 0),
(3, 'Prince Lycaon', 1, '1.0', 1, 0),
(4, 'Lord Roderic', 5, '1.0', 1, 0),
(5, 'Cookiemonster', 5, '1.0', 0, 0),
(6, 'Innkeeper', 5, '1.0', 0, 0),
(7, 'The ''horadric'' Mage', 5, '1.0', 1, 0),
(8, 'Osmo, the Jester King', 5, '1.0', 1, 0),
(9, 'Kvothe, the Arcane', 2, '1.1', 1, 0),
(10, 'Loan Shark', 5, '1.1', 1, 0),
(11, 'Jack in the Box', 5, '1.3', 1, 0);

-- Version info
INSERT INTO `VersionInfo` (`store`, `version`, `url`, `details`) VALUES
('GooglePlay', '1.2.0', 'market://details?id=air.com.mazebert.MazebertTD', 'New wizard skills!\n\nA new black market item!\n\nSeveral bugfixes..'),
('iTunes', '1.2.0', 'https://itunes.apple.com/us/app/mazebert-td/id714699723?mt=8&uo=4', 'New wizard skills!\n\nA new black market item!\n\nSeveral bugfixes..');