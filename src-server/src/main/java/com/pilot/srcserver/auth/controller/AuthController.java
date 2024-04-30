package com.pilot.srcserver.auth.controller;

import com.pilot.srcserver.auth.request.UserRequest;
import com.pilot.srcserver.auth.response.ErrorResponse;
import com.pilot.srcserver.auth.response.Response;
import com.pilot.srcserver.auth.response.UserResponse;
import com.pilot.srcserver.auth.service.JwtService;
import com.pilot.srcserver.entity.Role;
import com.pilot.srcserver.entity.User;
import com.pilot.srcserver.repository.RoleRepository;
import com.pilot.srcserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository r;

    @PostMapping("signup")
    public Response signup(@RequestBody UserRequest userRequest) {
        Role role = r.findById(0L).orElseThrow();
        var user = User.builder().id(new Random().nextInt(1000000)).username(userRequest.getUsername())
                        .password(passwordEncoder.encode(userRequest.getPassword()))
                                .roles(Set.of(role))
                                        .build();
        userRepository.save(user);
        return UserResponse.builder()
                .token(jwtService.GenerateToken(userRequest.getUsername())).build();
    }

    @PostMapping("login")
    public Response AuthenticateAndGetToken(@RequestBody UserRequest userRequest){
        System.out.println(userRequest.getUsername());
        System.out.println(userRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        var user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        System.out.println(user.getUsername());
        System.out.println(user.getPassword());

        return UserResponse.builder()
                .token(jwtService.GenerateToken(userRequest.getUsername())).build();
    }
}
