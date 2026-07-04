package com.gestionSalon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "horaires_travail")
public class HoraireTravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JourSemaine jourSemaine;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;

    @Column(nullable = false)
    private Boolean supprimee = false;

    @Column(nullable = true)
    private LocalDateTime dateSuppression;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "prestataire_id",
            nullable = false
    )
    private Utilisateur prestataire;
}