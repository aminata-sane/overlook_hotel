package com.example.demo.service;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.Gestionnaire;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // Récupérer tous les feedbacks
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    // Récupérer un feedback par ID
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    // Créer un nouveau feedback
    public Feedback createFeedback(Feedback feedback) {
        if (feedback.getClient() == null) {
            throw new RuntimeException("Un feedback doit être associé à un client");
        }
        return feedbackRepository.save(feedback);
    }

    // Mettre à jour un feedback
    public Feedback updateFeedback(Long id, Feedback feedbackDetails) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'id " + id));

        if (feedbackDetails.getClient() != null) feedback.setClient(feedbackDetails.getClient());
        if (feedbackDetails.getNote() != null) feedback.setNote(feedbackDetails.getNote());
        if (feedbackDetails.getCommentaire() != null) feedback.setCommentaire(feedbackDetails.getCommentaire());

        return feedbackRepository.save(feedback);
    }

    // Supprimer un feedback
    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'id " + id));
        feedbackRepository.delete(feedback);
    }

    // Permettre au gestionnaire de répondre à un feedback
    public Feedback repondreFeedback(Long feedbackId, String reponse, Gestionnaire gestionnaire) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'id " + feedbackId));
        
        feedback.setReponse(reponse);
        feedback.setGestionnaire(gestionnaire);
        
        return feedbackRepository.save(feedback);
    }

    // Récupérer tous les feedbacks d’un gestionnaire
    public List<Feedback> getFeedbacksByGestionnaire(Gestionnaire gestionnaire) {
        return feedbackRepository.findByGestionnaire(gestionnaire);
    }
}
