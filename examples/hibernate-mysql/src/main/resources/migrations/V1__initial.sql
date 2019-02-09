CREATE TABLE `users` (
  `id` VARCHAR(255) NOT NULL,
  `roles` VARCHAR(500) DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `confirm_uuid` VARCHAR(255) NOT NULL,
  `confirmed` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `confirm_uuid` (`confirm_uuid`),
  INDEX `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
