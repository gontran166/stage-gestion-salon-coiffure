package com.gestionSalon.repository;

import com.gestionSalon.entity.RendezVous;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.entity.enumeration.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    Optional<RendezVous> findByIdAndSupprimeeFalse(Long id);

    List<RendezVous>
    findByPrestataireAndDateAndStatutAndSupprimeeFalse(
            Utilisateur prestataire,
            LocalDate date,
            StatutRendezVous statut
    );

    List<RendezVous> findByClientAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
            Utilisateur client
    );

    List<RendezVous> findByPrestataireAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
            Utilisateur client
    );

    List<RendezVous> findByPrestataireAndDateAndSupprimeeFalse(
            Utilisateur prestataire,
            LocalDate date
    );
}
