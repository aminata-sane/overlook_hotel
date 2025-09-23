package com.example.demo.service;

import com.example.demo.entity.Employe;
import com.example.demo.repository.EmployeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeService {

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository employeRepository) {
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

    // Crée ou met à jour un employé
    public Employe createEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    // Met à jour un employé existant
    public Employe updateEmploye(Long id, Employe employeDetails) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id " + id));

        employe.setNom(employeDetails.getNom());
        employe.setPrenom(employeDetails.getPrenom());
        employe.setEmail(employeDetails.getEmail());
        employe.setMotDePasse(employeDetails.getMotDePasse());
        employe.setHoraires(employeDetails.getHoraires());
        employe.setConges(employeDetails.getConges());
        employe.setFormations(employeDetails.getFormations());

        return employeRepository.save(employe);
    }

    // Supprime un employé
    public void deleteEmploye(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id " + id));
        employeRepository.delete(employe);
    }

    // Ajouter un horaire à un employé
    public Employe ajouterHoraire(Long employeId, String horaire) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id " + employeId));
        employe.getHoraires().add(horaire);
        return employeRepository.save(employe);
    }

    // Ajouter une période de congé
    public Employe demanderConge(Long employeId, String dateDebut, String dateFin) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id " + employeId));
        String periode = dateDebut + " au " + dateFin;
        employe.getConges().add(periode);
        return employeRepository.save(employe);
    }

    // Ajouter une formation suivie
    public Employe suivreFormation(Long employeId, String formation) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id " + employeId));
        employe.getFormations().add(formation);
        return employeRepository.save(employe);
    }
}
