package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.service.GestionClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestionnaire/clients")
public class GestionClientController {

    private final GestionClientService clientService;

    public GestionClientController(GestionClientService clientService) {
        this.clientService = clientService;
    }

    // GET /api/gestionnaire/clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    // GET /api/gestionnaire/clients/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/gestionnaire/clients
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    // PUT /api/gestionnaire/clients/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        try {
            Client updatedClient = clientService.updateClient(id, clientDetails);
            return ResponseEntity.ok(updatedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/gestionnaire/clients/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

