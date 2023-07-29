CREATE TABLE `user_data` (
  `dados_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome_completo` varchar(255) DEFAULT NULL,
  `data_nascimento` varchar(10) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `celular` varchar(20) DEFAULT NULL,
  `endereco` varchar(255) DEFAULT NULL,
  `trabalho` varchar(255) DEFAULT NULL,
  `cep` varchar(10) DEFAULT NULL,
  `cpf` varchar(14) DEFAULT NULL,
  PRIMARY KEY (`dados_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
