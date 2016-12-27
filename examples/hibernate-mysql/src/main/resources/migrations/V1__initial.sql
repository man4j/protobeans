CREATE TABLE `users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `roles` varchar(500) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `confirm_uuid` varchar(255) NOT NULL,
  `confirmed` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `confirm_uuid` (`confirm_uuid`),
  INDEX `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
