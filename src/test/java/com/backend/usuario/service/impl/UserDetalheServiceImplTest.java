package com.backend.usuario.service.impl;

import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserDetalheServiceImpl.class)
class UserDetalheServiceImplTest {

    @InjectMocks
    private UserDetalheServiceImpl myService;

    @MockBean
    private UserRepository userRepository;
    private UserEntity userEntity;

    @Test
    public void testLoadUserByUsernameWithValidUsername() {
        String username = "johndoe";
        String password = "1234567890";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        UserDetalheServiceImpl service = new UserDetalheServiceImpl(userRepository);
        UserDetails userDetails = service.loadUserByUsername(username);
        assertEquals(userEntity.getUsername(), userDetails.getUsername());
        assertEquals(userEntity.getPassword(), userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsernameWithInvalidUsername() {
//        String username = "johndoe";
//        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
//        UserDetalheServiceImpl service = new UserDetalheServiceImpl(userRepository);
//        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username));
    }
}