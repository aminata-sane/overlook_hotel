package com.example.demo.service;

import com.example.demo.entity.Chambre;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Client;
import com.example.demo.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ChambreService chambreService;

    public ReservationService(ReservationRepository reservationRepository, ChambreService chambreService) {
        this.reservationRepository = reservationRepository;
        this.chambreService = chambreService;
    }

    // Créer une réservation pour un client
    public Reservation creerReservation(Client client, Long chambreId, LocalDate debut, LocalDate fin) {
        Chambre chambre = chambreService.reserverChambre(chambreId);

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(debut);
        reservation.setDateFin(fin);

        long jours = fin.toEpochDay() - debut.toEpochDay() + 1; // Inclut le dernier jour
        reservation.setPrixTotal(chambre.getPrix() * jours);

        reservation.setStatut("CONFIRMEE");

        return reservationRepository.save(reservation);
    }

    // Lister toutes les réservations
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    // Récupérer une réservation par ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Lister les réservations d'un client
    public List<Reservation> getReservationsByClient(Client client) {
        return reservationRepository.findByClientId(client.getId());
    }

    // Lister les réservations d'une chambre
    public List<Reservation> getReservationsByChambre(Long chambreId) {
        return reservationRepository.findByChambreId(chambreId);
    }

    // Mettre à jour une réservation
    public Reservation updateReservation(Long id, LocalDate dateDebut, LocalDate dateFin, Long chambreId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id " + id));

        if (dateDebut != null) reservation.setDateDebut(dateDebut);
        if (dateFin != null) reservation.setDateFin(dateFin);

        if (chambreId != null && (reservation.getChambre() == null || !reservation.getChambre().getId().equals(chambreId))) {
            // Libérer l'ancienne chambre si elle existait
            if (reservation.getChambre() != null) {
                reservation.getChambre().setDisponible(true);
            }
            Chambre nouvelleChambre = chambreService.reserverChambre(chambreId);
            reservation.setChambre(nouvelleChambre);
        }

        // Recalcul du prix
        if (reservation.getDateDebut() != null && reservation.getDateFin() != null && reservation.getChambre() != null) {
            long jours = reservation.getDateFin().toEpochDay() - reservation.getDateDebut().toEpochDay() + 1;
            reservation.setPrixTotal(reservation.getChambre().getPrix() * jours);
        }

        return reservationRepository.save(reservation);
    }

    // Supprimer une réservation
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id " + id));

        // Libérer la chambre associée
        if (reservation.getChambre() != null) {
            reservation.getChambre().setDisponible(true);
        }

        reservationRepository.delete(reservation);
    }
}
