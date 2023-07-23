package com.backend.usuario.service;

import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.repository.entity.UserEntity;
import com.backend.usuario.repository.entity.UserRoleEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.repository.UserRoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserService.class)
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserRoleRepository userRoleRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private EntityManager entityManager;

    @BeforeEach
    void setup(){
        userService = new UserService(
                userRepository,
                userRoleRepository);
    }

    @Test
    void testListUsers_Success() {
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity user1 = new UserEntity();
        user1.setId(UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3"));
        user1.setUsername("john_doe");
        user1.setDateCreate(new Date());
        user1.setDateUpdate(new Date());
        user1.setEmail("john.doe@example.com");
        Long id = 1L;
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setId(id);
        user1.setRole(userRoleEntity);
        // Adicionar outras propriedades ao usuário 1

        userEntities.add(user1);

        // Configurar o comportamento simulado do repositório de usuários
        Page<UserEntity> page = new PageImpl<>(userEntities);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Chamar o método que está sendo testado
        Page<UserResponse> result = userService.listUsers(Pageable.unpaged());

        // Verificar o resultado
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        List<UserResponse> userList = result.getContent();
        assertEquals(1, userList.size());

        UserResponse userResponse1 = userList.get(0);
        assertEquals("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3", userResponse1.getId());
        assertEquals("john_doe", userResponse1.getUsername());
        assertEquals("john.doe@example.com", userResponse1.getEmail());
    }

    @Test
    void testListUsers_NoUsersFound() {
        Page<UserEntity> emptyPage = new PageImpl<>(Collections.emptyList());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);


        assertThrows(UserServiceException.class, () -> userService.listUsers(Pageable.unpaged()));
    }

    @Test
    void testListUsers_NoRoleFound() {
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity user1 = new UserEntity();
        user1.setId(UUID.randomUUID());
        user1.setUsername("john_doe");
        user1.setDateCreate(new Date());
        user1.setDateUpdate(new Date());
        user1.setEmail("john.doe@example.com");

        userEntities.add(user1);

        Page<UserEntity> page = new PageImpl<>(userEntities);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        assertThrows(UserServiceException.class, () -> userService.listUsers(Pageable.unpaged()));
    }

    @Test
    void saveUserShouldSaveSuccessfully(){
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(createUser().getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(createUser());

        UserEntity savedUser = this.userService.saveUserService(createUser());

        Assertions.assertThat(savedUser).isEqualTo(createUser());
    }
    @Test
    void saveUserShouldThrowDataIntegrityViolationExceptionWhenUsernameAlreadyExists() {
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() ->
                userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error when saving user: Username already registered - username: john.doe");
    }
    @Test
    void saveUserShouldThrowDataIntegrityViolationExceptionWhenEmailAlreadyExists() {
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(createUser().getEmail())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() ->
                userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error when saving user: Email already registered - email: john.doe@example.com");
    }
    @Test
    void saveUserShouldThrowUserServiceExceptionWhenUserServiceExceptionIsThrown() {
        when(userRepository.findByUsername(createUser().getUsername())).thenThrow(new UserServiceException("Error"));

        assertThatThrownBy(() -> userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error");
    }
    @Test
    void saveUserShouldThrowUserServiceExceptionWhenUnknownExceptionIsThrown() {
        when(userRepository.findByUsername(createUser().getUsername())).thenThrow(new RuntimeException("Error"));

        assertThatThrownBy(() -> userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Unknown error");
    }
    @Test
    void testDeleteUserSearch_sucess(){
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(uuid);
        Optional<UserEntity> user = Optional.of(userEntity);
        when(this.userRepository.findById(uuid)).thenReturn(user);
        this.userService.deleteUserSearch(uuid);
        verify(this.userRepository, timeout(1)).delete(userEntity);
    }
    @Test
    void testDeleteUserSearch_failure(){
        UUID uuid = UUID.fromString("95da51e8-d8bb-4370-8e9b-e33af89e780d");
        Optional<UserEntity> user = Optional.empty();
        when(this.userRepository.findById(uuid)).thenReturn(user);
        assertThatThrownBy(() ->
                this.userService.deleteUserSearch(uuid))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("User not found for ID: 95da51e8-d8bb-4370-8e9b-e33af89e780d");
    }

    @Test
    void  testGetRoleById(){
        Long id = 1L;
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setId(id);
        RoleUserRequest expectedRoleUser = new RoleUserRequest();
        expectedRoleUser.setId(id);

        when(this.userRoleRepository.findById(id)).thenReturn(Optional.of(userRoleEntity));

        RoleUserRequest actualRoleUser = this.userService.getRoleById(id);

        assertEquals(expectedRoleUser.getId(), actualRoleUser.getId());
    }
    @Test
    void testGetRoleById_RoleNotFound(){
        Long id = 1L;

        when(this.userRoleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserServiceException.class, () -> {
            this.userService.getRoleById(id);
        });

        verify(userRoleRepository, times(1)).findById(id);
    }
    @Test
    void testDeleteUser(){
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(uuid);
        Optional<UserEntity> user = Optional.of(userEntity);
        when(this.userRepository.findById(uuid)).thenReturn(user);
        assertDoesNotThrow(() -> {
            this.userService.deleteUser(uuid);
        });

        verify(userRepository, times(1)).deleteById(uuid);
    }
    @Test
    void testDeleteUser_UserServiceException() {
        UUID id = UUID.randomUUID();

        UserRepository userRepository = mock(UserRepository.class);
        doThrow(new UserServiceException("Failed to delete user ID: " + id)).when(userRepository).deleteById(id);

        UserServiceException exception = assertThrows(UserServiceException.class, () -> {
            this.userService.deleteUser(id);
        });
        assertEquals("Failed to delete user ID: " + id, exception.getMessage());
    }
    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setUsername("john.doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        return user;
    }
}