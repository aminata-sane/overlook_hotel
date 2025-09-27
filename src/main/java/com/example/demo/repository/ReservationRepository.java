package com.example.demo.repository;

import com.example.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Trouver les réservations qui se chevauchent avec une période donnée
    @Query("SELECT r FROM Reservation r WHERE r.chambre.id = :chambreId AND " +
           "((r.dateArrivee <= :dateDepart AND r.dateDepart >= :dateArrivee) OR " +
           "(r.dateArrivee >= :dateArrivee AND r.dateArrivee <= :dateDepart)) AND " +
           "r.statut != 'ANNULEE'")
    List<Reservation> findReservationsConflictuelles(
            @Param("chambreId") Long chambreId,
            @Param("dateArrivee") LocalDate dateArrivee,
            @Param("dateDepart") LocalDate dateDepart);

    // Trouver les réservations d'un client
    List<Reservation> findByClientIdOrderByDateArriveeDesc(Long clientId);

    // Trouver les réservations par statut
    List<Reservation> findByStatutOrderByDateArriveeAsc(Reservation.StatutReservation statut);

    // Trouver les réservations pour une période donnée
    @Query("SELECT r FROM Reservation r WHERE r.dateArrivee >= :dateDebut AND r.dateArrivee <= :dateFin ORDER BY r.dateArrivee ASC")
    List<Reservation> findReservationsPourPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}
