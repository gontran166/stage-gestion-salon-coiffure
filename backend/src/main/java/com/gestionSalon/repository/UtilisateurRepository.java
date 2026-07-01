package com.gestionSalon.repository;

import com.gestionSalon.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // On cherche par email, mais UNIQUEMENT parmi ceux qui ne sont pas supprimés logiquement
    Optional<Utilisateur> findByEmailAndSupprimeeFalse(String email);

    // Utile pour vérifier si un email ou téléphone existe déjà lors de l'inscription
    Boolean existsByEmail(String email);
    Boolean existsByTelephone(String telephone);
}