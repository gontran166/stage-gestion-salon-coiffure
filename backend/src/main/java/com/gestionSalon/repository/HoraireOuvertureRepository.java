package com.gestionSalon.repository;

import com.gestionSalon.entity.HoraireOuverture;
import com.gestionSalon.entity.enumeration.JourSemaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoraireOuvertureRepository
        extends JpaRepository<HoraireOuverture, Long> {

    Optional<HoraireOuverture> findByIdAndSupprimeeFalse(Long id);

    List<HoraireOuverture>
    findBySupprimeeFalseOrderByJourSemaineAscHeureDebutAsc();

    List<HoraireOuverture>
    findByJourSemaineAndSupprimeeFalse(
            JourSemaine jourSemaine
    );

    List<HoraireOuverture>
    findByJourSemaineAndSupprimeeFalseAndIdNot(
            JourSemaine jourSemaine,
            Long id
    );
}