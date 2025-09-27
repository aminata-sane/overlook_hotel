package com.example.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Panier implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<PanierItem> items;
    private Double prixTotal;
    
    public Panier() {
        this.items = new ArrayList<>();
        this.prixTotal = 0.0;
    }
    
    // Ajouter un item au panier
    public void ajouterItem(PanierItem item) {
        this.items.add(item);
        calculerPrixTotal();
    }
    
    // Supprimer un item du panier
    public void supprimerItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            calculerPrixTotal();
        }
    }
    
    // Supprimer un item par ID de chambre
    public void supprimerItemParChambre(Long chambreId) {
        items.removeIf(item -> item.getChambreId().equals(chambreId));
        calculerPrixTotal();
    }
    
    // Vider le panier
    public void vider() {
        items.clear();
        prixTotal = 0.0;
    }
    
    // Calculer le prix total
    public void calculerPrixTotal() {
        prixTotal = items.stream()
                        .mapToDouble(item -> item.getPrixTotal() != null ? item.getPrixTotal() : 0.0)
                        .sum();
    }
    
    // Vérifier si le panier est vide
    public boolean estVide() {
        return items.isEmpty();
    }
    
    // Obtenir le nombre d'items
    public int getNombreItems() {
        return items.size();
    }
    
    // Obtenir le nombre total de nuits
    public Long getNombreTotalNuits() {
        return items.stream()
                   .mapToLong(item -> item.getNombreNuits() != null ? item.getNombreNuits() : 0L)
                   .sum();
    }
    
    // Getters & Setters
    public List<PanierItem> getItems() {
        return items;
    }
    
    public void setItems(List<PanierItem> items) {
        this.items = items;
        calculerPrixTotal();
    }
    
    public Double getPrixTotal() {
        return prixTotal;
    }
    
    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }
}
