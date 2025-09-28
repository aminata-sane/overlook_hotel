package com.example.demo.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chambres")
public class Chambre {

    // Enum pour les types de chambres
    public enum TypeChambre {
        SIMPLE("Simple"),
        DOUBLE("Double"),
        SUITE("Suite"),
        FAMILIALE("Familiale"),
        DELUXE("Deluxe");

        private final String libelle;

        TypeChambre(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    // Enum pour les équipements/options de chambres
    public enum Equipement {
        JACUZZI("Jacuzzi"),
        LIT_MASSANT("Lit massant"),
        FAUTEUIL_MASSAGE_ELECTRIQUE("Fauteuil de massage électrique"),
        SOL_CHAUFFANT("Sol chauffant"),
        ECRAN_HOME_CINEMA("Écran home cinéma"),
        BALCON("Balcon"),
        VUE_MER("Vue sur mer"),
        VUE_MONTAGNE("Vue sur montagne"),
        MINI_BAR("Mini-bar"),
        CLIMATISATION("Climatisation"),
        WIFI_PREMIUM("WiFi premium"),
        COFFRE_FORT("Coffre-fort");

        private final String libelle;

        Equipement(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    // Enum pour les statuts de chambres
    public enum StatutChambre {
        DISPONIBLE("Disponible"),
        OCCUPEE("Occupée"),
        NETTOYAGE("En nettoyage"),
        MAINTENANCE("En maintenance"),
        HORS_SERVICE("Hors service"),
        INSPECTION("En inspection");

        private final String libelle;

        StatutChambre(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        public String getCssClass() {
            switch (this) {
                case DISPONIBLE: return "success";
                case OCCUPEE: return "primary";
                case NETTOYAGE: return "warning";
                case MAINTENANCE: return "danger";
                case HORS_SERVICE: return "dark";
                case INSPECTION: return "info";
                default: return "secondary";
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeChambre type;

    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    private Boolean disponible = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private StatutChambre statut = StatutChambre.DISPONIBLE;

    @Column(nullable = false)
    private Integer capacite;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @ElementCollection(targetClass = Equipement.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "chambre_equipements", joinColumns = @JoinColumn(name = "chambre_id"))
    @Column(name = "equipement")
    private Set<Equipement> equipements = new HashSet<>();

    // Chaque chambre est gérée par un gestionnaire
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gestionnaire_id")
    private Gestionnaire gestionnaire;

    // Constructeurs
    public Chambre() {}

    // Constructeur temporaire sans Gestionnaire
    public Chambre(String numero, TypeChambre type, Double prix, Integer capacite, String description, Boolean disponible) {
        this.numero = numero;
        this.type = type;
        this.prix = prix;
        this.capacite = capacite;
        this.description = description;
        this.disponible = disponible;
        this.equipements = new HashSet<>();
    }

    // Constructeur avec gestionnaire
    public Chambre(String numero, TypeChambre type, Double prix, Integer capacite, String description, Boolean disponible, Gestionnaire gestionnaire) {
        this.numero = numero;
        this.type = type;
        this.prix = prix;
        this.capacite = capacite;
        this.description = description;
        this.disponible = disponible;
        this.gestionnaire = gestionnaire;
        this.equipements = new HashSet<>();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public TypeChambre getType() { return type; }
    public void setType(TypeChambre type) { this.type = type; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public StatutChambre getStatut() { return statut; }
    public void setStatut(StatutChambre statut) { this.statut = statut; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Set<Equipement> getEquipements() { return equipements; }
    public void setEquipements(Set<Equipement> equipements) { this.equipements = equipements; }

    public Gestionnaire getGestionnaire() { return gestionnaire; }
    public void setGestionnaire(Gestionnaire gestionnaire) { this.gestionnaire = gestionnaire; }

    // Méthodes utilitaires
    public void reserver() { 
        this.disponible = false; 
    }
    
    public void liberer() { 
        this.disponible = true; 
    }
    
    public boolean estDisponible() {
        return this.disponible;
    }

    // Méthodes pour gérer les équipements
    public void ajouterEquipement(Equipement equipement) {
        this.equipements.add(equipement);
    }

    public void retirerEquipement(Equipement equipement) {
        this.equipements.remove(equipement);
    }

    public boolean possedeEquipement(Equipement equipement) {
        return this.equipements.contains(equipement);
    }
    
    // Méthode toString pour debug
    @Override
    public String toString() {
        return "Chambre{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", type=" + type +
                ", prix=" + prix +
                ", capacite=" + capacite +
                ", description='" + description + '\'' +
                ", equipements=" + equipements +
                ", disponible=" + disponible +
                '}';
    }
}


