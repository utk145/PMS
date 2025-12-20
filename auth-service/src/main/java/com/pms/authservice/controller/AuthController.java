package com.pms.authservice.controller;

import com.pms.authservice.dto.LoginRequestVO;
import com.pms.authservice.dto.LoginResponseVO;
import com.pms.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Login", description = "Login to the system")
    public ResponseEntity<LoginResponseVO> login(@RequestBody LoginRequestVO loginRequestVO) {

        Optional<String> tokenOptional = authService.authenticate(loginRequestVO);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new LoginResponseVO(tokenOptional.get()));

    }

    @GetMapping(path = "/validate")
    @Operation(summary = "Validate Token", description = "Validate the request header")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String authHeader) {
        // Authorization: Bearer <token>

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7)) ? ResponseEntity.ok("Valid") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }


}
