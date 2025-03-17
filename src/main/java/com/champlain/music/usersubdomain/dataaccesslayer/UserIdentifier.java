package com.champlain.music.usersubdomain.dataaccesslayer;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@Data
public class UserIdentifier {

    private String userId;

    public UserIdentifier() {
        this.userId = UUID.randomUUID().toString();
    }
}
