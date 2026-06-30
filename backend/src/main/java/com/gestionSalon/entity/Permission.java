package com.gestionSalon.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    //------constructeurs vide------

    public Permission() {

    }

    //-------Constructeurs parametré-------

    public Permission(String nom) {
        this.nom = nom;
    }

}
