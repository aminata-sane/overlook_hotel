package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.entity.Reservation;
import com.example.demo.repository.ClientRepository;
import com.example.demo.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ClientRepository clientRepository;

    public ReservationController(ReservationService reservationService, ClientRepository clientRepository) {
        this.reservationService = reservationService;
        this.clientRepository = clientRepository;
    }

    // Créer une réservation
    @PostMapping("/{clientId}")
    public ResponseEntity<?> creerReservation(
            @PathVariable Long clientId,
            @RequestParam Long chambreId,
            @RequestParam String debut,
            @RequestParam String fin
    ) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LocalDate dateDebut = LocalDate.parse(debut);
        LocalDate dateFin = LocalDate.parse(fin);

        Reservation reservation = reservationService.creerReservation(clientOpt.get(), chambreId, dateDebut, dateFin);
        return ResponseEntity.ok(reservation);
    }

    // Récupérer toutes les réservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.findAll();
    }

    // Récupérer une réservation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    // Mettre à jour une réservation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long id,
            @RequestParam(required = false) String debut,
            @RequestParam(required = false) String fin,
            @RequestParam(required = false) Long chambreId
    ) {
        try {
            LocalDate dateDebut = (debut != null) ? LocalDate.parse(debut) : null;
            LocalDate dateFin = (fin != null) ? LocalDate.parse(fin) : null;

            Reservation updated = reservationService.updateReservation(id, dateDebut, dateFin, chambreId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer toutes les réservations d’un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> getReservationsByClient(@PathVariable Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Reservation> reservations = reservationService.getReservationsByClient(clientOpt.get());
            return ResponseEntity.ok(reservations);
        }
}

