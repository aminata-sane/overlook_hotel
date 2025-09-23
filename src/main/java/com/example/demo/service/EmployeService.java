package com.example.demo.service;

import com.example.demo.entity.Employe;
import com.example.demo.repository.EmployeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeService {

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    // Récupérer tous les employés
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    // Récupérer un employé par ID
    public Optional<Employe> getEmployeById(Long id) {
        return employeRepository.findById(id);
    }

    // Créer ou mettre à jour un employé
    public Employe saveEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    // Supprimer un employé
    public void deleteEmploye(Long id) {
        employeRepository.deleteById(id);
    }

    // Ajouter un horaire à un employé
    public Employe ajouterHoraire(Employe e, String horaire) {
        Employe employe = employeRepository.findById(e.getId())
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        employe.getHoraires().add(horaire);
        return employeRepository.save(employe);
    }

    // Demander un congé pour un employé
    public Employe demanderConge(Employe e, String dateDebut, String dateFin) {
        Employe employe = employeRepository.findById(e.getId())
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        String periode = dateDebut + " au " + dateFin;
        employe.getConges().add(periode);
        return employeRepository.save(employe);
    }

    // Suivre une formation pour un employé
    public Employe suivreFormation(Employe e, String formation) {
        Employe employe = employeRepository.findById(e.getId())
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        employe.getFormations().add(formation);
        return employeRepository.save(employe);
    }
}
