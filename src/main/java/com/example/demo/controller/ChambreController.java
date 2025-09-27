package com.example.demo.controller;

import com.example.demo.model.Chambre;
import com.example.demo.service.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chambres")
public class ChambreController {

    @Autowired
    private ChambreService chambreService;

    // Page d'accueil des chambres - Liste toutes les chambres
    @GetMapping
    public String listChambres(Model model) {
        List<Chambre> chambres = chambreService.getAllChambres();
        List<Chambre> chambresDisponibles = chambreService.getChambresDisponibles();
        
        model.addAttribute("chambres", chambres);
        model.addAttribute("chambresDisponibles", chambresDisponibles);
        model.addAttribute("nombreChambres", chambreService.getNombreChambres());
        model.addAttribute("nombreDisponibles", chambreService.getNombreChambresDisponibles());
        
        return "chambres/liste"; // templates/chambres/liste.html
    }

    // Page de création d'une nouvelle chambre
    @GetMapping("/nouvelle")
    public String nouvelleChambre(Model model) {
        model.addAttribute("chambre", new Chambre());
        model.addAttribute("types", Chambre.TypeChambre.values());
        return "chambres/nouvelle"; // templates/chambres/nouvelle.html
    }

    // Traiter la création d'une nouvelle chambre
    @PostMapping("/nouvelle")
    public String creerChambre(@ModelAttribute Chambre chambre, 
                              RedirectAttributes redirectAttributes) {
        try {
            chambreService.createChambre(chambre);
            redirectAttributes.addFlashAttribute("succes", 
                "Chambre " + chambre.getNumero() + " créée avec succès !");
            
            return "redirect:/chambres";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/chambres/nouvelle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la création de la chambre : " + e.getMessage());
            return "redirect:/chambres/nouvelle";
        }
    }

    // Voir les détails d'une chambre
    @GetMapping("/{id}")
    public String voirChambre(@PathVariable Long id, Model model) {
        Optional<Chambre> chambre = chambreService.getChambreById(id);
        if (chambre.isPresent()) {
            model.addAttribute("chambre", chambre.get());
            return "chambres/details"; // templates/chambres/details.html
        } else {
            return "redirect:/chambres";
        }
    }

    // Page de modification d'une chambre
    @GetMapping("/{id}/modifier")
    public String modifierChambre(@PathVariable Long id, Model model) {
        Optional<Chambre> chambre = chambreService.getChambreById(id);
        if (chambre.isPresent()) {
            model.addAttribute("chambre", chambre.get());
            model.addAttribute("types", Chambre.TypeChambre.values());
            return "chambres/modifier"; // templates/chambres/modifier.html
        } else {
            return "redirect:/chambres";
        }
    }

    // Traiter la modification d'une chambre
    @PostMapping("/{id}/modifier")
    public String updateChambre(@PathVariable Long id, 
                               @ModelAttribute Chambre chambreDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            chambreService.updateChambre(id, chambreDetails);
            redirectAttributes.addFlashAttribute("succes", 
                "Chambre " + chambreDetails.getNumero() + " modifiée avec succès !");
            
            return "redirect:/chambres";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/chambres/" + id + "/modifier";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la modification : " + e.getMessage());
            return "redirect:/chambres/" + id + "/modifier";
        }
    }

    // Supprimer une chambre
    @PostMapping("/{id}/supprimer")
    public String supprimerChambre(@PathVariable Long id, 
                                   RedirectAttributes redirectAttributes) {
        try {
            // Récupérer la chambre pour afficher son numéro dans le message
            Optional<Chambre> chambre = chambreService.getChambreById(id);
            String numeroChambre = chambre.map(Chambre::getNumero).orElse("inconnue");
            
            chambreService.deleteChambre(id);
            redirectAttributes.addFlashAttribute("succes", 
                "Chambre " + numeroChambre + " supprimée avec succès !");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la suppression : " + e.getMessage());
        }
        
        return "redirect:/chambres";
    }

    // Changer le statut de disponibilité d'une chambre
    @PostMapping("/{id}/toggle-disponibilite")
    public String toggleDisponibilite(@PathVariable Long id, 
                                     RedirectAttributes redirectAttributes) {
        try {
            // Récupérer l'état actuel pour le message
            Optional<Chambre> chambreOpt = chambreService.getChambreById(id);
            if (chambreOpt.isPresent()) {
                Chambre chambre = chambreOpt.get();
                String messageAction = chambre.getDisponible() ? "marquée comme occupée" : "marquée comme disponible";
                
                chambreService.toggleDisponibilite(id);
                redirectAttributes.addFlashAttribute("succes", 
                    "Chambre " + chambre.getNumero() + " " + messageAction + ".");
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Chambre non trouvée !");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors du changement de statut : " + e.getMessage());
        }
        
        return "redirect:/chambres";
    }
}


