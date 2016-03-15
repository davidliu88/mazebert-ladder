ALTER TABLE PlayerPurchasedProduct ADD COLUMN appVersion varchar(5) COLLATE utf8_bin DEFAULT NULL;
ALTER TABLE PlayerPurchasedProduct ADD COLUMN purchaseDate datetime DEFAULT NULL;