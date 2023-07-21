package com.backend.usuario.repository;

import com.backend.usuario.entity.UserCreationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCreationHistoryRepository extends JpaRepository<UserCreationHistoryEntity, Long> {
}
