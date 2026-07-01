package com.gestionSalon.service;

import com.gestionSalon.dto.InscriptionDTO;
import com.gestionSalon.dto.ConnexionDTO;
import com.gestionSalon.dto.TokenResponse;
import com.gestionSalon.entity.Role;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.mapper.AuthenticationMapper;
import com.gestionSalon.repository.RoleRepository;
import com.gestionSalon.repository.UtilisateurRepository;
import com.gestionSalon.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationMapper authenticationMapper;
    private final RoleRepository roleRepository;

    public TokenResponse inscrireClient(InscriptionDTO dto) {
        // 1. Vérification de l'unicité de l'email et téléphone
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }
        if (utilisateurRepository.existsByTelephone(dto.getTelephone())) {
            throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé.");
        }

        // Utilisation du mapper pour initialiser l'entité
        Utilisateur client = authenticationMapper.toEntity(dto);

        // Compléter avec les champs générés ou sécurisés côté serveur
        client.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        client.setActif(true);
        client.setSupprimee(false);
        client.setDateCreation(LocalDateTime.now());

        // récuperer le role client et l'attribuer par défaut à l'utilisateur
        Role roleClient = roleRepository.findByNom("CLIENT")
                .orElseThrow(() -> new RuntimeException("Rôle CLIENT introuvable"));

        client.setRole(roleClient);

        // enregistrer le client
        utilisateurRepository.save(client);


        // Génération du token d'accès
        String token = jwtUtils.generateToken(client);

        return TokenResponse.builder()
                .accessToken(token)
                .build();
    }

    public TokenResponse connecter(ConnexionDTO dto) {
        // 1. Déclencher l'authentification Spring Security (va appeler notre UserDetailsService et valider le mot de passe)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getMotDePasse()
                )
        );

        // 2. Si l'authentification réussit, on récupère l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findByEmailAndSupprimeeFalse(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable."));

        // 3. Génération du token
        String token = jwtUtils.generateToken(utilisateur);

        return TokenResponse.builder()
                .accessToken(token)
                .build();
    }
}