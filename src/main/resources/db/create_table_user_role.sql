CREATE TABLE `user_roles` (
  `role_id` bigint(32) NOT NULL,
  `user_role` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;