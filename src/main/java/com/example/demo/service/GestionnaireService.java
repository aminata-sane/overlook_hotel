package com.example.demo.service;

import com.example.demo.entity.Gestionnaire;
import com.example.demo.repository.GestionnaireRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestionnaireService {

    private final GestionnaireRepository gestionnaireRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public List<Gestionnaire> getAllGestionnaires() {
        return gestionnaireRepository.findAll();
    }

    public Optional<Gestionnaire> getGestionnaireById(Long id) {
        return gestionnaireRepository.findById(id);
    }

    public Optional<Gestionnaire> getGestionnaireByEmail(String email) {
        return gestionnaireRepository.findByEmail(email);
    }

    public Gestionnaire createGestionnaire(Gestionnaire gestionnaire) {
        return gestionnaireRepository.save(gestionnaire);
    }

    public Gestionnaire updateGestionnaire(Long id, Gestionnaire gestionnaireDetails) {
        Gestionnaire gestionnaire = gestionnaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestionnaire non trouvé avec l'id " + id));

        gestionnaire.setNom(gestionnaireDetails.getNom());
        gestionnaire.setPrenom(gestionnaireDetails.getPrenom());
        gestionnaire.setEmail(gestionnaireDetails.getEmail());
        gestionnaire.setMotDePasse(gestionnaireDetails.getMotDePasse());

        return gestionnaireRepository.save(gestionnaire);
    }

    public void deleteGestionnaire(Long id) {
        Gestionnaire gestionnaire = gestionnaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestionnaire non trouvé avec l'id " + id));
        gestionnaireRepository.delete(gestionnaire);
    }
}

