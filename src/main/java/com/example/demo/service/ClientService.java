package com.example.demo.service;

import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Récupérer tous les clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Récupérer un client par ID
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    // Récupérer un client par email
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    // Récupérer les clients par statut
    public List<Client> getClientsByStatut(Client.StatutClient statut) {
        return clientRepository.findByStatut(statut);
    }

    // Récupérer les clients actifs
    public List<Client> getClientsActifs() {
        return clientRepository.findByStatut(Client.StatutClient.ACTIF);
    }

    // Récupérer les clients par nom
    public List<Client> getClientsByNom(String nom) {
        return clientRepository.findByNomContaining(nom);
    }

    // Créer un nouveau client (inscription)
    public Client createClient(Client client) {
        // Vérifier si l'email existe déjà
        Optional<Client> clientExistant = clientRepository.findByEmail(client.getEmail());
        if (clientExistant.isPresent()) {
            throw new RuntimeException("Un client avec l'email " + client.getEmail() + " existe déjà !");
        }

        // Définir le statut par défaut si non spécifié
        if (client.getStatut() == null) {
            client.setStatut(Client.StatutClient.ACTIF);
        }

        return clientRepository.save(client);
    }

    // Mettre à jour un client
    public Client updateClient(Long id, Client clientDetails) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();

            // Vérifier si le nouvel email n'est pas déjà utilisé par un autre client
            if (!client.getEmail().equals(clientDetails.getEmail())) {
                Optional<Client> existant = clientRepository.findByEmail(clientDetails.getEmail());
                if (existant.isPresent()) {
                    throw new RuntimeException("L'email " + clientDetails.getEmail() + " est déjà utilisé !");
                }
            }

            // Mettre à jour les champs
            client.setNom(clientDetails.getNom());
            client.setPrenom(clientDetails.getPrenom());
            client.setEmail(clientDetails.getEmail());
            client.setTelephone(clientDetails.getTelephone());
            client.setDateNaissance(clientDetails.getDateNaissance());
            
            // Ne pas mettre à jour le mot de passe s'il est vide
            if (clientDetails.getMotDePasse() != null && !clientDetails.getMotDePasse().trim().isEmpty()) {
                client.setMotDePasse(clientDetails.getMotDePasse());
            }
            
            if (clientDetails.getStatut() != null) {
                client.setStatut(clientDetails.getStatut());
            }

            return clientRepository.save(client);
        } else {
            throw new RuntimeException("Client avec l'ID " + id + " non trouvé !");
        }
    }

    // Supprimer un client
    public void deleteClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            clientRepository.deleteById(id);
        } else {
            throw new RuntimeException("Client avec l'ID " + id + " non trouvé !");
        }
    }

    // Authentification - Vérifier les identifiants
    public Optional<Client> authenticate(String email, String motDePasse) {
        Optional<Client> clientOpt = clientRepository.findByEmail(email);
        
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            
            // Vérifier le mot de passe (simple comparaison pour l'instant)
            if (client.getMotDePasse().equals(motDePasse)) {
                // Vérifier que le client est actif
                if (client.getStatut() == Client.StatutClient.ACTIF) {
                    return Optional.of(client);
                } else {
                    throw new RuntimeException("Ce compte est " + client.getStatut().toString().toLowerCase() + ". Contactez l'administration.");
                }
            } else {
                throw new RuntimeException("Mot de passe incorrect.");
            }
        } else {
            throw new RuntimeException("Aucun compte trouvé avec cet email.");
        }
    }

    // Changer le statut d'un client
    public void changerStatutClient(Long id, Client.StatutClient nouveauStatut) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setStatut(nouveauStatut);
            clientRepository.save(client);
        } else {
            throw new RuntimeException("Client avec l'ID " + id + " non trouvé !");
        }
    }

    // Activer un client
    public void activerClient(Long id) {
        changerStatutClient(id, Client.StatutClient.ACTIF);
    }

    // Suspendre un client
    public void suspendreClient(Long id) {
        changerStatutClient(id, Client.StatutClient.SUSPENDU);
    }

    // Désactiver un client
    public void desactiverClient(Long id) {
        changerStatutClient(id, Client.StatutClient.INACTIF);
    }

    // Changer le mot de passe d'un client
    public void changerMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            
            // Vérifier l'ancien mot de passe
            if (!client.getMotDePasse().equals(ancienMotDePasse)) {
                throw new RuntimeException("Ancien mot de passe incorrect !");
            }
            
            // Mettre à jour avec le nouveau mot de passe
            client.setMotDePasse(nouveauMotDePasse);
            clientRepository.save(client);
        } else {
            throw new RuntimeException("Client avec l'ID " + id + " non trouvé !");
        }
    }

    // Statistiques
    public long getNombreClients() {
        return clientRepository.count();
    }

    public long getNombreClientsActifs() {
        return clientRepository.countByStatut(Client.StatutClient.ACTIF);
    }

    public long getNombreClientsParStatut(Client.StatutClient statut) {
        return clientRepository.countByStatut(statut);
    }

    // Vérifier si un client existe
    public boolean clientExists(String email) {
        return clientRepository.findByEmail(email).isPresent();
    }

    // Vérifier si un client est actif
    public boolean isClientActif(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(c -> c.getStatut() == Client.StatutClient.ACTIF).orElse(false);
    }

    // Recherche de clients (pour une fonction de recherche)
    public List<Client> rechercherClients(String terme) {
        // Recherche par nom, prénom ou email
        return clientRepository.findByNomContainingOrPrenomContainingOrEmailContaining(
            terme, terme, terme);
    }
}
