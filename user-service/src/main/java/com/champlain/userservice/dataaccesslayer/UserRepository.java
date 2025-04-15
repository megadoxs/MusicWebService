package com.champlain.userservice.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserIdentifier_UserId(String userIdentifier);
}
