package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employes")
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

    // Gestion avancée : horaires, congés, formations
    @ElementCollection
    private List<String> horaires = new ArrayList<>();

    @ElementCollection
    private List<String> conges = new ArrayList<>();

    @ElementCollection
    private List<String> formations = new ArrayList<>();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public List<String> getHoraires() { return horaires; }
    public void setHoraires(List<String> horaires) { this.horaires = horaires; }

    public List<String> getConges() { return conges; }
    public void setConges(List<String> conges) { this.conges = conges; }

    public List<String> getFormations() { return formations; }
    public void setFormations(List<String> formations) { this.formations = formations; }
}
