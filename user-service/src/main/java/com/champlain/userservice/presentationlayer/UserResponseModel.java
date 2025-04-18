package com.champlain.userservice.presentationlayer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseModel extends RepresentationModel<UserResponseModel> {
    private String userId;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
}
