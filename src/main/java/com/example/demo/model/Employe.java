package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employes")
public class Employe {

    // Enum pour les rôles des employés
    public enum RoleEmploye {
        RECEPTIONNISTE("Réceptionniste"),
        FEMME_DE_MENAGE("Femme de ménage"),
        MAINTENANCE("Maintenance"),
        GESTIONNAIRE("Gestionnaire"),
        DIRECTEUR("Directeur");

        private final String libelle;

        RoleEmploye(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        @Override
        public String toString() {
            return libelle;
        }
    }

    // Enum pour le statut de l'employé
    public enum StatutEmploye {
        ACTIF("Actif"),
        INACTIF("Inactif"),
        SUSPENDU("Suspendu"),
        EN_CONGE("En congé");

        private final String libelle;

        StatutEmploye(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        @Override
        public String toString() {
            return libelle;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @Column(nullable = false)
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEmploye role = RoleEmploye.RECEPTIONNISTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEmploye statut = StatutEmploye.ACTIF;

    @Column
    private LocalDate dateEmbauche;

    @Column
    private String telephone;

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
        this.role = RoleEmploye.RECEPTIONNISTE;
        this.statut = StatutEmploye.ACTIF;
        this.dateEmbauche = LocalDate.now();
    }

    public Employe(String nom, String prenom, String email, String motDePasse, RoleEmploye role) {
        this(nom, prenom, email, motDePasse);
        this.role = role;
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

    public RoleEmploye getRole() { return role; }
    public void setRole(RoleEmploye role) { this.role = role; }

    public StatutEmploye getStatut() { return statut; }
    public void setStatut(StatutEmploye statut) { this.statut = statut; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

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

    // Méthodes utilitaires supplémentaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public boolean isActif() {
        return statut == StatutEmploye.ACTIF;
    }

    public boolean isGestionnaire() {
        return role == RoleEmploye.GESTIONNAIRE || role == RoleEmploye.DIRECTEUR;
    }

    @Override
    public String toString() {
        return "Employe{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", statut=" + statut +
                ", dateEmbauche=" + dateEmbauche +
                '}';
    }
}


