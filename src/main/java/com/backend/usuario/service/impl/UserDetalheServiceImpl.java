package com.backend.usuario.service.impl;

import com.backend.usuario.config.data.DetalherUserData;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j(topic = "UserDetalheServiceImpl")
@AllArgsConstructor
public class UserDetalheServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = this.userRepository.findByUsername(username);
        if(userEntityOptional.isPresent()){
            log.info("Usuário não encontrado");
            throw new UsernameNotFoundException("Usuário "+ username+ " não encontrado !");
        }
        return new DetalherUserData(userEntityOptional);
    }
}
