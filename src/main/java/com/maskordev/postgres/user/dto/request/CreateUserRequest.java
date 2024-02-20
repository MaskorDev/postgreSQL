package com.maskordev.postgres.user.dto.request;

import lombok.Getter;

@Getter
public class CreateUserRequest {
    private String firstName;
    private String lastName;
}
