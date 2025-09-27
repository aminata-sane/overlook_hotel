package com.example.demo.service;

import com.example.demo.model.Feedback;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackServiceSimple {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // CRUD de base
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Feedback createFeedback(Feedback feedback) {
        // Validation de la note
        if (feedback.getNote() == null || feedback.getNote() < 1 || feedback.getNote() > 5) {
            throw new RuntimeException("La note doit être comprise entre 1 et 5");
        }

        // Validation du commentaire
        if (feedback.getCommentaire() == null || feedback.getCommentaire().trim().isEmpty()) {
            throw new RuntimeException("Le commentaire ne peut pas être vide");
        }

        // Définir la date de création si non définie
        if (feedback.getDateCreation() == null) {
            feedback.setDateCreation(LocalDateTime.now());
        }

        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(Long id, Feedback feedbackDetails) {
        return feedbackRepository.findById(id)
                .map(feedback -> {
                    feedback.setCommentaire(feedbackDetails.getCommentaire());
                    feedback.setNote(feedbackDetails.getNote());
                    
                    return feedbackRepository.save(feedback);
                })
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'ID : " + id));
    }

    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback non trouvé avec l'ID : " + id);
        }
        feedbackRepository.deleteById(id);
    }

    // Recherche
    public List<Feedback> rechercherFeedbacks(String terme) {
        return feedbackRepository.rechercherFeedbacks(terme);
    }

    // Statistiques de base
    public long getNombreFeedbacks() {
        return feedbackRepository.count();
    }

    public Double getNoteMoyenne() {
        Double moyenne = feedbackRepository.getNoteMoyenne();
        return moyenne != null ? moyenne : 0.0;
    }
}
