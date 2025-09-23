package com.example.demo.controller;

import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    // Page d'accueil
    @GetMapping("/")
    public String accueil() {
        return "index"; // Redirige vers templates/index.html
    }

    // Afficher le formulaire d'inscription
    @GetMapping("/inscription")
    public String afficherInscription(Model model) {
        model.addAttribute("client", new Client());
        return "inscription"; //templates/inscription.html
    }

    // Traiter l'inscription
    @PostMapping("/inscription")
    public String inscrireClient(@ModelAttribute Client client, Model model) {
        // Vérifier si l'email existe déjà
        Optional<Client> clientExistant = clientRepository.findByEmail(client.getEmail());
        if (clientExistant.isPresent()) {
            model.addAttribute("erreur", "Cet email est déjà utilisé !");
            return "inscription";
        }

        // Sauvegarder le nouveau client
        clientRepository.save(client);
        model.addAttribute("succes", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
        return "connexion";
    }

    // Liste de tous les clients (pour test)
    @GetMapping("/clients")
    public String listeClients(Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        return "liste-clients"; // templates/liste-clients.html
    }
    
}
