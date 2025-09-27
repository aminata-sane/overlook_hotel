package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fidelite")
public class Fidelite {

    // Enum pour les niveaux de fidélité
    public enum NiveauFidelite {
        BRONZE("Bronze", 0, 999),
        ARGENT("Argent", 1000, 2499),
        OR("Or", 2500, 4999),
        PLATINE("Platine", 5000, 9999),
        DIAMANT("Diamant", 10000, Integer.MAX_VALUE);

        private final String libelle;
        private final int seuilMin;
        private final int seuilMax;

        NiveauFidelite(String libelle, int seuilMin, int seuilMax) {
            this.libelle = libelle;
            this.seuilMin = seuilMin;
            this.seuilMax = seuilMax;
        }

        public String getLibelle() { return libelle; }
        public int getSeuilMin() { return seuilMin; }
        public int getSeuilMax() { return seuilMax; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", unique = true)
    private Client client;

    @Min(value = 0, message = "Les points ne peuvent pas être négatifs")
    @Column(name = "points", nullable = false)
    private int points = 0;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_derniere_modification")
    private LocalDateTime dateDerniereModification;

    // Constructeurs
    public Fidelite() {}

    public Fidelite(Client client) {
        this.client = client;
        this.points = 0;
        this.dateCreation = LocalDateTime.now();
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { 
        this.client = client;
        this.dateDerniereModification = LocalDateTime.now();
    }

    public int getPoints() { return points; }
    public void setPoints(int points) { 
        this.points = Math.max(0, points); // S'assurer que les points ne sont jamais négatifs
        this.dateDerniereModification = LocalDateTime.now();
    }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateDerniereModification() { return dateDerniereModification; }
    public void setDateDerniereModification(LocalDateTime dateDerniereModification) { 
        this.dateDerniereModification = dateDerniereModification; 
    }

    // Méthodes utilitaires
    public void ajouterPoints(int pointsAAjouter) {
        if (pointsAAjouter > 0) {
            this.points += pointsAAjouter;
            this.dateDerniereModification = LocalDateTime.now();
        }
    }

    public boolean retirerPoints(int pointsARetirer) {
        if (pointsARetirer > 0 && this.points >= pointsARetirer) {
            this.points -= pointsARetirer;
            this.dateDerniereModification = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public NiveauFidelite getNiveauFidelite() {
        for (NiveauFidelite niveau : NiveauFidelite.values()) {
            if (this.points >= niveau.getSeuilMin() && this.points <= niveau.getSeuilMax()) {
                return niveau;
            }
        }
        return NiveauFidelite.BRONZE; // Par défaut
    }

    public int getPointsJusquProchainNiveau() {
        NiveauFidelite niveauActuel = getNiveauFidelite();
        if (niveauActuel == NiveauFidelite.DIAMANT) {
            return 0; // Déjà au niveau maximum
        }
        
        // Trouver le prochain niveau
        NiveauFidelite[] niveaux = NiveauFidelite.values();
        for (int i = 0; i < niveaux.length - 1; i++) {
            if (niveaux[i] == niveauActuel) {
                return niveaux[i + 1].getSeuilMin() - this.points;
            }
        }
        return 0;
    }

    public double getPourcentageVersProchainNiveau() {
        NiveauFidelite niveauActuel = getNiveauFidelite();
        if (niveauActuel == NiveauFidelite.DIAMANT) {
            return 100.0; // Déjà au niveau maximum
        }

        int pointsMinNiveauActuel = niveauActuel.getSeuilMin();
        int pointsMinProchainNiveau = getPointsJusquProchainNiveau() + this.points;
        int plageNiveau = pointsMinProchainNiveau - pointsMinNiveauActuel;
        int pointsDansNiveau = this.points - pointsMinNiveauActuel;

        return (pointsDansNiveau * 100.0) / plageNiveau;
    }

    // Méthodes toString et equals/hashCode
    @Override
    public String toString() {
        return "Fidelite{" +
                "id=" + id +
                ", client=" + (client != null ? client.getPrenom() + " " + client.getNom() : "null") +
                ", points=" + points +
                ", niveau=" + getNiveauFidelite().getLibelle() +
                ", dateCreation=" + dateCreation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fidelite)) return false;
        Fidelite fidelite = (Fidelite) o;
        return id != null && id.equals(fidelite.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
