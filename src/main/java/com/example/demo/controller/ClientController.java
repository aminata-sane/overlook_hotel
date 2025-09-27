package com.example.demo.controller;

import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Page d'accueil déplacée vers AccueilController
    // Ancienne méthode accueil() supprimée pour éviter les conflits de mapping

    // Page d'administration (ancienne page d'accueil)
    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {
        // Passer les informations de session au template
        Client clientConnecte = (Client) session.getAttribute("clientConnecte");
        if (clientConnecte != null) {
            model.addAttribute("clientConnecte", clientConnecte);
        }
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
        try {
            clientService.createClient(client);
            model.addAttribute("succes", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "connexion";
        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage());
            return "inscription";
        } catch (Exception e) {
            model.addAttribute("erreur", "Erreur lors de l'inscription : " + e.getMessage());
            return "inscription";
        }
    }

    // Liste de tous les clients (pour test)
    @GetMapping("/clients")
    public String listeClients(Model model) {
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        model.addAttribute("nombreClients", clientService.getNombreClients());
        model.addAttribute("nombreActifs", clientService.getNombreClientsActifs());
        return "liste-clients"; // templates/liste-clients.html
    }
    
    // Afficher la page de connexion
    @GetMapping("/connexion")
    public String afficherConnexion() {
        return "connexion"; // templates/connexion.html
    }
    
    // Traiter la connexion
    @PostMapping("/connexion")
    public String traiterConnexion(@RequestParam String email, 
                                  @RequestParam String motDePasse, 
                                  Model model, 
                                  HttpSession session) {
        try {
            Optional<Client> clientOpt = clientService.authenticate(email, motDePasse);
            
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                
                // Connexion réussie - sauvegarder dans la session
                session.setAttribute("clientConnecte", client);
                session.setAttribute("nomClient", client.getPrenom() + " " + client.getNom());
                
                model.addAttribute("succes", "Connexion réussie ! Bienvenue " + client.getPrenom() + " !");
                return "index"; // Redirection vers l'accueil
            } else {
                model.addAttribute("erreur", "Erreur de connexion.");
                return "connexion";
            }
        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage());
            return "connexion";
        } catch (Exception e) {
            model.addAttribute("erreur", "Erreur lors de la connexion : " + e.getMessage());
            return "connexion";
        }
    }
    
    // Déconnexion
    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session, Model model) {
        session.invalidate(); // Supprimer toute la session
        model.addAttribute("succes", "Vous êtes déconnecté avec succès.");
        return "index";
    }
    
}
