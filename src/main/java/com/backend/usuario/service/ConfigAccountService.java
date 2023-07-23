package com.backend.usuario.service;

import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static com.backend.usuario.domain.constants.SecurityConstants.ACTIVE_ACCOUNT;
import static com.backend.usuario.domain.constants.SecurityConstants.DEACTIVE_ACCOUNT;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ControllerAdvice
public class ConfigAccountService {

    private final UserRepository userRepository;

    public UserEntity getUserActiveAccount(UUID userId) {
        Optional<UserEntity> optional = userRepository.findById(userId);
        if(!optional.isPresent()){
            log.info("getUserDesativeAccount() - Usuário com ID {} não encontrado ", userId);
            throw new UserServiceException("getUserDesativeAccount() - Usuário não encontrado");
        }

        UserEntity userEntity = optional.get();

        if (userEntity.getActive().equals(DEACTIVE_ACCOUNT)) {
            userEntity.setActive(ACTIVE_ACCOUNT);
            return userRepository.save(userEntity);
        } else {
            log.error("User account is already active.");
            throw new UserServiceException("User account is already active.");
        }
    }

    public UserEntity getUserDesativeAccount(UUID uuid){
        Optional<UserEntity> optional = this.userRepository.findById(uuid);
        if(!optional.isPresent()){
            log.info("getUserDesativeAccount() - Usuário com ID {} não encontrado ", uuid);
            throw new UserServiceException("getUserDesativeAccount() - Usuário não encontrado");
        }

        UserEntity userEntity = optional.get();

        if(userEntity.getActive().equals(ACTIVE_ACCOUNT)){
            userEntity.setActive(DEACTIVE_ACCOUNT);
            return this.userRepository.save(userEntity);
        }else {
            log.error("User account is already desactive.");
            throw new UserServiceException("User account is already desactive.");
        }
    }

}
