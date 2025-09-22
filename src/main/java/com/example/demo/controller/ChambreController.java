package com.example.demo.controller;

import com.example.demo.entity.Chambre;
import com.example.demo.repository.ChambreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestionnaire/chambres")
public class ChambreController {

    @Autowired
    private ChambreRepository chambreRepository;

    
    @GetMapping
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Chambre> getChambreById(@PathVariable Long id) {
        Optional<Chambre> chambre = chambreRepository.findById(id);
        return chambre.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    
    @PostMapping
    public Chambre createChambre(@RequestBody Chambre chambre) {
        return chambreRepository.save(chambre);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Chambre> updateChambre(@PathVariable Long id, @RequestBody Chambre chambreDetails) {
        Optional<Chambre> chambreOptional = chambreRepository.findById(id);
        if (!chambreOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Chambre chambre = chambreOptional.get();
        chambre.setNumero(chambreDetails.getNumero());
        chambre.setType(chambreDetails.getType());
        chambre.setPrix(chambreDetails.getPrix());
        chambre.setDisponible(chambreDetails.getDisponible());

        Chambre updatedChambre = chambreRepository.save(chambre);
        return ResponseEntity.ok(updatedChambre);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChambre(@PathVariable Long id) {
        if (!chambreRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        chambreRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
