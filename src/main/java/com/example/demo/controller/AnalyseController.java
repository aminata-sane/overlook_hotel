package com.example.demo.controller;

import com.example.demo.service.AnalyseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gestionnaire/analyses")
public class AnalyseController {

    @Autowired
    private AnalyseService analyseService;

    @GetMapping("/taux-occupation")
    public ResponseEntity<Integer> getTauxOccupation() {
        return ResponseEntity.ok(analyseService.getTauxOccupation());
    }

    @GetMapping("/satisfaction-moyenne")
    public ResponseEntity<Double> getSatisfactionMoyenne() {
        return ResponseEntity.ok(analyseService.getSatisfactionMoyenne());
    }

    @PostMapping("/calcul-taux-occupation")
    public ResponseEntity<Void> calculerTauxOccupation(@RequestParam int chambresOccupees,
                                                       @RequestParam int chambresTotales) {
        analyseService.calculerTauxOccupation(chambresOccupees, chambresTotales);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calcul-satisfaction")
    public ResponseEntity<Void> calculerSatisfaction(@RequestParam double totalNotes,
                                                     @RequestParam int nbCommentaires) {
        analyseService.calculerSatisfactionMoyenne(totalNotes, nbCommentaires);
        return ResponseEntity.ok().build();
    }
}
