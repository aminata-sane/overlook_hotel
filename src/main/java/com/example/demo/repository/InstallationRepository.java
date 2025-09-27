package com.example.demo.repository;

import com.example.demo.model.Installation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallationRepository extends JpaRepository<Installation, Long> {

    // Trouver les installations disponibles
    List<Installation> findByDisponibleTrue();

    // Trouver par type d'installation
    List<Installation> findByType(Installation.TypeInstallation type);

    // Trouver les installations disponibles par type
    List<Installation> findByTypeAndDisponibleTrue(Installation.TypeInstallation type);

    // Trouver par capacité minimale
    List<Installation> findByCapaciteGreaterThanEqual(Integer capaciteMin);

    // Rechercher par nom
    @Query("SELECT i FROM Installation i WHERE LOWER(i.nom) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<Installation> rechercherInstallations(@Param("terme") String terme);

    // Compter par type
    long countByType(Installation.TypeInstallation type);

    // Trouver les installations avec une capacité dans une fourchette
    @Query("SELECT i FROM Installation i WHERE i.capacite BETWEEN :capaciteMin AND :capaciteMax")
    List<Installation> findByCapaciteRange(@Param("capaciteMin") Integer capaciteMin, @Param("capaciteMax") Integer capaciteMax);
}
