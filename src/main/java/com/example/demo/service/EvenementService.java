package com.example.demo.service;

import com.example.demo.model.Evenement;
import com.example.demo.model.Client;
import com.example.demo.model.Installation;
import com.example.demo.repository.EvenementRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.InstallationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstallationRepository installationRepository;

    // CRUD de base
    public List<Evenement> getAllEvenements() {
        return evenementRepository.findAll();
    }

    public Optional<Evenement> getEvenementById(Long id) {
        return evenementRepository.findById(id);
    }

    public Evenement createEvenement(Evenement evenement) {
        // Validation des dates
        validateDates(evenement);
        
        // Vérification de la disponibilité de l'installation
        if (evenement.getInstallation() != null) {
            validateInstallationDisponible(evenement);
        }

        return evenementRepository.save(evenement);
    }

    public Evenement updateEvenement(Long id, Evenement evenementDetails) {
        return evenementRepository.findById(id)
                .map(evenement -> {
                    // Validation des dates
                    validateDates(evenementDetails);
                    
                    evenement.setTitre(evenementDetails.getTitre());
                    evenement.setDescription(evenementDetails.getDescription());
                    evenement.setDateDebut(evenementDetails.getDateDebut());
                    evenement.setDateFin(evenementDetails.getDateFin());
                    evenement.setMaxParticipants(evenementDetails.getMaxParticipants());
                    evenement.setStatut(evenementDetails.getStatut());
                    evenement.setInstallation(evenementDetails.getInstallation());
                    
                    return evenementRepository.save(evenement);
                })
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID : " + id));
    }

    public void deleteEvenement(Long id) {
        if (!evenementRepository.existsById(id)) {
            throw new RuntimeException("Événement non trouvé avec l'ID : " + id);
        }
        evenementRepository.deleteById(id);
    }

    // Gestion des participants
    public Evenement ajouterParticipant(Long evenementId, Long clientId) {
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        evenement.addParticipant(client);
        return evenementRepository.save(evenement);
    }

    public Evenement retirerParticipant(Long evenementId, Long clientId) {
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        evenement.removeParticipant(client);
        return evenementRepository.save(evenement);
    }

    // Méthodes de recherche et filtrage
    public List<Evenement> getEvenementsAVenir() {
        return evenementRepository.findByDateDebutGreaterThanEqualOrderByDateDebut(LocalDate.now());
    }

    public List<Evenement> getEvenementsPasses() {
        return evenementRepository.findByDateFinLessThanOrderByDateFinDesc(LocalDate.now());
    }

    public List<Evenement> getEvenementsEnCours() {
        return evenementRepository.findEvenementsenCours(LocalDate.now());
    }

    public List<Evenement> getEvenementsByStatut(Evenement.StatutEvenement statut) {
        return evenementRepository.findByStatut(statut);
    }

    public List<Evenement> getEvenementsByInstallation(Long installationId) {
        Installation installation = installationRepository.findById(installationId)
                .orElseThrow(() -> new RuntimeException("Installation non trouvée"));
        return evenementRepository.findByInstallation(installation);
    }

    public List<Evenement> getEvenementsByClient(Long clientId) {
        return evenementRepository.findEvenementsByClientId(clientId);
    }

    public List<Evenement> rechercherEvenements(String terme) {
        return evenementRepository.rechercherEvenements(terme);
    }

    public List<Evenement> getEvenementsAujourdhui() {
        return evenementRepository.findByDateDebutEqualsOrderByTitre(LocalDate.now());
    }

    public List<Evenement> getEvenementsSemaine() {
        LocalDate debut = LocalDate.now();
        LocalDate fin = debut.plusDays(7);
        return evenementRepository.findEvenementsSemaine(debut, fin);
    }

    // Gestion du statut
    public Evenement changerStatut(Long id, Evenement.StatutEvenement nouveauStatut) {
        return evenementRepository.findById(id)
                .map(evenement -> {
                    evenement.setStatut(nouveauStatut);
                    return evenementRepository.save(evenement);
                })
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID : " + id));
    }

    // Statistiques
    public long getNombreEvenements() {
        return evenementRepository.count();
    }

    public long getNombreEvenementsParStatut(Evenement.StatutEvenement statut) {
        return evenementRepository.countByStatut(statut);
    }

    // Méthodes de validation privées
    private void validateDates(Evenement evenement) {
        if (evenement.getDateDebut() == null) {
            throw new RuntimeException("La date de début est obligatoire");
        }
        
        if (evenement.getDateFin() != null && evenement.getDateFin().isBefore(evenement.getDateDebut())) {
            throw new RuntimeException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }

    private void validateInstallationDisponible(Evenement evenement) {
        Installation installation = evenement.getInstallation();
        if (!installation.estDisponible()) {
            throw new RuntimeException("L'installation " + installation.getNom() + " n'est pas disponible");
        }

        // Vérifier les conflits de dates avec d'autres événements
        List<Evenement> evenementsConflits = evenementRepository.findEvenementsByPeriode(
                evenement.getDateDebut(), 
                evenement.getDateFin() != null ? evenement.getDateFin() : evenement.getDateDebut()
        );

        for (Evenement autre : evenementsConflits) {
            if (autre.getInstallation() != null && 
                autre.getInstallation().getId().equals(installation.getId()) &&
                !autre.getId().equals(evenement.getId())) {
                throw new RuntimeException("L'installation " + installation.getNom() + 
                    " est déjà réservée pour cette période par l'événement : " + autre.getTitre());
            }
        }
    }
}
