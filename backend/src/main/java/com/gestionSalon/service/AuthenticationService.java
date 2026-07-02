package com.gestionSalon.service;

import com.gestionSalon.dto.auth.*;
import com.gestionSalon.entity.Role;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.exception.BusinessValidationException;
import com.gestionSalon.mapper.AuthenticationMapper;
import com.gestionSalon.repository.RoleRepository;
import com.gestionSalon.repository.UtilisateurRepository;
import com.gestionSalon.security.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 1. Vérification de l'unicité du numéro de téléphone
        if (utilisateurRepository.existsByTelephone(dto.getTelephone())) {
            throw new BusinessValidationException(
                    "telephone",
                    "Ce numéro de téléphone est déjà utilisé."
            );
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

        String refreshToken = jwtUtils.generateRefreshToken(client);

        return TokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse connecter(ConnexionDTO dto) {
        // 1. Déclencher l'authentification Spring Security (va appeler notre UserDetailsService et valider le mot de passe)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getTelephone(),
                        dto.getMotDePasse()
                )
        );

        // 2. Si l'authentification réussit, on récupère l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findByTelephoneAndSupprimeeFalse(dto.getTelephone())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable."));

        // 3. Génération du token
        String token = jwtUtils.generateToken(utilisateur);

        String refreshToken = jwtUtils.generateRefreshToken(utilisateur);

        return TokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void changePassword(ChangePasswordDTO dto) {

        Utilisateur utilisateur =
                (Utilisateur) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        // Vérification ancien mot de passe
        if (!passwordEncoder.matches(
                dto.getAncienMotDePasse(),
                utilisateur.getMotDePasse()
        )) {

            throw new IllegalArgumentException(
                    "Ancien mot de passe incorrect."
            );
        }

        // Vérification confirmation
        if (!dto.getNouveauMotDePasse()
                .equals(dto.getConfirmationMotDePasse())) {

            throw new IllegalArgumentException(
                    "La confirmation du mot de passe est incorrecte."
            );
        }

        // Vérification nouveau différent de l'ancien
        if (passwordEncoder.matches(
                dto.getNouveauMotDePasse(),
                utilisateur.getMotDePasse()
        )) {

            throw new IllegalArgumentException(
                    "Le nouveau mot de passe doit être différent de l'ancien."
            );
        }

        utilisateur.setMotDePasse(
                passwordEncoder.encode(dto.getNouveauMotDePasse())
        );

        utilisateurRepository.save(utilisateur);
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {

        String userTelephone =
                jwtUtils.extractUsername(
                        request.getRefreshToken()
                );

        Utilisateur utilisateur =
                utilisateurRepository
                        .findByTelephoneAndSupprimeeFalse(userTelephone)
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );

        if (!jwtUtils.isTokenValid(
                request.getRefreshToken(),
                utilisateur
        )) {

            throw new IllegalArgumentException(
                    "Refresh token invalide."
            );
        }

        String accessToken =
                jwtUtils.generateToken(utilisateur);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .build();
    }
}