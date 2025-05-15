package com.champlain.apigatewayservice.presentationlayer.userdto;


import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseModel extends RepresentationModel<UserResponseModel> {
    private String userId;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
}
