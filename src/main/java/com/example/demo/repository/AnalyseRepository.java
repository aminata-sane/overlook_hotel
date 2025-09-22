package com.example.demo.repository;

import com.example.demo.entity.Analyse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyseRepository extends JpaRepository<Analyse, Long> {
    // Tu peux ajouter des méthodes personnalisées si nécessaire
}
