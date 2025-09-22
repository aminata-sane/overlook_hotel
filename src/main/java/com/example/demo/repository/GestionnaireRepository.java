package com.example.demo.repository;

import com.example.demo.entity.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long> {
    // Méthode pour retrouver un gestionnaire par email (utile pour l'authentification)
    Optional<Gestionnaire> findByEmail(String email);
}
