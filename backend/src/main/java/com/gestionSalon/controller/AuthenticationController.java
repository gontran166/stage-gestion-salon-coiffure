package com.gestionSalon.controller;

import com.gestionSalon.dto.InscriptionDTO;
import com.gestionSalon.dto.ConnexionDTO;
import com.gestionSalon.dto.TokenResponse;
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

}

