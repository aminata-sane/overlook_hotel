package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.entity.Fidelite;
import com.example.demo.repository.FideliteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FideliteService {

    private final FideliteRepository fideliteRepository;

    public FideliteService(FideliteRepository fideliteRepository) {
        this.fideliteRepository = fideliteRepository;
    }

    public Optional<Fidelite> getFideliteByClient(Client client) {
        return fideliteRepository.findByClient(client);
    }

    public Fidelite ajouterPoints(Client client, int points) {
        Fidelite fidelite = fideliteRepository.findByClient(client)
                .orElse(new Fidelite(client));
        fidelite.setPoints(fidelite.getPoints() + points);
        return fideliteRepository.save(fidelite);
    }

    public Fidelite utiliserPoints(Client client, int points) {
        Fidelite fidelite = fideliteRepository.findByClient(client)
                .orElseThrow(() -> new RuntimeException("Client n'a pas de compte fidélité"));

        int nouveauSolde = fidelite.getPoints() - points;
        if (nouveauSolde < 0) {
            throw new RuntimeException("Points insuffisants");
        }

        fidelite.setPoints(nouveauSolde);
        return fideliteRepository.save(fidelite);
    }
}

