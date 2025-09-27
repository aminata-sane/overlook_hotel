package com.example.demo.controller;

import com.example.demo.model.Fidelite;
import com.example.demo.service.FideliteService;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@Controller
@RequestMapping("/fidelite")
public class FideliteController {

    @Autowired
    private FideliteService fideliteService;

    @Autowired
    private ClientService clientService;

    // Page d'accueil des cartes de fidélité - Liste toutes les cartes
    @GetMapping
    public String listFidelites(Model model) {
        List<Fidelite> fidelites = fideliteService.getAllFidelites();
        Map<String, Object> statistiques = fideliteService.getStatistiques();
        
        model.addAttribute("fidelites", fidelites);
        model.addAttribute("statistiques", statistiques);
        
        // Top clients
        List<Fidelite> topClients = fideliteService.getTopClients();
        model.addAttribute("topClients", topClients);
        
        return "fidelite/liste"; // templates/fidelite/liste.html
    }

    // Page de création d'une nouvelle carte de fidélité
    @GetMapping("/nouvelle")
    public String nouvelleFidelite(Model model) {
        model.addAttribute("fidelite", new Fidelite());
        model.addAttribute("clients", clientService.getAllClients());
        return "fidelite/nouvelle"; // templates/fidelite/nouvelle.html
    }

    // Traiter la création d'une nouvelle carte de fidélité
    @PostMapping("/nouvelle")
    public String creerFidelite(@ModelAttribute Fidelite fidelite, 
                               @RequestParam("clientId") Long clientId,
                               RedirectAttributes redirectAttributes) {
        try {
            System.out.println("DEBUG: Création fidélité pour client ID: " + clientId);
            System.out.println("DEBUG: Points: " + fidelite.getPoints());
            
            // Récupérer le client par son ID
            var client = clientService.getClientById(clientId);
            if (client.isEmpty()) {
                redirectAttributes.addFlashAttribute("erreur", 
                    "Client introuvable");
                return "redirect:/fidelite/nouvelle";
            }
            
            // Créer la carte de fidélité
            fidelite.setClient(client.get());
            fideliteService.createFidelite(fidelite);
            
            redirectAttributes.addFlashAttribute("succes", 
                "Carte de fidélité créée avec succès !");
            return "redirect:/fidelite";
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la création de la carte : " + e.getMessage());
            return "redirect:/fidelite/nouvelle";
        }
    }

    // Voir les détails d'une carte de fidélité
    @GetMapping("/{id}")
    public String voirFidelite(@PathVariable Long id, Model model) {
        Optional<Fidelite> fidelite = fideliteService.getFideliteById(id);
        if (fidelite.isPresent()) {
            model.addAttribute("fidelite", fidelite.get());
            return "fidelite/details"; // templates/fidelite/details.html
        } else {
            return "redirect:/fidelite";
        }
    }

    // Page de modification d'une carte de fidélité
    @GetMapping("/{id}/modifier")
    public String modifierFidelite(@PathVariable Long id, Model model) {
        Optional<Fidelite> fidelite = fideliteService.getFideliteById(id);
        if (fidelite.isPresent()) {
            model.addAttribute("fidelite", fidelite.get());
            model.addAttribute("clients", clientService.getAllClients());
            return "fidelite/modifier"; // templates/fidelite/modifier.html
        } else {
            return "redirect:/fidelite";
        }
    }

