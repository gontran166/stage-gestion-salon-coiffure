package com.gestionSalon.controller;

import com.gestionSalon.dto.ProfileDTO;
import com.gestionSalon.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile() {
        return ResponseEntity.ok(userService.getProfil());
    }
}
