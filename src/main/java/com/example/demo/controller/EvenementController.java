package com.example.demo.controller;

import com.example.demo.model.Evenement;
import com.example.demo.service.EvenementService;
import com.example.demo.service.InstallationService;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/evenements")
public class EvenementController {

    @Autowired
    private EvenementService evenementService;

    @Autowired
    private InstallationService installationService;

    @Autowired
    private ClientService clientService;

    // Page d'accueil des événements - Liste tous les événements
    @GetMapping
    public String listEvenements(Model model) {
        List<Evenement> evenements = evenementService.getAllEvenements();
        List<Evenement> evenementsAVenir = evenementService.getEvenementsAVenir();
        List<Evenement> evenementsEnCours = evenementService.getEvenementsEnCours();
        
        model.addAttribute("evenements", evenements);
        model.addAttribute("evenementsAVenir", evenementsAVenir);
        model.addAttribute("evenementsEnCours", evenementsEnCours);
        model.addAttribute("nombreEvenements", evenementService.getNombreEvenements());
        model.addAttribute("evenementsAujourdhui", evenementService.getEvenementsAujourdhui());
        
        // Statistiques par statut
        model.addAttribute("nombrePlanifies", evenementService.getNombreEvenementsParStatut(Evenement.StatutEvenement.PLANIFIE));
        model.addAttribute("nombreEnCours", evenementService.getNombreEvenementsParStatut(Evenement.StatutEvenement.EN_COURS));
        model.addAttribute("nombreTermines", evenementService.getNombreEvenementsParStatut(Evenement.StatutEvenement.TERMINE));
        model.addAttribute("nombreAnnules", evenementService.getNombreEvenementsParStatut(Evenement.StatutEvenement.ANNULE));
        
        return "evenements/liste"; // templates/evenements/liste.html
    }

    // Page de création d'un nouvel événement
    @GetMapping("/nouveau")
    public String nouvelEvenement(Model model) {
        model.addAttribute("evenement", new Evenement());
        model.addAttribute("statuts", Evenement.StatutEvenement.values());
        model.addAttribute("installations", installationService.getInstallationsDisponibles());
        return "evenements/nouveau"; // templates/evenements/nouveau.html
    }

    // Traiter la création d'un nouvel événement
    @PostMapping("/nouveau")
    public String creerEvenement(@ModelAttribute Evenement evenement, 
                                RedirectAttributes redirectAttributes,
                                Model model) {
        try {
            evenementService.createEvenement(evenement);
            redirectAttributes.addFlashAttribute("succes", 
                "Événement " + evenement.getTitre() + " créé avec succès !");
            
            return "redirect:/evenements";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            model.addAttribute("evenement", evenement);
            model.addAttribute("statuts", Evenement.StatutEvenement.values());
            model.addAttribute("installations", installationService.getInstallationsDisponibles());
            return "evenements/nouveau";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la création de l'événement : " + e.getMessage());
            model.addAttribute("evenement", evenement);
            model.addAttribute("statuts", Evenement.StatutEvenement.values());
            model.addAttribute("installations", installationService.getInstallationsDisponibles());
            return "evenements/nouveau";
        }
    }

    // Voir les détails d'un événement
    @GetMapping("/{id}")
    public String voirEvenement(@PathVariable Long id, Model model) {
        Optional<Evenement> evenement = evenementService.getEvenementById(id);
        if (evenement.isPresent()) {
            model.addAttribute("evenement", evenement.get());
            model.addAttribute("clients", clientService.getAllClients());
            return "evenements/details"; // templates/evenements/details.html
        } else {
            return "redirect:/evenements";
        }
    }

    // Page de modification d'un événement
    @GetMapping("/{id}/modifier")
    public String modifierEvenement(@PathVariable Long id, Model model) {
        Optional<Evenement> evenement = evenementService.getEvenementById(id);
        if (evenement.isPresent()) {
            model.addAttribute("evenement", evenement.get());
            model.addAttribute("statuts", Evenement.StatutEvenement.values());
            model.addAttribute("installations", installationService.getAllInstallations());
            return "evenements/modifier"; // templates/evenements/modifier.html
        } else {
            return "redirect:/evenements";
        }
    }

