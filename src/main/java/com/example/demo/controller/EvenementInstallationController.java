package com.example.demo.controller;

import com.example.demo.entity.Evenement;
import com.example.demo.entity.Installation;
import com.example.demo.entity.Client;
import com.example.demo.service.EvenementService;
import com.example.demo.service.InstallationService;
import com.example.demo.repository.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestionnaire/evenements-installations")
public class EvenementInstallationController {

    private final EvenementService evenementService;
    private final InstallationService installationService;
    private final ClientRepository clientRepository;

    public EvenementInstallationController(EvenementService evenementService,
                                           InstallationService installationService,
                                           ClientRepository clientRepository) {
        this.evenementService = evenementService;
        this.installationService = installationService;
        this.clientRepository = clientRepository;
    }

    // =================== ÉVÉNEMENTS ===================

    @GetMapping("/evenements")
    public List<Evenement> getAllEvenements() {
        return evenementService.getAllEvenements();
    }

    @GetMapping("/evenements/{id}")
    public ResponseEntity<Evenement> getEvenementById(@PathVariable Long id) {
        return evenementService.getEvenementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/evenements")
    public ResponseEntity<Evenement> createEvenement(@RequestBody Evenement evenement) {
        return ResponseEntity.ok(evenementService.createEvenement(evenement));
    }

    @PutMapping("/evenements/{id}")
    public ResponseEntity<Evenement> updateEvenement(@PathVariable Long id, @RequestBody Evenement evenementDetails) {
        try {
            Evenement updated = evenementService.updateEvenement(id, evenementDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/evenements/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        try {
            evenementService.deleteEvenement(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/evenements/{id}/participants/{clientId}")
    public ResponseEntity<Evenement> addParticipant(@PathVariable Long id, @PathVariable Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.notFound().build();

        try {
            Evenement updated = evenementService.getEvenementById(id)
                    .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
            updated.getParticipants().add(clientOpt.get());
            return ResponseEntity.ok(evenementService.updateEvenement(id, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // =================== INSTALLATIONS ===================

    @GetMapping("/installations")
    public List<Installation> getAllInstallations() {
        return installationService.getAllInstallations();
    }

    @GetMapping("/installations/{id}")
    public ResponseEntity<Installation> getInstallationById(@PathVariable Long id) {
        return installationService.getInstallationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/installations")
    public ResponseEntity<Installation> createInstallation(@RequestBody Installation installation) {
        return ResponseEntity.ok(installationService.createInstallation(installation));
    }

    @PutMapping("/installations/{id}")
    public ResponseEntity<Installation> updateInstallation(@PathVariable Long id, @RequestBody Installation installationDetails) {
        try {
            Installation updated = installationService.updateInstallation(id, installationDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/installations/{id}")
    public ResponseEntity<Void> deleteInstallation(@PathVariable Long id) {
        try {
            installationService.deleteInstallation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =================== LIAISON ÉVÉNEMENT / INSTALLATION ===================

    @PostMapping("/evenements/{evenementId}/installation/{installationId}")
    public ResponseEntity<Void> lierInstallation(@PathVariable Long evenementId,
                                                 @PathVariable Long installationId) {
        try {
            evenementService.lierInstallation(evenementId, installationId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/evenements/{evenementId}/installation")
    public ResponseEntity<Void> delierInstallation(@PathVariable Long evenementId) {
        try {
            evenementService.delierInstallation(evenementId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
