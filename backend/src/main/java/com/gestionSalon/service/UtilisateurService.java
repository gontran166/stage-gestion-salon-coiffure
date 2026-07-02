package com.gestionSalon.service;

import com.gestionSalon.dto.ProfileDTO;
import com.gestionSalon.dto.UpdateProfileDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.CreateUtilisateurDTO;
import com.gestionSalon.dto.utilisateur.UpdateRoleDTO;
import com.gestionSalon.dto.utilisateur.UpdateUtilisateurDTO;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.entity.Role;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.exception.BusinessValidationException;
import com.gestionSalon.mapper.UtilisateurMapper;
import com.gestionSalon.repository.RoleRepository;
import com.gestionSalon.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilisateurMapper utilisateurMapper;

    public ProfileDTO getProfil() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateur =
                (Utilisateur) authentication.getPrincipal();

        return ProfileDTO.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .telephone(utilisateur.getTelephone())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole().getNom())
                .actif(utilisateur.getActif())
                .build();
    }

    // modifier un profile
    public ProfileDTO updateProfile(UpdateProfileDTO dto) {

        Utilisateur utilisateur =
                (Utilisateur) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        if (utilisateurRepository.existsByEmailAndIdNot(
                dto.getEmail(),
                utilisateur.getId())) {

            throw new IllegalArgumentException(
                    "Cet email est déjà utilisé."
            );
        }

        if (utilisateurRepository.existsByTelephoneAndIdNot(
                dto.getTelephone(),
                utilisateur.getId())) {

            throw new IllegalArgumentException(
                    "Ce numéro de téléphone est déjà utilisé."
            );
        }

        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setTelephone(dto.getTelephone());

        utilisateurRepository.save(utilisateur);

        return utilisateurMapper.toProfileDTO(utilisateur);
    }

    public Page<UtilisateurDTO> getAllUsers(int page, int size) {

        size = Math.min(size, 50);

        Pageable pageable =
                PageRequest.of(page, size);

        return utilisateurRepository
                .findAllBySupprimeeFalse(pageable)
                .map(utilisateurMapper::toUtilisateurDTO);
    }

    // chercher un utilisateur par son id
    public UtilisateurDTO getUserById(Long id) {

        Utilisateur utilisateur = utilisateurRepository
                .findByIdAndSupprimeeFalse(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Utilisateur introuvable."
                        )
                );

        return utilisateurMapper.toUtilisateurDTO(utilisateur);
    }

    // ajouter un nouveau utilisateur
    @Transactional
    public UtilisateurDTO createUser(CreateUtilisateurDTO dto) {

        if (utilisateurRepository.existsByEmail(dto.getEmail())) {

            throw new BusinessValidationException(
                    "email",
                    "Cet email est déjà utilisé."
            );
        }

        if (utilisateurRepository.existsByTelephone(dto.getTelephone())) {

            throw new BusinessValidationException(
                    "telephone",
                    "Ce numéro de téléphone est déjà utilisé."
            );
        }

        Role role = roleRepository
                .findByNom(dto.getRole())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Rôle introuvable."
                        )
                );

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
                .motDePasse(
                        passwordEncoder.encode(
                                dto.getMotDePasse()
                        )
                )
                .role(role)
                .actif(true)
                .supprimee(false)
                .build();

        utilisateurRepository.save(utilisateur);

        return utilisateurMapper.toUtilisateurDTO(
                utilisateur
        );
    }

    @Transactional
    public UtilisateurDTO updateUser(
            Long id,
            UpdateUtilisateurDTO dto
    ) {

        Utilisateur utilisateur = utilisateurRepository
                .findByIdAndSupprimeeFalse(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Utilisateur introuvable."
                        )
                );

        if (utilisateurRepository.existsByEmailAndIdNot(
                dto.getEmail(),
                utilisateur.getId()
        )) {

            throw new BusinessValidationException(
                    "email",
                    "Cet email est déjà utilisé."
            );
        }

        if (utilisateurRepository.existsByTelephoneAndIdNot(
                dto.getTelephone(),
                utilisateur.getId()
        )) {

            throw new BusinessValidationException(
                    "telephone",
                    "Ce numéro de téléphone est déjà utilisé."
            );
        }

        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setTelephone(dto.getTelephone());

        utilisateurRepository.save(utilisateur);

        return utilisateurMapper.toUtilisateurDTO(utilisateur);
    }

    // supprimer un utilisateur
    @Transactional
    public MessageResponse deleteUser(Long id) {

        Utilisateur adminConnecte =
                (Utilisateur) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        if (adminConnecte.getId().equals(id)) {
            throw new BusinessValidationException(
                    "user",
                    "Vous ne pouvez pas supprimer votre propre compte."
            );
        }

        Utilisateur utilisateur = utilisateurRepository
                .findByIdAndSupprimeeFalse(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Utilisateur introuvable."
                        )
                );

        utilisateur.setSupprimee(true);
        utilisateur.setDateSuppression(LocalDateTime.now());

        utilisateurRepository.save(utilisateur);

        return MessageResponse.builder()
                .message("Utilisateur supprimé avec succès.")
                .build();
    }

    // modifier le rôle d'un utilisateur
    @Transactional
    public UtilisateurDTO updateUserRole(
            Long id,
            UpdateRoleDTO dto
    ) {

        Utilisateur adminConnecte =
                (Utilisateur) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        if (
                adminConnecte.getId().equals(id)
                        && !"ADMIN".equals(dto.getRole())
        ) {

            throw new BusinessValidationException(
                    "role",
                    "Vous ne pouvez pas modifier votre propre rôle."
            );
        }

        Utilisateur utilisateur = utilisateurRepository
                .findByIdAndSupprimeeFalse(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Utilisateur introuvable."
                        )
                );

        Role role = roleRepository
                .findByNom(dto.getRole())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Rôle introuvable."
                        )
                );

        utilisateur.setRole(role);

        utilisateurRepository.save(utilisateur);

        return utilisateurMapper.toUtilisateurDTO(utilisateur);
    }

}
