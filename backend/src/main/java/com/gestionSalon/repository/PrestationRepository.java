package com.gestionSalon.repository;

import com.gestionSalon.entity.Prestation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestationRepository
        extends JpaRepository<Prestation, Long> {

    Optional<Prestation> findByIdAndSupprimeeFalse(Long id);
    Page<Prestation> findAllBySupprimeeFalse(Pageable pageable);
    boolean existsByNomAndSupprimeeFalse(String nom);
    boolean existsByNomAndIdNotAndSupprimeeFalse(String nom, Long id);
    List<Prestation> findByIdInAndSupprimeeFalse(List<Long> ids);

}