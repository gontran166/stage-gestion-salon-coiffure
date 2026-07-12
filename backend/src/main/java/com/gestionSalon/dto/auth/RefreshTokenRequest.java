package com.gestionSalon.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "RefreshTokenRequest",
        description = "Contient le refresh token permettant de générer un nouveau JWT lorsque le token d'accès a expiré."
)
public class RefreshTokenRequest {

    @Schema(
            description = "Refresh token valide obtenu lors de l'authentification",
            example = "eyJhbGciOiJIUzI1NiJ9.refreshTokenExemple",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le refresh token est obligatoire"
    )
    private String refreshToken;
}