package com.example.demo.controller;

import com.example.demo.model.Employe;
import com.example.demo.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
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

    // Route pour gestionnaire - doit être avant /{id} pour éviter les conflits
    @GetMapping("/liste-gestionnaire")
    public String listeEmployesGestionnaire(Model model) {
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

    // Ajouter un congé
    @PostMapping("/{id}/conges/ajouter")
    public String ajouterConge(@PathVariable Long id, 
                              @RequestParam String dateDebut,
                              @RequestParam String dateFin,
                              RedirectAttributes redirectAttributes) {
        try {
            Optional<Employe> employeOpt = employeService.getEmployeById(id);
            if (employeOpt.isPresent()) {
                Employe employe = employeOpt.get();
                String conge = "Du " + dateDebut + " au " + dateFin;
                employe.addConge(conge);
                employeService.saveEmploye(employe);
                redirectAttributes.addFlashAttribute("succes", "Congé ajouté avec succès !");
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

    // === GESTION DES STATUTS DES CHAMBRES ===
    
    @Autowired
    private com.example.demo.service.ChambreService chambreService;
    
    // Page de connexion employé
    @GetMapping("/connexion")
    public String connexionEmploye(Model model) {
        return "employes/connexion"; // templates/employes/connexion.html
    }
    
    // Traiter la connexion employé
    @PostMapping("/connexion")
    public String traiterConnexionEmploye(@RequestParam String email, 
                                         @RequestParam String motDePasse,
                                         HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        try {
            Optional<Employe> employeOpt = employeService.getEmployeByEmail(email);
            
            if (employeOpt.isPresent()) {
                Employe employe = employeOpt.get();
                
                // Vérifier le mot de passe et le statut
                if (employe.getMotDePasse().equals(motDePasse)) {
                    if (employe.getStatut() == Employe.StatutEmploye.ACTIF) {
                        // Connexion réussie
                        session.setAttribute("employeConnecte", employe);
                        redirectAttributes.addFlashAttribute("success", 
                            "Connexion réussie ! Bienvenue " + employe.getPrenom() + " !");
                        
                        // Rediriger selon le rôle
                        if (employe.getRole() == Employe.RoleEmploye.GESTIONNAIRE) {
                            return "redirect:/gestionnaire";
                        } else {
                            return "redirect:/employes/dashboard";
                        }
                    } else {
                        redirectAttributes.addFlashAttribute("erreur", 
                            "Votre compte n'est pas actif. Contactez votre gestionnaire.");
                    }
                } else {
                    redirectAttributes.addFlashAttribute("erreur", "Email ou mot de passe incorrect.");
                }
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Email ou mot de passe incorrect.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la connexion : " + e.getMessage());
        }
        
        return "redirect:/employes/connexion";
    }
    
    // Déconnexion employé
    @GetMapping("/deconnexion")
    public String deconnexionEmploye(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("employeConnecte");
        redirectAttributes.addFlashAttribute("success", "Vous avez été déconnecté avec succès.");
        return "redirect:/employes/connexion";
    }
    
    // Page de dashboard employé
    @GetMapping("/dashboard")
    public String dashboardEmploye(Model model, HttpSession session) {
        // Vérifier si l'employé est connecté
        Employe employeConnecte = (Employe) session.getAttribute("employeConnecte");
        if (employeConnecte == null) {
            return "redirect:/employes/connexion";
        }
        
        model.addAttribute("employeConnecte", employeConnecte);
        List<com.example.demo.model.Chambre> chambres = chambreService.getAllChambres();
        
        // Grouper les chambres par statut
        java.util.Map<com.example.demo.model.Chambre.StatutChambre, Long> chambresParStatut = 
            chambres.stream().collect(java.util.stream.Collectors.groupingBy(
                com.example.demo.model.Chambre::getStatut, java.util.stream.Collectors.counting()));
        
        model.addAttribute("chambres", chambres);
        model.addAttribute("chambresParStatut", chambresParStatut);
        model.addAttribute("statuts", com.example.demo.model.Chambre.StatutChambre.values());
        
        return "dashboard-employe"; // templates/dashboard-employe.html
    }
    
    // Mettre à jour le statut d'une chambre
    @PostMapping("/chambres/{id}/statut")
    public String changerStatutChambre(@PathVariable Long id, 
                                      @RequestParam com.example.demo.model.Chambre.StatutChambre statut,
                                      RedirectAttributes redirectAttributes) {
        try {
            Optional<com.example.demo.model.Chambre> optionalChambre = chambreService.getChambreById(id);
            if (optionalChambre.isPresent()) {
                com.example.demo.model.Chambre chambre = optionalChambre.get();
                com.example.demo.model.Chambre.StatutChambre ancienStatut = chambre.getStatut();
                
                chambre.setStatut(statut);
                
                // Mettre à jour la disponibilité selon le statut
                chambre.setDisponible(statut == com.example.demo.model.Chambre.StatutChambre.DISPONIBLE);
                
                chambreService.updateChambre(id, chambre);
                
                redirectAttributes.addFlashAttribute("success", 
                    "Statut de la chambre " + chambre.getNumero() + " changé de " + 
                    ancienStatut.getLibelle() + " à " + statut.getLibelle());
            } else {
                redirectAttributes.addFlashAttribute("error", "Chambre non trouvée");
            }
                
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erreur lors du changement de statut : " + e.getMessage());
        }
        
        return "redirect:/employes/dashboard";
    }
}

