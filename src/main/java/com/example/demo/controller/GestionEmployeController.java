package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.entity.Employe;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestionnaire")
public class GestionEmployeController {

    @Autowired
    private EmployeRepository employeRepository;



    //Employe
    @GetMapping("/employes")
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    @GetMapping("/employes/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        Optional<Employe> employe = employeRepository.findById(id);
        return employe.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/employes")
    public Employe createEmploye(@RequestBody Employe employe) {
        return employeRepository.save(employe);
    }

    @PutMapping("/employes/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody Employe employeDetails) {
        Optional<Employe> employeOptional = employeRepository.findById(id);
        if (!employeOptional.isPresent()) return ResponseEntity.notFound().build();

        Employe employe = employeOptional.get();
        employe.setNom(employeDetails.getNom());
        employe.setPrenom(employeDetails.getPrenom());
        employe.setEmail(employeDetails.getEmail());
        employe.setMotDePasse(employeDetails.getMotDePasse());

        return ResponseEntity.ok(employeRepository.save(employe));
    }

    @DeleteMapping("/employes/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        if (!employeRepository.existsById(id)) return ResponseEntity.notFound().build();
        employeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
