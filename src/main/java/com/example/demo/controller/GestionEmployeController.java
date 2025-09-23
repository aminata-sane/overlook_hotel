package com.example.demo.controller;

import com.example.demo.entity.Employe;
import com.example.demo.service.GestionEmployeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestionnaire/employes")
public class GestionEmployeController {

    private final GestionEmployeService employeService;

    public GestionEmployeController(GestionEmployeService employeService) {
        this.employeService = employeService;
    }

    // GET /api/gestionnaire/employes
    @GetMapping
    public ResponseEntity<List<Employe>> getAllEmployes() {
        return ResponseEntity.ok(employeService.getAllEmployes());
    }

    // GET /api/gestionnaire/employes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        return employeService.getEmployeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/gestionnaire/employes
    @PostMapping
    public ResponseEntity<Employe> createEmploye(@RequestBody Employe employe) {
        return ResponseEntity.ok(employeService.createEmploye(employe));
    }

    // PUT /api/gestionnaire/employes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody Employe employeDetails) {
        try {
            Employe updatedEmploye = employeService.updateEmploye(id, employeDetails);
            return ResponseEntity.ok(updatedEmploye);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/gestionnaire/employes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        try {
            employeService.deleteEmploye(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

