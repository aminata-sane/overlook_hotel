package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chambres")
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    private Boolean disponible = true;

    // Chaque chambre est gérée par un gestionnaire
    @ManyToOne
    @JoinColumn(name = "gestionnaire_id")
    private Gestionnaire gestionnaire;

    // Constructeurs
    public Chambre() {}

    public Chambre(String numero, String type, Double prix, Boolean disponible, Gestionnaire gestionnaire) {
        this.numero = numero;
        this.type = type;
        this.prix = prix;
        this.disponible = disponible;
        this.gestionnaire = gestionnaire;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public Gestionnaire getGestionnaire() { return gestionnaire; }
    public void setGestionnaire(Gestionnaire gestionnaire) { this.gestionnaire = gestionnaire; }

    // Méthodes utilitaires
    public void reserver() { this.disponible = false; }
    public void liberer() { this.disponible = true; }
}

