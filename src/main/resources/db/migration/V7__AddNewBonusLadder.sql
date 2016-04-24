-- Backup old bonus time table
CREATE TABLE BonusTime_1_3_0 LIKE BonusTime;
INSERT INTO BonusTime_1_3_0 SELECT * FROM BonusTime;

-- Reset all bonus time entries
DELETE FROM BonusTime WHERE 1;