package com.gestionSalon.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "Utilisateur")
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String telephone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private Boolean actif = true;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    // suppression logique
    private Boolean supprimee = false;
    private LocalDateTime dateSuppression;

    @OneToOne(mappedBy = "Utilisateur")
    private List<Role> role;

    //----Constructeurs vide-----
    public Utilisateur(){

    }
    //-----Constructeur parametré---
    public Utilisateur(String nom, String prenom, String telephone, String email,String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.motDePasse = motDePasse;
    }

}
