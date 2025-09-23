package com.example.demo.service;

import com.example.demo.entity.Employe;
import com.example.demo.repository.EmployeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestionEmployeService {

    private final EmployeRepository employeRepository;

    public GestionEmployeService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    // Retourne tous les employés
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    // Retourne un employé par ID
    public Optional<Employe> getEmployeById(Long id) {
        return employeRepository.findById(id);
    }

    // Crée un nouvel employé
    public Employe createEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    public Employe updateEmploye(Long id, Employe employeDetails) {
    Employe employe = employeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id " + id));

    employe.setNom(employeDetails.getNom());
    employe.setPrenom(employeDetails.getPrenom());
    employe.setEmail(employeDetails.getEmail());
    // Supprimer ou adapter cette ligne
    // employe.setRole(employeDetails.getRole());

    return employeRepository.save(employe);
}


    
    public void deleteEmploye(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id " + id));
        employeRepository.delete(employe);
    }
}

