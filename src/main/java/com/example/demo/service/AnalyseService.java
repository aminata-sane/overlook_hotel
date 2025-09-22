package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class AnalyseService {

    private int tauxOccupation;
    private double satisfactionMoyenne;

    // Constructeur
    public AnalyseService() {
        this.tauxOccupation = 0;
        this.satisfactionMoyenne = 0.0;
    }

    // Getters & Setters
    public int getTauxOccupation() { return tauxOccupation; }
    public void setTauxOccupation(int tauxOccupation) { this.tauxOccupation = tauxOccupation; }

    public double getSatisfactionMoyenne() { return satisfactionMoyenne; }
    public void setSatisfactionMoyenne(double satisfactionMoyenne) { this.satisfactionMoyenne = satisfactionMoyenne; }

    // Méthodes pour calculer ou mettre à jour les KPI
    public void calculerTauxOccupation(int chambresOccupees, int chambresTotales) {
        if (chambresTotales > 0) {
            this.tauxOccupation = (chambresOccupees * 100) / chambresTotales;
        }
    }

    public void calculerSatisfactionMoyenne(double totalNotes, int nbCommentaires) {
        if (nbCommentaires > 0) {
            this.satisfactionMoyenne = totalNotes / nbCommentaires;
        }
    }
}
