package com.example.demo.service;

import com.example.demo.model.Fidelite;
import com.example.demo.model.Client;
import com.example.demo.repository.FideliteRepository;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class FideliteService {

    @Autowired
    private FideliteRepository fideliteRepository;

    @Autowired
    private ClientRepository clientRepository;

    // CRUD de base
    public List<Fidelite> getAllFidelites() {
        return fideliteRepository.findAll();
    }

    public Optional<Fidelite> getFideliteById(Long id) {
        return fideliteRepository.findById(id);
    }

    public Optional<Fidelite> getFideliteByClient(Client client) {
        return fideliteRepository.findByClient(client);
    }

    public Optional<Fidelite> getFideliteByClientId(Long clientId) {
        return fideliteRepository.findByClientId(clientId);
    }

    public Fidelite createFidelite(Fidelite fidelite) {
        // Vérifier qu'un client n'a pas déjà une carte de fidélité
        if (fidelite.getClient() != null) {
            Optional<Fidelite> existante = fideliteRepository.findByClient(fidelite.getClient());
            if (existante.isPresent()) {
                throw new RuntimeException("Le client a déjà une carte de fidélité");
            }
        }
        return fideliteRepository.save(fidelite);
    }

    public Fidelite updateFidelite(Long id, Fidelite fideliteDetails) {
        return fideliteRepository.findById(id)
                .map(fidelite -> {
                    fidelite.setPoints(fideliteDetails.getPoints());
                    if (fideliteDetails.getClient() != null) {
                        fidelite.setClient(fideliteDetails.getClient());
                    }
                    return fideliteRepository.save(fidelite);
                })
                .orElseThrow(() -> new RuntimeException("Carte de fidélité non trouvée avec l'ID : " + id));
    }

    public void deleteFidelite(Long id) {
        if (!fideliteRepository.existsById(id)) {
            throw new RuntimeException("Carte de fidélité non trouvée avec l'ID : " + id);
        }
        fideliteRepository.deleteById(id);
    }

    // Méthodes spécifiques à la gestion des points
    public Fidelite ajouterPoints(Long clientId, int points) {
        Optional<Fidelite> fideliteOpt = fideliteRepository.findByClientId(clientId);
        
        if (fideliteOpt.isEmpty()) {
            // Créer automatiquement une carte de fidélité si elle n'existe pas
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
            Fidelite nouvelleFidelite = new Fidelite(client);
            nouvelleFidelite.ajouterPoints(points);
            return fideliteRepository.save(nouvelleFidelite);
        }
        
        Fidelite fidelite = fideliteOpt.get();
        fidelite.ajouterPoints(points);
        return fideliteRepository.save(fidelite);
    }

    public Fidelite retirerPoints(Long clientId, int points) {
        Fidelite fidelite = fideliteRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Carte de fidélité non trouvée pour ce client"));
        
        if (!fidelite.retirerPoints(points)) {
            throw new RuntimeException("Points insuffisants pour cette opération");
        }
        
        return fideliteRepository.save(fidelite);
    }

    // Méthodes de recherche et statistiques
    public List<Fidelite> getFidelitesByNiveau(Fidelite.NiveauFidelite niveau) {
        return fideliteRepository.findByNiveauFidelite(niveau.getSeuilMin(), niveau.getSeuilMax());
    }

    public List<Fidelite> getTopClients() {
        List<Fidelite> allFidelites = fideliteRepository.findTopByOrderByPointsDesc();
        return allFidelites.size() > 10 ? allFidelites.subList(0, 10) : allFidelites;
    }

    public List<Fidelite> getClientsByPointsMin(int pointsMin) {
        return fideliteRepository.findByPointsGreaterThanEqual(pointsMin);
    }

    public Map<String, Object> getStatistiques() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCartes", fideliteRepository.count());
        stats.put("moyennePoints", fideliteRepository.findAveragePoints());
        stats.put("totalPoints", fideliteRepository.findTotalPoints());
        
        // Statistiques par niveau
        for (Fidelite.NiveauFidelite niveau : Fidelite.NiveauFidelite.values()) {
            long count = fideliteRepository.countByNiveauFidelite(niveau.getSeuilMin(), niveau.getSeuilMax());
            stats.put("nombre" + niveau.name(), count);
        }
        
        return stats;
    }

    // Méthodes utilitaires
    public Fidelite creerOuMettreAJourFidelite(Long clientId, int pointsAAjouter) {
        Optional<Fidelite> fideliteOpt = fideliteRepository.findByClientId(clientId);
        
        if (fideliteOpt.isPresent()) {
            Fidelite fidelite = fideliteOpt.get();
            fidelite.ajouterPoints(pointsAAjouter);
            return fideliteRepository.save(fidelite);
        } else {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
            Fidelite nouvelleFidelite = new Fidelite(client);
            nouvelleFidelite.ajouterPoints(pointsAAjouter);
            return fideliteRepository.save(nouvelleFidelite);
        }
    }

    public boolean peutUtiliserPoints(Long clientId, int pointsNecessaires) {
        Optional<Fidelite> fideliteOpt = fideliteRepository.findByClientId(clientId);
        return fideliteOpt.isPresent() && fideliteOpt.get().getPoints() >= pointsNecessaires;
    }

    // Calculer les points à attribuer selon un montant d'achat
    public int calculerPointsPourAchat(double montantAchat) {
        // 1 point pour chaque euro dépensé, avec bonus selon le montant
        int pointsBase = (int) Math.floor(montantAchat);
        
        // Bonus selon le montant
        if (montantAchat >= 500) {
            return (int) (pointsBase * 1.5); // Bonus de 50%
        } else if (montantAchat >= 200) {
            return (int) (pointsBase * 1.2); // Bonus de 20%
        } else if (montantAchat >= 100) {
            return (int) (pointsBase * 1.1); // Bonus de 10%
        }
        
        return pointsBase;
    }
}
