package com.pms.authservice.service;

import com.pms.authservice.dto.LoginRequestVO;
import com.pms.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    /**
     * Authenticates a user based on the provided login request.
     * <p>
     * If the request is valid, a token is returned.
     * If the request is invalid or the token is expired, an empty {@link Optional} is returned.
     *
     * @param loginRequestVO the login request containing user credentials
     * @return an {@link Optional} containing the authentication token if successful, otherwise empty
     */
    public Optional<String> authenticate(LoginRequestVO loginRequestVO) {
        Optional<String> token = this.userService
                .findByEmail(loginRequestVO.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestVO.getPassword(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
        return token;
    }
}
