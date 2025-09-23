package com.example.demo.service;

import com.example.demo.entity.Analyse;
import com.example.demo.entity.Chambre;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Reservation.StatutReservation;
import com.example.demo.repository.AnalyseRepository;
import com.example.demo.repository.ChambreRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyseService {

    private final AnalyseRepository analyseRepository;
    private final ChambreRepository chambreRepository;
    private final ReservationRepository reservationRepository;
    private final FeedbackRepository feedbackRepository;

    public AnalyseService(AnalyseRepository analyseRepository,
                          ChambreRepository chambreRepository,
                          ReservationRepository reservationRepository,
                          FeedbackRepository feedbackRepository) {
        this.analyseRepository = analyseRepository;
        this.chambreRepository = chambreRepository;
        this.reservationRepository = reservationRepository;
        this.feedbackRepository = feedbackRepository;
    }

    // Calculer le taux d'occupation en pourcentage
    public int calculerTauxOccupation() {
        List<Chambre> toutesChambres = chambreRepository.findAll();

        long chambresOccupees = reservationRepository.findAll().stream()
                .filter(res -> res.getStatut() == StatutReservation.CONFIRMEE)
                .count();

        return toutesChambres.isEmpty() ? 0 : (int)((chambresOccupees * 100) / toutesChambres.size());
    }

    // Calculer la satisfaction moyenne à partir des feedbacks
    public double calculerSatisfactionMoyenne() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        if (feedbacks.isEmpty()) return 0.0;

        double sommeNotes = feedbacks.stream()
                .filter(f -> f.getNote() != null)
                .mapToDouble(Feedback::getNote)
                .sum();

        long nbNotes = feedbacks.stream()
                .filter(f -> f.getNote() != null)
                .count();

        return nbNotes == 0 ? 0.0 : sommeNotes / nbNotes;
    }

    // Calculer les revenus totaux à partir des réservations confirmées
    public double calculerRevenus() {
        return reservationRepository.findAll().stream()
                .filter(res -> res.getStatut() == StatutReservation.CONFIRMEE)
                .mapToDouble(res -> res.getChambre() != null && res.getChambre().getPrix() != null
                        ? res.getChambre().getPrix()
                        : 0)
                .sum();
    }

    // Générer et sauvegarder un objet Analyse
    public Analyse genererAnalyse() {
        Analyse analyse = new Analyse();
        analyse.setOccupationChambres(calculerTauxOccupation());
        analyse.setSatisfactionClient(calculerSatisfactionMoyenne());
        analyse.setRevenus(calculerRevenus());

        return analyseRepository.save(analyse);
    }

    // Récupérer toutes les analyses
    public List<Analyse> getAllAnalyses() {
        return analyseRepository.findAll();
    }
}

