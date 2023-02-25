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
@Transactional
public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    public UserEntity saveUserEntity(UserEntity userEntity){
        boolean existeNome = false;
        Optional<UserEntity> optional = this.userRepository.findByUsername(userEntity.getUsername());
        if(!optional.isPresent()){
            LOGGER.info("Usuário salvo com sucesso !" + userEntity.getUsername());
            existeNome = true;
        }else{
            LOGGER.info("Usuário ja cadastrado" +  userEntity.getUsername());
            throw new RuntimeException("Username já cadastrado :" + userEntity.getUsername());
        }
        return this.userRepository.save(userEntity);
    }
}
