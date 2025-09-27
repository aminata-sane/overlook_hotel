package com.example.demo.controller;

import com.example.demo.model.Chambre;
import com.example.demo.model.Client;
import com.example.demo.model.Reservation;
import com.example.demo.model.Panier;
import com.example.demo.model.PanierItem;
import com.example.demo.service.ChambreService;
import com.example.demo.service.ClientService;
import com.example.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class ClientReservationController {

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientService clientService;

    // Afficher le formulaire de réservation pour une chambre spécifique
    @GetMapping("/reservation/chambre/{id}")
    public String afficherFormulaireReservation(@PathVariable Long id, Model model, HttpSession session) {
        // Récupérer la chambre
        Optional<Chambre> chambreOpt = chambreService.getChambreById(id);
        if (chambreOpt.isEmpty()) {
            model.addAttribute("erreur", "Chambre non trouvée.");
            return "redirect:/";
        }

        Chambre chambre = chambreOpt.get();
        model.addAttribute("chambre", chambre);

        // Vérifier si le client est connecté
        Client clientConnecte = (Client) session.getAttribute("clientConnecte");
        if (clientConnecte != null) {
            model.addAttribute("clientConnecte", clientConnecte);
        }

        // Créer un objet réservation vide pour le formulaire
        Reservation reservation = new Reservation();
        reservation.setDateArrivee(LocalDate.now().plusDays(1)); // Par défaut demain
        reservation.setDateDepart(LocalDate.now().plusDays(2)); // Par défaut après-demain
        reservation.setNombreAdultes(1); // Par défaut 1 adulte
        reservation.setNombreEnfants(0); // Par défaut 0 enfant
        model.addAttribute("reservation", reservation);

        // Créer un client vide pour l'inscription rapide si pas connecté
        if (clientConnecte == null) {
            model.addAttribute("client", new Client());
        }

        return "reservation-form"; // templates/reservation-form.html
    }

    // Traiter la réservation (client connecté) - Ajout au panier
    @PostMapping("/reservation/ajouter-panier")
    @ResponseBody
    public Map<String, Object> ajouterAuPanier(@ModelAttribute Reservation reservation,
                                               @RequestParam Long chambreId,
                                               HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Récupérer la chambre
            Optional<Chambre> chambreOpt = chambreService.getChambreById(chambreId);
            if (chambreOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Chambre non trouvée");
                return response;
            }

            Chambre chambre = chambreOpt.get();
            
            // Créer un PanierItem
            String chambreNom = chambre.getDescription() != null && chambre.getDescription().contains(" - ") 
                ? chambre.getDescription().substring(0, chambre.getDescription().indexOf(" - "))
                : "Chambre " + chambre.getType().getLibelle() + " - " + chambre.getNumero();
            
            String imageUrl = chambre.getImageUrl() != null ? chambre.getImageUrl() : getDefaultImageUrl(chambre.getType().name());
            
            PanierItem item = new PanierItem(
                chambreId,
                chambreNom,
                chambre.getType().getLibelle(),
                chambre.getPrix(),
                imageUrl,
                reservation.getDateArrivee(),
                reservation.getDateDepart(),
                reservation.getNombreAdultes(),
                reservation.getNombreEnfants() != null ? reservation.getNombreEnfants() : 0,
                reservation.getCommentaires()
            );

            // Obtenir le panier et ajouter l'item
            Panier panier = obtenirPanier(session);
            panier.ajouterItem(item);

            // Répondre avec succès
            response.put("success", true);
            response.put("message", "Réservation ajoutée au panier avec succès !");
            response.put("nombreItems", panier.getNombreItems());
            response.put("prixTotal", panier.getPrixTotal());
            response.put("item", Map.of(
                "chambreNom", item.getChambreNom(),
                "nombreNuits", item.getNombreNuits(),
                "prixTotal", item.getPrixTotal()
            ));

            return response;

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return response;
        }
    }

    // Obtenir ou créer le panier de session
    private Panier obtenirPanier(HttpSession session) {
        Panier panier = (Panier) session.getAttribute("panier");
        if (panier == null) {
            panier = new Panier();
            session.setAttribute("panier", panier);
        }
        return panier;
    }

    // Méthode utilitaire pour les images par défaut
    private String getDefaultImageUrl(String type) {
        return switch (type) {
            case "SIMPLE" -> "https://images.unsplash.com/photo-1618773928121-c32242e63f39?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80";
            case "DOUBLE" -> "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80";
            case "SUITE" -> "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80";
            case "FAMILIALE" -> "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80";
            case "DELUXE" -> "https://images.unsplash.com/photo-1591088398332-8a7791972843?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80";
            default -> "https://images.unsplash.com/photo-1566665797739-1674de7a421a?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80";
        };
    }

    // Traiter la réservation avec inscription rapide (client non connecté)
    @PostMapping("/reservation/avec-inscription")
    public String reserverAvecInscription(@ModelAttribute("client") Client client,
                                          @ModelAttribute("reservation") Reservation reservation,
                                          @RequestParam Long chambreId,
                                          HttpSession session,
                                          Model model) {
        try {
            // Créer le compte client
            Client nouveauClient = clientService.createClient(client);
            
            // Connecter automatiquement le client
            session.setAttribute("clientConnecte", nouveauClient);

            // Créer la réservation
            Reservation nouvelleReservation = reservationService.creerReservation(
                nouveauClient, 
                chambreId, 
                reservation.getDateArrivee(), 
                reservation.getDateDepart(),
                reservation.getNombreAdultes(),
                reservation.getNombreEnfants()
            );

            model.addAttribute("succes", "Compte créé et réservation confirmée avec succès !");
            model.addAttribute("reservation", nouvelleReservation);
            return "reservation-confirmation";

        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage());
            Optional<Chambre> chambreOpt = chambreService.getChambreById(chambreId);
            if (chambreOpt.isPresent()) {
                model.addAttribute("chambre", chambreOpt.get());
            }
            model.addAttribute("client", client);
            model.addAttribute("reservation", reservation);
            return "reservation-form";
        }
    }
}
