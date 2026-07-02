package com.gestionSalon.controller;

import com.gestionSalon.dto.auth.*;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody InscriptionDTO dto) {
        TokenResponse response = authenticationService.inscrireClient(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody ConnexionDTO dto) {
        TokenResponse response = authenticationService.connecter(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordDTO dto
    ) {

        authenticationService.changePassword(dto);

        return ResponseEntity.ok(
                new MessageResponse(
                        "Mot de passe modifié avec succès."
                )
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                authenticationService.refreshToken(request)
        );
    }
}

