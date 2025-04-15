package com.champlain.userservice.presentationlayer;


import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class UserRequestModel {
    private String username;
    private String lastName;
    private String firstName;
    private String email;
    private Date dateOfBirth;
    private String password1;
    private String password2;
}
