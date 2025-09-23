package com.example.demo.service;

import com.example.demo.entity.Evenement;
import com.example.demo.entity.Installation;
import com.example.demo.repository.EvenementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final InstallationService installationService;

    public EvenementService(EvenementRepository evenementRepository,
                            InstallationService installationService) {
        this.evenementRepository = evenementRepository;
        this.installationService = installationService;
    }

    // =================== ÉVÉNEMENTS ===================

    public List<Evenement> getAllEvenements() {
        return evenementRepository.findAll();
    }

    public Optional<Evenement> getEvenementById(Long id) {
        return evenementRepository.findById(id);
    }

    public Evenement createEvenement(Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    public Evenement updateEvenement(Long id, Evenement evenementDetails) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id " + id));

        evenement.setTitre(evenementDetails.getTitre());
        evenement.setDescription(evenementDetails.getDescription());
        evenement.setDateDebut(evenementDetails.getDateDebut());
        evenement.setDateFin(evenementDetails.getDateFin());
        evenement.setInstallation(evenementDetails.getInstallation());

        return evenementRepository.save(evenement);
    }

    public void deleteEvenement(Long id) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id " + id));
        evenementRepository.delete(evenement);
    }

    // =================== INSTALLATIONS ===================

    public void lierInstallation(Long evenementId, Long installationId) {
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id " + evenementId));

        Installation installation = installationService.getInstallationById(installationId)
                .orElseThrow(() -> new RuntimeException("Installation non trouvée avec l'id " + installationId));

        evenement.setInstallation(installation);
        evenementRepository.save(evenement);
    }

    public void delierInstallation(Long evenementId) {
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id " + evenementId));

        evenement.setInstallation(null);
        evenementRepository.save(evenement);
    }
}
