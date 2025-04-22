package com.champlain.userservice.dataaccesslayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryIntegrationTest {
    private final String VALID_USER_ID = "c27242a2-abb9-45b2-a85d-ed9ffa15f92c";
    private final String NOT_FOUND_USER_ID = "c3540a89-cb47-4c96-888eff96708db4d0";
    private final String VALID_USER_FIRST_NAME = "Alice";
    private final String VALID_USER_LAST_NAME = "Johnson";
    private final String VALID_USER_EMAIL =  "alice.johnson@example.com";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenCustomerDoesNotExist_ReturnNull() {
        //act
        User customer = userRepository.findUserByUserIdentifier_UserId(NOT_FOUND_USER_ID);

        //assert
        assertNull(customer);
    }

    @Test
    public void whenUserExist_ReturnUserById() {
        //act
        User user = userRepository.findUserByUserIdentifier_UserId(VALID_USER_ID);

        //assert
        assertNotNull(user);
        assertEquals(VALID_USER_ID, user.getUserIdentifier().getUserId());
        assertEquals(VALID_USER_FIRST_NAME, user.getFirstName());
        assertEquals(VALID_USER_LAST_NAME, user.getLastName());
        assertEquals(VALID_USER_EMAIL, user.getEmail());
    }

    @Test
    public void whenUsersExist_ReturnAllUsers() {
        Long sizeDB = userRepository.count();

        //act
        List<User> users = userRepository.findAll();

        //assert
        assertEquals(sizeDB, users.size());
    }

    @Test
    public void whenAddUser_ReturnUser(){
        User user = new User();
        user.setFirstName("awdaw");
        user.setLastName("asdaw");
        user.setEmail("a@a.com");
        user.setUserIdentifier(new UserIdentifier());
        user.setDateOfBirth(new Date());
        user.setPassword("awdawd");
        user.setUsername("adwad");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getUserIdentifier().getUserId(), savedUser.getUserIdentifier().getUserId());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getDateOfBirth(), savedUser.getDateOfBirth());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    public void whenUpdateUser_ReturnUser(){
        User foundUser = userRepository.findUserByUserIdentifier_UserId(VALID_USER_ID);

        User user = new User();
        user.setFirstName("awdaw");
        user.setLastName("asdaw");
        user.setEmail("a@a.com");
        user.setUserIdentifier(foundUser.getUserIdentifier());
        user.setDateOfBirth(new Date());
        user.setPassword("awdawd");
        user.setUsername("adwad");
        user.setId(foundUser.getId());

        User savedUser = userRepository.save(user);

        userRepository.save(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getUserIdentifier().getUserId(), savedUser.getUserIdentifier().getUserId());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getDateOfBirth(), savedUser.getDateOfBirth());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    public void whenDeleteUser_ReturnNull(){
        userRepository.delete(userRepository.findUserByUserIdentifier_UserId(VALID_USER_ID));

        assertNull(userRepository.findUserByUserIdentifier_UserId(VALID_USER_ID));
    }
}
