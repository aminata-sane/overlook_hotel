package com.example.demo.repository;

import com.example.demo.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    // Recherche par email (pour l'authentification)
    Optional<Employe> findByEmail(String email);

    // Recherche par rôle
    List<Employe> findByRole(Employe.RoleEmploye role);

    // Recherche par statut
    List<Employe> findByStatut(Employe.StatutEmploye statut);

    // Employés actifs (méthode utilitaire)
    default List<Employe> findEmployesActifs() {
        return findByStatut(Employe.StatutEmploye.ACTIF);
    }

    // Recherche par nom ou prénom
    List<Employe> findByNomContainingOrPrenomContaining(String nom, String prenom);

    // Recherche par nom, prénom ou email
    List<Employe> findByNomContainingOrPrenomContainingOrEmailContaining(
            String nom, String prenom, String email);

    // Compter par statut
    long countByStatut(Employe.StatutEmploye statut);

    // Compter par rôle
    long countByRole(Employe.RoleEmploye role);

    // Vérifier si un email existe
    boolean existsByEmail(String email);
}
