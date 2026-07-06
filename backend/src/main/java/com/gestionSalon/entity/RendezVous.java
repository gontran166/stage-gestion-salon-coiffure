package com.gestionSalon.entity;

import com.gestionSalon.entity.enumeration.StatutRendezVous;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Utilisateur client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prestataire_id")
    private Utilisateur prestataire;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prestation_id")
    private Prestation prestation;

    @Column(name = "date_rendez_vous", nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRendezVous statut;


    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    private LocalDateTime dateSuppression;
    private Boolean supprimee = false;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }

}
