package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestionClientService {

    private final ClientRepository clientRepository;

    public GestionClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id " + id));

        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setEmail(clientDetails.getEmail());
        client.setMotDePasse(clientDetails.getMotDePasse());

        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id " + id));
        clientRepository.delete(client);
    }
}

