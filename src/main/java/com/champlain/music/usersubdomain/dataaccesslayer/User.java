package com.champlain.music.usersubdomain.dataaccesslayer;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String userIdentifier;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;

    private String username;
    private String password;





}
