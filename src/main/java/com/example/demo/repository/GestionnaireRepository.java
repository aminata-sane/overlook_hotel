package com.example.demo.repository;

import com.example.demo.model.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long> {
    
    // Rechercher par email
    Optional<Gestionnaire> findByEmail(String email);
    
    // Rechercher les gestionnaires actifs
    List<Gestionnaire> findByActifTrue();
    
    // Rechercher par nom ou prénom (insensible à la casse)
    @Query("SELECT g FROM Gestionnaire g WHERE " +
           "LOWER(g.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(g.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Gestionnaire> findByNomOrPrenomContainingIgnoreCase(String searchTerm);
    
    // Compter le nombre de gestionnaires actifs
    long countByActifTrue();
    
    // Trouver les gestionnaires qui ont le plus de chambres
    @Query("SELECT g FROM Gestionnaire g LEFT JOIN g.chambres c " +
           "GROUP BY g ORDER BY COUNT(c) DESC")
    List<Gestionnaire> findGestionnairesOrderByNombreChambresDesc();
    
    // Trouver les gestionnaires qui ont le plus de feedbacks
    @Query("SELECT g FROM Gestionnaire g LEFT JOIN g.feedbacks f " +
           "GROUP BY g ORDER BY COUNT(f) DESC")
    List<Gestionnaire> findGestionnairesOrderByNombreFeedbacksDesc();
    
    // Vérifier si un email existe déjà (pour l'unique constraint)
    boolean existsByEmail(String email);
    
    // Trouver les gestionnaires sans chambres assignées
    @Query("SELECT g FROM Gestionnaire g WHERE g.chambres IS EMPTY")
    List<Gestionnaire> findGestionnairesSansChambres();
}
