CREATE TABLE `userconnection` (
  `userId` VARCHAR(255) NOT NULL,
  `providerId` VARCHAR(255) NOT NULL,
  `providerUserId` VARCHAR(255) NOT NULL,
  `rank` INT(11) NOT NULL,
  `displayName` VARCHAR(255) NULL DEFAULT NULL,
  `profileUrl` VARCHAR(512) NULL DEFAULT NULL,
  `imageUrl` VARCHAR(512) NULL DEFAULT NULL,
  `accessToken` VARCHAR(512) NOT NULL,
  `secret` VARCHAR(512) NULL DEFAULT NULL,
  `refreshToken` VARCHAR(512) NULL DEFAULT NULL,
  `expireTime` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`userId`, `providerId`, `providerUserId`),
  INDEX `UserConnectionRank` (`userId`, `providerId`, `rank`),
  INDEX `ProviderIdProviderUserId` (`providerId`, `providerUserId`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;
