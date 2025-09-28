package com.example.demo.service;

import com.example.demo.model.Employe;
import com.example.demo.model.Gestionnaire;
import com.example.demo.repository.EmployeRepository;
import com.example.demo.repository.GestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GestionnaireService {

    @Autowired
    private GestionnaireRepository gestionnaireRepository;
    
    @Autowired
    private EmployeRepository employeRepository;

    // Créer un nouveau gestionnaire
    public Gestionnaire creerGestionnaire(Gestionnaire gestionnaire) {
        if (gestionnaire == null) {
            throw new IllegalArgumentException("Le gestionnaire ne peut pas être null");
        }
        
        if (gestionnaire.getEmail() == null || gestionnaire.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email du gestionnaire est obligatoire");
        }
        
        // Vérifier que l'email n'existe pas déjà
        if (gestionnaireRepository.existsByEmail(gestionnaire.getEmail().trim())) {
            throw new IllegalArgumentException("Un gestionnaire avec cet email existe déjà");
        }
        
        // Normaliser l'email
        gestionnaire.setEmail(gestionnaire.getEmail().trim().toLowerCase());
        
        return gestionnaireRepository.save(gestionnaire);
    }

    // Mettre à jour un gestionnaire
    public Gestionnaire mettreAJourGestionnaire(Gestionnaire gestionnaire) {
        if (gestionnaire.getId() == null) {
            throw new IllegalArgumentException("L'ID du gestionnaire ne peut pas être null pour une mise à jour");
        }
        
        // Vérifier que le gestionnaire existe
        if (!gestionnaireRepository.existsById(gestionnaire.getId())) {
            throw new IllegalArgumentException("Gestionnaire non trouvé avec l'ID: " + gestionnaire.getId());
        }
        
        return gestionnaireRepository.save(gestionnaire);
    }

    // Supprimer un gestionnaire (désactivation logique)
    public void supprimerGestionnaire(Long id) {
        Optional<Gestionnaire> gestionnaireOpt = gestionnaireRepository.findById(id);
        if (gestionnaireOpt.isPresent()) {
            Gestionnaire gestionnaire = gestionnaireOpt.get();
            gestionnaire.setActif(false);
            gestionnaireRepository.save(gestionnaire);
        } else {
            throw new IllegalArgumentException("Gestionnaire non trouvé avec l'ID: " + id);
        }
    }

    // Supprimer définitivement un gestionnaire
    public void supprimerDefinitivementGestionnaire(Long id) {
        if (!gestionnaireRepository.existsById(id)) {
            throw new IllegalArgumentException("Gestionnaire non trouvé avec l'ID: " + id);
        }
        gestionnaireRepository.deleteById(id);
    }

    // Récupérer tous les gestionnaires
    @Transactional(readOnly = true)
    public List<Gestionnaire> obtenirTousLesGestionnaires() {
        return gestionnaireRepository.findAll();
    }

    // Récupérer tous les gestionnaires actifs
    @Transactional(readOnly = true)
    public List<Gestionnaire> obtenirGestionnairesActifs() {
        return gestionnaireRepository.findByActifTrue();
    }

    // Récupérer un gestionnaire par ID
    @Transactional(readOnly = true)
    public Optional<Gestionnaire> obtenirGestionnaireParId(Long id) {
        return gestionnaireRepository.findById(id);
    }

    // Récupérer un gestionnaire par email
    @Transactional(readOnly = true)
    public Optional<Gestionnaire> obtenirGestionnaireParEmail(String email) {
        return gestionnaireRepository.findByEmail(email);
    }

    // Rechercher des gestionnaires par nom/prénom
    @Transactional(readOnly = true)
    public List<Gestionnaire> rechercherGestionnaires(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return obtenirGestionnairesActifs();
        }
        return gestionnaireRepository.findByNomOrPrenomContainingIgnoreCase(searchTerm.trim());
    }

    // Compter le nombre de gestionnaires actifs
    @Transactional(readOnly = true)
    public long compterGestionnairesActifs() {
        return gestionnaireRepository.countByActifTrue();
    }

    // Obtenir les gestionnaires classés par nombre de chambres
    @Transactional(readOnly = true)
    public List<Gestionnaire> obtenirGestionnairesParNombreChambres() {
        return gestionnaireRepository.findGestionnairesOrderByNombreChambresDesc();
    }

    // Obtenir les gestionnaires classés par nombre de feedbacks
    @Transactional(readOnly = true)
    public List<Gestionnaire> obtenirGestionnairesParNombreFeedbacks() {
        return gestionnaireRepository.findGestionnairesOrderByNombreFeedbacksDesc();
    }

    // Obtenir les gestionnaires sans chambres assignées
    @Transactional(readOnly = true)
    public List<Gestionnaire> obtenirGestionnairesSansChambres() {
        return gestionnaireRepository.findGestionnairesSansChambres();
    }

    // Réactiver un gestionnaire
    public void reactiverGestionnaire(Long id) {
        Optional<Gestionnaire> gestionnaireOpt = gestionnaireRepository.findById(id);
        if (gestionnaireOpt.isPresent()) {
            Gestionnaire gestionnaire = gestionnaireOpt.get();
            gestionnaire.setActif(true);
            gestionnaireRepository.save(gestionnaire);
        } else {
            throw new IllegalArgumentException("Gestionnaire non trouvé avec l'ID: " + id);
        }
    }

    // Vérifier si un email existe déjà
    @Transactional(readOnly = true)
    public boolean emailExiste(String email) {
        return gestionnaireRepository.existsByEmail(email);
    }

    // Vérifier si un email existe pour un autre gestionnaire (utile pour les mises à jour)
    @Transactional(readOnly = true)
    public boolean emailExistePourAutreGestionnaire(String email, Long gestionnaireId) {
        Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmail(email);
        return gestionnaire.isPresent() && !gestionnaire.get().getId().equals(gestionnaireId);
    }

    // Nouvelles méthodes pour la gestion des employés
    @Transactional(readOnly = true)
    public List<Employe> obtenirTousLesEmployes() {
        return employeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Employe> obtenirEmployesActifs() {
        return employeRepository.findByStatut(Employe.StatutEmploye.ACTIF);
    }

    @Transactional(readOnly = true)
    public List<Employe> obtenirEmployesEnConge() {
        return employeRepository.findByStatut(Employe.StatutEmploye.EN_CONGE);
    }

    @Transactional(readOnly = true)
    public Map<Employe.RoleEmploye, Long> obtenirRepartitionParRole() {
        List<Employe> employes = employeRepository.findAll();
        return employes.stream()
                .collect(Collectors.groupingBy(
                    Employe::getRole,
                    Collectors.counting()
                ));
    }

    @Transactional(readOnly = true)
    public Map<Employe.StatutEmploye, Long> obtenirRepartitionParStatut() {
        List<Employe> employes = employeRepository.findAll();
        return employes.stream()
                .collect(Collectors.groupingBy(
                    Employe::getStatut,
                    Collectors.counting()
                ));
    }

    @Transactional(readOnly = true)
    public List<Employe> obtenirEmployesRecents(int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("La limite doit être supérieure à 0");
        }
        return employeRepository.findTop10ByOrderByDateEmbaucheDesc().stream()
                .limit(limite)
                .collect(Collectors.toList());
    }

    // Créer un nouveau employé (depuis l'interface gestionnaire)
    public Employe creerEmploye(Employe employe) {
        if (employe == null) {
            throw new IllegalArgumentException("L'employé ne peut pas être null");
        }
        
        if (employe.getEmail() == null || employe.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email de l'employé est obligatoire");
        }
        
        // Normaliser l'email
        employe.setEmail(employe.getEmail().trim().toLowerCase());
        
        return employeRepository.save(employe);
    }

    // Obtenir les statistiques générales pour le dashboard gestionnaire
    @Transactional(readOnly = true)
    public Map<String, Object> obtenirStatistiquesGenerales() {
        Map<String, Object> stats = new java.util.HashMap<>();
        
        stats.put("totalGestionnaires", compterGestionnairesActifs());
        stats.put("totalEmployes", employeRepository.countByStatut(Employe.StatutEmploye.ACTIF));
        stats.put("employesEnConge", employeRepository.countByStatut(Employe.StatutEmploye.EN_CONGE));
        stats.put("repartitionRoles", obtenirRepartitionParRole());
        stats.put("repartitionStatuts", obtenirRepartitionParStatut());
        
        return stats;
    }

    // Vérifier si un gestionnaire peut être supprimé (pas de chambres assignées)
    @Transactional(readOnly = true)
    public boolean peutEtreSupprime(Long gestionnaireId) {
        Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findById(gestionnaireId);
        if (gestionnaire.isPresent()) {
            // Vérifier s'il a des chambres assignées
            return gestionnaire.get().getChambres() == null || gestionnaire.get().getChambres().isEmpty();
        }
        return false;
    }
}
