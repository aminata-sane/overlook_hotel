package com.example.demo.controller;

import com.example.demo.model.Installation;
import com.example.demo.service.InstallationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/installations")
public class InstallationController {

    @Autowired
    private InstallationService installationService;

    // Page d'accueil des installations - Liste toutes les installations
    @GetMapping
    public String listInstallations(Model model) {
        List<Installation> installations = installationService.getAllInstallations();
        List<Installation> installationsDisponibles = installationService.getInstallationsDisponibles();
        
        model.addAttribute("installations", installations);
        model.addAttribute("installationsDisponibles", installationsDisponibles);
        model.addAttribute("nombreInstallations", installationService.getNombreInstallations());
        model.addAttribute("nombreDisponibles", installationService.getNombreInstallationsDisponibles());
        
        // Statistiques par type
        model.addAttribute("nombreSallesConference", installationService.getNombreInstallationsParType(Installation.TypeInstallation.SALLE_CONFERENCE));
        model.addAttribute("nombreSallesReunion", installationService.getNombreInstallationsParType(Installation.TypeInstallation.SALLE_REUNION));
        model.addAttribute("nombreRestaurants", installationService.getNombreInstallationsParType(Installation.TypeInstallation.RESTAURANT));
        model.addAttribute("nombreBars", installationService.getNombreInstallationsParType(Installation.TypeInstallation.BAR));
        
        return "installations/liste"; // templates/installations/liste.html
    }

    // Page de création d'une nouvelle installation
    @GetMapping("/nouvelle")
    public String nouvelleInstallation(Model model) {
        model.addAttribute("installation", new Installation());
        model.addAttribute("types", Installation.TypeInstallation.values());
        return "installations/nouvelle"; // templates/installations/nouvelle.html
    }

    // Traiter la création d'une nouvelle installation
    @PostMapping("/nouvelle")
    public String creerInstallation(@ModelAttribute Installation installation, 
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        try {
            installationService.createInstallation(installation);
            redirectAttributes.addFlashAttribute("succes", 
                "Installation " + installation.getNom() + " créée avec succès !");
            
            return "redirect:/installations";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            model.addAttribute("installation", installation);
            model.addAttribute("types", Installation.TypeInstallation.values());
            return "installations/nouvelle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la création de l'installation : " + e.getMessage());
            model.addAttribute("installation", installation);
            model.addAttribute("types", Installation.TypeInstallation.values());
            return "installations/nouvelle";
        }
    }

    // Voir les détails d'une installation
    @GetMapping("/{id}")
    public String voirInstallation(@PathVariable Long id, Model model) {
        Optional<Installation> installation = installationService.getInstallationById(id);
        if (installation.isPresent()) {
            model.addAttribute("installation", installation.get());
            return "installations/details"; // templates/installations/details.html
        } else {
            return "redirect:/installations";
        }
    }

    // Page de modification d'une installation
    @GetMapping("/{id}/modifier")
    public String modifierInstallation(@PathVariable Long id, Model model) {
        Optional<Installation> installation = installationService.getInstallationById(id);
        if (installation.isPresent()) {
            model.addAttribute("installation", installation.get());
            model.addAttribute("types", Installation.TypeInstallation.values());
            return "installations/modifier"; // templates/installations/modifier.html
        } else {
            return "redirect:/installations";
        }
    }

    // Traiter la modification d'une installation
    @PostMapping("/{id}/modifier")
    public String updateInstallation(@PathVariable Long id, 
                                   @ModelAttribute Installation installationDetails,
                                   RedirectAttributes redirectAttributes) {
        try {
            installationService.updateInstallation(id, installationDetails);
            redirectAttributes.addFlashAttribute("succes", 
                "Installation " + installationDetails.getNom() + " modifiée avec succès !");
            
            return "redirect:/installations";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/installations/" + id + "/modifier";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la modification : " + e.getMessage());
            return "redirect:/installations/" + id + "/modifier";
        }
    }

    // Supprimer une installation
    @PostMapping("/{id}/supprimer")
    public String supprimerInstallation(@PathVariable Long id, 
                                      RedirectAttributes redirectAttributes) {
        try {
            Optional<Installation> installation = installationService.getInstallationById(id);
            String nomInstallation = installation.map(Installation::getNom).orElse("inconnu");
            
            installationService.deleteInstallation(id);
            redirectAttributes.addFlashAttribute("succes", 
                "Installation " + nomInstallation + " supprimée avec succès !");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la suppression : " + e.getMessage());
        }
        
        return "redirect:/installations";
    }

    // Changer la disponibilité d'une installation
    @PostMapping("/{id}/changer-disponibilite")
    public String changerDisponibilite(@PathVariable Long id, 
                                     @RequestParam Boolean disponible,
                                     RedirectAttributes redirectAttributes) {
        try {
            Optional<Installation> installationOpt = installationService.getInstallationById(id);
            if (installationOpt.isPresent()) {
                Installation installation = installationOpt.get();
                installationService.changerDisponibilite(id, disponible);
                String statut = disponible ? "disponible" : "indisponible";
                redirectAttributes.addFlashAttribute("succes", 
                    "Installation " + installation.getNom() + " marquée comme " + statut);
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Installation non trouvée !");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors du changement de disponibilité : " + e.getMessage());
        }
        
        return "redirect:/installations";
    }

    // Page de recherche d'installations
    @GetMapping("/rechercher")
    public String rechercherInstallations(@RequestParam(required = false) String terme, Model model) {
        if (terme != null && !terme.trim().isEmpty()) {
            List<Installation> resultats = installationService.rechercherInstallations(terme);
            model.addAttribute("installations", resultats);
            model.addAttribute("terme", terme);
            model.addAttribute("nombreResultats", resultats.size());
        } else {
            model.addAttribute("installations", installationService.getAllInstallations());
        }
        
        return "installations/recherche"; // templates/installations/recherche.html
    }

    // Installations par type
    @GetMapping("/type/{type}")
    public String installationsByType(@PathVariable String type, Model model) {
        try {
            Installation.TypeInstallation typeEnum = Installation.TypeInstallation.valueOf(type.toUpperCase());
            List<Installation> installations = installationService.getInstallationsByType(typeEnum);
            
            model.addAttribute("installations", installations);
            model.addAttribute("type", typeEnum);
            model.addAttribute("nombreInstallations", installations.size());
            
            return "installations/par-type"; // templates/installations/par-type.html
        } catch (IllegalArgumentException e) {
            return "redirect:/installations";
        }
    }

    // Installations disponibles seulement
    @GetMapping("/disponibles")
    public String installationsDisponibles(Model model) {
        List<Installation> installations = installationService.getInstallationsDisponibles();
        
        model.addAttribute("installations", installations);
        model.addAttribute("nombreInstallations", installations.size());
        model.addAttribute("filtreApplique", "Disponibles seulement");
        
        return "installations/disponibles"; // templates/installations/disponibles.html
    }
}
