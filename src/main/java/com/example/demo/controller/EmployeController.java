package com.example.demo.controller;

import com.example.demo.entity.Employe;
import com.example.demo.service.EmployeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employes")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    // GET : liste de tous les employés
    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeService.getAllEmployes();
    }

    // GET : un employé par ID
    @GetMapping("/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        return employeService.getEmployeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST : créer un nouvel employé
    @PostMapping
    public ResponseEntity<Employe> createEmploye(@Valid @RequestBody Employe employe) {
        Employe saved = employeService.saveEmploye(employe);
        return ResponseEntity.ok(saved);
    }

    // PUT : mise à jour partielle d’un employé
    @PutMapping("/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody Employe employeDetails) {
        return employeService.getEmployeById(id)
                .map(employe -> {
                    if (employeDetails.getNom() != null) employe.setNom(employeDetails.getNom());
                    if (employeDetails.getPrenom() != null) employe.setPrenom(employeDetails.getPrenom());
                    if (employeDetails.getEmail() != null) employe.setEmail(employeDetails.getEmail());
                    if (employeDetails.getMotDePasse() != null) employe.setMotDePasse(employeDetails.getMotDePasse());
                    if (employeDetails.getHoraires() != null) employe.setHoraires(employeDetails.getHoraires());
                    if (employeDetails.getConges() != null) employe.setConges(employeDetails.getConges());
                    if (employeDetails.getFormations() != null) employe.setFormations(employeDetails.getFormations());

                    Employe updated = employeService.saveEmploye(employe);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE : suppression d’un employé
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        return employeService.getEmployeById(id)
                .map(e -> {
                    employeService.deleteEmploye(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // === DTOs internes avec validation ===
    public static class HoraireRequest {
        @NotBlank
        private String horaire;

        public String getHoraire() { return horaire; }
        public void setHoraire(String horaire) { this.horaire = horaire; }
    }

    public static class CongeRequest {
        @NotNull
        private String dateDebut;
        @NotNull
        private String dateFin;

        public String getDateDebut() { return dateDebut; }
        public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
        public String getDateFin() { return dateFin; }
        public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    }

    public static class FormationRequest {
        @NotBlank
        private String formation;

        public String getFormation() { return formation; }
        public void setFormation(String formation) { this.formation = formation; }
    }

    // POST : ajouter un horaire
    @PostMapping("/{id}/horaire")
    public ResponseEntity<Employe> ajouterHoraire(@PathVariable Long id, @Valid @RequestBody HoraireRequest request) {
        return employeService.getEmployeById(id)
                .map(e -> {
                    employeService.ajouterHoraire(e, request.getHoraire());
                    return ResponseEntity.ok(employeService.saveEmploye(e));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // POST : demander un congé
    @PostMapping("/{id}/conge")
    public ResponseEntity<Employe> demanderConge(@PathVariable Long id, @Valid @RequestBody CongeRequest request) {
        return employeService.getEmployeById(id)
                .map(e -> {
                    employeService.demanderConge(e, request.getDateDebut(), request.getDateFin());
                    return ResponseEntity.ok(employeService.saveEmploye(e));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // POST : suivre une formation
    @PostMapping("/{id}/formation")
    public ResponseEntity<Employe> suivreFormation(@PathVariable Long id, @Valid @RequestBody FormationRequest request) {
        return employeService.getEmployeById(id)
                .map(e -> {
                    employeService.suivreFormation(e, request.getFormation());
                    return ResponseEntity.ok(employeService.saveEmploye(e));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
