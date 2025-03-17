package com.champlain.music.usersubdomain.presentationlayer;


import com.champlain.music.usersubdomain.dataaccesslayer.UserIdentifier;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class UserRequestModel {
    private UserIdentifier userIdentifier;
    private String userId;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
    private Date dateOfBirth;
    private String password1;
    private String password2;

}
