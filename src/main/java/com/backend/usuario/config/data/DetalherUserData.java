package com.backend.usuario.config.data;

import com.backend.usuario.repository.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public class DetalherUserData implements UserDetails {
    private final Optional<UserEntity> userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
    @Override
    public String getPassword() {
        return this.userEntity.orElse(new UserEntity()).getPassword();
    }
    @Override
    public String getUsername() {
        return this.userEntity.orElse(new UserEntity()).getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
