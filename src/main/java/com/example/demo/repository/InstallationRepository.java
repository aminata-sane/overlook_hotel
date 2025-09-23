package com.example.demo.repository;

import com.example.demo.entity.Installation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallationRepository extends JpaRepository<Installation, Long> {
    // Tu peux ajouter des méthodes personnalisées si nécessaire, par exemple :
    // List<Installation> findByType(String type);
}
