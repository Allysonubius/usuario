package com.backend.usuario.repository;

import com.backend.usuario.entity.UserHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoryRepository extends JpaRepository<UserHistoryEntity, Long> {
}
