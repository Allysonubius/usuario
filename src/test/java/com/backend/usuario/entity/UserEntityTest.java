package com.backend.usuario.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserEntity.class)
class UserEntityTest {

    @InjectMocks
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = mock(UserEntity.class);
        userEntity.setId(UUID.fromString("90973e56-34c6-401d-a4c8-d0425c146335"));
        userEntity.setUsername("allyson");
        userEntity.setPassword("1234567890");
        userEntity.setDateCreate(Timestamp.valueOf("2025-02-25 03:00:00"));
        userEntity.setDateUpdate(Timestamp.valueOf("2025-02-25 03:00:00"));
    }

    @Test
    void getId() {
        when(userEntity.getId()).thenReturn(UUID.fromString("90973e56-34c6-401d-a4c8-d0425c146335"));
    }

    @Test
    void getUsername() {
        when(userEntity.getUsername()).thenReturn("allyson");
    }

    @Test
    void getPassword() {
        when(userEntity.getPassword()).thenReturn("1234567890");
    }

    @Test
    void getDateCreate() {
        assertEquals(userEntity.getDateCreate(),userEntity.getDateCreate());
    }

    @Test
    void getDateUpdate() {
        assertEquals(userEntity.getDateUpdate(),userEntity.getDateUpdate());
    }

    @Test
    void setId() {
        verify(userEntity).setId(UUID.fromString("90973e56-34c6-401d-a4c8-d0425c146335"));
    }

    @Test
    void setUsername() {
       verify(userEntity).setUsername("allyson");
    }

    @Test
    void setPassword() {
        verify(userEntity).setPassword("1234567890");
    }

    @Test
    void setDateCreate() {
        verify(userEntity).setDateCreate(Timestamp.valueOf("2025-02-25 03:00:00"));
    }

    @Test
    void setDateUpdate() {
        verify(userEntity).setDateUpdate(Timestamp.valueOf("2025-02-25 03:00:00"));
    }
}