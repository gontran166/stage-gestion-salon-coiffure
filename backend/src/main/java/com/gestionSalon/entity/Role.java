package com.gestionSalon.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;
    //-----Relation----------------
    @ManyToMany(mappedBy = "Role")
    private List<Permission> permissions;

    //------constructeurs vide-----
    public Role() {

    }
    //-----constructeurs parametré----
    public Role(String nom) {
        this.nom = nom;
    }


}
