package com.example.demo.service;

import com.example.demo.entity.Installation;
import com.example.demo.repository.InstallationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstallationService {

    private final InstallationRepository installationRepository;

    public InstallationService(InstallationRepository installationRepository) {
        this.installationRepository = installationRepository;
    }

    // Retourne toutes les installations
    public List<Installation> getAllInstallations() {
        return installationRepository.findAll();
    }

    // Retourne une installation par ID
    public Optional<Installation> getInstallationById(Long id) {
        return installationRepository.findById(id);
    }

    // Crée une nouvelle installation
    public Installation createInstallation(Installation installation) {
        return installationRepository.save(installation);
    }

    // Met à jour une installation
    public Installation updateInstallation(Long id, Installation installationDetails) {
        Installation installation = installationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Installation non trouvée avec l'id " + id));

        installation.setNom(installationDetails.getNom());
        installation.setType(installationDetails.getType());
        installation.setCapacite(installationDetails.getCapacite());

        return installationRepository.save(installation);
    }

    // Supprime une installation
    public void deleteInstallation(Long id) {
        Installation installation = installationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Installation non trouvée avec l'id " + id));
        installationRepository.delete(installation);
    }
}

