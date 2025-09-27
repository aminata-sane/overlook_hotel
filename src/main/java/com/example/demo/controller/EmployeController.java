package com.example.demo.controller;

import com.example.demo.model.Employe;
import com.example.demo.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employes")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    // Page d'accueil des employés - Liste tous les employés
    @GetMapping
    public String listEmployes(Model model) {
        List<Employe> employes = employeService.getAllEmployes();
        List<Employe> employesActifs = employeService.getEmployesActifs();
        
        model.addAttribute("employes", employes);
        model.addAttribute("employesActifs", employesActifs);
        model.addAttribute("nombreEmployes", employeService.getNombreEmployes());
        model.addAttribute("nombreActifs", employeService.getNombreEmployesActifs());
        
        // Statistiques par rôle
        model.addAttribute("nombreReceptionnistes", employeService.getNombreEmployesParRole(Employe.RoleEmploye.RECEPTIONNISTE));
        model.addAttribute("nombreFemmesMenage", employeService.getNombreEmployesParRole(Employe.RoleEmploye.FEMME_DE_MENAGE));
        model.addAttribute("nombreMaintenance", employeService.getNombreEmployesParRole(Employe.RoleEmploye.MAINTENANCE));
        model.addAttribute("nombreGestionnaires", employeService.getNombreEmployesParRole(Employe.RoleEmploye.GESTIONNAIRE));
        
        return "employes/liste"; // templates/employes/liste.html
    }

    // Page de création d'un nouvel employé
    @GetMapping("/nouveau")
    public String nouvelEmploye(Model model) {
        model.addAttribute("employe", new Employe());
        model.addAttribute("roles", Employe.RoleEmploye.values());
        model.addAttribute("statuts", Employe.StatutEmploye.values());
        return "employes/nouveau"; // templates/employes/nouveau.html
    }

    // Traiter la création d'un nouvel employé
    @PostMapping("/nouveau")
    public String creerEmploye(@ModelAttribute Employe employe, 
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            // Debug : afficher les données reçues
            System.out.println("Données reçues - Nom: " + employe.getNom() + 
                             ", Prénom: " + employe.getPrenom() + 
                             ", Email: " + employe.getEmail() +
                             ", Rôle: " + employe.getRole() +
                             ", Statut: " + employe.getStatut());
            
            employeService.createEmploye(employe);
            redirectAttributes.addFlashAttribute("succes", 
                "Employé " + employe.getNomComplet() + " créé avec succès !");
            
            return "redirect:/employes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            // Réafficher le formulaire avec les données existantes
            model.addAttribute("employe", employe);
            model.addAttribute("roles", Employe.RoleEmploye.values());
            model.addAttribute("statuts", Employe.StatutEmploye.values());
            return "employes/nouveau";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la création de l'employé : " + e.getMessage());
            // Réafficher le formulaire avec les données existantes
            model.addAttribute("employe", employe);
            model.addAttribute("roles", Employe.RoleEmploye.values());
            model.addAttribute("statuts", Employe.StatutEmploye.values());
            return "employes/nouveau";
        }
    }

    // Voir les détails d'un employé
    @GetMapping("/{id}")
    public String voirEmploye(@PathVariable Long id, Model model) {
        Optional<Employe> employe = employeService.getEmployeById(id);
        if (employe.isPresent()) {
            model.addAttribute("employe", employe.get());
            return "employes/details"; // templates/employes/details.html
        } else {
            return "redirect:/employes";
        }
    }

    // Page de modification d'un employé
    @GetMapping("/{id}/modifier")
    public String modifierEmploye(@PathVariable Long id, Model model) {
        Optional<Employe> employe = employeService.getEmployeById(id);
        if (employe.isPresent()) {
            model.addAttribute("employe", employe.get());
            model.addAttribute("roles", Employe.RoleEmploye.values());
            model.addAttribute("statuts", Employe.StatutEmploye.values());
            return "employes/modifier"; // templates/employes/modifier.html
        } else {
            return "redirect:/employes";
        }
    }

    // Traiter la modification d'un employé
    @PostMapping("/{id}/modifier")
    public String updateEmploye(@PathVariable Long id, 
                               @ModelAttribute Employe employeDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            employeService.updateEmploye(id, employeDetails);
            redirectAttributes.addFlashAttribute("succes", 
                "Employé " + employeDetails.getNomComplet() + " modifié avec succès !");
            
            return "redirect:/employes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/employes/" + id + "/modifier";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la modification : " + e.getMessage());
            return "redirect:/employes/" + id + "/modifier";
        }
    }

    // Supprimer un employé
    @PostMapping("/{id}/supprimer")
    public String supprimerEmploye(@PathVariable Long id, 
                                   RedirectAttributes redirectAttributes) {
        try {
            // Récupérer l'employé pour afficher son nom dans le message
            Optional<Employe> employe = employeService.getEmployeById(id);
            String nomEmploye = employe.map(Employe::getNomComplet).orElse("inconnu");
            
            employeService.deleteEmploye(id);
            redirectAttributes.addFlashAttribute("succes", 
                "Employé " + nomEmploye + " supprimé avec succès !");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la suppression : " + e.getMessage());
        }
        
        return "redirect:/employes";
    }

    // Changer le statut d'un employé
    @PostMapping("/{id}/changer-statut")
    public String changerStatut(@PathVariable Long id, 
                               @RequestParam Employe.StatutEmploye nouveauStatut,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<Employe> employeOpt = employeService.getEmployeById(id);
            if (employeOpt.isPresent()) {
                Employe employe = employeOpt.get();
                employeService.changerStatutEmploye(id, nouveauStatut);
                redirectAttributes.addFlashAttribute("succes", 
                    "Statut de " + employe.getNomComplet() + " changé vers : " + nouveauStatut.getLibelle());
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Employé non trouvé !");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors du changement de statut : " + e.getMessage());
        }
        
        return "redirect:/employes";
    }

    // Page de gestion des horaires d'un employé
    @GetMapping("/{id}/horaires")
    public String gererHoraires(@PathVariable Long id, Model model) {
        Optional<Employe> employe = employeService.getEmployeById(id);
        if (employe.isPresent()) {
            model.addAttribute("employe", employe.get());
            return "employes/horaires"; // templates/employes/horaires.html
        } else {
            return "redirect:/employes";
        }
    }

    // Ajouter un horaire
    @PostMapping("/{id}/horaires/ajouter")
    public String ajouterHoraire(@PathVariable Long id, 
                                @RequestParam String horaire,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<Employe> employeOpt = employeService.getEmployeById(id);
            if (employeOpt.isPresent()) {
                Employe employe = employeOpt.get();
                employeService.ajouterHoraire(employe, horaire);
                employeService.saveEmploye(employe);
                redirectAttributes.addFlashAttribute("succes", "Horaire ajouté avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Employé non trouvé !");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", "Erreur : " + e.getMessage());
        }
        
        return "redirect:/employes/" + id + "/horaires";
    }

    // Page de recherche d'employés
    @GetMapping("/rechercher")
    public String rechercherEmployes(@RequestParam(required = false) String terme, Model model) {
        if (terme != null && !terme.trim().isEmpty()) {
            List<Employe> resultats = employeService.rechercherEmployes(terme);
            model.addAttribute("employes", resultats);
            model.addAttribute("terme", terme);
            model.addAttribute("nombreResultats", resultats.size());
        } else {
            model.addAttribute("employes", employeService.getAllEmployes());
        }
        
        return "employes/recherche"; // templates/employes/recherche.html
    }
}

