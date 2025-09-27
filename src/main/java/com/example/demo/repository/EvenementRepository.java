package com.example.demo.repository;

import com.example.demo.model.Evenement;
import com.example.demo.model.Installation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    // Trouver tous les événements par statut
    List<Evenement> findByStatut(Evenement.StatutEvenement statut);

    // Trouver les événements à venir
    List<Evenement> findByDateDebutGreaterThanEqualOrderByDateDebut(LocalDate date);

    // Trouver les événements passés
    List<Evenement> findByDateFinLessThanOrderByDateFinDesc(LocalDate date);

    // Trouver les événements en cours
    @Query("SELECT e FROM Evenement e WHERE e.dateDebut <= :aujourd AND (e.dateFin IS NULL OR e.dateFin >= :aujourd)")
    List<Evenement> findEvenementsenCours(@Param("aujourd") LocalDate aujourd);

    // Trouver les événements par installation
    List<Evenement> findByInstallation(Installation installation);

    // Trouver les événements par période
    @Query("SELECT e FROM Evenement e WHERE e.dateDebut <= :dateFin AND (e.dateFin IS NULL OR e.dateFin >= :dateDebut)")
    List<Evenement> findEvenementsByPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    // Rechercher par titre ou description
    @Query("SELECT e FROM Evenement e WHERE LOWER(e.titre) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<Evenement> rechercherEvenements(@Param("terme") String terme);

    // Compter les événements par statut
    long countByStatut(Evenement.StatutEvenement statut);

    // Trouver les événements d'un client spécifique
    @Query("SELECT e FROM Evenement e JOIN e.participants p WHERE p.id = :clientId")
    List<Evenement> findEvenementsByClientId(@Param("clientId") Long clientId);

    // Trouver les événements d'aujourd'hui
    List<Evenement> findByDateDebutEqualsOrderByTitre(LocalDate date);

    // Trouver les événements de la semaine prochaine
    @Query("SELECT e FROM Evenement e WHERE e.dateDebut >= :dateDebut AND e.dateDebut <= :dateFin ORDER BY e.dateDebut")
    List<Evenement> findEvenementsSemaine(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}
