package com.backend.usuario.service.impl;

import com.backend.usuario.config.data.DetalherUserData;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDetalheServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = this.userRepository.findByUsernameOpt(username);
        if(userEntityOptional.isPresent()){
            throw new UsernameNotFoundException("Usuário "+ username+ " não encontrado !");
        }
        return new DetalherUserData(userEntityOptional);
    }
}
