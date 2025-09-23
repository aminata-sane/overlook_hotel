package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentaire;
    private Integer note; // 1 à 5
    private LocalDateTime dateCreation = LocalDateTime.now();

    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    
    @ManyToOne
    @JoinColumn(name = "gestionnaire_id")
    private Gestionnaire gestionnaire;

    private String reponse;

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
    public Gestionnaire getGestionnaire() { return gestionnaire; }
    public void setGestionnaire(Gestionnaire gestionnaire) { this.gestionnaire = gestionnaire; }
    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }
}

