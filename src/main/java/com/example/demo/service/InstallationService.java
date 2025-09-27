package com.example.demo.service;

import com.example.demo.model.Installation;
import com.example.demo.repository.InstallationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InstallationService {

    @Autowired
    private InstallationRepository installationRepository;

    // CRUD de base
    public List<Installation> getAllInstallations() {
        return installationRepository.findAll();
    }

    public Optional<Installation> getInstallationById(Long id) {
        return installationRepository.findById(id);
    }

    public Installation createInstallation(Installation installation) {
        return installationRepository.save(installation);
    }

    public Installation updateInstallation(Long id, Installation installationDetails) {
        return installationRepository.findById(id)
                .map(installation -> {
                    installation.setNom(installationDetails.getNom());
                    installation.setDescription(installationDetails.getDescription());
                    installation.setCapacite(installationDetails.getCapacite());
                    installation.setType(installationDetails.getType());
                    installation.setDisponible(installationDetails.getDisponible());
                    
                    return installationRepository.save(installation);
                })
                .orElseThrow(() -> new RuntimeException("Installation non trouvée avec l'ID : " + id));
    }

    public void deleteInstallation(Long id) {
        if (!installationRepository.existsById(id)) {
            throw new RuntimeException("Installation non trouvée avec l'ID : " + id);
        }
        installationRepository.deleteById(id);
    }

    // Méthodes de recherche et filtrage
    public List<Installation> getInstallationsDisponibles() {
        return installationRepository.findByDisponibleTrue();
    }

    public List<Installation> getInstallationsByType(Installation.TypeInstallation type) {
        return installationRepository.findByType(type);
    }

    public List<Installation> getInstallationsDisponiblesByType(Installation.TypeInstallation type) {
        return installationRepository.findByTypeAndDisponibleTrue(type);
    }

    public List<Installation> getInstallationsByCapaciteMin(Integer capaciteMin) {
        return installationRepository.findByCapaciteGreaterThanEqual(capaciteMin);
    }

    public List<Installation> rechercherInstallations(String terme) {
        return installationRepository.rechercherInstallations(terme);
    }

    // Gestion de la disponibilité
    public Installation changerDisponibilite(Long id, Boolean disponible) {
        return installationRepository.findById(id)
                .map(installation -> {
                    installation.setDisponible(disponible);
                    return installationRepository.save(installation);
                })
                .orElseThrow(() -> new RuntimeException("Installation non trouvée avec l'ID : " + id));
    }

    // Statistiques
    public long getNombreInstallations() {
        return installationRepository.count();
    }

    public long getNombreInstallationsParType(Installation.TypeInstallation type) {
        return installationRepository.countByType(type);
    }

    public long getNombreInstallationsDisponibles() {
        return installationRepository.findByDisponibleTrue().size();
    }
}
