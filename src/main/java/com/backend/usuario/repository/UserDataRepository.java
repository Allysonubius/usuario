package com.backend.usuario.repository;

import com.backend.usuario.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserDataEntity, Long> {
}
