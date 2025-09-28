package com.example.demo.controller;

import com.example.demo.model.Gestionnaire;
import com.example.demo.service.GestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestionnaires")
public class GestionnaireController {

    @Autowired
    private GestionnaireService gestionnaireService;

    // Liste des gestionnaires
    @GetMapping
    public String listeGestionnaires(@RequestParam(required = false) String search, Model model) {
        List<Gestionnaire> gestionnaires;
        
        if (search != null && !search.trim().isEmpty()) {
            gestionnaires = gestionnaireService.rechercherGestionnaires(search);
            model.addAttribute("search", search);
        } else {
            gestionnaires = gestionnaireService.obtenirGestionnairesActifs();
        }
        
        model.addAttribute("gestionnaires", gestionnaires);
        model.addAttribute("nombreTotal", gestionnaireService.compterGestionnairesActifs());
        
        return "gestionnaires/liste";
    }

    // Détails d'un gestionnaire
    @GetMapping("/{id}")
    public String detailsGestionnaire(@PathVariable Long id, Model model) {
        Optional<Gestionnaire> gestionnaireOpt = gestionnaireService.obtenirGestionnaireParId(id);
        
        if (gestionnaireOpt.isPresent()) {
            model.addAttribute("gestionnaire", gestionnaireOpt.get());
            return "gestionnaires/details";
        } else {
            return "redirect:/gestionnaires?error=Gestionnaire+non+trouvé";
        }
    }



    // Formulaire modification gestionnaire
    @GetMapping("/{id}/modifier")
    public String modifierGestionnaireForm(@PathVariable Long id, Model model) {
        Optional<Gestionnaire> gestionnaireOpt = gestionnaireService.obtenirGestionnaireParId(id);
        
        if (gestionnaireOpt.isPresent()) {
            model.addAttribute("gestionnaire", gestionnaireOpt.get());
            return "gestionnaires/modifier";
        } else {
            return "redirect:/gestionnaires?error=Gestionnaire+non+trouvé";
        }
    }

    // Mettre à jour un gestionnaire
    @PostMapping("/{id}/modifier")
    public String mettreAJourGestionnaire(@PathVariable Long id,
                                         @Valid @ModelAttribute Gestionnaire gestionnaire,
                                         BindingResult result,
                                         RedirectAttributes redirectAttributes) {
        
        // Vérifier si l'email existe pour un autre gestionnaire
        if (gestionnaireService.emailExistePourAutreGestionnaire(gestionnaire.getEmail(), id)) {
            result.rejectValue("email", "error.gestionnaire", "Un autre gestionnaire avec cet email existe déjà");
        }
        
        if (result.hasErrors()) {
            return "gestionnaires/modifier";
        }
        
        try {
            gestionnaire.setId(id);
            Gestionnaire gestionnaireModifie = gestionnaireService.mettreAJourGestionnaire(gestionnaire);
            redirectAttributes.addFlashAttribute("success", 
                "Gestionnaire " + gestionnaireModifie.getNomComplet() + " modifié avec succès");
            return "redirect:/gestionnaires/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
            return "redirect:/gestionnaires/" + id + "/modifier";
        }
    }

    // Supprimer un gestionnaire (désactivation)
    @PostMapping("/{id}/supprimer")
    public String supprimerGestionnaire(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Gestionnaire> gestionnaireOpt = gestionnaireService.obtenirGestionnaireParId(id);
            if (gestionnaireOpt.isPresent()) {
                String nomComplet = gestionnaireOpt.get().getNomComplet();
                gestionnaireService.supprimerGestionnaire(id);
                redirectAttributes.addFlashAttribute("success", 
                    "Gestionnaire " + nomComplet + " désactivé avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Gestionnaire non trouvé");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        
        return "redirect:/gestionnaires";
    }

    // Réactiver un gestionnaire
    @PostMapping("/{id}/reactiver")
    public String reactiverGestionnaire(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Gestionnaire> gestionnaireOpt = gestionnaireService.obtenirGestionnaireParId(id);
            if (gestionnaireOpt.isPresent()) {
                String nomComplet = gestionnaireOpt.get().getNomComplet();
                gestionnaireService.reactiverGestionnaire(id);
                redirectAttributes.addFlashAttribute("success", 
                    "Gestionnaire " + nomComplet + " réactivé avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Gestionnaire non trouvé");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la réactivation: " + e.getMessage());
        }
        
        return "redirect:/gestionnaires/" + id;
    }

    // Page des statistiques des gestionnaires
    @GetMapping("/statistiques")
    public String statistiquesGestionnaires(Model model) {
        // Données des gestionnaires
        model.addAttribute("nombreTotal", gestionnaireService.compterGestionnairesActifs());
        model.addAttribute("gestionnairesSansChambres", gestionnaireService.obtenirGestionnairesSansChambres());
        model.addAttribute("gestionnairesParChambres", gestionnaireService.obtenirGestionnairesParNombreChambres());
        model.addAttribute("gestionnairesParFeedbacks", gestionnaireService.obtenirGestionnairesParNombreFeedbacks());
        
        // Données des employés
        model.addAttribute("tousLesEmployes", gestionnaireService.obtenirTousLesEmployes());
        model.addAttribute("employesActifs", gestionnaireService.obtenirEmployesActifs());
        model.addAttribute("employesEnConge", gestionnaireService.obtenirEmployesEnConge());
        model.addAttribute("repartitionParRole", gestionnaireService.obtenirRepartitionParRole());
        model.addAttribute("repartitionParStatut", gestionnaireService.obtenirRepartitionParStatut());
        model.addAttribute("employesRecents", gestionnaireService.obtenirEmployesRecents(5));
        
        return "gestionnaires/statistiques";
    }
    
    // Formulaire de création de gestionnaire
    @GetMapping("/nouveau")
    public String formulaireNouveauGestionnaire(Model model) {
        model.addAttribute("gestionnaire", new Gestionnaire());
        return "gestionnaires/nouveau";
    }
    
    // Traitement de la création de gestionnaire  
    @PostMapping("/nouveau")
    public String creerNouveauGestionnaire(@Valid @ModelAttribute Gestionnaire gestionnaire,
                                         BindingResult result,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        
        // Vérifier si l'email existe déjà
        if (gestionnaireService.emailExistePourAutreGestionnaire(gestionnaire.getEmail(), null)) {
            result.rejectValue("email", "error.gestionnaire", "Un gestionnaire avec cet email existe déjà");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("gestionnaire", gestionnaire);
            return "gestionnaires/nouveau";
        }
        
        try {
            Gestionnaire nouveauGestionnaire = gestionnaireService.creerGestionnaire(gestionnaire);
            redirectAttributes.addFlashAttribute("success", 
                "Gestionnaire " + nouveauGestionnaire.getNomComplet() + " créé avec succès !");
            
            return "redirect:/gestionnaires";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("gestionnaire", gestionnaire);
            return "gestionnaires/nouveau";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création du gestionnaire : " + e.getMessage());
            model.addAttribute("gestionnaire", gestionnaire);
            return "gestionnaires/nouveau";
        }
    }
}
