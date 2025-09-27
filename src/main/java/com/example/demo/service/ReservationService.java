package com.example.demo.service;

import com.example.demo.model.Chambre;
import com.example.demo.model.Client;
import com.example.demo.model.Reservation;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ChambreService chambreService;

    // Créer une réservation pour un client
    public Reservation creerReservation(Client client, Long chambreId, LocalDate dateArrivee, LocalDate dateDepart, Integer nombreAdultes, Integer nombreEnfants) {
        // Vérifier que la chambre existe et est disponible
        Optional<Chambre> chambreOpt = chambreService.getChambreById(chambreId);
        if (chambreOpt.isEmpty()) {
            throw new RuntimeException("Chambre non trouvée avec l'id " + chambreId);
        }
        
        Chambre chambre = chambreOpt.get();
        if (!chambre.getDisponible()) {
            throw new RuntimeException("Chambre non disponible");
        }

        // Vérifier que les dates sont cohérentes
        if (dateArrivee.isAfter(dateDepart)) {
            throw new RuntimeException("La date d'arrivée doit être antérieure à la date de départ");
        }
        
        if (dateArrivee.isBefore(LocalDate.now())) {
            throw new RuntimeException("La date d'arrivée ne peut pas être dans le passé");
        }

        // Vérifier qu'il n'y a pas de conflit avec d'autres réservations
        List<Reservation> conflits = reservationRepository.findReservationsConflictuelles(chambreId, dateArrivee, dateDepart);
        if (!conflits.isEmpty()) {
            throw new RuntimeException("Cette chambre n'est pas disponible pour ces dates");
        }

        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateArrivee(dateArrivee);
        reservation.setDateDepart(dateDepart);
        reservation.setNombreAdultes(nombreAdultes != null ? nombreAdultes : 1);
        reservation.setNombreEnfants(nombreEnfants != null ? nombreEnfants : 0);

        // Calculer le prix total
        reservation.calculerPrixTotal();
        reservation.setStatut(Reservation.StatutReservation.CONFIRMEE);

        return reservationRepository.save(reservation);
    }

    // Récupérer toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Récupérer une réservation par ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Récupérer les réservations d'un client
    public List<Reservation> getReservationsByClient(Long clientId) {
        return reservationRepository.findByClientIdOrderByDateArriveeDesc(clientId);
    }

    // Récupérer les réservations pour une chambre (via les conflits)
    public List<Reservation> getReservationsByChambre(Long chambreId, LocalDate dateDebut, LocalDate dateFin) {
        return reservationRepository.findReservationsConflictuelles(chambreId, dateDebut, dateFin);
    }

    // Annuler une réservation
    public void annulerReservation(Long id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setStatut(Reservation.StatutReservation.ANNULEE);
            
            // Libérer la chambre
            if (reservation.getChambre() != null) {
                Chambre chambre = reservation.getChambre();
                chambre.setDisponible(true);
                chambreService.updateChambre(chambre.getId(), chambre);
            }
            
            reservationRepository.save(reservation);
        }
    }

    // Supprimer une réservation
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
