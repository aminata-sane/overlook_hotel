package com.example.demo.controller;

import com.example.demo.entity.Employe;
import com.example.demo.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employe")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeService.getAllEmployes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        Optional<Employe> employe = employeService.getEmployeById(id);
        return employe.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Employe createEmploye(@RequestBody Employe employe) {
        return employeService.saveEmploye(employe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody Employe employeDetails) {
        Optional<Employe> employeOptional = employeService.getEmployeById(id);
        if (!employeOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Employe employe = employeOptional.get();
        employe.setNom(employeDetails.getNom());
        employe.setPrenom(employeDetails.getPrenom());
        employe.setEmail(employeDetails.getEmail());
        employe.setMotDePasse(employeDetails.getMotDePasse());
        employe.setHoraires(employeDetails.getHoraires());
        employe.setConges(employeDetails.getConges());
        employe.setFormations(employeDetails.getFormations());

        Employe updatedEmploye = employeService.saveEmploye(employe);
        return ResponseEntity.ok(updatedEmploye);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        employeService.deleteEmploye(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints spécifiques pour horaires, congés, formations
    @PostMapping("/{id}/horaire")
    public ResponseEntity<Void> ajouterHoraire(@PathVariable Long id, @RequestBody String horaire) {
        employeService.getEmployeById(id).ifPresent(e -> employeService.ajouterHoraire(e, horaire));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/conge")
    public ResponseEntity<Void> demanderConge(@PathVariable Long id, @RequestBody String periode) {
        String[] dates = periode.split(" au ");
        if (dates.length == 2) {
            employeService.getEmployeById(id).ifPresent(e -> employeService.demanderConge(e, dates[0], dates[1]));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/formation")
    public ResponseEntity<Void> suivreFormation(@PathVariable Long id, @RequestBody String formation) {
        employeService.getEmployeById(id).ifPresent(e -> employeService.suivreFormation(e, formation));
        return ResponseEntity.ok().build();
    }
}
