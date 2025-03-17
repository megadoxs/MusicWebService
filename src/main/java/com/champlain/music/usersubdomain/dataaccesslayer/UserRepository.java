package com.champlain.music.usersubdomain.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends 
        JpaRepository<User, Integer> {
       User findUserByUserIdentifier(String userIdentifier);
//       User findUserByUserIdentifier(UserIdentifier userIdentifier);

}
