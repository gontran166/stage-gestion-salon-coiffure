package com.gestionSalon.repository;

import com.gestionSalon.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Page<Utilisateur> findAllBySupprimeeFalse(Pageable pageable);

    List<Utilisateur> findByIdInAndSupprimeeFalse(List<Long> ids);

    Optional<Utilisateur> findByIdAndSupprimeeFalse(Long id);

    List<Utilisateur> findByRole_NomAndSupprimeeFalse(String roleNom);

    // On cherche par email, mais UNIQUEMENT parmi ceux qui ne sont pas supprimés logiquement
    Optional<Utilisateur> findByTelephoneAndSupprimeeFalse(String telephone);

    // Utile pour vérifier si un téléphone existe déjà lors de l'inscription
    Boolean existsByTelephoneAndSupprimeeFalse(String telephone);

    boolean existsByTelephoneAndIdNotAndSupprimeeFalse(String telephone, Long id);
}