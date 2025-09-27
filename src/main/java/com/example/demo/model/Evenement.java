package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evenements")
public class Evenement {

    // Enum pour le statut de l'événement
    public enum StatutEvenement {
        PLANIFIE("Planifié"),
        EN_COURS("En cours"),
        TERMINE("Terminé"),
        ANNULE("Annulé"),
        REPORTE("Reporté");

        private final String libelle;

        StatutEvenement(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre de l'événement est obligatoire")
    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    @Column(name = "titre", nullable = false)
    private String titre;

    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    @Column(name = "description")
    private String description;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Min(value = 1, message = "Le nombre maximum de participants doit être au moins de 1")
    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutEvenement statut = StatutEvenement.PLANIFIE;

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

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public StatutEvenement getStatut() { return statut; }
    public void setStatut(StatutEvenement statut) { this.statut = statut; }

    // Méthodes utilitaires
    public void addParticipant(Client client) {
        if (!participants.contains(client)) {
            if (maxParticipants != null && participants.size() >= maxParticipants) {
                throw new RuntimeException("Nombre maximum de participants atteint pour cet événement");
            }
            participants.add(client);
        }
    }

    public void removeParticipant(Client client) {
        participants.remove(client);
    }

    public int getNombreParticipants() {
        return participants.size();
    }

    public boolean estComplet() {
        return maxParticipants != null && participants.size() >= maxParticipants;
    }

    public boolean estEnCours() {
        LocalDate aujourd = LocalDate.now();
        return dateDebut.equals(aujourd) || 
               (dateDebut.isBefore(aujourd) && 
                (dateFin == null || !dateFin.isBefore(aujourd)));
    }

    public boolean estTermine() {
        return dateFin != null && dateFin.isBefore(LocalDate.now());
    }

    public boolean estAVenir() {
        return dateDebut.isAfter(LocalDate.now());
    }

    public long getDureeEnJours() {
        if (dateFin == null) return 1;
        return dateDebut.until(dateFin).getDays() + 1;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut=" + statut +
                ", participants=" + participants.size() +
                '}';
    }
}


