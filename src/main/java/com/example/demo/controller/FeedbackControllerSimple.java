package com.example.demo.controller;

import com.example.demo.model.Feedback;
import com.example.demo.service.FeedbackServiceSimple;
import com.example.demo.service.ClientService;
import com.example.demo.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/feedbacks")
public class FeedbackControllerSimple {

    @Autowired
    private FeedbackServiceSimple feedbackService;

    @Autowired
    private ClientService clientService;
    
    @Autowired
    private EmployeService employeService;

    // Page d'accueil des feedbacks - Liste tous les feedbacks
    @GetMapping
    public String listFeedbacks(Model model) {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("nombreFeedbacks", feedbackService.getNombreFeedbacks());
        model.addAttribute("noteMoyenne", String.format("%.1f", feedbackService.getNoteMoyenne()));
        
        return "feedbacks/liste"; // templates/feedbacks/liste.html
    }

    // Page de création d'un nouveau feedback
    @GetMapping("/nouveau")
    public String nouveauFeedback(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("employes", employeService.getAllEmployes());
        return "feedbacks/nouveau"; // templates/feedbacks/nouveau.html
    }

    // Traiter la création d'un nouveau feedback
    @PostMapping("/nouveau")
    public String creerFeedback(@ModelAttribute Feedback feedback, 
                               RedirectAttributes redirectAttributes) {
        try {
            feedbackService.createFeedback(feedback);
            redirectAttributes.addFlashAttribute("succes", 
                "Feedback créé avec succès !");
            return "redirect:/feedbacks";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la création du feedback : " + e.getMessage());
            return "redirect:/feedbacks/nouveau";
        }
    }

    // Voir les détails d'un feedback
    @GetMapping("/{id}")
    public String voirFeedback(@PathVariable Long id, Model model) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            model.addAttribute("feedback", feedback.get());
            return "feedbacks/details"; // templates/feedbacks/details.html
        } else {
            return "redirect:/feedbacks";
        }
    }

    // Supprimer un feedback
    @PostMapping("/{id}/supprimer")
    public String supprimerFeedback(@PathVariable Long id, 
                                   RedirectAttributes redirectAttributes) {
        try {
            feedbackService.deleteFeedback(id);
            redirectAttributes.addFlashAttribute("succes", 
                "Feedback supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la suppression : " + e.getMessage());
        }
        
        return "redirect:/feedbacks";
    }

    // Page de recherche de feedbacks
    @GetMapping("/rechercher")
    public String rechercherFeedbacks(@RequestParam(required = false) String terme, Model model) {
        if (terme != null && !terme.trim().isEmpty()) {
            List<Feedback> resultats = feedbackService.rechercherFeedbacks(terme);
            model.addAttribute("feedbacks", resultats);
            model.addAttribute("terme", terme);
            model.addAttribute("nombreResultats", resultats.size());
        } else {
            model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        }
        
        return "feedbacks/recherche"; // templates/feedbacks/recherche.html
    }

    // Page de modification d'un feedback
    @GetMapping("/{id}/modifier")
    public String modifierFeedback(@PathVariable Long id, Model model) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            model.addAttribute("feedback", feedback.get());
            model.addAttribute("clients", clientService.getAllClients());
            model.addAttribute("employes", employeService.getAllEmployes());
            return "feedbacks/modifier"; // templates/feedbacks/modifier.html
        } else {
            return "redirect:/feedbacks";
        }
    }

    // Traiter la modification d'un feedback
    @PostMapping("/{id}/modifier")
    public String sauvegarderFeedback(@PathVariable Long id, 
                                      @ModelAttribute Feedback feedback, 
                                      RedirectAttributes redirectAttributes) {
        try {
            feedbackService.updateFeedback(id, feedback);
            redirectAttributes.addFlashAttribute("succes", 
                "Feedback modifié avec succès !");
            return "redirect:/feedbacks/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", 
                "Erreur lors de la modification du feedback : " + e.getMessage());
            return "redirect:/feedbacks/" + id + "/modifier";
        }
    }
}
