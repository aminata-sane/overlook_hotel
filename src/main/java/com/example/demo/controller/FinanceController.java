package com.example.demo.controller;

import com.example.demo.service.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/finances")
public class FinanceController {

    @Autowired
    private ChambreService chambreService;

    @GetMapping
    public String pageFinances(Model model) {
        try {
            // Statistiques générales
            long totalReservations = 15; // Simulé pour l'exemple
            long totalChambres = chambreService.getAllChambres().size();
            
            // Calculs financiers simulés (à adapter selon votre logique métier)
            double chiffresAffairesMensuel = totalReservations * 150.0; // Prix moyen simulé
            double chiffresAffairesAnnuel = chiffresAffairesMensuel * 12;
            double tauxOccupation = totalChambres > 0 ? (totalReservations * 100.0 / totalChambres) : 0;
            double revenuMoyenParChambre = totalChambres > 0 ? (chiffresAffairesMensuel / totalChambres) : 0;
            
            // Données pour les graphiques (simulées)
            List<String> moisLabels = Arrays.asList("Jan", "Fév", "Mar", "Avr", "Mai", "Jun", 
                                                   "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc");
            List<Double> revenus = Arrays.asList(12000.0, 15000.0, 18000.0, 20000.0, 25000.0, 30000.0,
                                                35000.0, 32000.0, 28000.0, 22000.0, 18000.0, 16000.0);
            
            List<String> categoriesDepenses = Arrays.asList("Personnel", "Maintenance", "Marketing", "Utilities", "Autres");
            List<Double> depenses = Arrays.asList(8000.0, 3000.0, 2000.0, 1500.0, 1000.0);
            
            // Ajout des données au modèle
            model.addAttribute("chiffresAffairesMensuel", chiffresAffairesMensuel);
            model.addAttribute("chiffresAffairesAnnuel", chiffresAffairesAnnuel);
            model.addAttribute("tauxOccupation", tauxOccupation);
            model.addAttribute("revenuMoyenParChambre", revenuMoyenParChambre);
            model.addAttribute("totalReservations", totalReservations);
            model.addAttribute("totalChambres", totalChambres);
            model.addAttribute("moisLabels", moisLabels);
            model.addAttribute("revenus", revenus);
            model.addAttribute("categoriesDepenses", categoriesDepenses);
            model.addAttribute("depenses", depenses);
            
            // Date actuelle
            model.addAttribute("dateActuelle", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            return "finances/dashboard";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des données financières");
            return "finances/dashboard";
        }
    }
    
    @GetMapping("/rapports")
    public String rapportsFinanciers(Model model) {
        // Page des rapports financiers détaillés
        return "finances/rapports";
    }
    
    @GetMapping("/budget")
    public String gestionBudget(Model model) {
        // Page de gestion du budget
        return "finances/budget";
    }
}
