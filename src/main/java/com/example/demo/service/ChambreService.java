package com.example.demo.service;

import com.example.demo.model.Chambre;
import com.example.demo.repository.ChambreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChambreService {

    @Autowired
    private ChambreRepository chambreRepository;

    // Récupérer toutes les chambres
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    // Récupérer une chambre par ID
    public Optional<Chambre> getChambreById(Long id) {
        return chambreRepository.findById(id);
    }

    // Récupérer une chambre par numéro
    public Optional<Chambre> getChambreByNumero(String numero) {
        return chambreRepository.findByNumero(numero);
    }

    // Récupérer les chambres disponibles
    public List<Chambre> getChambresDisponibles() {
        return chambreRepository.findByDisponibleTrue();
    }

    // Récupérer les chambres par type
    public List<Chambre> getChambresByType(Chambre.TypeChambre type) {
        return chambreRepository.findByType(type);
    }

    // Récupérer les chambres disponibles d'un type donné
    public List<Chambre> getChambresDisponiblesByType(Chambre.TypeChambre type) {
        return chambreRepository.findByTypeAndDisponibleTrue(type);
    }

    // Récupérer les chambres dans une fourchette de prix
    public List<Chambre> getChambresByPrix(Double prixMin, Double prixMax) {
        return chambreRepository.findByPrixBetween(prixMin, prixMax);
    }

    // Créer une nouvelle chambre
    public Chambre createChambre(Chambre chambre) {
        // Vérifier si le numéro existe déjà
        Optional<Chambre> existante = chambreRepository.findByNumero(chambre.getNumero());
        if (existante.isPresent()) {
            throw new RuntimeException("Une chambre avec le numéro " + chambre.getNumero() + " existe déjà !");
        }
        
        return chambreRepository.save(chambre);
    }

    // Mettre à jour une chambre
    public Chambre updateChambre(Long id, Chambre chambreDetails) {
        Optional<Chambre> chambreOpt = chambreRepository.findById(id);
        if (chambreOpt.isPresent()) {
            Chambre chambre = chambreOpt.get();
            
            // Vérifier si le nouveau numéro n'est pas déjà utilisé par une autre chambre
            if (!chambre.getNumero().equals(chambreDetails.getNumero())) {
                Optional<Chambre> existante = chambreRepository.findByNumero(chambreDetails.getNumero());
                if (existante.isPresent()) {
                    throw new RuntimeException("Le numéro " + chambreDetails.getNumero() + " est déjà utilisé !");
                }
            }
            
            // Mettre à jour les champs
            chambre.setNumero(chambreDetails.getNumero());
            chambre.setType(chambreDetails.getType());
            chambre.setPrix(chambreDetails.getPrix());
            chambre.setDisponible(chambreDetails.getDisponible());
            
            return chambreRepository.save(chambre);
        } else {
            throw new RuntimeException("Chambre avec l'ID " + id + " non trouvée !");
        }
    }

    // Supprimer une chambre
    public void deleteChambre(Long id) {
        Optional<Chambre> chambre = chambreRepository.findById(id);
        if (chambre.isPresent()) {
            chambreRepository.deleteById(id);
        } else {
            throw new RuntimeException("Chambre avec l'ID " + id + " non trouvée !");
        }
    }

    // Réserver une chambre
    public void reserverChambre(Long id) {
        Optional<Chambre> chambreOpt = chambreRepository.findById(id);
        if (chambreOpt.isPresent()) {
            Chambre chambre = chambreOpt.get();
            if (!chambre.getDisponible()) {
                throw new RuntimeException("La chambre " + chambre.getNumero() + " est déjà occupée !");
            }
            chambre.reserver();
            chambreRepository.save(chambre);
        } else {
            throw new RuntimeException("Chambre avec l'ID " + id + " non trouvée !");
        }
    }

    // Libérer une chambre
    public void libererChambre(Long id) {
        Optional<Chambre> chambreOpt = chambreRepository.findById(id);
        if (chambreOpt.isPresent()) {
            Chambre chambre = chambreOpt.get();
            if (chambre.getDisponible()) {
                throw new RuntimeException("La chambre " + chambre.getNumero() + " est déjà disponible !");
            }
            chambre.liberer();
            chambreRepository.save(chambre);
        } else {
            throw new RuntimeException("Chambre avec l'ID " + id + " non trouvée !");
        }
    }

    // Basculer la disponibilité d'une chambre
    public void toggleDisponibilite(Long id) {
        Optional<Chambre> chambreOpt = chambreRepository.findById(id);
        if (chambreOpt.isPresent()) {
            Chambre chambre = chambreOpt.get();
            if (chambre.getDisponible()) {
                chambre.reserver();
            } else {
                chambre.liberer();
            }
            chambreRepository.save(chambre);
        } else {
            throw new RuntimeException("Chambre avec l'ID " + id + " non trouvée !");
        }
    }

    // Statistiques
    public long getNombreChambres() {
        return chambreRepository.count();
    }

    public long getNombreChambresDisponibles() {
        return chambreRepository.countByDisponibleTrue();
    }

    public long getNombreChambresParType(Chambre.TypeChambre type) {
        return chambreRepository.countByType(type);
    }

    // Vérifier si une chambre existe
    public boolean chambreExists(String numero) {
        return chambreRepository.findByNumero(numero).isPresent();
    }

    // Vérifier si une chambre est disponible
    public boolean isChambreDisponible(Long id) {
        Optional<Chambre> chambre = chambreRepository.findById(id);
        return chambre.map(Chambre::getDisponible).orElse(false);
    }
}
