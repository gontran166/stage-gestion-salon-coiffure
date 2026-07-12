package com.gestionSalon.repository;

import com.gestionSalon.entity.RendezVous;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.entity.enumeration.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    List<RendezVous> findByPrestataireIdAndDateAndSupprimeeFalseOrderByHeureDebutAsc(
            Long prestataireId,
            LocalDate date
    );

    List<RendezVous> findByPrestataireIdAndDateBetweenAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
            Long prestataireId,
            LocalDate dateDebut,
            LocalDate dateFin
    );

    List<RendezVous> findByDateBetweenAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
            LocalDate dateDebut,
            LocalDate dateFin
    );

    List<RendezVous>
    findByPrestataireIdAndDateAndStatutInAndSupprimeeFalse(
            Long prestataireId,
            LocalDate date,
            List<StatutRendezVous> statuts
    );

    @Query("""
    SELECT
        r.prestation.id,
        r.prestation.nom,
        COUNT(r)
    FROM RendezVous r
    WHERE r.supprimee = false
      AND r.statut IN (
            com.gestionSalon.entity.enumeration.StatutRendezVous.CONFIRME,
            com.gestionSalon.entity.enumeration.StatutRendezVous.HONORE
      )
    GROUP BY r.prestation.id, r.prestation.nom
    ORDER BY COUNT(r) DESC
""")
    List<Object[]> getPrestationsPopulaires();

    @Query("""
    SELECT
        r.prestation.id,
        r.prestation.nom,
        COUNT(r)
    FROM RendezVous r
    WHERE r.supprimee = false
      AND r.date BETWEEN :debut AND :fin
      AND r.statut IN (
            com.gestionSalon.entity.enumeration.StatutRendezVous.CONFIRME,
            com.gestionSalon.entity.enumeration.StatutRendezVous.HONORE
      )
    GROUP BY r.prestation.id, r.prestation.nom
    ORDER BY COUNT(r) DESC
""")
    List<Object[]> getPrestationsPopulairesWithPeriod(
            LocalDate debut,
            LocalDate fin
    );

    long countBySupprimeeFalse();

    long countByDateBetweenAndSupprimeeFalse(
            LocalDate debut,
            LocalDate fin
    );

    long countByStatutAndSupprimeeFalse(
            StatutRendezVous statut
    );

    long countByStatutAndDateBetweenAndSupprimeeFalse(
            StatutRendezVous statut,
            LocalDate debut,
            LocalDate fin
    );

}
