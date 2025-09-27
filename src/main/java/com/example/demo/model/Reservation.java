package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date d'arrivée est obligatoire")
    @FutureOrPresent(message = "La date d'arrivée doit être aujourd'hui ou dans le futur")
    @Column(name = "date_arrivee", nullable = false)
    private LocalDate dateArrivee;

    @NotNull(message = "La date de départ est obligatoire")
    @Future(message = "La date de départ doit être dans le futur")
    @Column(name = "date_depart", nullable = false)
    private LocalDate dateDepart;

    @NotNull(message = "Le nombre d'adultes est obligatoire")
    @Min(value = 1, message = "Il doit y avoir au moins 1 adulte")
    @Max(value = 6, message = "Maximum 6 adultes par réservation")
    @Column(name = "nombre_adultes", nullable = false)
    private Integer nombreAdultes;

    @Min(value = 0, message = "Le nombre d'enfants ne peut pas être négatif")
    @Max(value = 4, message = "Maximum 4 enfants par réservation")
    @Column(name = "nombre_enfants")
    private Integer nombreEnfants = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "prix_total")
    private Double prixTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    @Column(name = "commentaires", columnDefinition = "TEXT")
    private String commentaires;

    public enum StatutReservation {
        EN_ATTENTE("En attente"),
        CONFIRMEE("Confirmée"),
        ANNULEE("Annulée"),
        TERMINEE("Terminée");

        private final String libelle;

        StatutReservation(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    // Constructeurs
    public Reservation() {}

    public Reservation(LocalDate dateArrivee, LocalDate dateDepart, Integer nombreAdultes, Integer nombreEnfants) {
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nombreAdultes = nombreAdultes;
        this.nombreEnfants = nombreEnfants;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(LocalDate dateArrivee) { this.dateArrivee = dateArrivee; }

    public LocalDate getDateDepart() { return dateDepart; }
    public void setDateDepart(LocalDate dateDepart) { this.dateDepart = dateDepart; }

    public Integer getNombreAdultes() { return nombreAdultes; }
    public void setNombreAdultes(Integer nombreAdultes) { this.nombreAdultes = nombreAdultes; }

    public Integer getNombreEnfants() { return nombreEnfants; }
    public void setNombreEnfants(Integer nombreEnfants) { this.nombreEnfants = nombreEnfants; }

    public Chambre getChambre() { return chambre; }
    public void setChambre(Chambre chambre) { this.chambre = chambre; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }

    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }

    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }

    // Méthodes utilitaires
    public long getNombreNuits() {
        if (dateArrivee != null && dateDepart != null) {
            return ChronoUnit.DAYS.between(dateArrivee, dateDepart);
        }
        return 0;
    }

    public int getNombrePersonnesTotal() {
        return nombreAdultes + (nombreEnfants != null ? nombreEnfants : 0);
    }

    public void calculerPrixTotal() {
        if (chambre != null && chambre.getPrix() != null) {
            this.prixTotal = chambre.getPrix() * getNombreNuits();
        }
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dateArrivee=" + dateArrivee +
                ", dateDepart=" + dateDepart +
                ", nombreAdultes=" + nombreAdultes +
                ", nombreEnfants=" + nombreEnfants +
                ", prixTotal=" + prixTotal +
                ", statut=" + statut +
                '}';
    }
}
