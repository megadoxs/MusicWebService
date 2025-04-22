package com.champlain.userservice.dataaccesslayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}
