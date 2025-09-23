package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Lister toutes les réservations d'un client
    List<Reservation> findByClientId(Long clientId);

    // Lister toutes les réservations d'une chambre
    List<Reservation> findByChambreId(Long chambreId);
}

