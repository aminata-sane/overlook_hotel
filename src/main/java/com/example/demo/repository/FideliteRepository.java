package com.example.demo.repository;

import com.example.demo.model.Fidelite;
import com.example.demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FideliteRepository extends JpaRepository<Fidelite, Long> {

    // Trouver la fidélité par client
    Optional<Fidelite> findByClient(Client client);
    
    // Trouver la fidélité par ID du client
    Optional<Fidelite> findByClientId(Long clientId);

    // Trouver tous les clients par niveau de fidélité
    @Query("SELECT f FROM Fidelite f WHERE f.points >= :seuilMin AND f.points <= :seuilMax ORDER BY f.points DESC")
    List<Fidelite> findByNiveauFidelite(@Param("seuilMin") int seuilMin, @Param("seuilMax") int seuilMax);

    // Trouver les clients avec le plus de points
    @Query("SELECT f FROM Fidelite f ORDER BY f.points DESC")
    List<Fidelite> findTopByOrderByPointsDesc();

    // Trouver les clients avec un nombre minimum de points
    List<Fidelite> findByPointsGreaterThanEqual(int points);

    // Trouver les clients avec un nombre de points dans une plage
    List<Fidelite> findByPointsBetween(int pointsMin, int pointsMax);

    // Compter le nombre de clients par niveau
    @Query("SELECT COUNT(f) FROM Fidelite f WHERE f.points >= :seuilMin AND f.points <= :seuilMax")
    long countByNiveauFidelite(@Param("seuilMin") int seuilMin, @Param("seuilMax") int seuilMax);

    // Moyenne des points de fidélité
    @Query("SELECT AVG(f.points) FROM Fidelite f")
    Double findAveragePoints();

    // Total des points attribués
    @Query("SELECT SUM(f.points) FROM Fidelite f")
    Long findTotalPoints();

    // Clients les plus fidèles (top 10)
    @Query("SELECT f FROM Fidelite f ORDER BY f.points DESC")
    List<Fidelite> findTop10ByOrderByPointsDesc();
}
