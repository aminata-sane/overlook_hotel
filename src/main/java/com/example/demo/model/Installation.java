package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "installations")
public class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'installation est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false)
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Column(name = "description")
    private String description;

    @Min(value = 1, message = "La capacité doit être au moins de 1")
    @Column(name = "capacite")
    private Integer capacite;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_installation")
    private TypeInstallation type;

    @Column(name = "disponible")
    private Boolean disponible = true;

    // Enum pour les types d'installations
    public enum TypeInstallation {
        SALLE_CONFERENCE("Salle de conférence"),
        SALLE_REUNION("Salle de réunion"),
        RESTAURANT("Restaurant"),
        BAR("Bar"),
        SPA("Spa"),
        PISCINE("Piscine"),
        SALLE_SPORT("Salle de sport"),
        JARDIN("Jardin"),
        TERRASSE("Terrasse"),
        AUTRE("Autre");

        private final String libelle;

        TypeInstallation(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    // Constructeurs
    public Installation() {}

    public Installation(String nom, TypeInstallation type) {
        this.nom = nom;
        this.type = type;
    }

    public Installation(String nom, String description, Integer capacite, TypeInstallation type) {
        this.nom = nom;
        this.description = description;
        this.capacite = capacite;
        this.type = type;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }

    public TypeInstallation getType() { return type; }
    public void setType(TypeInstallation type) { this.type = type; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    // Méthodes utilitaires
    public String getNomComplet() {
        return nom + (type != null ? " (" + type.getLibelle() + ")" : "");
    }

    public boolean estDisponible() {
        return disponible != null && disponible;
    }

    @Override
    public String toString() {
        return "Installation{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type=" + type +
                ", capacite=" + capacite +
                ", disponible=" + disponible +
                '}';
    }
}
