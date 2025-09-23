package com.example.demo.controller;

import com.example.demo.entity.Analyse;
import com.example.demo.service.AnalyseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestionnaire/analyses")
public class AnalyseController {

    private final AnalyseService analyseService;

    public AnalyseController(AnalyseService analyseService) {
        this.analyseService = analyseService;
    }

    // Récupérer toutes les analyses sauvegardées
    @GetMapping
    public ResponseEntity<List<Analyse>> getAllAnalyses() {
        return ResponseEntity.ok(analyseService.getAllAnalyses());
    }

    // Calculer et générer une nouvelle analyse et la sauvegarder
    @PostMapping("/generer")
    public ResponseEntity<Analyse> genererAnalyse() {
        Analyse analyse = analyseService.genererAnalyse();
        return ResponseEntity.ok(analyse);
    }

    // Récupérer le taux d'occupation actuel
    @GetMapping("/taux-occupation")
    public ResponseEntity<Integer> getTauxOccupation() {
        int taux = analyseService.calculerTauxOccupation();
        return ResponseEntity.ok(taux);
    }

    // Récupérer la satisfaction moyenne actuelle
    @GetMapping("/satisfaction-moyenne")
    public ResponseEntity<Double> getSatisfactionMoyenne() {
        double satisfaction = analyseService.calculerSatisfactionMoyenne();
        return ResponseEntity.ok(satisfaction);
    }

    // Récupérer les revenus totaux actuels
    @GetMapping("/revenus")
    public ResponseEntity<Double> getRevenus() {
        double revenus = analyseService.calculerRevenus();
        return ResponseEntity.ok(revenus);
    }
}

