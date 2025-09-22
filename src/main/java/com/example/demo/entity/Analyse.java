package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "analyses")
public class Analyse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int occupationChambres;  // ex: pourcentage d'occupation
    private double satisfactionClient; // ex: note moyenne
    private double revenus; // revenus totaux

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getOccupationChambres() { return occupationChambres; }
    public void setOccupationChambres(int occupationChambres) { this.occupationChambres = occupationChambres; }

    public double getSatisfactionClient() { return satisfactionClient; }
    public void setSatisfactionClient(double satisfactionClient) { this.satisfactionClient = satisfactionClient; }

    public double getRevenus() { return revenus; }
    public void setRevenus(double revenus) { this.revenus = revenus; }
}
