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

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    // Gestion avancée : horaires, congés, formations
    @ElementCollection
    @CollectionTable(name = "employe_horaires", joinColumns = @JoinColumn(name = "employe_id"))
    @Column(name = "horaire")
    private List<String> horaires = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "employe_conges", joinColumns = @JoinColumn(name = "employe_id"))
    @Column(name = "conge")
    private List<String> conges = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "employe_formations", joinColumns = @JoinColumn(name = "employe_id"))
    @Column(name = "formation")
    private List<String> formations = new ArrayList<>();

    // Constructeurs
    public Employe() {}

    public Employe(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

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

    // Méthodes utilitaires
    public void addHoraire(String horaire) { horaires.add(horaire); }
    public void removeHoraire(String horaire) { horaires.remove(horaire); }

    public void addConge(String conge) { conges.add(conge); }
    public void removeConge(String conge) { conges.remove(conge); }

    public void addFormation(String formation) { formations.add(formation); }
    public void removeFormation(String formation) { formations.remove(formation); }
}
