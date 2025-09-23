package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "installations")
public class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;

    private int capacite; // nombre de personnes maximum

    private String type; // nouveau champ ajouté pour remplacer getType()

    // Relation avec les événements utilisant cette installation
    @OneToMany(mappedBy = "installation", cascade = CascadeType.ALL)
    private List<Evenement> evenements = new ArrayList<>();

    // Constructeurs
    public Installation() {}

    public Installation(String nom, String description, int capacite, String type) {
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

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<Evenement> getEvenements() { return evenements; }
    public void setEvenements(List<Evenement> evenements) { this.evenements = evenements; }

    // Méthodes utilitaires
    public void addEvenement(Evenement evenement) {
        if (!evenements.contains(evenement)) {
            evenements.add(evenement);
            evenement.setInstallation(this);
        }
    }

    public void removeEvenement(Evenement evenement) {
        evenements.remove(evenement);
        if (evenement.getInstallation() == this) {
            evenement.setInstallation(null);
        }
    }
}