    // Traiter la modification d'une carte de fidélité
    @PostMapping("/{id}/modifier")
    public String sauvegarderFidelite(@PathVariable Long id, 
                                      @ModelAttribute Fidelite fidelite, 
                                      RedirectAttributes redirectAttributes) {
        try {
            fideliteService.updateFidelite(id, fidelite);
            redirectAttributes.addFlashAttribute("succes", 
                "Carte de fidélité modifiée avec succès !");
            return "redirect:/fidelite/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la modification : " + e.getMessage());
            return "redirect:/fidelite/" + id + "/modifier";
        }
    }

    // Supprimer une carte de fidélité
    @PostMapping("/{id}/supprimer")
    public String supprimerFidelite(@PathVariable Long id, 
                                   RedirectAttributes redirectAttributes) {
        try {
            fideliteService.deleteFidelite(id);
            redirectAttributes.addFlashAttribute("succes", 
                "Carte de fidélité supprimée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la suppression : " + e.getMessage());
        }
        
        return "redirect:/fidelite";
    }

    // Page pour ajouter des points
    @GetMapping("/{id}/ajouter-points")
    public String ajouterPointsForm(@PathVariable Long id, Model model) {
        Optional<Fidelite> fidelite = fideliteService.getFideliteById(id);
        if (fidelite.isPresent()) {
            model.addAttribute("fidelite", fidelite.get());
            return "fidelite/ajouter-points"; // templates/fidelite/ajouter-points.html
        } else {
            return "redirect:/fidelite";
        }
    }

    // Traiter l'ajout de points
    @PostMapping("/{id}/ajouter-points")
    public String ajouterPoints(@PathVariable Long id, 
                               @RequestParam int points,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<Fidelite> fideliteOpt = fideliteService.getFideliteById(id);
            if (fideliteOpt.isPresent()) {
                fideliteService.ajouterPoints(fideliteOpt.get().getClient().getId(), points);
                redirectAttributes.addFlashAttribute("succes", 
                    points + " points ajoutés avec succès !");
            }
            return "redirect:/fidelite/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de l'ajout de points : " + e.getMessage());
            return "redirect:/fidelite/" + id + "/ajouter-points";
        }
    }

    // Page pour retirer des points
    @GetMapping("/{id}/retirer-points")
    public String retirerPointsForm(@PathVariable Long id, Model model) {
        Optional<Fidelite> fidelite = fideliteService.getFideliteById(id);
        if (fidelite.isPresent()) {
            model.addAttribute("fidelite", fidelite.get());
            return "fidelite/retirer-points"; // templates/fidelite/retirer-points.html
        } else {
            return "redirect:/fidelite";
        }
    }

    // Traiter le retrait de points
    @PostMapping("/{id}/retirer-points")
    public String retirerPoints(@PathVariable Long id, 
                               @RequestParam int points,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<Fidelite> fideliteOpt = fideliteService.getFideliteById(id);
            if (fideliteOpt.isPresent()) {
                fideliteService.retirerPoints(fideliteOpt.get().getClient().getId(), points);
                redirectAttributes.addFlashAttribute("succes", 
                    points + " points retirés avec succès !");
            }
            return "redirect:/fidelite/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors du retrait de points : " + e.getMessage());
            return "redirect:/fidelite/" + id + "/retirer-points";
        }
    }

    // Page des statistiques
    @GetMapping("/statistiques")
    public String statistiques(Model model) {
        Map<String, Object> statistiques = fideliteService.getStatistiques();
        List<Fidelite> topClients = fideliteService.getTopClients();
        
        model.addAttribute("statistiques", statistiques);
        model.addAttribute("topClients", topClients);
        
        // Répartition par niveau
        for (Fidelite.NiveauFidelite niveau : Fidelite.NiveauFidelite.values()) {
            List<Fidelite> clientsNiveau = fideliteService.getFidelitesByNiveau(niveau);
            model.addAttribute("clients" + niveau.name(), clientsNiveau);
        }
        
        return "fidelite/statistiques"; // templates/fidelite/statistiques.html
    }

    // Route de test pour diagnostiquer le problème
    @GetMapping("/test")
    public String testFidelite(Model model) {
        try {
            List<Fidelite> fidelites = fideliteService.getAllFidelites();
            model.addAttribute("fidelites", fidelites);
            
            Map<String, Object> statistiques = fideliteService.getStatistiques();
            model.addAttribute("statistiques", statistiques);
            
            return "fidelite/test";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
}
