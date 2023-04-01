package com.backend.usuario.service.impl;

import com.backend.usuario.config.data.DetalherUserData;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserDetalheServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername() - Starting find user by username - username:[{}]", username);
        Optional<UserEntity> userEntityOptional = this.userRepository.findByUsername(username);
        if(!userEntityOptional.isPresent()){
            log.info("loadUserByUsername() - User not found - username:[{}]", username);
            throw new UserServiceException("Usuário "+ username+ " não encontrado !");
        }
        log.info("loadUserByUsername() - Finishing find user by username - username:[{}]", username);
        return new DetalherUserData(userEntityOptional);
    }
}
