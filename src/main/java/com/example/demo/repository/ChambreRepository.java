package com.example.demo.repository;

import com.example.demo.entity.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    // Liste des chambres disponibles
    List<Chambre> findByDisponibleTrue();
}
