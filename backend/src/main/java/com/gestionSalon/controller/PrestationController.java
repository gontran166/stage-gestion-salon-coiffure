package com.gestionSalon.controller;

import com.gestionSalon.dto.prestation.CreatePrestationDTO;
import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.dto.prestation.UpdatePrestationDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.service.PrestationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestations")
@RequiredArgsConstructor
public class PrestationController {

    private final PrestationService prestationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrestationDTO> create(
            @Valid @RequestBody CreatePrestationDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prestationService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PrestationDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                prestationService.findAll(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestationDTO> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                prestationService.findById(id)
        );
    }

    @GetMapping("/{id}/prestataires")
    public ResponseEntity<List<UtilisateurDTO>> findPrestataires(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                prestationService.findPrestationsPrestataires(id)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrestationDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePrestationDTO dto
    ) {

        return ResponseEntity.ok(
                prestationService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(prestationService.delete(id));
    }
}