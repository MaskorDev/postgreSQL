package com.maskordev.postgres.user.dto.response;

import com.maskordev.postgres.user.entity.UserEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserResponse extends UserEntity {
    public static UserResponse of(UserEntity user) {
        return (UserResponse) UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
