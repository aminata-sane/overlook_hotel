package com.example.demo.controller;

import com.example.demo.model.Chambre;
import com.example.demo.model.Reservation;
import com.example.demo.repository.ChambreRepository;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.model.Chambre.Equipement;
import jakarta.annotation.PostConstruct;

@Controller
public class AccueilController {

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @PostConstruct
    public void initialiserChambresExemple() {
        long nombreChambres = chambreRepository.count();
        System.out.println("============================================");
        System.out.println("INITIALISATION: " + nombreChambres + " chambre(s) trouvée(s) en base");
        System.out.println("============================================");
        
        // Initialiser les statuts des chambres existantes
        List<Chambre> chambresExistantes = chambreRepository.findAll();
        boolean statutsMisAJour = false;
        for (Chambre chambre : chambresExistantes) {
            if (chambre.getStatut() == null) {
                // Définir le statut selon la disponibilité
                if (chambre.getDisponible()) {
                    chambre.setStatut(Chambre.StatutChambre.DISPONIBLE);
                } else {
                    chambre.setStatut(Chambre.StatutChambre.OCCUPEE);
                }
                chambreRepository.save(chambre);
                statutsMisAJour = true;
            }
        }
        
        if (statutsMisAJour) {
            System.out.println("Statuts des chambres existantes mis à jour !");
        }
        
        if(nombreChambres < 3) {
            System.out.println("Seulement " + nombreChambres + " chambre(s) trouvée(s). Ajout de chambres d'exemple...");
            
            // Chambre de luxe
            if(chambreRepository.findByNumero("SUITE-001").isEmpty()) {
                Chambre suiteLuxe = new Chambre();
                suiteLuxe.setNumero("SUITE-001");
                suiteLuxe.setType(Chambre.TypeChambre.SUITE);
                suiteLuxe.setCapacite(4);
                suiteLuxe.setPrix(650.00);
                suiteLuxe.setDisponible(true);
                suiteLuxe.setDescription("Suite Présidentielle - Suite de prestige avec vue panoramique sur les montagnes. Salon séparé avec cheminée, chambre king-size, salle de bain avec jacuzzi et douche à l'italienne. Décoration raffinée avec mobilier d'époque.");
                suiteLuxe.setImageUrl("https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80");
                suiteLuxe.ajouterEquipement(Equipement.WIFI_PREMIUM);
                suiteLuxe.ajouterEquipement(Equipement.CLIMATISATION);
                suiteLuxe.ajouterEquipement(Equipement.MINI_BAR);
                suiteLuxe.ajouterEquipement(Equipement.ECRAN_HOME_CINEMA);
                suiteLuxe.ajouterEquipement(Equipement.JACUZZI);
                suiteLuxe.ajouterEquipement(Equipement.BALCON);
                suiteLuxe.ajouterEquipement(Equipement.VUE_MONTAGNE);
                chambreRepository.save(suiteLuxe);
            }
            
            // Chambre familiale
            if(chambreRepository.findByNumero("FAM-001").isEmpty()) {
                Chambre chambreFamiliale = new Chambre();
                chambreFamiliale.setNumero("FAM-001");
                chambreFamiliale.setType(Chambre.TypeChambre.FAMILIALE);
                chambreFamiliale.setCapacite(6);
                chambreFamiliale.setPrix(380.00);
                chambreFamiliale.setDisponible(true);
                chambreFamiliale.setDescription("Chambre Familiale Deluxe - Spacieuse chambre familiale avec lit king-size et lits superposés pour enfants. Coin salon avec canapé-lit, grande salle de bain avec double vasque. Parfaite pour les séjours en famille.");
                chambreFamiliale.setImageUrl("https://images.unsplash.com/photo-1566665797739-1674de7a421a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2074&q=80");
                chambreFamiliale.ajouterEquipement(Equipement.WIFI_PREMIUM);
                chambreFamiliale.ajouterEquipement(Equipement.CLIMATISATION);
                chambreFamiliale.ajouterEquipement(Equipement.ECRAN_HOME_CINEMA);
                chambreFamiliale.ajouterEquipement(Equipement.MINI_BAR);
                chambreFamiliale.ajouterEquipement(Equipement.BALCON);
                chambreRepository.save(chambreFamiliale);
            }
            
            // Chambre romantique
            if(chambreRepository.findByNumero("ROM-001").isEmpty()) {
                Chambre chambreRomantique = new Chambre();
                chambreRomantique.setNumero("ROM-001");
                chambreRomantique.setType(Chambre.TypeChambre.DOUBLE);
                chambreRomantique.setCapacite(2);
                chambreRomantique.setPrix(295.00);
                chambreRomantique.setDisponible(true);
                chambreRomantique.setDescription("Chambre Romantique Vue Lac - Chambre double romantique avec vue imprenable sur le lac. Lit king-size avec linge de luxe, salle de bain avec baignoire sur pieds, terrasse privée. Ambiance intimiste pour les couples.");
                chambreRomantique.setImageUrl("https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by-wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80");
                chambreRomantique.ajouterEquipement(Equipement.WIFI_PREMIUM);
                chambreRomantique.ajouterEquipement(Equipement.CLIMATISATION);
                chambreRomantique.ajouterEquipement(Equipement.MINI_BAR);
                chambreRomantique.ajouterEquipement(Equipement.VUE_MER);
                chambreRomantique.ajouterEquipement(Equipement.BALCON);
                chambreRomantique.ajouterEquipement(Equipement.LIT_MASSANT);
                chambreRepository.save(chambreRomantique);
            }
            
            System.out.println("Chambres d'exemple ajoutées avec succès !");
        }
    }

