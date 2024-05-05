package com.mawe.webapp.controller;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mawe.webapp.dto.AuthenticationRequest;
import com.mawe.webapp.dto.SignupRequest;
import com.mawe.webapp.dto.UserDto;
import com.mawe.webapp.entity.User;
import com.mawe.webapp.repository.UserRepository;
import com.mawe.webapp.services.auth.AuthService;
import com.mawe.webapp.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public static final String TOKEN_PREFIX = "Baerer ";

    public static final String HEADER_STRING = "Authentication";

    private final AuthService authService;

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response) throws IOException, JSONException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password!");
        }

        final UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userdetails.getUsername());
        final String jwt = jwtUtil.generateToken(userdetails.getUsername());

        if (optionalUser.isPresent()) {

            response.getWriter().write(new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .toString());

            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGER, Origin" +
                    "X-Requested-With, Content-Type, Accept, X-Custom-header");
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);

        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {

        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>("User alredy exist", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
