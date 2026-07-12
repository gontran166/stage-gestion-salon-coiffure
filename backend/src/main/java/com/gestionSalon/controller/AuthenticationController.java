package com.gestionSalon.controller;

import com.gestionSalon.dto.auth.*;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentification",
        description = """
                Endpoints liés à l'authentification et à la gestion des accès.
                
                Ces APIs permettent :
                - l'inscription d'un client ;
                - la connexion d'un utilisateur ;
                - le changement de mot de passe ;
                - le renouvellement d'un token JWT expiré.
                """
)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(
            summary = "Inscription d'un client",
            description = """
                    Permet à un visiteur de créer un compte client.
                    
                    Un compte client est créé avec le rôle CLIENT et peut ensuite
                    se connecter à l'application afin de consulter les prestations
                    et prendre des rendez-vous.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Compte client créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données d'inscription invalides"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Le numéro de téléphone est déjà utilisé"
            )
    })
    public ResponseEntity<TokenResponse> register(
            @Valid @RequestBody InscriptionDTO dto
    ) {

        TokenResponse response =
                authenticationService.inscrireClient(dto);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    @Operation(
            summary = "Connexion d'un utilisateur",
            description = """
                    Authentifie un utilisateur à l'aide de son numéro de téléphone
                    et de son mot de passe.
                    
                    En cas de succès, un token JWT et un refresh token sont retournés.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Connexion réussie"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de connexion invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Identifiants incorrects"
            )
    })
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody ConnexionDTO dto
    ) {

        TokenResponse response =
                authenticationService.connecter(dto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Changer son mot de passe",
            description = """
                    Permet à un utilisateur authentifié de modifier son mot de passe.
                    
                    L'ancien mot de passe doit être fourni afin de confirmer l'identité
                    de l'utilisateur avant l'enregistrement du nouveau mot de passe.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mot de passe modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ancien mot de passe incorrect ou données invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            )
    })
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
    @Operation(
            summary = "Renouveler un token JWT",
            description = """
                    Permet d'obtenir un nouveau token JWT à partir d'un refresh token valide.
                    
                    Cette opération évite à l'utilisateur de devoir se reconnecter
                    lorsque son token d'accès a expiré.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Nouveau token généré avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Refresh token invalide"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Refresh token expiré ou non reconnu"
            )
    })
    public ResponseEntity<TokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                authenticationService.refreshToken(request)
        );
    }
}