    @GetMapping("/accueil")
    public String accueil(Model model) {
        // Créer un objet de réservation vide pour le formulaire
        Reservation reservation = new Reservation();
        model.addAttribute("reservation", reservation);
        
        // Ajouter les chambres disponibles
        List<Chambre> chambres = chambreRepository.findAll();
        model.addAttribute("chambres", chambres);
        
        // Ajouter les types de chambres pour la sélection
        model.addAttribute("typesChambres", Chambre.TypeChambre.values());
        
        // Ajouter seulement 3 chambres en vedette pour donner envie au client
        List<Chambre> chambresVedette = chambres.stream()
                .sorted((c1, c2) -> {
                    // Prioriser les chambres disponibles mais inclure toutes
                    if (c1.getDisponible() && !c2.getDisponible()) return -1;
                    if (!c1.getDisponible() && c2.getDisponible()) return 1;
                    // Ensuite trier par type pour avoir de la diversité
                    return c1.getType().compareTo(c2.getType());
                })
                .limit(3) // Limiter à 3 chambres pour donner envie de voir plus
                .collect(Collectors.toList());
        
        model.addAttribute("chambresVedette", chambresVedette);

        return "accueil";
    }

    @PostMapping("/rechercher-chambres")
    public String rechercherChambres(@ModelAttribute Reservation reservation, Model model) {
        // Validation des dates
        if (reservation.getDateArrivee() == null || reservation.getDateDepart() == null) {
            model.addAttribute("erreur", "Les dates d'arrivée et de départ sont obligatoires");
            return "redirect:/?erreur=dates";
        }

        if (reservation.getDateArrivee().isAfter(reservation.getDateDepart()) || 
            reservation.getDateArrivee().isEqual(reservation.getDateDepart())) {
            model.addAttribute("erreur", "La date de départ doit être après la date d'arrivée");
            return "redirect:/?erreur=dates";
        }

        // Rechercher les chambres disponibles
        List<Chambre> toutesChambres = chambreRepository.findAll();
        List<Chambre> chambresDisponibles = toutesChambres.stream()
                .filter(Chambre::getDisponible)
                .filter(chambre -> chambre.getCapacite() >= reservation.getNombrePersonnesTotal())
                .filter(chambre -> estChambreDisponible(chambre, reservation.getDateArrivee(), reservation.getDateDepart()))
                .collect(Collectors.toList());

        model.addAttribute("chambresDisponibles", chambresDisponibles);
        model.addAttribute("reservation", reservation);
        model.addAttribute("dateArrivee", reservation.getDateArrivee());
        model.addAttribute("dateDepart", reservation.getDateDepart());
        model.addAttribute("nombrePersonnes", reservation.getNombrePersonnesTotal());

        return "resultats-recherche";
    }

    @GetMapping("/chambre/{id}")
    public String detailsChambre(@PathVariable Long id, Model model) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
        
        model.addAttribute("chambre", chambre);
        model.addAttribute("reservation", new Reservation());
        
        return "details-chambre";
    }



    private boolean estChambreDisponible(Chambre chambre, LocalDate dateArrivee, LocalDate dateDepart) {
        List<Reservation> reservationsConflictuelles = reservationRepository
                .findReservationsConflictuelles(chambre.getId(), dateArrivee, dateDepart);
        return reservationsConflictuelles.isEmpty();
    }
}
