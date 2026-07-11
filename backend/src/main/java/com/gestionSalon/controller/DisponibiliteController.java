package com.gestionSalon.controller;

import com.gestionSalon.dto.CreneauDisponibleDTO;
import com.gestionSalon.service.DisponibiliteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/creneaux-disponibles")
@Tag(name = "Gestion des disponiblités des prestataires", description = "Obtenir les créneaux libres en fonction de la prestation, du prestataire et la date choisie")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class DisponibiliteController {

    private final DisponibiliteService disponibiliteService;

    @GetMapping
    @Operation(summary = "Récupérer les créneaux libres", description = "En fonction d'une prestation, d'un prestataire et d'une date (jour)")
    public ResponseEntity<List<CreneauDisponibleDTO>>
    getCreneauxDisponibles(

            @RequestParam Long prestationId,

            @RequestParam Long prestataireId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) throws BadRequestException {

        return ResponseEntity.ok(
                disponibiliteService.getCreneauxDisponibles(
                        prestationId,
                        prestataireId,
                        date
                )
        );
    }
}
