package com.gestionSalon.controller;

import com.gestionSalon.dto.planning.PlanningRendezVousDTO;
import com.gestionSalon.repository.RendezVousRepository;
import com.gestionSalon.service.PlanningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prestataires/")
@Tag(name = "Planning prestataires/salon",description = "Consultation des planning")
@RequiredArgsConstructor
public class PlanningController {

    private final RendezVousRepository rendezVousRepository;
    private final PlanningService planningService;


    @GetMapping("/{id}/planning/jour")
    @Operation(summary = "Récupérer planning journalier prestataire", description = "Récupérer le planning journalier d'un prestataire réserver aux prestataires et au gérant")
    @PreAuthorize("hasAnyRole('PRESTATAIRE','ADMIN')")
    public ResponseEntity<List<PlanningRendezVousDTO>> getPlanningJour(
            @PathVariable Long id,
            @RequestParam LocalDate date
    ) {

        return ResponseEntity.ok(
                planningService.getPlanningJour(
                        id,
                        date
                )
        );
    }

    @GetMapping("/{id}/planning/semaine")
    @Operation(summary = "Récupérer planning hebdomadaire prestataire", description = "Récupérer le planning hebdomadaire d'un prestataire réserver aux prestataires et au gérant")
    @PreAuthorize("hasAnyRole('PRESTATAIRE','ADMIN')")
    public ResponseEntity<List<PlanningRendezVousDTO>> getPlanningSemaine(
            @PathVariable Long id,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(
                planningService.getPlanningSemaine(
                        id,
                        date
                )
        );
    }
}
