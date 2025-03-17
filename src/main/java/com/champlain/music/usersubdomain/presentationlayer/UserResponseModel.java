package com.champlain.music.usersubdomain.presentationlayer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseModel {
    private String userId;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
}
