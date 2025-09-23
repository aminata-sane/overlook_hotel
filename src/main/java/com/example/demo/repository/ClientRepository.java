package com.example.demo.repository;

import com.example.demo.model.Client;
import com.example.demo.model.Client.StatutClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Méthodes automatiques héritées de JpaRepository :
    // save(), findAll(), findById(), delete(), etc.

    // Méthodes personnalisées (Spring génère automatiquement le SQL )
    Optional<Client> findByEmail(String email);
    List<Client> findByStatut(StatutClient statut);
    List<Client> findByNomContaining(String nom);
    Optional<Client> findByEmailAndMotDePasse(String email, String motDePasse);
    
}
