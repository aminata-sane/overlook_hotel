package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // Page d'accueil principale - Espace client
    @GetMapping("/")
    public String accueilPrincipal() {
        return "redirect:/accueil";
    }

    // Portail professionnel pour employés et gestionnaires
    @GetMapping("/portail")
    public String portailProfessionnel() {
        return "index-principal";
    }

    // Redirection vers l'espace client (page de réservation existante)
    @GetMapping("/client")
    public String espaceClient() {
        return "redirect:/accueil";
    }

    // Redirection vers l'espace employé
    @GetMapping("/employe")
    public String espaceEmploye() {
        return "redirect:/employes/dashboard";
    }

    // Route directe vers le dashboard employé
    @GetMapping("/dashboard-employe")
    public String dashboardEmploye() {
        return "redirect:/employes/dashboard";
    }

    // Redirection vers l'espace gestionnaire
    @GetMapping("/gestionnaire")  
    public String espaceGestionnaire() {
        return "dashboard-gestionnaire";
    }
}
