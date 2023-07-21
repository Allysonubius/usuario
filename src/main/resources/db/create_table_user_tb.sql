CREATE TABLE `user_tb` (
  `id_user` varchar(55) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(55) NOT NULL,
  `user_role` bigint(32) NOT NULL,
  `date_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `date_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  KEY `user_role` (`user_role`),
  CONSTRAINT `user_tb_ibfk_1` FOREIGN KEY (`user_role`) REFERENCES `user_roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;