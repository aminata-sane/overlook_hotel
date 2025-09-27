package com.example.demo.service;

import com.example.demo.model.Gestionnaire;
import com.example.demo.repository.GestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GestionnaireService {

    @Autowired
    private GestionnaireRepository gestionnaireRepository;

    // Créer un nouveau gestionnaire
    public Gestionnaire creerGestionnaire(Gestionnaire gestionnaire) {
        // Vérifier que l'email n'existe pas déjà
        if (gestionnaireRepository.existsByEmail(gestionnaire.getEmail())) {
            throw new IllegalArgumentException("Un gestionnaire avec cet email existe déjà");
        }
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
}
