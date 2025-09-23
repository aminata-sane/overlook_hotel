package com.example.demo.service;

import com.example.demo.entity.Chambre;
import com.example.demo.repository.ChambreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChambreService {

    private final ChambreRepository chambreRepository;

    public ChambreService(ChambreRepository chambreRepository) {
        this.chambreRepository = chambreRepository;
    }

    // Retourne toutes les chambres
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    // Retourne une chambre par ID
    public Optional<Chambre> getChambreById(Long id) {
        return chambreRepository.findById(id);
    }

    // Crée une nouvelle chambre
    public Chambre createChambre(Chambre chambre) {
        return chambreRepository.save(chambre);
    }

    // Met à jour une chambre
    public Chambre updateChambre(Long id, Chambre chambreDetails) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée avec l'id " + id));

        chambre.setNumero(chambreDetails.getNumero());
        chambre.setType(chambreDetails.getType());
        chambre.setPrix(chambreDetails.getPrix());
        chambre.setDisponible(chambreDetails.getDisponible());
        chambre.setGestionnaire(chambreDetails.getGestionnaire());

        return chambreRepository.save(chambre);
    }

    // Supprime une chambre
    public void deleteChambre(Long id) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée avec l'id " + id));
        chambreRepository.delete(chambre);
    }

    // Réserve une chambre
    public Chambre reserverChambre(Long chambreId) {
        Chambre chambre = chambreRepository.findById(chambreId)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée avec l'id " + chambreId));

        if (!chambre.getDisponible()) {
            throw new RuntimeException("Chambre déjà réservée");
        }

        chambre.setDisponible(false); // Marque la chambre comme occupée
        return chambreRepository.save(chambre);
    }

    // Retourne toutes les chambres disponibles
    public List<Chambre> getChambresDisponibles() {
        return chambreRepository.findByDisponibleTrue();
    }
}
