package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    // Enum pour le type de feedback
    public enum TypeFeedback {
        GENERAL("Général"),
        SERVICE_CHAMBRE("Service chambre"),
        RESTAURANT("Restaurant"),
        ACCUEIL("Accueil"),
        INSTALLATIONS("Installations"),
        EVENEMENT("Événement"),
        AUTRE("Autre");

        private final String libelle;

        TypeFeedback(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    // Enum pour le statut du feedback
    public enum StatutFeedback {
        EN_ATTENTE("En attente"),
        EN_COURS("En cours de traitement"),
        RESOLU("Résolu"),
        FERME("Fermé");

        private final String libelle;

        StatutFeedback(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le commentaire est obligatoire")
    @Size(max = 2000, message = "Le commentaire ne peut pas dépasser 2000 caractères")
    @Column(name = "commentaire", nullable = false, length = 2000)
    private String commentaire;

    @Min(value = 1, message = "La note doit être entre 1 et 5")
    @Max(value = 5, message = "La note doit être entre 1 et 5")
    @Column(name = "note")
    private Integer note; // 1 à 5 (optionnel)

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "type_feedback")
    private TypeFeedback type = TypeFeedback.GENERAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutFeedback statut = StatutFeedback.EN_ATTENTE;

    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    
    // Employé qui a traité/répondu au feedback (généralement un gestionnaire)
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

    // Gestionnaire qui a traité le feedback (relation directe)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gestionnaire_id")
    private Gestionnaire gestionnaire;

    @Size(max = 1000, message = "La réponse ne peut pas dépasser 1000 caractères")
    @Column(name = "reponse", length = 1000)
    private String reponse;

    @Column(name = "date_reponse")
    private LocalDateTime dateReponse;

    // Constructeurs
    public Feedback() {}

    public Feedback(String commentaire, Integer note, Client client) {
        this.commentaire = commentaire;
        this.note = note;
        this.client = client;
    }

    public Feedback(String commentaire, Integer note, Client client, TypeFeedback type) {
        this.commentaire = commentaire;
        this.note = note;
        this.client = client;
        this.type = type;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
    public TypeFeedback getType() { return type; }
    public void setType(TypeFeedback type) { this.type = type; }

    public StatutFeedback getStatut() { return statut; }
    public void setStatut(StatutFeedback statut) { this.statut = statut; }

    public Gestionnaire getGestionnaire() { return gestionnaire; }
    public void setGestionnaire(Gestionnaire gestionnaire) { this.gestionnaire = gestionnaire; }

    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { 
        this.reponse = reponse;
        if (reponse != null && !reponse.trim().isEmpty()) {
            this.dateReponse = LocalDateTime.now();
        }
    }

    public LocalDateTime getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDateTime dateReponse) { this.dateReponse = dateReponse; }

    // Méthodes utilitaires
    public boolean aUneReponse() {
        return reponse != null && !reponse.trim().isEmpty();
    }

    public boolean estResolu() {
        return statut == StatutFeedback.RESOLU || statut == StatutFeedback.FERME;
    }

    public String getNoteEtoiles() {
        if (note == null) return "";
        StringBuilder etoiles = new StringBuilder();
        for (int i = 0; i < note; i++) {
            etoiles.append("⭐");
        }
        for (int i = note; i < 5; i++) {
            etoiles.append("☆");
        }
        return etoiles.toString();
    }

    public String getNomClient() {
        return client != null ? (client.getPrenom() + " " + client.getNom()) : "Anonyme";
    }

    public String getNomEmploye() {
        return employe != null ? employe.getNomComplet() : "Non assigné";
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", note=" + note +
                ", type=" + type +
                ", statut=" + statut +
                ", client=" + getNomClient() +
                ", dateCreation=" + dateCreation +
                '}';
    }
}

