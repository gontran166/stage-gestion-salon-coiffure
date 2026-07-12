package com.gestionSalon.dto.prestation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ImageUploadResponseDTO",
        description = "Réponse retournée après l'ajout ou la modification de l'image d'une prestation."
)
public class ImageUploadResponseDTO {

    @Schema(
            description = "Identifiant de la prestation concernée",
            example = "5"
    )
    private Long prestationId;

    @Schema(
            description = "URL publique permettant d'accéder à l'image de la prestation",
            example = "http://localhost:8080/uploads/prestations/coupe-homme-123456.jpg"
    )
    private String imageUrl;
}