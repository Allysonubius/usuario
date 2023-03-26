package com.backend.usuario.repository;

import com.backend.usuario.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByPassword(String password);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(UUID id);
    void deleteById(UUID id);
}
