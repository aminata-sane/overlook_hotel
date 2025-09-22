package com.example.demo.controller;

import com.example.demo.entity.Fidelite;
import com.example.demo.entity.Client;
import com.example.demo.service.FideliteService;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/client/fidelite")
public class FideliteController {

    @Autowired
    private FideliteService fideliteService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/{clientId}")
    public ResponseEntity<Fidelite> getFidelite(@PathVariable Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Fidelite> fidelite = fideliteService.getFideliteByClient(clientOpt.get());
        return fidelite.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{clientId}/ajouter")
    public ResponseEntity<Fidelite> ajouterPoints(@PathVariable Long clientId, @RequestParam int points) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        Fidelite fidelite = fideliteService.ajouterPoints(client, points);
        return ResponseEntity.ok(fidelite);
    }

    @PostMapping("/{clientId}/utiliser")
    public ResponseEntity<Fidelite> utiliserPoints(@PathVariable Long clientId, @RequestParam int points) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        Fidelite fidelite = fideliteService.utiliserPoints(client, points);
        return ResponseEntity.ok(fidelite);
    }
}
