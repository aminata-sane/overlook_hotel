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

    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    public Optional<Employe> getEmployeById(Long id) {
        return employeRepository.findById(id);
    }

    public Employe saveEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    public void deleteEmploye(Long id) {
        employeRepository.deleteById(id);
    }

    // Méthodes pour la gestion avancée
    public void ajouterHoraire(Employe employe, String horaire) {
        employe.getHoraires().add(horaire);
        employeRepository.save(employe);
    }

    public void demanderConge(Employe employe, String dateDebut, String dateFin) {
        String periode = dateDebut + " au " + dateFin;
        employe.getConges().add(periode);
        employeRepository.save(employe);
    }

    public void suivreFormation(Employe employe, String formation) {
        employe.getFormations().add(formation);
        employeRepository.save(employe);
    }
}
