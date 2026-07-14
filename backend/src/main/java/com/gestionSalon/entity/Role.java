package com.gestionSalon.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Ex: ADMIN, CLIENT, COIFFEUR
    private String nom;

    // Définition propre de la table de jointure pour la relation ManyToMany
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_role_permission",
                            columnNames = {
                                    "role_id",
                                    "permission_id"
                            }
                    )
            }
    )
    private List<Permission> permissions;

    // Constructeur paramétré
    public Role(String nom) {
        this.nom = nom;
    }
}