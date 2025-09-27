package com.example.demo.repository;

import com.example.demo.model.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    
    // Chambres disponibles
    List<Chambre> findByDisponibleTrue();
    
    // Chambres non disponibles
    List<Chambre> findByDisponibleFalse();
    
    // Rechercher par numéro de chambre
    Optional<Chambre> findByNumero(String numero);
    
    // Rechercher par type de chambre
    List<Chambre> findByType(Chambre.TypeChambre type);
    
    // Chambres disponibles d'un type donné
    List<Chambre> findByTypeAndDisponibleTrue(Chambre.TypeChambre type);
    
    // Chambres dans une fourchette de prix
    List<Chambre> findByPrixBetween(Double prixMin, Double prixMax);
    
    // Chambres disponibles dans une fourchette de prix
    @Query("SELECT c FROM Chambre c WHERE c.disponible = true AND c.prix BETWEEN :prixMin AND :prixMax")
    List<Chambre> findChambresDisponiblesParPrix(@Param("prixMin") Double prixMin, @Param("prixMax") Double prixMax);
    
    // Compter les chambres disponibles
    long countByDisponibleTrue();
    
    // Compter les chambres par type
    long countByType(Chambre.TypeChambre type);
}
