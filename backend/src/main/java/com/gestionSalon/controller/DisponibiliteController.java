package com.gestionSalon.controller;

import com.gestionSalon.dto.CreneauDisponibleDTO;
import com.gestionSalon.service.DisponibiliteService;
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
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class DisponibiliteController {

    private final DisponibiliteService disponibiliteService;

    @GetMapping
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
