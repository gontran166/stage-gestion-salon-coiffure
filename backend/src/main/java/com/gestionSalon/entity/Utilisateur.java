package com.gestionSalon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder                    // Permet d'utiliser Utilisateur.builder()
@AllArgsConstructor         // Requis par @Builder
@NoArgsConstructor
@Entity
@Table(name = "utilisateurs")
public class Utilisateur implements UserDetails { // Implémentation obligatoire pour Spring Security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 50)
    private String nom;

    @Column(nullable = true, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 20)
    private String telephone;


    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private Boolean actif = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    @Column(nullable = false)
    private Boolean supprimee = false;
    private LocalDateTime dateSuppression;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "prestataire_prestations",
            joinColumns = @JoinColumn(name = "prestataire_id"),
            inverseJoinColumns = @JoinColumn(name = "prestation_id"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_prestataire_prestation",
                            columnNames = {
                                    "prestataire_id",
                                    "prestation_id"
                            }
                    )
            }
    )
    private List<Prestation> prestations = new ArrayList<>();

    @OneToMany(
            mappedBy = "prestataire",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<HoraireTravail> horairesTravail =
            new ArrayList<>();


    // =========================================================================
    // Méthodes de l'interface UserDetails pour la gestion des privilèges
    // =========================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (role != null) {
            // 1. Ajouter le rôle principal (Ex: ROLE_ADMIN, ROLE_CLIENT)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getNom()));

            // 2. Ajouter toutes les permissions associées à ce rôle (Ex: USER_READ, USER_CREATE)
            if (role.getPermissions() != null) {
                role.getPermissions().forEach(permission -> {
                    authorities.add(new SimpleGrantedAuthority(permission.getNom()));
                });
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.motDePasse;
    }

    @Override
    public String getUsername() {
        return this.telephone; // Notre identifiant de connexion est le numéro de téléphone
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Le compte n'expire pas
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Le compte n'est pas verrouillé
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Les identifiants n'expirent pas
    }

    @Override
    public boolean isEnabled() {
        // Le compte est fonctionnel s'il est marqué comme actif et non supprimé logiquement
        return this.actif && !this.supprimee;
    }
}