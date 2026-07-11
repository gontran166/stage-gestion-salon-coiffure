package com.gestionSalon.controller;

import com.gestionSalon.dto.auth.*;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name="Authentification", description = "Connexion utilisateur, Inscription client, Changement de mot de passe et refresh token")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Inscription client",description = "Permettre à un client de créer un compte")
    @ApiResponse(responseCode = "201", description = "compte client créé avec succès")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody InscriptionDTO dto) {
        TokenResponse response = authenticationService.inscrireClient(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion d'un utilisateur", description = "Permettre à un utilisateur de se connecter")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody ConnexionDTO dto) {
        TokenResponse response = authenticationService.connecter(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    @Operation(summary = "Changer son mot de passe", description = "Permettre à un utilisateur de changer son mot de passe")
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
    @Operation(summary = "Régénérer token",description = "Régénérer un nouveau token quand l'ancien devient invalide")
    public ResponseEntity<TokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                authenticationService.refreshToken(request)
        );
    }
}

