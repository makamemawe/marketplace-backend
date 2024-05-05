package com.mawe.webapp.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mawe.webapp.dto.SignupRequest;
import com.mawe.webapp.dto.UserDto;
import com.mawe.webapp.entity.User;
import com.mawe.webapp.enums.UserRole;
import com.mawe.webapp.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto createUser(SignupRequest signupRequest) {

        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createUser.getId());

        return userDto;

    }

    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);

        if (null == adminAccount) {
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));

            userRepository.save(user);

        }
    }

}
