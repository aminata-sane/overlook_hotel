package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evenements")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    private String description;

    @Column(nullable = false)
    private LocalDate dateDebut;

    private LocalDate dateFin;

    // Relation avec les clients qui ont réservé l'événement
    @ManyToMany
    @JoinTable(
        name = "evenement_clients",
        joinColumns = @JoinColumn(name = "evenement_id"),
        inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<Client> participants = new ArrayList<>();

    // Si on veut gérer l'installation liée à l'événement
    @ManyToOne
    @JoinColumn(name = "installation_id")
    private Installation installation;

    // Constructeurs
    public Evenement() {}

    public Evenement(String titre, LocalDate dateDebut, LocalDate dateFin) {
        this.titre = titre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public List<Client> getParticipants() { return participants; }
    public void setParticipants(List<Client> participants) { this.participants = participants; }

    public Installation getInstallation() { return installation; }
    public void setInstallation(Installation installation) { this.installation = installation; }

    // Méthodes utilitaires
    public void addParticipant(Client client) {
        if (!participants.contains(client)) participants.add(client);
    }

    public void removeParticipant(Client client) {
        participants.remove(client);
    }
}
