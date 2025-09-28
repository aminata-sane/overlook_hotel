package com.example.demo.service;

import com.example.demo.model.Employe;
import com.example.demo.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    // Récupérer tous les employés
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    // Récupérer un employé par ID
    public Optional<Employe> getEmployeById(Long id) {
        return employeRepository.findById(id);
    }

    // Récupérer un employé par email
    public Optional<Employe> getEmployeByEmail(String email) {
        return employeRepository.findByEmail(email);
    }

    // Récupérer les employés par rôle
    public List<Employe> getEmployesByRole(Employe.RoleEmploye role) {
        return employeRepository.findByRole(role);
    }

    // Récupérer les employés par statut
    public List<Employe> getEmployesByStatut(Employe.StatutEmploye statut) {
        return employeRepository.findByStatut(statut);
    }

    // Récupérer les employés actifs
    public List<Employe> getEmployesActifs() {
        return employeRepository.findByStatutOrderByNomAsc(Employe.StatutEmploye.ACTIF);
    }

    // Créer un nouvel employé
    public Employe createEmploye(Employe employe) {
        // Vérifier si l'email existe déjà
        if (employeRepository.existsByEmail(employe.getEmail())) {
            throw new RuntimeException("Un employé avec l'email " + employe.getEmail() + " existe déjà !");
        }

        // Définir les valeurs par défaut si non spécifiées
        if (employe.getRole() == null) {
            employe.setRole(Employe.RoleEmploye.RECEPTIONNISTE);
        }
        if (employe.getStatut() == null) {
            employe.setStatut(Employe.StatutEmploye.ACTIF);
        }
        if (employe.getDateEmbauche() == null) {
            employe.setDateEmbauche(LocalDate.now());
        }

        return employeRepository.save(employe);
    }

    // Mettre à jour un employé
    public Employe updateEmploye(Long id, Employe employeDetails) {
        Optional<Employe> employeOpt = employeRepository.findById(id);
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();

            // Vérifier si le nouvel email n'est pas déjà utilisé par un autre employé
            if (!employe.getEmail().equals(employeDetails.getEmail())) {
                if (employeRepository.existsByEmail(employeDetails.getEmail())) {
                    throw new RuntimeException("L'email " + employeDetails.getEmail() + " est déjà utilisé !");
                }
            }

            // Mettre à jour les champs
            employe.setNom(employeDetails.getNom());
            employe.setPrenom(employeDetails.getPrenom());
            employe.setEmail(employeDetails.getEmail());
            employe.setTelephone(employeDetails.getTelephone());
            
            // Ne pas mettre à jour le mot de passe s'il est vide
            if (employeDetails.getMotDePasse() != null && !employeDetails.getMotDePasse().trim().isEmpty()) {
                employe.setMotDePasse(employeDetails.getMotDePasse());
            }
            
            if (employeDetails.getRole() != null) {
                employe.setRole(employeDetails.getRole());
            }
            if (employeDetails.getStatut() != null) {
                employe.setStatut(employeDetails.getStatut());
            }
            if (employeDetails.getDateEmbauche() != null) {
                employe.setDateEmbauche(employeDetails.getDateEmbauche());
            }

            return employeRepository.save(employe);
        } else {
            throw new RuntimeException("Employé avec l'ID " + id + " non trouvé !");
        }
    }

    // Supprimer un employé
    public void deleteEmploye(Long id) {
        Optional<Employe> employe = employeRepository.findById(id);
        if (employe.isPresent()) {
            employeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Employé avec l'ID " + id + " non trouvé !");
        }
    }

    // Authentification - Vérifier les identifiants
    public Optional<Employe> authenticate(String email, String motDePasse) {
        Optional<Employe> employeOpt = employeRepository.findByEmail(email);
        
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();
            
            // Vérifier le mot de passe
            if (employe.getMotDePasse().equals(motDePasse)) {
                // Vérifier que l'employé est actif
                if (employe.getStatut() == Employe.StatutEmploye.ACTIF) {
                    return Optional.of(employe);
                } else {
                    throw new RuntimeException("Ce compte employé est " + employe.getStatut().toString().toLowerCase() + ". Contactez l'administration.");
                }
            } else {
                throw new RuntimeException("Mot de passe incorrect.");
            }
        } else {
            throw new RuntimeException("Aucun compte employé trouvé avec cet email.");
        }
    }

    // Changer le statut d'un employé
    public void changerStatutEmploye(Long id, Employe.StatutEmploye nouveauStatut) {
        Optional<Employe> employeOpt = employeRepository.findById(id);
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();
            employe.setStatut(nouveauStatut);
            employeRepository.save(employe);
        } else {
            throw new RuntimeException("Employé avec l'ID " + id + " non trouvé !");
        }
    }

    // Changer le mot de passe d'un employé
    public void changerMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse) {
        Optional<Employe> employeOpt = employeRepository.findById(id);
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();
            
            // Vérifier l'ancien mot de passe
            if (!employe.getMotDePasse().equals(ancienMotDePasse)) {
                throw new RuntimeException("Ancien mot de passe incorrect !");
            }
            
            // Mettre à jour avec le nouveau mot de passe
            employe.setMotDePasse(nouveauMotDePasse);
            employeRepository.save(employe);
        } else {
            throw new RuntimeException("Employé avec l'ID " + id + " non trouvé !");
        }
    }

    // Statistiques
    public long getNombreEmployes() {
        return employeRepository.count();
    }

    public long getNombreEmployesActifs() {
        return employeRepository.countByStatut(Employe.StatutEmploye.ACTIF);
    }

    public long getNombreEmployesParStatut(Employe.StatutEmploye statut) {
        return employeRepository.countByStatut(statut);
    }

    public long getNombreEmployesParRole(Employe.RoleEmploye role) {
        return employeRepository.countByRole(role);
    }

    // Recherche d'employés
    public List<Employe> rechercherEmployes(String terme) {
        return employeRepository.findByNomContainingOrPrenomContainingOrEmailContaining(
            terme, terme, terme);
    }

    // Vérifier si un employé existe
    public boolean employeExists(String email) {
        return employeRepository.existsByEmail(email);
    }

    // Vérifier si un employé est actif
    public boolean isEmployeActif(Long id) {
        Optional<Employe> employe = employeRepository.findById(id);
        return employe.map(Employe::isActif).orElse(false);
    }

    // Méthodes pour la compatibilité avec le controller existant
    public Employe saveEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    // Gestion des horaires
    public void ajouterHoraire(Employe employe, String horaire) {
        employe.addHoraire(horaire);
    }

    public void supprimerHoraire(Employe employe, String horaire) {
        employe.removeHoraire(horaire);
    }

    // Gestion des congés
    public void demanderConge(Employe employe, String dateDebut, String dateFin) {
        String conge = "Du " + dateDebut + " au " + dateFin;
        employe.addConge(conge);
    }

    public void supprimerConge(Employe employe, String conge) {
        employe.removeConge(conge);
    }

    // Gestion des formations
    public void suivreFormation(Employe employe, String formation) {
        employe.addFormation(formation);
    }

    public void supprimerFormation(Employe employe, String formation) {
        employe.removeFormation(formation);
    }
}
