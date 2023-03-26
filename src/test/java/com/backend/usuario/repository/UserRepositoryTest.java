package com.backend.usuario.repository;

import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserRepositoryTest.class)
class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity.setPassword("testePassword");
        userEntity.setUsername("testeUsername");
    }

    @Test
    void findByUsername() {
        userEntity.setPassword(userEntity.getUsername());
        when(userRepository.findByPassword(userEntity.getUsername())).thenReturn(Optional.of(userEntity));

        // Act
        Optional<UserEntity> foundUser = userRepository.findByPassword(userEntity.getUsername());

        // Assert
        verify(userRepository).findByPassword(userEntity.getUsername());
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(userEntity, foundUser.get());
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());

        // Act
        Optional<UserEntity> foundUser = userRepository.findByUsername(userEntity.getUsername());

        // Assert
        verify(userRepository).findByUsername(userEntity.getUsername());
        Assertions.assertFalse(foundUser.isPresent());
    }

    @Test
    void findByPassword() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(userEntity.getPassword());
        when(userRepository.findByPassword(userEntity.getPassword())).thenReturn(Optional.of(userEntity));

        // Act
        Optional<UserEntity> foundUser = userRepository.findByPassword(userEntity.getPassword());

        // Assert
        verify(userRepository).findByPassword(userEntity.getPassword());
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(userEntity, foundUser.get());
    }

    @Test
    void testFindByPasswordNotFound() {
        when(userRepository.findByPassword(userEntity.getPassword())).thenReturn(Optional.empty());

        // Act
        Optional<UserEntity> foundUser = userRepository.findByPassword(userEntity.getPassword());

        // Assert
        verify(userRepository).findByPassword(userEntity.getPassword());
        Assertions.assertFalse(foundUser.isPresent());
    }
}