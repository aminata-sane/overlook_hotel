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
                return "dashboard-client"; // Redirection vers le dashboard client
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
    
    // Dashboard client (espace personnel)
    @GetMapping("/dashboard-client")
    public String dashboardClient(HttpSession session, Model model) {
        Client clientConnecte = (Client) session.getAttribute("clientConnecte");
        if (clientConnecte == null) {
            model.addAttribute("erreur", "Vous devez être connecté pour accéder à cette page.");
            return "connexion";
        }
        
        model.addAttribute("client", clientConnecte);
        return "dashboard-client";
    }

    // Mes réservations
    @GetMapping("/mes-reservations")
    public String mesReservations(HttpSession session, Model model) {
        Client clientConnecte = (Client) session.getAttribute("clientConnecte");
        if (clientConnecte == null) {
            return "redirect:/connexion";
        }
        
        // TODO: Récupérer les réservations du client
        model.addAttribute("message", "Fonctionnalité en développement - Vos réservations apparaîtront ici prochainement.");
        return "dashboard-client";
    }

    // Mon historique
    @GetMapping("/mon-historique")
    public String monHistorique(HttpSession session, Model model) {
        Client clientConnecte = (Client) session.getAttribute("clientConnecte");
        if (clientConnecte == null) {
            return "redirect:/connexion";
        }
        
        model.addAttribute("message", "Fonctionnalité en développement - Votre historique apparaîtra ici prochainement.");
        return "dashboard-client";
    }

    // Mon profil
    @GetMapping("/mon-profil")
    public String monProfil(HttpSession session, Model model) {
        Client clientConnecte = (Client) session.getAttribute("clientConnecte");
        if (clientConnecte == null) {
            return "redirect:/connexion";
        }
        
        model.addAttribute("message", "Fonctionnalité en développement - Modification du profil bientôt disponible.");
        return "dashboard-client";
    }

    // Services
    @GetMapping("/services")
    public String services(HttpSession session, Model model) {
        Client clientConnecte = (Client) session.getAttribute("clientConnecte");
        if (clientConnecte == null) {
            return "redirect:/connexion";
        }
        
        model.addAttribute("message", "Fonctionnalité en développement - Nos services seront détaillés ici prochainement.");
        return "dashboard-client";
    }

    // Avis
    @GetMapping("/avis")
    public String avis(HttpSession session, Model model) {
        Client clientConnecte = (Client) session.getAttribute("clientConnecte");
        if (clientConnecte == null) {
            return "redirect:/connexion";
        }
        
        model.addAttribute("message", "Fonctionnalité en développement - Système d'avis bientôt disponible.");
        return "dashboard-client";
    }

}
