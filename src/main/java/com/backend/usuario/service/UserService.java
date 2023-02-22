package com.backend.usuario.service;

import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j(topic = "UserService")
public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public String saveUserEntity(UserEntity userEntity){
        boolean existName = false;
        Optional<UserEntity> optional = this.userRepository.findByUsername(userEntity.getUsername());
        if(!optional.isPresent()){
            log.info("Usuário salvo com sucesso !" + userEntity.getUsername());
            existName = true;
        }else{
            log.info("Usuário ja cadastrado" +  userEntity.getUsername());
            throw new RuntimeException("Username já cadastrado :" + userEntity.getUsername());
        }
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        return saveUserEntity(userEntity);
    }
}
