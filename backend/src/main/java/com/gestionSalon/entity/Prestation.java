package com.gestionSalon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prestations")
public class Prestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private Integer dureeMinutes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(length = 50)
    private String categorie;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean supprimee = false;

    @Column(nullable = true)
    private LocalDateTime dateSuppression;

    @ManyToMany(mappedBy = "prestations")
    private List<Utilisateur> prestataires = new ArrayList<>();
}