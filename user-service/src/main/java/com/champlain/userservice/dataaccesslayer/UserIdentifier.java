package com.champlain.userservice.dataaccesslayer;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
