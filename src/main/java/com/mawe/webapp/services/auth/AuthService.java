package com.mawe.webapp.services.auth;

import com.mawe.webapp.dto.SignupRequest;
import com.mawe.webapp.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);

    Boolean hasUserWithEmail(String email);

}
