package com.example.demo.controller;

import com.example.demo.entity.Evenement;
import com.example.demo.entity.Client;
import com.example.demo.repository.EvenementRepository;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestionnaire/evenements")
public class EvenementController {

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private ClientRepository clientRepository;

    
    @GetMapping
    public List<Evenement> getAllEvenements() {
        return evenementRepository.findAll();
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<Evenement> getEvenementById(@PathVariable Long id) {
        Optional<Evenement> evenement = evenementRepository.findById(id);
        return evenement.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    
    @PostMapping
    public Evenement createEvenement(@RequestBody Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Evenement> updateEvenement(@PathVariable Long id, @RequestBody Evenement evenementDetails) {
        Optional<Evenement> evenementOptional = evenementRepository.findById(id);
        if (!evenementOptional.isPresent()) return ResponseEntity.notFound().build();

        Evenement evenement = evenementOptional.get();
        evenement.setTitre(evenementDetails.getTitre());
        evenement.setDescription(evenementDetails.getDescription());
        evenement.setDateDebut(evenementDetails.getDateDebut());
        evenement.setDateFin(evenementDetails.getDateFin());
        evenement.setParticipants(evenementDetails.getParticipants());

        return ResponseEntity.ok(evenementRepository.save(evenement));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        if (!evenementRepository.existsById(id)) return ResponseEntity.notFound().build();
        evenementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    
    @PostMapping("/{id}/participants/{clientId}")
    public ResponseEntity<Evenement> addParticipant(@PathVariable Long id, @PathVariable Long clientId) {
        Optional<Evenement> evenementOptional = evenementRepository.findById(id);
        Optional<Client> clientOptional = clientRepository.findById(clientId);

        if (!evenementOptional.isPresent() || !clientOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Evenement evenement = evenementOptional.get();
        Client client = clientOptional.get();
        List<Client> participants = evenement.getParticipants();
        if (!participants.contains(client)) {
            participants.add(client);
        }
        evenement.setParticipants(participants);

        return ResponseEntity.ok(evenementRepository.save(evenement));
    }
}
