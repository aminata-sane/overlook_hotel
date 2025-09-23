package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "analyses")
public class Analyse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "occupation_chambres")
    private int occupationChambres;  // ex: pourcentage d'occupation

    @Column(name = "satisfaction_client")
    private double satisfactionClient; // ex: note moyenne

    @Column(name = "revenus")
    private double revenus; // revenus totaux

    // Constructeurs
    public Analyse() {}

    public Analyse(int occupationChambres, double satisfactionClient, double revenus) {
        this.occupationChambres = occupationChambres;
        this.satisfactionClient = satisfactionClient;
        this.revenus = revenus;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getOccupationChambres() { return occupationChambres; }
    public void setOccupationChambres(int occupationChambres) { this.occupationChambres = occupationChambres; }

    public double getSatisfactionClient() { return satisfactionClient; }
    public void setSatisfactionClient(double satisfactionClient) { this.satisfactionClient = satisfactionClient; }

    public double getRevenus() { return revenus; }
    public void setRevenus(double revenus) { this.revenus = revenus; }

    // Méthode utilitaire pour mettre à jour l'occupation
    public void updateOccupation(int chambresOccupees, int chambresTotales) {
        if (chambresTotales > 0) {
            this.occupationChambres = (int) ((double) chambresOccupees / chambresTotales * 100);
        }
    }

    // Méthode utilitaire pour calculer la satisfaction moyenne
    public void updateSatisfaction(double totalNotes, int nbCommentaires) {
        if (nbCommentaires > 0) {
            this.satisfactionClient = totalNotes / nbCommentaires;
        }
    }
}
