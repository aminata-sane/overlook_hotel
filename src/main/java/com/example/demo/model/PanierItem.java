package com.example.demo.model;

import java.time.LocalDate;
import java.io.Serializable;

public class PanierItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long chambreId;
    private String chambreNom;
    private String chambreType;
    private Double prixParNuit;
    private String imageUrl;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;
    private Integer nombreAdultes;
    private Integer nombreEnfants;
    private String commentaires;
    private Double prixTotal;
    private Long nombreNuits;
    
    // Constructeurs
    public PanierItem() {}
    
    public PanierItem(Long chambreId, String chambreNom, String chambreType, Double prixParNuit, 
                      String imageUrl, LocalDate dateArrivee, LocalDate dateDepart, 
                      Integer nombreAdultes, Integer nombreEnfants, String commentaires) {
        this.chambreId = chambreId;
        this.chambreNom = chambreNom;
        this.chambreType = chambreType;
        this.prixParNuit = prixParNuit;
        this.imageUrl = imageUrl;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nombreAdultes = nombreAdultes;
        this.nombreEnfants = nombreEnfants;
        this.commentaires = commentaires;
        this.calculerPrixTotal();
    }
    
    // Méthodes utilitaires
    public void calculerPrixTotal() {
        if (dateArrivee != null && dateDepart != null && prixParNuit != null) {
            this.nombreNuits = dateDepart.toEpochDay() - dateArrivee.toEpochDay();
            this.prixTotal = prixParNuit * nombreNuits;
        }
    }
    
    // Getters & Setters
    public Long getChambreId() { return chambreId; }
    public void setChambreId(Long chambreId) { this.chambreId = chambreId; }
    
    public String getChambreNom() { return chambreNom; }
    public void setChambreNom(String chambreNom) { this.chambreNom = chambreNom; }
    
    public String getChambreType() { return chambreType; }
    public void setChambreType(String chambreType) { this.chambreType = chambreType; }
    
    public Double getPrixParNuit() { return prixParNuit; }
    public void setPrixParNuit(Double prixParNuit) { 
        this.prixParNuit = prixParNuit;
        calculerPrixTotal();
    }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LocalDate getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(LocalDate dateArrivee) { 
        this.dateArrivee = dateArrivee;
        calculerPrixTotal();
    }
    
    public LocalDate getDateDepart() { return dateDepart; }
    public void setDateDepart(LocalDate dateDepart) { 
        this.dateDepart = dateDepart;
        calculerPrixTotal();
    }
    
    public Integer getNombreAdultes() { return nombreAdultes; }
    public void setNombreAdultes(Integer nombreAdultes) { this.nombreAdultes = nombreAdultes; }
    
    public Integer getNombreEnfants() { return nombreEnfants; }
    public void setNombreEnfants(Integer nombreEnfants) { this.nombreEnfants = nombreEnfants; }
    
    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    
    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }
    
    public Long getNombreNuits() { return nombreNuits; }
    public void setNombreNuits(Long nombreNuits) { this.nombreNuits = nombreNuits; }
    
    public Integer getNombrePersonnesTotal() {
        return nombreAdultes + (nombreEnfants != null ? nombreEnfants : 0);
    }
}
