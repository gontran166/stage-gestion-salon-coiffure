package com.gestionSalon.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor          // Génère automatiquement le constructeur vide avec Lombok
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    // Constructeur paramétré
    public Permission(String nom) {
        this.nom = nom;
    }
}