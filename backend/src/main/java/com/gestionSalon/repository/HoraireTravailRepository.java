package com.gestionSalon.repository;

import com.gestionSalon.entity.HoraireTravail;
import com.gestionSalon.entity.JourSemaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoraireTravailRepository extends JpaRepository<HoraireTravail, Long> {

    Optional<HoraireTravail> findByIdAndSupprimeeFalse(Long id);

    // liste des horaires de travail d'un prestataire
    List<HoraireTravail> findByPrestataireIdAndSupprimeeFalseOrderByJourSemaineAscHeureDebutAsc(Long prestataireId);
    List<HoraireTravail> findByPrestataireIdAndSupprimeeFalse(Long prestataireId);

    // liste des horaires de travail d'un prestataire pour un jour donné
    List<HoraireTravail> findByPrestataireIdAndJourSemaineAndSupprimeeFalse(
            Long prestataireId,
            JourSemaine jourSemaine
    );

    List<HoraireTravail>
    findByPrestataireIdAndJourSemaineAndIdNotAndSupprimeeFalse(
            Long prestataireId,
            JourSemaine jourSemaine,
            Long id
    );

}