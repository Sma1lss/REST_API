package org.example.controller;

import org.example.dto.login.LoginRequest;
import org.example.dto.login.LoginResponse;
import org.example.dto.user.UserRegistrationRequest;
import org.example.dto.user.UserResponseDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.security.JwtTokenProvider;
import org.example.service.EmailService;
import org.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final EmailService emailService;


    public LoginController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserMapper userMapper, EmailService emailService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        try {
            User newUser = userMapper.userRegistrationRequestToUser(registrationRequest);

            String verificationLink = "http://localhost:8080/api/verify?token=" + newUser.getId();

            try {
                emailService.sendVerificationEmail(newUser, verificationLink);
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
                System.err.println("Error while sending verification email: " + e.getMessage());

            }

            UserResponseDTO userResponseDTO = userMapper.userToUserResponseDTO(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
