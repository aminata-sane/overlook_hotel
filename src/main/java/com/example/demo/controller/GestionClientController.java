package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.entity.Employe;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestionnaire")
public class GestionClientController {

    @Autowired
    private ClientRepository clientRepository;

    // clients
    @GetMapping("/clients")
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/clients")
    public Client createClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (!clientOptional.isPresent()) return ResponseEntity.notFound().build();

        Client client = clientOptional.get();
        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setEmail(clientDetails.getEmail());
        client.setMotDePasse(clientDetails.getMotDePasse());

        return ResponseEntity.ok(clientRepository.save(client));
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (!clientRepository.existsById(id)) return ResponseEntity.notFound().build();
        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