    // Traiter la modification d'un événement
    @PostMapping("/{id}/modifier")
    public String updateEvenement(@PathVariable Long id, 
                                 @ModelAttribute Evenement evenementDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            evenementService.updateEvenement(id, evenementDetails);
            redirectAttributes.addFlashAttribute("succes", 
                "Événement " + evenementDetails.getTitre() + " modifié avec succès !");
            
            return "redirect:/evenements";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/evenements/" + id + "/modifier";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la modification : " + e.getMessage());
            return "redirect:/evenements/" + id + "/modifier";
        }
    }

    // Supprimer un événement
    @PostMapping("/{id}/supprimer")
    public String supprimerEvenement(@PathVariable Long id, 
                                   RedirectAttributes redirectAttributes) {
        try {
            Optional<Evenement> evenement = evenementService.getEvenementById(id);
            String titreEvenement = evenement.map(Evenement::getTitre).orElse("inconnu");
            
            evenementService.deleteEvenement(id);
            redirectAttributes.addFlashAttribute("succes", 
                "Événement " + titreEvenement + " supprimé avec succès !");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la suppression : " + e.getMessage());
        }
        
        return "redirect:/evenements";
    }

    // Changer le statut d'un événement
    @PostMapping("/{id}/changer-statut")
    public String changerStatut(@PathVariable Long id, 
                               @RequestParam Evenement.StatutEvenement nouveauStatut,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<Evenement> evenementOpt = evenementService.getEvenementById(id);
            if (evenementOpt.isPresent()) {
                Evenement evenement = evenementOpt.get();
                evenementService.changerStatut(id, nouveauStatut);
                redirectAttributes.addFlashAttribute("succes", 
                    "Statut de l'événement " + evenement.getTitre() + " changé vers : " + nouveauStatut.getLibelle());
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Événement non trouvé !");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors du changement de statut : " + e.getMessage());
        }
        
        return "redirect:/evenements";
    }

    // Ajouter un participant
    @PostMapping("/{id}/participants/ajouter")
    public String ajouterParticipant(@PathVariable Long id, 
                                   @RequestParam Long clientId,
                                   RedirectAttributes redirectAttributes) {
        try {
            evenementService.ajouterParticipant(id, clientId);
            redirectAttributes.addFlashAttribute("succes", "Participant ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", "Erreur : " + e.getMessage());
        }
        
        return "redirect:/evenements/" + id;
    }

    // Retirer un participant
    @PostMapping("/{id}/participants/{clientId}/retirer")
    public String retirerParticipant(@PathVariable Long id, 
                                   @PathVariable Long clientId,
                                   RedirectAttributes redirectAttributes) {
        try {
            evenementService.retirerParticipant(id, clientId);
            redirectAttributes.addFlashAttribute("succes", "Participant retiré avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", "Erreur : " + e.getMessage());
        }
        
        return "redirect:/evenements/" + id;
    }

    // Page de recherche d'événements
    @GetMapping("/rechercher")
    public String rechercherEvenements(@RequestParam(required = false) String terme, Model model) {
        if (terme != null && !terme.trim().isEmpty()) {
            List<Evenement> resultats = evenementService.rechercherEvenements(terme);
            model.addAttribute("evenements", resultats);
            model.addAttribute("terme", terme);
            model.addAttribute("nombreResultats", resultats.size());
        } else {
            model.addAttribute("evenements", evenementService.getAllEvenements());
        }
        
        return "evenements/recherche"; // templates/evenements/recherche.html
    }

    // Événements par statut
    @GetMapping("/statut/{statut}")
    public String evenementsByStatut(@PathVariable String statut, Model model) {
        try {
            Evenement.StatutEvenement statutEnum = Evenement.StatutEvenement.valueOf(statut.toUpperCase());
            List<Evenement> evenements = evenementService.getEvenementsByStatut(statutEnum);
            
            model.addAttribute("evenements", evenements);
            model.addAttribute("statut", statutEnum);
            model.addAttribute("nombreEvenements", evenements.size());
            
            return "evenements/par-statut"; // templates/evenements/par-statut.html
        } catch (IllegalArgumentException e) {
            return "redirect:/evenements";
        }
    }
}
