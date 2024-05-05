package com.mawe.webapp.dto;

import com.mawe.webapp.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String email;

    private String password;

    private UserRole userRole;

}
