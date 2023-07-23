package com.backend.usuario.service;

import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.backend.usuario.domain.response.role.RoleResponse;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.entity.UserCreationHistoryEntity;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.entity.UserRoleEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserCreationHistoryRepository;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ControllerAdvice
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserCreationHistoryRepository userCreationHistoryRepository;
    private final EntityManager entityManager;

    /**
     * @param userEntity
     * @return
     */
    public UserEntity saveUserService(UserEntity userEntity){
        try{
            log.info("loginUser() - Starting saving new user - user:[{}]", userEntity.toString());
            Optional<UserEntity> optionalUsername = this.userRepository.findByUsername(userEntity.getUsername());
            checkIfUsernameAlreadyExists(optionalUsername, userEntity);

            Optional<UserEntity> optionalEmail = this.userRepository.findByEmail(userEntity.getEmail());
            checkIfEmailAlreadyExists(optionalEmail, userEntity);

            saveUserCreationHistory(userEntity);

            UserEntity savedUser = this.userRepository.save(userEntity);
            log.info("saveUserService() - User saved successfully - user: {}", savedUser.toString());
            return savedUser;
        } catch (UserServiceException e) {
            log.info("saveUserService() - Error when saving user - message: {}", e.getMessage());
            throw new UserServiceException("Error when saving user: " + e.getMessage());
        } catch (Exception e) {
            log.error("saveUserService() - Unknown error when saving user - message: {}", e.getMessage());
            throw new UserServiceException("Unknown error when saving user: " + e.getMessage());
        }
    }
    @Transactional
    public UserCreationHistoryEntity saveUserCreationHistory(UserEntity userEntity) {
        // Se a entidade UserEntity estiver detached, torne-a managed usando o merge
        if (!entityManager.contains(userEntity)) {
            userEntity = entityManager.merge(userEntity);
        }

        UserCreationHistoryEntity history = new UserCreationHistoryEntity();
        history.setIdUser(String.valueOf(userEntity.getId()));
        history.setUsername(userEntity.getUsername());
        history.setPassword(userEntity.getPassword());
        history.setEmail(userEntity.getEmail());
        history.setUserRole(userEntity.getRole().getId());
        history.setDateCreated(new Date());

        return userCreationHistoryRepository.save(history);
    }
    /**
     * @return
     */
    public Page<UserResponse> listUsers(Pageable pageable){
        log.info("listUsers() - Starting user list");

        Page<UserEntity> entityPage = listUsersRepository(pageable);

        List<UserEntity> userEntities = entityPage.getContent();
        List<UserResponse> responseList = new ArrayList<>();

        for(UserEntity user : userEntities){
            UserResponse response = new UserResponse();
            response.setId(user.getId().toString());
            response.setUsername(user.getUsername());

            response.setDateCreate(String.valueOf(user.getDateCreate()));
            response.setDateUpdate(String.valueOf(user.getDateUpdate()));


            response.setEmail(user.getEmail());

            // Mapear list de roles
            Set<RoleResponse> roleResponses = new HashSet<>();
            if(user.getRole() != null){
                RoleResponse roleResponse = new RoleResponse();
                roleResponse.setId(user.getRole().getId().toString());
                roleResponse.setRole(user.getRole().getRole());
                roleResponses.add(roleResponse);
            }else{
                log.info("listUsers() - No Role found for user: {}", user.getRole());
                throw new UserServiceException("No role found for user : " + user.getRole());
            }
            response.setRole(roleResponses);

            response.setActive(user.getActive());

            responseList.add(response);
        }

        log.info("listUsers() - Completed user list users:{}");
        return new PageImpl<>(responseList, pageable,entityPage.getTotalElements());
    }
    /**
     * @return
     */
    private Page<UserEntity> listUsersRepository(Pageable pageable){
        log.info("listUsersRepository() - Querying user repository");
        Page<UserEntity> list = this.userRepository.findAll(pageable);
        if(list.isEmpty()){
            log.error("listUsersRepository() - No users found users:{}" , list.stream().toList());
            throw new UserServiceException("listUsersRepository() - No users found users:{}" + list.stream().toList());
        }
        log.info("listUsersRepository() - Completed user query user:{}", list.stream().toList());
        return list;
    }
    /**
     * @param id
     */
    public void deleteUser(UUID id){
        try {
            log.info("deleteUser() - Starting to delete user - ID: {}", id);
            deleteUserSearch(id);
            this.userRepository.deleteById(id);
        }catch (UserServiceException e){
            log.info("deleteUser() - Failed to delete user ID:{}. Reason: {}", id, e.getMessage());
            throw new UserServiceException("Failed to delete user ID: " + id);
        }
    }
    /**
     * @param id
     * @return
     */
    public RoleUserRequest getRoleById(Long id) {
        log.info("getRoleById() - Starting get role by id - id:{}", id);
        UserRoleEntity userRole = getUserRoleById(id).get();
        RoleUserRequest roleUser = new RoleUserRequest();
        roleUser.setId(userRole.getId());
        log.info("getRoleById() - Role found - id:{}", id);
        return roleUser;
    }
    /**
     * @param id
     */
    public void deleteUserSearch(UUID id) {
        Optional<UserEntity> userOpt = this.userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            this.userRepository.delete(user);
            log.info("deleteUserSearch() - User deleted successfully - ID: {}", id);
        } else {
            log.error("deleteUserSearch() - User not found for ID:{}", id);
            throw new UserServiceException("User not found for ID: " + id);
        }
    }
    /**
     * @param optionalUsername
     * @param userEntity
     */
    private void checkIfUsernameAlreadyExists(Optional<UserEntity> optionalUsername, UserEntity userEntity) {
        if(optionalUsername.isPresent()){
            log.info("saveUserService() - Username already registered - username:{}", userEntity.getUsername());
            throw new UserServiceException("Username already registered - username: " + userEntity.getUsername());
        }
    }
    /**
     * @param optionalEmail
     * @param userEntity
     */
    private void checkIfEmailAlreadyExists(Optional<UserEntity> optionalEmail, UserEntity userEntity) {
        if(optionalEmail.isPresent()){
            log.info("saveUserService() - Email already registered - email:{}", userEntity.getEmail());
            throw new UserServiceException("Email already registered - email: " + userEntity.getEmail());
        }
    }
    /**
     * @param id
     * @return
     */
    private Optional<UserRoleEntity> getUserRoleById(Long id) {
        log.info("getUserRoleById() - Retrieving role by ID:{}", id);
        Optional<UserRoleEntity> userRoleOpt = this.userRoleRepository.findById(id);
        if(userRoleOpt.isPresent()){
            log.info("getUserRoleById() - Role found for ID:{}", id);
            return userRoleOpt;
        }else {
            log.error("getRoleById() - Role not found for ID:{}", id);
            throw new UserServiceException("Role not found for ID: " + id);
        }
    }
}
