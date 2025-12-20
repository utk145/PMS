package com.pms.authservice.controller;

import com.pms.authservice.dto.LoginRequestVO;
import com.pms.authservice.dto.LoginResponseVO;
import com.pms.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "APIs for authentication management")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseVO> login(@RequestBody LoginRequestVO loginRequestVO) {

        Optional<String> tokenOptional = authService.authenticate(loginRequestVO);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new LoginResponseVO(tokenOptional.get()));

    }

}
